# Documentation Développeur - Jeu Tron

## Vue d'ensemble

Ce projet est un jeu Tron développé en Python avec Pygame, conçu pour une borne d'arcade. Le code a été restructuré de manière modulaire avec chaque classe dans son propre fichier pour améliorer la maintenabilité.

## Architecture du projet

### Structure des fichiers

```
arcade-tron-game/
├── main.py                 # Point d'entrée principal du jeu
├── tron_game.py            # Point d'entrée alternatif
├── config.py               # Configuration et constantes globales
├── direction.py            # Énumération des directions
├── player.py               # Classe Player (joueur humain)
├── ai.py                   # Classe AI (intelligence artificielle)
├── game_main.py            # Classe Game (logique principale du jeu)
├── menu_main.py            # Classe Menu (menu principal)
├── menu_item.py            # Classe MenuItem (éléments de menu)
├── options_main.py         # Classe Options (menu des options)
├── option_item.py          # Classe OptionItem (éléments d'options)
├── score_screen.py         # Classe ScoreScreen (écran de fin de partie)
├── assets/                 # Ressources du jeu
│   ├── images/            # Images (logo, etc.)
│   └── sounds/            # Effets sonores
└── utils/                 # Utilitaires de génération de ressources
```

## Classes principales

### 1. TronGame (main.py)
**Responsabilité :** Contrôleur principal du jeu, gestion des états et de la boucle principale.

```python
class TronGame:
    def __init__(self):
        # Initialisation pygame, écran, états
    
    def run(self):
        # Boucle principale du jeu
    
    def process_menu_action(self, action):
        # Traitement des actions du menu
```

**États gérés :**
- `menu` : Menu principal
- `game` : Partie en cours
- `options` : Menu des options
- `score_screen` : Écran de fin de partie

### 2. Game (game_main.py)
**Responsabilité :** Logique principale du jeu, gestion des joueurs, collisions, et rendu.

```python
class Game:
    def __init__(self, screen, mode="single", difficulty="moyen", move_delay=None):
        # Initialisation de la partie
    
    def update(self):
        # Mise à jour de l'état du jeu
        # Retourne True si game over
    
    def draw(self):
        # Rendu visuel du jeu
    
    def handle_event(self, event):
        # Gestion des événements en jeu
```

**Modes de jeu :**
- `single` : 1 joueur contre IA
- `multi` : 2 joueurs humains

### 3. Player (player.py)
**Responsabilité :** Comportement d'un joueur humain, déplacement, rendu de la traînée.

```python
class Player:
    def __init__(self, x, y, color, glow_color, controls=None, move_delay=None):
        # Initialisation du joueur
    
    def move(self, current_time, grid):
        # Déplacement du joueur
        # Retourne True en cas de collision
    
    def change_direction(self, new_direction):
        # Changement de direction (avec vérification demi-tour)
    
    def draw(self, surface, grid_size):
        # Rendu du joueur et de sa traînée avec effets glow
```

### 4. AI (ai.py)
**Responsabilité :** Intelligence artificielle héritant de Player.

```python
class AI(Player):
    def __init__(self, x, y, color, glow_color, difficulty="moyen"):
        # Initialisation de l'IA avec niveau de difficulté
    
    def update(self, current_time, grid, opponent=None):
        # Mise à jour de l'IA (prise de décision)
    
    def evaluate_move(self, direction, grid, opponent_pos=None):
        # Évaluation de la qualité d'un mouvement
```

**Niveaux de difficulté :**
- `facile` : Look-ahead 5, update 300ms
- `moyen` : Look-ahead 10, update 200ms  
- `difficile` : Look-ahead 15, update 100ms

### 5. Menu (menu_main.py)
**Responsabilité :** Affichage et gestion du menu principal.

```python
class Menu:
    def __init__(self, screen):
        # Initialisation du menu avec éléments MenuItem
    
    def handle_event(self, event):
        # Gestion navigation et sélection menu
        # Retourne action sélectionnée
    
    def draw(self):
        # Rendu du menu avec effets visuels
```

### 6. Direction (direction.py)
**Responsabilité :** Énumération des directions possibles.

```python
class Direction(Enum):
    UP = (0, -1)
    DOWN = (0, 1)
    LEFT = (-1, 0)
    RIGHT = (1, 0)
```

## Configuration (config.py)

