set -euo pipefail

TARGET_PORT="${1:-}"
CONF_PATH="/etc/nginx/conf.d/finpik-upstream.conf"

if [[ -z "${TARGET_PORT}" ]]; then
  echo "Usage: $0 <8080|8081>"
  exit 1
fi
if [[ "${TARGET_PORT}" != "8080" && "${TARGET_PORT}" != "8081" ]]; then
  echo "TARGET_PORT must be 8080 or 8081"
  exit 1
fi

sudo bash -c "cat > ${CONF_PATH}" <<EOF
upstream finpik_upstream {
  server 127.0.0.1:${TARGET_PORT};
  keepalive 64;
}
EOF

sudo nginx -t
sudo systemctl reload nginx
echo "[OK] Nginx upstream switched to ${TARGET_PORT}"
