#!/bin/bash
# Script de mise à jour automatique via Git

echo "🔄 Vérification des mises à jour sur Git..."
git fetch origin

M_LOCAL=$(git rev-parse @)
M_REMOTE=$(git rev-parse @{u})

if [ $M_LOCAL = $M_REMOTE ]; then
    echo "✅ La borne est déjà à jour."
else
    echo "⚠️ Nouvelle version détectée ! Téléchargement..."
    git pull origin main
    
    echo "☕ Recompilation complète du code..."
    chmod +x compilation.sh
    ./compilation.sh
    
    echo "🔄 Relance du menu..."
    # On tue l'ancien menu Java
    pkill -f "java.*Main"
    # Le script lancerBorne.sh (qui tourne en fond) va détecter l'arrêt et éteindre la borne,
    # ou on peut juste relancer java à la main ici.
    java -cp ".:MG2D" Main &
fi