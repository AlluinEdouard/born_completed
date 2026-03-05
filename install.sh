#!/bin/bash
# Script d'installation automatique de la borne d'arcade

echo "🚀 Début de l'installation..."

# 1. Mise à jour du système et installation des dépendances
echo "Mise à jour du système et installation de Java/Git..."
sudo apt update && sudo apt upgrade -y
sudo apt install default-jdk git -y

# 2. Rendre les scripts vitaux exécutables
echo "Configuration des droits d'exécution..."
chmod +x compilation.sh lancerBorne.sh clean.sh

# 3. Compilation initiale de tout le projet
echo "☕ Compilation du projet (MG2D, Menu et Jeux) via compilation.sh..."
./compilation.sh

# 4. Configuration du lancement automatique au démarrage
echo "Configuration du lancement automatique..."
# On vérifie si la ligne existe déjà dans .bashrc pour ne pas la mettre en double
if ! grep -q "lancerBorne.sh" ~/.bashrc; then
    echo "cd ~/born && ./lancerBorne.sh" >> ~/.bashrc
fi

echo "✅ Installation terminée ! Redémarrez le Raspberry Pi (sudo reboot) pour tester."