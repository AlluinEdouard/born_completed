# Tron - Jeu d'Arcade

Un jeu inspiré du film Tron, développé avec Pygame, pour une borne d'arcade.

## Description

Inspiré de l'univers de Tron, ce jeu consiste à diriger un "Light Cycle" (cycle de lumière) qui laisse une traînée derrière lui. Le but est de faire en sorte que l'adversaire entre en collision avec une traînée ou le bord de l'écran, tout en évitant soi-même les collisions.

## Fonctionnalités

- Menu d'accueil interactif avec effets visuels style arcade
- Mode solo contre une IA avec plusieurs niveaux de difficulté
- Mode deux joueurs sur le même écran
- Effets visuels néon inspirés de l'univers Tron
- Effets sonores et musique d'ambiance
- Interface optimisée pour borne d'arcade
- **Code restructuré** en modules séparés pour une meilleure maintenabilité

## Installation rapide

### Prérequis
- Python 3.6 ou supérieur
- Pygame 2.0 ou supérieur

### Installation
```bash
pip install pygame
python main.py
```

## Documentation

📖 **[Guide Utilisateur](GUIDE_UTILISATEUR.md)** - Instructions complètes pour jouer

🛠️ **[Documentation Développeur](DOCUMENTATION_DEVELOPPEUR.md)** - Architecture et développement

## Structure du projet

```
├── main.py              # Point d'entrée principal
├── config.py            # Configuration globale
├── direction.py         # Énumération des directions
├── player.py           # Classe Player
├── ai.py               # Intelligence artificielle
├── game_main.py        # Logique principale du jeu
├── menu_main.py        # Menu principal
├── options_main.py     # Menu des options
├── score_screen.py     # Écran de fin de partie
├── assets/             # Ressources (sons, images)
└── utils/              # Utilitaires de génération
```

## Contrôles

**Joueur 1:** Flèches directionnelles
**Joueur 2:** WASD
**Général:** P (pause), ESPACE (restart), ÉCHAP (menu)

## Crédits

- Développé par Louis Bruche
- Basé sur le film Tron de Disney
- Développé avec Pygame

---

*Consultez le [Guide Utilisateur](GUIDE_UTILISATEUR.md) pour des instructions détaillées !*
