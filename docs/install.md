# Guide d'Installation

Ce guide explique comment déployer le logiciel de la borne sur un nouveau Raspberry Pi.

## Prérequis matériels
* Raspberry Pi 3 Model B (ou supérieur)
* Carte MicroSD avec **Raspberry Pi OS** fraîchement installé.
* Contrôleurs d'arcade branchés sur les ports GPIO ou USB (configurés pour émuler les touches clavier spécifiques, voir le Guide Utilisateur).

## 🛠️ Installation logicielle

1. **Mise à jour du système :**
   ```bash
   sudo apt update && sudo apt upgrade -y

2. **Installation de Java**
    ```bash
    sudo apt install default-jdk -y

3. **Code du logiciel sur le git**
    ```bash
    git clone [URL_DU_DEPOT_IUT]
    cd born

4. **Compiler le jeu**
    ```bash
    ./compilation.sh
    
5. **Exécuter le jeu**
    ```bash
    ./LancerBorne.sh

