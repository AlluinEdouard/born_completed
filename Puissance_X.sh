#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"

if command -v xdotool >/dev/null 2>&1; then
  xdotool mousemove 1280 1024 >/dev/null 2>&1 || true
fi
cd "$ROOT_DIR/projet/Puissance_X"
java -Dmg2d.fullscreen=true -Dmg2d.fullscreen.mode=desktop -Dsun.java2d.pmoffscreen=false -cp ".:../..:$MG2D_DIR" Main

# -Dsun.java2d.pmoffscreen=false : Améliore les performances sur les système Unix utilisant X11 (donc Raspbian est concerné).
# -Dsun.java2d.opengl=true : Utilise OpenGL (peut améliorer les performances).
