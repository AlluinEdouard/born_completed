#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

cd "$ROOT_DIR/projet/ball-blast"

PYTHON_BIN="python3.7"
if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  PYTHON_BIN="python3"
fi

if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  echo "Python3 introuvable. Installe Python 3 pour lancer ball-blast."
  exit 1
fi

export PYTHONPATH="$PWD/src"
"$PYTHON_BIN" src/__main__.py
