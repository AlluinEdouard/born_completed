#!/bin/bash
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"

xdotool mousemove 1280 1024
cd "$ROOT_DIR/projet/JavaSpace"
touch highscore
java -cp ".:../..:$MG2D_DIR" Main
