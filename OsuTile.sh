#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

cd "$ROOT_DIR/projet/OsuTile"

PYTHON_BIN="python3.7"
if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  PYTHON_BIN="python3"
fi

if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  echo "Python3 introuvable. Installe Python 3 pour lancer OsuTile."
  exit 1
fi

"$PYTHON_BIN" main.py
