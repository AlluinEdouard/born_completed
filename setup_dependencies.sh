#!/bin/bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

if ! command -v python3 >/dev/null 2>&1; then
  echo "Python3 introuvable. Installe Python 3 pour continuer."
  exit 1
fi

PYTHON_BIN="python3"

echo "=== Installation des dependances Python ==="
"$PYTHON_BIN" -m pip install --upgrade pip || true

# TronGame
if [ -f "$ROOT_DIR/projet/TronGame/requirements.txt" ]; then
  "$PYTHON_BIN" -m pip install -r "$ROOT_DIR/projet/TronGame/requirements.txt"
fi

# ball-blast
if [ -f "$ROOT_DIR/projet/ball-blast/requirements.txt" ]; then
  "$PYTHON_BIN" -m pip install -r "$ROOT_DIR/projet/ball-blast/requirements.txt"
fi

# OsuTile (pygame requis)
"$PYTHON_BIN" -m pip install pygame

# PianoTile (librosa requis)
"$PYTHON_BIN" -m pip install librosa

echo "=== Verification Love2D (CursedWare) ==="
if command -v love >/dev/null 2>&1; then
  echo "Love2D deja installe."
elif [ -x "/Applications/love.app/Contents/MacOS/love" ]; then
  echo "Love2D detecte dans /Applications (love.app)."
else
  if command -v brew >/dev/null 2>&1; then
    echo "Installation de Love2D via Homebrew..."
    brew install --cask love
  else
    echo "Love2D introuvable et Homebrew absent."
    echo "Installe Love2D manuellement ou via Homebrew (brew install --cask love)."
  fi
fi

echo "=== Terminé ==="
