#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe AI du jeu Tron
Définit le comportement de l'IA
"""

import random
from player import Player
from direction import Direction
from config import AI_DIFFICULTY_SETTINGS

class AI(Player):
    def __init__(self, x, y, color, glow_color, difficulty="moyen"):
        super().__init__(x, y, color, glow_color)
        self.difficulty = difficulty

        # Utiliser les paramètres de difficulté depuis le fichier de configuration
        difficulty_settings = AI_DIFFICULTY_SETTINGS.get(difficulty, AI_DIFFICULTY_SETTINGS["moyen"])

        self.look_ahead = difficulty_settings["look_ahead"]
        self.last_ai_update = 0
        self.ai_update_interval = difficulty_settings["update_interval"]

    def get_available_moves(self, grid):
        """Renvoie les directions possibles sans collision immédiate"""
        available = []
        head_x, head_y = self.positions[-1]
        grid_height = len(grid)
        grid_width = len(grid[0])

        for direction in list(Direction):
            # Ne pas faire demi-tour
            current_dx, current_dy = self.direction.value
            new_dx, new_dy = direction.value
            if current_dx + new_dx == 0 and current_dy + new_dy == 0:
                continue

            # Vérifier si la direction est libre
            dx, dy = direction.value
            new_x, new_y = head_x + dx, head_y + dy

            # Vérifier les limites de la grille
            if new_x < 0 or new_x >= grid_width or new_y < 0 or new_y >= grid_height:
                continue

            # Vérifier la collision avec les traînées
            if grid[new_y][new_x] != 0:
                continue

            available.append(direction)

        return available

    def evaluate_move(self, direction, grid, opponent_pos=None):
        """Évalue la qualité d'un mouvement dans une direction donnée"""
        head_x, head_y = self.positions[-1]
        dx, dy = direction.value

        # Simuler le mouvement
        look_ahead_x, look_ahead_y = head_x, head_y
        space_available = 0

        # Regarder devant
        for i in range(self.look_ahead):
            look_ahead_x += dx
            look_ahead_y += dy

            # Vérifier les limites
            if (look_ahead_x < 0 or look_ahead_x >= len(grid[0]) or
                look_ahead_y < 0 or look_ahead_y >= len(grid)):
                return space_available

            # Vérifier la collision
            if grid[look_ahead_y][look_ahead_x] != 0:
                return space_available

            space_available += 1

            # Si on a un adversaire, considérer sa position pour éviter la collision
            if opponent_pos and (look_ahead_x, look_ahead_y) == opponent_pos:
                return 0

        return space_available + self.calculate_open_space(look_ahead_x, look_ahead_y, grid)

    def calculate_open_space(self, x, y, grid, depth=3):
        """Calculer l'espace ouvert à partir d'une position (floodfill limité)"""
        if depth <= 0:
            return 0

        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
        space = 1  # Compte la case actuelle
        grid_height = len(grid)
        grid_width = len(grid[0])

        for dx, dy in directions:
            new_x, new_y = x + dx, y + dy

            # Vérifier les limites
            if new_x < 0 or new_x >= grid_width or new_y < 0 or new_y >= grid_height:
                continue

            # Vérifier si la case est libre
            if grid[new_y][new_x] == 0:
                # Marquer temporairement pour éviter de recompter
                grid[new_y][new_x] = -1
                space += self.calculate_open_space(new_x, new_y, grid, depth - 1)
                # Restaurer
                grid[new_y][new_x] = 0

        return space

    def update(self, current_time, grid, opponent=None):
        """Met à jour l'IA"""
        if current_time - self.last_ai_update < self.ai_update_interval:
            return False

        # Obtenir les mouvements possibles
        available_moves = self.get_available_moves(grid)

        if not available_moves:
            # Aucun mouvement possible, l'IA a perdu
            self.alive = False
            return True

        # Position de l'adversaire pour l'éviter
        opponent_pos = None
        if opponent and opponent.positions:
            opponent_pos = opponent.positions[-1]

        # Évaluer chaque mouvement
        best_move = None
        best_score = -1

        for move in available_moves:
            score = self.evaluate_move(move, grid, opponent_pos)

            # Ajouter un peu de hasard selon la difficulté
            if self.difficulty == "easy":
                score += random.randint(0, 5)
            elif self.difficulty == "medium":
                score += random.randint(0, 3)

            if score > best_score:
                best_score = score
                best_move = move

        # Changer de direction
        if best_move:
            self.change_direction(best_move)

        self.last_ai_update = current_time
        return False
