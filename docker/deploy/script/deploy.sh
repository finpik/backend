set -euo pipefail

APP_DIR="/srv/app"                     # 서버 배포 디렉토리
COMPOSE_DIR="${APP_DIR}/deploy"
ENV_FILE="${APP_DIR}/.env"             # 서버에 상주
IMAGE_REPO_DEFAULT="ghcr.io/your/repo" # Actions에서 ENV로 덮어쓸 수 있음
IMAGE_REPO="${IMAGE_REPO:-$IMAGE_REPO_DEFAULT}"
IMAGE_TAG="${IMAGE_TAG:?IMAGE_TAG is required}"   # 필수: 배포할 이미지 태그 (예: git SHA)

BLUE_PORT=8080
GREEN_PORT=8081

# ===== 유틸 =====
health_check() {
  local port="$1"
  local tries=30
  echo "[INFO] Health check on :${port}"
  for i in $(seq 1 $tries); do
    if curl -fsS "http://127.0.0.1:${port}/actuator/health" | grep -q '"status":"UP"'; then
      echo "[OK] Healthy on :${port}"
      return 0
    fi
    sleep 2
  done
  echo "[ERROR] Health check failed on :${port}"
  return 1
}

current_active_port() {
  # Nginx upstream 파일에서 현재 포트를 추출
  local conf="/etc/nginx/conf.d/finpik-upstream.conf"
  if [[ -f "$conf" ]]; then
    grep -oE '127\.0\.0\.1:[0-9]+' "$conf" | awk -F: '{print $2}' | tail -n1
  else
    echo ""  # 미설정이면 빈 값
  fi
}

# ===== 시작 =====
cd "${APP_DIR}"

# .env 존재 확인
if [[ ! -f "${ENV_FILE}" ]]; then
  echo "[WARN] ${ENV_FILE} not found. Creating from template if exists."
  if [[ -f "${COMPOSE_DIR}/.env.template" ]]; then
    cp "${COMPOSE_DIR}/.env.template" "${ENV_FILE}"
    echo "[WARN] Fill ${ENV_FILE} with real values before deploying."
  fi
fi

ACTIVE_PORT="$(current_active_port)"
if [[ "${ACTIVE_PORT}" == "${BLUE_PORT}" ]]; then
  TARGET_COLOR="green"
  TARGET_PORT="${GREEN_PORT}"
elif [[ "${ACTIVE_PORT}" == "${GREEN_PORT}" ]]; then
  TARGET_COLOR="blue"
  TARGET_PORT="${BLUE_PORT}"
else
  # 초기 상황(미설정)이면 블루부터 시작
  TARGET_COLOR="blue"
  TARGET_PORT="${BLUE_PORT}"
fi

echo "[INFO] Active port: ${ACTIVE_PORT:-none}, Target: ${TARGET_COLOR}(:${TARGET_PORT})"
export IMAGE_REPO IMAGE_TAG

# 1) 대상 색으로 컨테이너 기동/갱신
echo "[1/4] Starting ${TARGET_COLOR}..."
if [[ "${TARGET_COLOR}" == "blue" ]]; then
  docker compose -f "${COMPOSE_DIR}/docker-compose.blue.yml" --env-file "${ENV_FILE}" pull
  docker compose -f "${COMPOSE_DIR}/docker-compose.blue.yml" --env-file "${ENV_FILE}" up -d
else
  docker compose -f "${COMPOSE_DIR}/docker-compose.green.yml" --env-file "${ENV_FILE}" pull
  docker compose -f "${COMPOSE_DIR}/docker-compose.green.yml" --env-file "${ENV_FILE}" up -d
fi

# 2) 헬스체크
health_check "${TARGET_PORT}"

# 3) 트래픽 스위치 (Nginx 업스트림 포트 전환)
echo "[3/4] Switching Nginx upstream to :${TARGET_PORT}"
sudo "${COMPOSE_DIR}/script/switch_upstream.sh" "${TARGET_PORT}"

# 4) 이전 색 정리(선택) - 안전하게 남겨두고 싶다면 주석 처리
echo "[4/4] Stopping old stack (optional cleanup)"
if [[ "${TARGET_COLOR}" == "blue" ]]; then
  docker compose -f "${COMPOSE_DIR}/docker-compose.green.yml" --env-file "${ENV_FILE}" down || true
else
  docker compose -f "${COMPOSE_DIR}/docker-compose.blue.yml" --env-file "${ENV_FILE}" down || true
fi

echo "[DONE] Deployed ${TARGET_COLOR} (:${TARGET_PORT}) with image ${IMAGE_REPO}:${IMAGE_TAG}"
