# Guide Utilisateur - Jeu Tron

## Présentation

Bienvenue dans **TRON**, un jeu d'arcade inspiré du film culte de Disney ! Dirigez votre Light Cycle (cycle de lumière) et évitez les collisions dans cet univers futuriste aux couleurs néon.

## Installation et lancement

### Prérequis
- **Python 3.6+** installé sur votre système
- **Pygame 2.0+** 

### Installation
1. **Télécharger le jeu :**
   ```bash
   git clone [url-du-projet]
   cd arcade-tron-game
   ```

2. **Installer les dépendances :**
   ```bash
   pip install pygame
   ```

3. **Lancer le jeu :**
   ```bash
   python main.py
   ```

## Règles du jeu

### Objectif
- Survivre le plus longtemps possible en évitant les collisions
- Faire percuter l'adversaire contre un mur ou une traînée de lumière
- Le dernier joueur en vie remporte la partie

### Mécaniques de jeu
- Chaque joueur contrôle un **Light Cycle** qui se déplace continuellement
- Le cycle laisse une **traînée de lumière** indestructible derrière lui
- **Collision = élimination** (mur, traînée propre, traînée adverse)
- Impossible de faire demi-tour (pas de marche arrière)

## Modes de jeu

### 🎮 1 Joueur (Solo)
Affrontez une **intelligence artificielle** avec 3 niveaux de difficulté :
- **Facile :** IA prévisible, idéal pour débuter
- **Moyen :** IA équilibrée, défi standard  
- **Difficile :** IA agressive, pour les experts

### 👥 2 Joueurs (Multijoueur local)
Défiez un ami sur le même écran dans des duels intenses !

## Contrôles

### 🎯 Joueur 1 (Bleu)
- **↑** : Monter
- **↓** : Descendre  
- **←** : Aller à gauche
- **→** : Aller à droite

### 🎯 Joueur 2 (Orange) - Mode 2 joueurs uniquement
- **W** : Monter
- **S** : Descendre
- **A** : Aller à gauche
- **D** : Aller à droite

### ⚙️ Contrôles généraux
- **P** : Mettre en pause / Reprendre
- **ESPACE** : Redémarrer (après game over) / Voir résultats
- **ÉCHAP** : Retour au menu principal
- **↑/↓** : Navigation dans les menus
- **ENTRÉE** : Sélectionner/Confirmer

## Contrôles Spéciaux (Mode Borne d'Arcade)

Le jeu est optimisé pour fonctionner en mode plein écran sur une borne d'arcade :

### Commandes Système
- **F11** : Basculer entre mode plein écran et mode fenêtré
- **Alt + Entrée** : Alternative pour basculer le mode d'affichage
- **Échap** : Retour au menu principal depuis n'importe quel écran, ou quitter le jeu depuis le menu

### Mode Plein Écran Automatique
- Le jeu démarre automatiquement en mode plein écran (configuré dans `config.py`)
- La résolution s'adapte automatiquement à l'écran utilisé
- Parfait pour les bornes d'arcade avec écrans de différentes tailles

## Interface utilisateur

### Menu principal
- **1 JOUEUR** : Partie solo contre l'IA
- **2 JOUEURS** : Partie multijoueur local
- **OPTIONS** : Paramètres du jeu
- **QUITTER** : Fermer le jeu

### Écran de jeu
- **Coins supérieurs :** Temps écoulé et mode de jeu
- **Traînées colorées :** Historique des déplacements
- **Têtes brillantes :** Position actuelle des joueurs
- **Scores latéraux :** Longueur des traînées

### Menu Options
- **Difficulté IA :** Facile / Moyen / Difficile
- **Vitesse :** Lente / Normale / Rapide
- **Son :** Activé / Désactivé
- **←/→** : Changer les valeurs
- **ENTRÉE** : Retour au menu

### Écran de fin
- **Gagnant** affiché en grand
- **Statistiques** de la partie
- **ESPACE** : Rejouer avec les mêmes paramètres
- **ÉCHAP** : Retour au menu principal

## Stratégies et conseils

### 🎯 Débutants
- **Restez près des bords** au début pour avoir plus d'espace
- **Observez les mouvements** de l'adversaire 
- **Gardez vos distances** pour éviter les pièges
- **Utilisez la pause** pour réfléchir à votre stratégie

### 🎯 Intermédiaires  
- **Créez des spirales** pour piéger l'adversaire
- **Forcez l'adversaire** vers les coins
- **Anticipez ses mouvements** plusieurs coups à l'avance
- **Variez votre vitesse** de jeu selon les situations

### 🎯 Experts
- **Maîtrisez le timing** pour des manœuvres serrées
- **Exploitez l'espace** de manière optimale
- **Psychologie :** Feignez vos intentions
- **Contre-attaquez** rapidement après avoir évité un piège

## Personnalisation

### Paramètres visuels
- **Style Tron authentique** avec effets de glow
- **Couleurs néon** : Bleu cyan et orange
- **Grille animée** en arrière-plan
- **Effets lumineux** sur les traînées

### Paramètres audio
- **Effets sonores** : Navigation, sélection, collision
- **Contrôle du volume** via les options
- **Possibilité de désactiver** complètement le son

### Vitesse de jeu
- **3 vitesses disponibles** pour s'adapter à tous les niveaux
- **Modification en temps réel** via le menu options
- **Paramètre conservé** entre les parties

## Résolution de problèmes

### Le jeu ne se lance pas
```bash
# Vérifier l'installation de pygame
pip install pygame --upgrade

# Vérifier la version Python
python --version  # Doit être 3.6+
```

### Pas de son
- Vérifiez que le son n'est pas désactivé dans les options
- Vérifiez le volume système de votre ordinateur
- Les fichiers audio sont dans `assets/sounds/`

### Performance lente
- Fermez les autres applications
- Le jeu tourne à 60 FPS par défaut
- Réduisez la vitesse via les options si nécessaire

### Contrôles qui ne répondent pas
- Vérifiez que la fenêtre du jeu a le focus
- Évitez d'appuyer sur plusieurs touches simultanément
- Redémarrez le jeu si le problème persiste

## Ressources supplémentaires

### Fichiers du jeu
- **Configuration :** `config.py` - Paramètres techniques
- **Ressources :** Dossier `assets/` - Sons et images
- **Utilitaires :** Dossier `utils/` - Outils de génération

### Support technique
- Consultez le fichier `README.md` pour plus d'informations
- Vérifiez la documentation développeur pour les détails techniques
- Les logs d'erreur s'affichent dans la console

## Crédits

- **Développé par :** Louis Bruche
- **Inspiré du film :** Tron (Disney)
- **Technologie :** Python + Pygame
- **Licence :** Voir fichier LICENSE

---

**Amusez-vous bien et que le meilleur cycliste gagne ! 🏍️⚡**
