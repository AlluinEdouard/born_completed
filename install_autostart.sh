#!/bin/bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
AUTOSTART_DIR="$HOME/.config/autostart"
LOCAL_BIN_DIR="$HOME/.local/bin"
WRAPPER_PATH="$LOCAL_BIN_DIR/borne_arcade_start.sh"
DESKTOP_PATH="$AUTOSTART_DIR/borne.desktop"

mkdir -p "$AUTOSTART_DIR" "$LOCAL_BIN_DIR"

cat > "$WRAPPER_PATH" <<EOF
#!/bin/bash
set -e
exec "$ROOT_DIR/lancerBorne.sh"
EOF
chmod +x "$WRAPPER_PATH"

cp "$ROOT_DIR/borne.desktop" "$DESKTOP_PATH"

echo "Autostart installe dans: $DESKTOP_PATH"
echo "Script de lancement cree: $WRAPPER_PATH"
echo "Redemarrez le Raspberry pour verifier le lancement automatique."
