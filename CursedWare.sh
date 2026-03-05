#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

cd "$ROOT_DIR/projet/CursedWare"

LOVE_BIN="love"
if ! command -v "$LOVE_BIN" >/dev/null 2>&1; then
  if [ -x "/Applications/love.app/Contents/MacOS/love" ]; then
    LOVE_BIN="/Applications/love.app/Contents/MacOS/love"
  else
    echo "Love2D introuvable. Installe Love2D pour lancer CursedWare."
    exit 1
  fi
fi

"$LOVE_BIN" .
