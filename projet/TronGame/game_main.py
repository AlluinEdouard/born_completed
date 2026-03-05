#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe Game du jeu Tron
Définit la logique principale du jeu
"""

import pygame
import os
from player import Player
from ai import AI
from direction import Direction
from config import *

class Game:
    def __init__(self, screen, mode="single", difficulty="moyen", move_delay=None):
        self.screen = screen
        self.width, self.height = screen.get_size()
        self.mode = mode
        self.difficulty = difficulty
        self.game_over = False
        self.winner = None
        self.pause = False
        # Paramètres de vitesse
        self.move_delay = move_delay if move_delay is not None else MOVE_DELAY

        # Calcul de la taille de la grille
        self.cells_x = self.width // GRID_SIZE
        self.cells_y = self.height // GRID_SIZE        # Création de la grille (0 = vide, 1 = joueur 1, 2 = joueur 2)
        self.grid = [[0 for _ in range(self.cells_x)] for _ in range(self.cells_y)]        # Contrôles des joueurs
        player1_controls = {
            pygame.K_UP: Direction.UP,
            pygame.K_DOWN: Direction.DOWN,
            pygame.K_LEFT: Direction.LEFT,
            pygame.K_RIGHT: Direction.RIGHT
        }

        player2_controls = {
            pygame.K_o: Direction.UP,
            pygame.K_l: Direction.DOWN,
            pygame.K_k: Direction.LEFT,
            pygame.K_m: Direction.RIGHT
        }

        # Initialisation des joueurs
        self.player1 = Player(self.cells_x // 4, self.cells_y // 2,
                            BLUE, BLUE_GLOW, player1_controls, self.move_delay)

        if mode == "single":
            # IA comme adversaire
            self.player2 = AI(3 * self.cells_x // 4, self.cells_y // 2, ORANGE, ORANGE_GLOW, difficulty)
            self.player2.move_delay = self.move_delay
        else:
            # Deuxième joueur humain
            self.player2 = Player(3 * self.cells_x // 4, self.cells_y // 2,ORANGE, ORANGE_GLOW, player2_controls, self.move_delay)

        # Marquer les positions initiales
        self.update_grid()

        # Chargement des sons
        self.sound_crash = None
        self.sound_move = None
        try:
            self.sound_crash = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_CRASH))
            self.sound_move = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_MOVE))
        except Exception as e:
            print(f"Warning: Impossible de charger les effets sonores: {e}")
            # Continuer sans effets sonores        # Police
        self.font = pygame.font.Font(None, 48)
        self.small_font = pygame.font.Font(None, 24)

        # Images des touches pour la borne d'arcade
        self.key_r_img = None
        self.key_f_img = None
        try:
            self.key_r_img = pygame.image.load("./assets/images/key_r.png").convert_alpha()
            self.key_f_img = pygame.image.load("./assets/images/key_f.png").convert_alpha()
            # Redimensionner les images si nécessaire
            key_size = 32  # Taille souhaitée en pixels
            self.key_r_img = pygame.transform.scale(self.key_r_img, (key_size, key_size))
            self.key_f_img = pygame.transform.scale(self.key_f_img, (key_size, key_size))
        except Exception as e:
            print(f"Warning: Impossible de charger les images des touches: {e}")
            # Continuer sans les images        # Timer de jeu
        self.start_time = pygame.time.get_ticks()
        self.elapsed_time = 0
        
    def handle_event(self, event):
        """Gère les événements du jeu"""
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_p or event.key == pygame.K_f:
                # Mettre en pause / reprendre
                self.pause = not self.pause
            elif (event.key == pygame.K_SPACE or event.key == pygame.K_r) and self.game_over:
                # Redémarrer après game over
                self.__init__(self.screen, self.mode, self.difficulty)
            else:
                # Entrées des joueurs
                self.player1.handle_input(event.key)

                if self.mode == "multi":  # En mode 2 joueurs
                    self.player2.handle_input(event.key)

    def update(self):
        """Met à jour l'état du jeu, retourne True si game_over"""
        if self.pause or self.game_over:
            return self.game_over

        current_time = pygame.time.get_ticks()
        self.elapsed_time = (current_time - self.start_time) // 1000  # En secondes

        # Déplacer le joueur 1
        collision1 = self.player1.move(current_time, self.grid)

        # Déplacer le joueur 2 (IA ou humain)
        if isinstance(self.player2, AI):
            self.player2.update(current_time, self.grid, self.player1)
        collision2 = self.player2.move(current_time, self.grid)

        # Mise à jour de la grille
        self.update_grid()

        # Vérifier les collisions
        if collision1 or not self.player1.alive:
            self.player1.alive = False
            self.winner = "Joueur 2"
            self.game_over = True
            if self.sound_crash:
                self.sound_crash.play()

        if collision2 or not self.player2.alive:
            self.player2.alive = False
            if not self.game_over:  # Si le joueur 1 n'a pas déjà perdu
                self.winner = "Joueur 1"
                self.game_over = True
                if self.sound_crash:
                    self.sound_crash.play()

        # Si les deux joueurs sont morts en même temps (rare, mais possible)
        if not self.player1.alive and not self.player2.alive:
            self.winner = "Match nul"

        return self.game_over

    def update_grid(self):
        """Met à jour la grille avec les positions des joueurs"""
        # Réinitialiser la grille
        self.grid = [[0 for _ in range(self.cells_x)] for _ in range(self.cells_y)]

        # Marquer les positions du joueur 1
        for x, y in self.player1.positions:
            if 0 <= x < self.cells_x and 0 <= y < self.cells_y:
                self.grid[y][x] = 1

        # Marquer les positions du joueur 2
        for x, y in self.player2.positions:
            if 0 <= x < self.cells_x and 0 <= y < self.cells_y:
                self.grid[y][x] = 2

    def draw(self):
        """Dessine l'état actuel du jeu"""
        # Fond noir
        self.screen.fill(BLACK)

        # Dessiner une grille de fond subtile
        self.draw_background_grid()

        # Dessiner les joueurs
        self.player1.draw(self.screen, GRID_SIZE)
        self.player2.draw(self.screen, GRID_SIZE)

        # Afficher le score et le temps
        self.draw_hud()

        # Afficher le message de pause si nécessaire
        if self.pause:
            self.draw_pause_message()

        # Afficher le message de fin de partie si nécessaire
        if self.game_over:
            self.draw_game_over_message()

    def draw_background_grid(self):
        """Dessine une grille de fond style Tron"""
        grid_color = (10, 30, 50)

        # Lignes horizontales
        for y in range(0, self.height, GRID_SIZE * 5):
            pygame.draw.line(self.screen, grid_color, (0, y), (self.width, y), 1)

        # Lignes verticales
        for x in range(0, self.width, GRID_SIZE * 5):
            pygame.draw.line(self.screen, grid_color, (x, 0), (x, self.height), 1)

    def draw_hud(self):
        """Affiche l'interface utilisateur: score, temps, etc."""
        # Temps écoulé
        time_text = f"Temps: {self.elapsed_time}s"
        time_render = self.small_font.render(time_text, True, WHITE)
        self.screen.blit(time_render, (10, 10))

        # Mode de jeu
        mode_text = "Mode: 1 Joueur" if self.mode == "single" else "Mode: 2 Joueurs"
        mode_render = self.small_font.render(mode_text, True, WHITE)
        self.screen.blit(mode_render, (self.width - mode_render.get_width() - 10, 10))

        # Longueur des traînées
        p1_length = len(self.player1.positions)
        p2_length = len(self.player2.positions)

        # Score joueur 1
        p1_text = f"Joueur 1: {p1_length}"
        p1_render = self.small_font.render(p1_text, True, BLUE)
        self.screen.blit(p1_render, (10, 40))
        
        # Score joueur 2
        p2_text = f"Joueur 2: {p2_length}"
        p2_render = self.small_font.render(p2_text, True, ORANGE)
        self.screen.blit(p2_render, (self.width - p2_render.get_width() - 10, 40))
        
        # Afficher les touches de la borne d'arcade
        if self.key_r_img and self.key_f_img:
            # Position pour les touches (en bas de l'écran)
            key_y = self.height - 50
              # Touche R (ENTER)
            # self.screen.blit(self.key_r_img, (20, key_y))
            # r_text = "= Rejouer"
            # r_render = self.small_font.render(r_text, True, WHITE)
            # self.screen.blit(r_render, (60, key_y + 8))  # +8 pour centrer verticalement avec l'image
            
            # Touche F (ESCAPE)
            self.screen.blit(self.key_f_img, (self.width // 2, key_y))
            f_text = " Quitter"
            f_render = self.small_font.render(f_text, True, WHITE)
            self.screen.blit(f_render, (self.width // 2 + 40, key_y + 8))
            
    def draw_pause_message(self):
        """Affiche le message de pause"""
        # Fond semi-transparent
        overlay = pygame.Surface((self.width, self.height), pygame.SRCALPHA)
        overlay.fill((0, 0, 0, 128))
        self.screen.blit(overlay, (0, 0))

        # Message de pause
        pause_text = "PAUSE"
        pause_render = self.font.render(pause_text, True, WHITE)
        pause_rect = pause_render.get_rect(center=(self.width // 2, self.height // 2 - 30))
        self.screen.blit(pause_render, pause_rect)

        # Afficher l'image de la touche F avec texte explicatif
        if self.key_f_img:
            key_size = 48  # Taille un peu plus grande pour la visibilité
            key_img = pygame.transform.scale(self.key_f_img, (key_size, key_size))
            
            # Centrer l'ensemble image + texte
            text = "Appuyez pour continuer"
            text_render = self.small_font.render(text, True, WHITE)
            total_width = key_size + 10 + text_render.get_width()  # 10px d'espacement
            
            # Position de l'image
            key_x = self.width // 2 - total_width // 2
            key_y = self.height // 2 + 30
            self.screen.blit(key_img, (key_x, key_y))
            
            # Position du texte (à droite de l'image)
            text_x = key_x + key_size + 10
            text_y = key_y + (key_size - text_render.get_height()) // 2  # Centrer verticalement
            self.screen.blit(text_render, (text_x, text_y))

    def draw_game_over_message(self):
        """Affiche le message de fin de partie temporaire avant l'écran de score"""
        # Fond semi-transparent
        overlay = pygame.Surface((self.width, self.height), pygame.SRCALPHA)
        overlay.fill((0, 0, 0, 180))
        self.screen.blit(overlay, (0, 0))

        # Message de fin de partie
        game_over_text = "FIN DE PARTIE"
        game_over_render = self.font.render(game_over_text, True, RED)
        game_over_rect = game_over_render.get_rect(center=(self.width // 2, self.height // 2 - 60))
        self.screen.blit(game_over_render, game_over_rect)

        # Afficher le gagnant
        winner_text = f"{self.winner} gagne!" if self.winner != "Match nul" else "Match nul!"
        winner_color = BLUE if self.winner == "Joueur 1" else ORANGE
        if self.winner == "Match nul":
            winner_color = WHITE
        winner_render = self.font.render(winner_text, True, winner_color)
        winner_rect = winner_render.get_rect(center=(self.width // 2, self.height // 2))
        self.screen.blit(winner_render, winner_rect)

        # Afficher l'image de la touche R avec texte explicatif
        if self.key_r_img:
            key_size = 48  # Taille un peu plus grande pour la visibilité
            key_img = pygame.transform.scale(self.key_r_img, (key_size, key_size))
            
            # Centrer l'ensemble image + texte
            text = "Appuyez pour voir les résultats"
            text_render = self.small_font.render(text, True, WHITE)
            total_width = key_size + 10 + text_render.get_width()  # 10px d'espacement
            
            # Position de l'image
            key_x = self.width // 2 - total_width // 2
            key_y = self.height // 2 + 60
            self.screen.blit(key_img, (key_x, key_y))
            
            # Position du texte (à droite de l'image)
            text_x = key_x + key_size + 10
            text_y = key_y + (key_size - text_render.get_height()) // 2  # Centrer verticalement
            self.screen.blit(text_render, (text_x, text_y))

    def get_game_stats(self):
        """Renvoie un dictionnaire avec les statistiques de la partie pour l'écran de score"""
        return {
            "mode": self.mode,
            "winner": self.winner,
            "difficulty": self.difficulty,
            "elapsed_time": self.elapsed_time,
            "player1_positions": self.player1.positions,
            "player2_positions": self.player2.positions,
            "grid_width": self.cells_x,
            "grid_height": self.cells_y
        }