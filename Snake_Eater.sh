#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"

xdotool mousemove 1280 1024
cd "$ROOT_DIR/projet/Snake_Eater"
touch highscore
java -cp ".:../..:$MG2D_DIR" Snake_Eater
