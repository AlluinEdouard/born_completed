#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

cd "$ROOT_DIR/projet/DoodleJumpe"

# Utilise la meme image pour le menu du jeu et la vignette de la borne.
if [ -f "./img/image.png" ]; then
  cp -f "./img/image.png" "./photo_small.png"
fi

PYTHON_BIN="python3.7"
if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  PYTHON_BIN="python3"
fi

if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  echo "Python3 introuvable. Installe Python 3 pour lancer Doodle Jumpe."
  exit 1
fi

"$PYTHON_BIN" main.py
