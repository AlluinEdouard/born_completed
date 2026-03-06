#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"

if command -v xdotool >/dev/null 2>&1; then
  xdotool mousemove 1280 1024 >/dev/null 2>&1 || true
fi
cd "$ROOT_DIR/projet/JavaSpace"
touch highscore
java -Dmg2d.fullscreen=true -Dmg2d.fullscreen.mode=desktop -cp ".:../..:$MG2D_DIR" Main
