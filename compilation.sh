#!/bin/bash

set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
MG2D_DIR="$ROOT_DIR/MG2D"
MG2D_SRC="$MG2D_DIR/MG2D"
CP=".:$MG2D_DIR"

# Installer les dépendances runtime (Python, Love2D) si possible
if [ -x "$ROOT_DIR/setup_dependencies.sh" ]; then
  "$ROOT_DIR/setup_dependencies.sh"
else
  echo "setup_dependencies.sh introuvable ou non exécutable, étape ignorée"
fi

echo "Compilation de la bibliothèque MG2D"
echo "Veuillez patienter"
javac -cp "$CP" \
  "$MG2D_SRC"/*.java \
  "$MG2D_SRC"/geometrie/*.java \
  "$MG2D_SRC"/audio/*.java

echo "Compilation du menu de la borne d'arcade"
echo "Veuillez patienter"
javac -cp "$CP" *.java

cd projet


#PENSER A REMETTRE COMPILATION JEUX!!!
for i in *
do
    cd $i
    echo "Compilation du jeu "$i
    echo "Veuillez patienter"
    if ls *.java >/dev/null 2>&1; then
        javac -cp ".:../..:$MG2D_DIR" *.java
    else
        echo "Aucun fichier Java, compilation ignorée"
    fi
    cd ..
done

cd ..
