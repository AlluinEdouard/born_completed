#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"

xdotool mousemove 1280 1024
cd "$ROOT_DIR/projet/Puissance_X"
java -cp ".:../..:$MG2D_DIR" -Dsun.java2d.pmoffscreen=false Main

# -Dsun.java2d.pmoffscreen=false : Améliore les performances sur les système Unix utilisant X11 (donc Raspbian est concerné).
# -Dsun.java2d.opengl=true : Utilise OpenGL (peut améliorer les performances).
