#!/bin/bash
# Script d'automatisation locale de la documentation (Docs-as-Code)

echo "🚀 Démarrage de la génération de la documentation..."

# 1. Activation de l'environnement Python
source venv/bin/activate

# 2. Génération de la Javadoc (Code technique)
echo "☕ Génération de la Javadoc..."
mkdir -p docs/api
find . -name "*.java" > sources.txt
javadoc -d docs/api -encoding utf8 -docencoding utf8 -charset utf8 @sources.txt -quiet

# 3. Préparation du diff pour l'IA
echo "🔍 Analyse des modifications Git..."
# On récupère toutes les modifications non commitées
git diff HEAD > changes.diff

# Vérifier si le diff n'est pas vide
if [ -s changes.diff ]; then
    echo "🤖 Appel de Qwen3 pour l'analyse des changements..."
    python tools/ai_doc_patch.py changes.diff
else
    echo "ℹ️ Aucun changement détecté dans le code, l'IA n'a pas été appelée."
fi

# Nettoyage du fichier temporaire
rm changes.diff

# 4. Construction du site MkDocs
echo "🌐 Construction du site web MkDocs..."
mkdocs build

echo "✅ Terminé ! Le site est à jour dans le dossier 'site/'."