Toutes les constantes du jeu sont centralisées dans `config.py` :

```python
# Dimensions écran
SCREEN_WIDTH = 1024
SCREEN_HEIGHT = 768
FPS = 60

# Paramètres jeu
GRID_SIZE = 10
MOVE_DELAY = 50
TRAIL_LENGTH = 500

# Couleurs
BLACK = (0, 0, 0)
BLUE = (0, 80, 255)
CYAN = (0, 255, 255)
# ...

# Difficulté IA
AI_DIFFICULTY_SETTINGS = {
    "facile": {"look_ahead": 5, "update_interval": 300},
    "moyen": {"look_ahead": 10, "update_interval": 200},
    "difficile": {"look_ahead": 15, "update_interval": 100}
}

# Mode plein écran pour borne d'arcade
FULLSCREEN = True  # Définir sur False pour le mode fenêtré
```

## Configuration d'Affichage

### Mode Plein Écran
Le jeu est configuré pour démarrer en mode plein écran, idéal pour les bornes d'arcade.

**Fonctionnalités :**
- Détection automatique de la résolution d'écran
- Adaptation dynamique des dimensions (`SCREEN_WIDTH`, `SCREEN_HEIGHT`)
- Raccourcis clavier pour basculer les modes (F11, Alt+Entrée)
- Recréation automatique des composants lors du changement de mode

**Pour le développement :**
- Définir `FULLSCREEN = False` dans `config.py` pour le mode fenêtré
- Utiliser F11 pendant l'exécution pour basculer entre les modes

## Flux de données

1. **Initialisation :** `TronGame` → `Menu` → `MenuItem`
2. **Nouvelle partie :** `Menu` → `TronGame.process_menu_action()` → `Game` → `Player`/`AI`
3. **Gameplay :** `Game.update()` → `Player.move()` / `AI.update()` → collision detection
4. **Fin de partie :** `Game` → `ScoreScreen` → retour menu ou replay

## Gestion des événements

### Hiérarchie des événements :
1. `TronGame.run()` - Événements globaux (quit, escape)
2. État actuel (`menu`, `game`, `options`, `score_screen`)
3. Composants spécifiques (`Player.handle_input()`, etc.)

### Contrôles :
- **Joueur 1 :** Flèches directionnelles
- **Joueur 2 :** WASD
- **Global :** P (pause), ESPACE (restart), ÉCHAP (menu)

## Système de rendu

### Ordre de rendu :
1. Fond noir
2. Grille de fond (style Tron)
3. Traînées des joueurs (avec glow)
4. Têtes des joueurs
5. HUD (scores, temps)
6. Overlays (pause, game over)

### Effets visuels :
- **Glow effect :** Multiple surfaces avec alpha décroissant
- **Animations :** Frame counter pour effets cycliques
- **Style Tron :** Couleurs néon, grilles animées

## Tests et débogage

### Lancement du jeu :
```bash
python main.py          # Version principale
python tron_game.py     # Version alternative
```

### Messages de debug :
- Warnings pour ressources manquantes (sons, images)
- Print des erreurs de chargement
- Gestion gracieuse des échecs de chargement

## Extension du projet

### Ajouter un nouveau mode de jeu :
1. Modifier `Game.__init__()` pour accepter le nouveau mode
2. Adapter `Game.update()` pour la logique spécifique
3. Ajouter l'option dans `Menu`
4. Mettre à jour `TronGame.process_menu_action()`

### Ajouter une nouvelle difficulté IA :
1. Ajouter l'entrée dans `AI_DIFFICULTY_SETTINGS` (config.py)
2. Tester les paramètres `look_ahead` et `update_interval`

### Personnaliser l'apparence :
1. Modifier les couleurs dans `config.py`
2. Remplacer les ressources dans `assets/`
3. Adapter les effets visuels dans les méthodes `draw()`

## Bonnes pratiques

### Code :
- Chaque classe dans son propre fichier
- Imports centralisés depuis `config.py`
- Gestion d'erreurs pour les ressources
- Documentation docstring pour chaque méthode

### Performance :
- Limitation FPS à 60
- Surfaces pygame réutilisées
- Calculs de collision optimisés
- Traînées limitées en longueur

### Maintenabilité :
- Configuration centralisée
- Séparation des responsabilités
- Code modulaire et réutilisable
- Structure claire des fichiers
