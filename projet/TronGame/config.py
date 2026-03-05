#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Configuration du jeu Tron
"""

# Dimensions de l'écran
SCREEN_WIDTH = 1024
SCREEN_HEIGHT = 768
FPS = 60
TITLE = "TRON - Arcade Game"
FULLSCREEN = True  # Mode plein écran pour borne d'arcade (F11 pour basculer)

# Paramètres du jeu
GRID_SIZE = 10  # Taille d'une cellule en pixels
MOVE_DELAY = 50  # Millisecondes entre chaque mouvement (vitesse du jeu)
TRAIL_LENGTH = 500  # Longueur maximale des traînées de lumière

# Niveaux de difficulté de l'IA
AI_DIFFICULTY_SETTINGS = {
    "facile": {
        "look_ahead": 5,
        "update_interval": 300
    },
    "moyen": {
        "look_ahead": 10,
        "update_interval": 200
    },
    "difficile": {
        "look_ahead": 15,
        "update_interval": 100
    }
}

# Couleurs
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
BLUE = (0, 80, 255)
BLUE_GLOW = (30, 144, 255)
ORANGE = (255, 140, 0)
ORANGE_GLOW = (255, 165, 0)
CYAN = (0, 255, 255)
RED = (255, 0, 0)
GREEN = (0, 255, 0)
NEON_BLUE = (30, 100, 255)
NEON_PINK = (255, 0, 128)

# Contrôles des joueurs
PLAYER1_CONTROLS = {
    "UP": "UP",
    "DOWN": "DOWN",
    "LEFT": "LEFT",
    "RIGHT": "RIGHT"
}

PLAYER2_CONTROLS = {
    "UP": "O",
    "DOWN": "l",
    "LEFT": "k",
    "RIGHT": "m"
}

# Chemins des ressources
SOUNDS_DIR = "assets/sounds"
IMAGES_DIR = "assets/images"

# Sons
SOUND_NAVIGATE = "navigate.wav"
SOUND_SELECT = "select.wav"
SOUND_CRASH = "crash.wav"
SOUND_MOVE = "move.wav"

MUSIC_MENU = "music_menu_wav"

# Images
LOGO_IMAGE = "tron_logo.png"
