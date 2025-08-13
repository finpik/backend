# ====== 커스터마이즈 ======
IMAGE="${IMAGE:-ghcr.io/finpik/backend:latest}"   # 배포할 이미지
APP_NAME="${APP_NAME:-finpik-app}"               # 컨테이너 이름(고정)
PORT="${PORT:-8080}"                             # 호스트 바인딩 포트
ENV_FILE="${ENV_FILE:-/srv/app/.env}"
RESTART_POLICY="${RESTART_POLICY:-no}"           # 자동재시작 끄기: no (원하면 unless-stopped)
JAVA_OPTS="${JAVA_OPTS:--Xms1g -Xmx1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+ExitOnOutOfMemoryError}"

# ★ 네트워크 설정 추가
NETWORK="${NETWORK:-finpik-net}"

echo "== Pull image =="
docker pull "$IMAGE"

echo "== Stop old container (if any) =="
docker ps -a --filter "name=^/${APP_NAME}$" -q | xargs -r docker stop
docker ps -a --filter "name=^/${APP_NAME}$" -q | xargs -r docker rm

echo "== Run new container =="
docker run -d \
  --name "$APP_NAME" \
  --network "$NETWORK" \
  -p "${PORT}:8080" \
  --env-file "$ENV_FILE" \
  -e "SPRING_PROFILES_ACTIVE=prod" \
  -e "JAVA_TOOL_OPTIONS=${JAVA_OPTS}" \
  --health-cmd='curl -fsS http://localhost:8080/actuator/health || exit 1' \
  --health-interval=10s --health-timeout=5s --health-retries=5 \
  --restart "$RESTART_POLICY" \
  "$IMAGE"

docker network connect "$NETWORK" "$APP_NAME" 2>/dev/null || true

echo "== Wait for health (max ~60s) =="
for i in {1..12}; do
  STATUS=$(docker inspect --format='{{json .State.Health.Status}}' "$APP_NAME" 2>/dev/null || echo '"starting"')
  echo "  attempt $i: $STATUS"
  [[ "$STATUS" == '"healthy"' ]] && break
  sleep 5
done

if [[ "$STATUS" != '"healthy"' ]]; then
  echo "!! container is not healthy. recent logs:"
  docker logs --tail=200 "$APP_NAME" || true
  exit 1
fi

echo "== OK: ${APP_NAME} is healthy on :${PORT}"
docker logs -f "$APP_NAME"
