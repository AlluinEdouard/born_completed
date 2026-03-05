#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe Player du jeu Tron
Définit le comportement d'un joueur et sa traînée
"""

import pygame
from direction import Direction
from config import *

class Player:
    def __init__(self, x, y, color, glow_color, controls=None, move_delay=None):
        self.positions = [(x, y)]  # Historique des positions (la traînée)
        self.direction = Direction.RIGHT  # Direction initiale
        self.color = color
        self.glow_color = glow_color
        self.alive = True
        self.controls = controls or {}
        self.score = 0
        self.last_move_time = 0
        self.move_delay = move_delay if move_delay is not None else MOVE_DELAY

    def move(self, current_time, grid):
        """Déplace le joueur dans la direction actuelle"""
        if not self.alive or current_time - self.last_move_time < self.move_delay:
            return False

        # Obtenir la prochaine position
        head_x, head_y = self.positions[-1]
        dx, dy = self.direction.value
        new_x, new_y = head_x + dx, head_y + dy

        # Vérifier la collision avec les bords
        grid_width = len(grid[0])
        grid_height = len(grid)
        if new_x < 0 or new_x >= grid_width or new_y < 0 or new_y >= grid_height:
            self.alive = False
            return True  # Collision

        # Vérifier la collision avec les traînées
        if grid[new_y][new_x] != 0:
            self.alive = False
            return True  # Collision

        # Mettre à jour la position
        self.positions.append((new_x, new_y))

        # Limiter la longueur de la traînée
        if len(self.positions) > TRAIL_LENGTH:
            self.positions.pop(0)

        self.last_move_time = current_time
        return False  # Pas de collision

    def change_direction(self, new_direction):
        """Change la direction du joueur si ce n'est pas un demi-tour"""
        current_dx, current_dy = self.direction.value
        new_dx, new_dy = new_direction.value

        # Empêcher les demi-tours (aller à l'opposé de la direction actuelle)
        if current_dx + new_dx != 0 or current_dy + new_dy != 0:
            self.direction = new_direction

    def handle_input(self, key):
        """Gère les entrées clavier pour ce joueur"""
        if not self.controls:
            return False

        if key in self.controls:
            self.change_direction(self.controls[key])
            return True
        return False

    def draw(self, surface, grid_size):
        """Dessine le joueur et sa traînée"""
        if not self.alive:
            return

        # Dessiner la traînée avec effet de glow
        for i, (x, y) in enumerate(self.positions[:-1]):  # Toutes les positions sauf la tête
            # Calculer l'intensité de la couleur basée sur la position dans la traînée
            alpha = min(255, int(255 * i / len(self.positions)))            # Dessiner le glow (effet rectangulaire)
            glow_size = grid_size + 4
            glow_surface = pygame.Surface((glow_size, glow_size), pygame.SRCALPHA)
            pygame.draw.rect(glow_surface, (*self.glow_color, alpha // 3), (0, 0, glow_size, glow_size))
            surface.blit(glow_surface,
                        (x * grid_size - 2, y * grid_size - 2))

            # Dessiner la traînée
            pygame.draw.rect(surface, self.color,
                            (x * grid_size, y * grid_size, grid_size, grid_size))

        # Dessiner la tête du joueur
        if self.positions:
            head_x, head_y = self.positions[-1]            # Glow de la tête (effet rectangulaire)
            head_glow_size = grid_size + 8
            head_glow_surface = pygame.Surface((head_glow_size, head_glow_size), pygame.SRCALPHA)
            pygame.draw.rect(head_glow_surface, (*self.glow_color, 150),
                              (0, 0, head_glow_size, head_glow_size))
            surface.blit(head_glow_surface,
                        (head_x * grid_size - 4, head_y * grid_size - 4))

            # Tête
            pygame.draw.rect(surface, WHITE,
                            (head_x * grid_size - 2, head_y * grid_size - 2,
                             grid_size + 4, grid_size + 4))
            pygame.draw.rect(surface, self.color,
                            (head_x * grid_size, head_y * grid_size,
                             grid_size, grid_size))
