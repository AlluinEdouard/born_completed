#!/bin/bash

if command -v setxkbmap >/dev/null 2>&1; then
    setxkbmap borne || echo "Warning: impossible d'appliquer le layout clavier 'borne'."
else
    echo "Warning: setxkbmap introuvable, layout clavier non appliqué."
fi

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"

cd "$ROOT_DIR"
echo "nettoyage des répertoires"
echo "Veuillez patienter"
./clean.sh
./compilation.sh

echo "Lancement du  Menu"
echo "Veuillez patienter"

java -cp ".:$MG2D_DIR" Main

./clean.sh

for i in {30..1}
do
    echo Extinction de la borne dans $i secondes
    sleep 1
done

sudo halt
