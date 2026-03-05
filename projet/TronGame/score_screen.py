#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour l'écran de score du jeu Tron
Affiche les statistiques après une partie
"""

import pygame
import os
from config import *  # Importer toutes les constantes

class ScoreScreen:
    def __init__(self, screen, game_stats):
        self.screen = screen
        self.width, self.height = screen.get_size()
        self.stats = game_stats
        self.animation_frame = 0

        # Police de caractères
        self.title_font = pygame.font.Font(None, 72)
        self.subtitle_font = pygame.font.Font(None, 48)
        self.info_font = pygame.font.Font(None, 36)
        self.small_font = pygame.font.Font(None, 24)

        # Calculer des statistiques additionnelles
        self.calculate_additional_stats()

        # Animation pour l'apparition des éléments
        self.animation_progress = 0
        self.animation_speed = 0.02  # Vitesse d'animation

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
            # Continuer sans les images

        # Charger les effets sonores
        self.sound_navigate = None
        self.sound_select = None
        try:
            self.sound_navigate = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_NAVIGATE))
            self.sound_select = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_SELECT))
        except Exception as e:
            print(f"Warning: Impossible de charger les effets sonores: {e}")

    def calculate_additional_stats(self):
        """Calcule des statistiques supplémentaires pour l'affichage"""
        # Uniquement calcul de la distance parcourue (approximative)
        p1_distance = len(self.stats.get("player1_positions", [])) * GRID_SIZE
        p2_distance = len(self.stats.get("player2_positions", [])) * GRID_SIZE

        self.stats["p1_distance"] = p1_distance
        self.stats["p2_distance"] = p2_distance

    def handle_event(self, event):
        """Gère les événements de l'écran de score"""
        if event.type == pygame.KEYDOWN:
            if event.key in [pygame.K_SPACE, pygame.K_RETURN, pygame.K_r]:
                # Jouer le son de sélection si disponible
                if self.sound_select:
                    self.sound_select.play()
                return "replay"  # Signal pour rejouer
            elif event.key in [pygame.K_ESCAPE, pygame.K_f]:
                # Jouer le son de sélection si disponible
                if self.sound_select:
                    self.sound_select.play()
                return "menu"  # Signal pour retourner au menu

        return None  # Aucune action à effectuer

    def update(self):
        """Met à jour les animations de l'écran de score"""
        self.animation_frame = (self.animation_frame + 1) % 60

        # Progression de l'animation d'apparition
        if self.animation_progress < 1.0:
            self.animation_progress += self.animation_speed
        else:
            self.animation_progress = 1.0

    def draw(self):
        """Dessine l'écran de score"""
        # Arrière-plan
        self.screen.fill(BLACK)

        # Animation de grid de fond style Tron
        self.draw_grid_background()

        # Titre
        title_text = "RÉSULTATS DE LA PARTIE"
        self.draw_animated_text(title_text, self.title_font, CYAN,
                               (self.width // 2, 80), 0.1)

        # Afficher le gagnant
        winner = self.stats.get("winner", "Inconnu")
        winner_color = BLUE if winner == "Joueur 1" else ORANGE
        if winner == "Match nul":
            winner_color = WHITE

        winner_text = f"{winner} remporte la victoire!" if winner != "Match nul" else "Match nul!"
        self.draw_animated_text(winner_text, self.subtitle_font, winner_color,
                               (self.width // 2, 180), 0.2)

        # Afficher uniquement les distances parcourues
        p1_distance = self.stats.get("p1_distance", 0)
        p2_distance = self.stats.get("p2_distance", 0)

        # Joueur 1 distance avec mise en évidence
        p1_text = f"Joueur 1 - Distance parcourue: {p1_distance} pixels"
        self.draw_animated_text(p1_text, self.info_font, BLUE,
                               (self.width // 2, 280), 0.3)

        # Joueur 2 distance avec mise en évidence
        p2_text = f"Joueur 2 - Distance parcourue: {p2_distance} pixels"
        self.draw_animated_text(p2_text, self.info_font, ORANGE,
                               (self.width // 2, 340), 0.4)

        # Instructions - Mises en évidence
        instructions_y = self.height - 70
        
        # Afficher les touches de la borne d'arcade avec texte explicatif
        if hasattr(self, 'key_r_img') and hasattr(self, 'key_f_img') and self.key_r_img and self.key_f_img:
            key_y = self.height - 50
            key_size = 32
            
            # Touche R (Rejouer)
            self.screen.blit(self.key_r_img, (20, key_y))
            r_text = " Rejouer"
            r_render = self.small_font.render(r_text, True, WHITE)
            self.screen.blit(r_render, (60, key_y + 8))  # +8 pour centrer verticalement avec l'image
            
            # Touche F (Menu)
            self.screen.blit(self.key_f_img, (250, key_y))
            f_text = " Quitter"
            f_render = self.small_font.render(f_text, True, WHITE)
            self.screen.blit(f_render, (250 + 40, key_y + 8))
        else:
            # Texte standard si les images ne sont pas disponibles
            instructions_text = "Appuyez sur R pour rejouer ou F pour revenir au menu"
            self.draw_animated_text(instructions_text, self.small_font, WHITE,
                                   (self.width // 2, self.height - 50), 0.9)

    def draw_animated_text(self, text, font, color, position, delay_factor=0):
        """Dessine un texte avec une animation d'apparition"""
        # Calculer l'alpha basé sur la progression de l'animation et le facteur de délai
        animation_point = self.animation_progress - delay_factor
        alpha = max(0, min(255, int(animation_point * 255 / 0.3)))

        if alpha <= 0:
            return

        # Créer une surface pour le texte
        text_surface = font.render(text, True, color)
        text_surface.set_alpha(alpha)

        # Positionner et dessiner le texte
        text_rect = text_surface.get_rect(center=position)

        # Effet de "glow" pour style arcade si le texte est complètement visible
        if alpha >= 240:
            # Paramètres du glow
            glow_color = tuple(max(0, c - 100) for c in color)
            glow_size = 3

            for i in range(glow_size):
                glow_alpha = max(0, 150 - i * 40)
                if glow_alpha > 0:
                    glow = font.render(text, True, glow_color)
                    glow.set_alpha(glow_alpha)
                    glow_rect = glow.get_rect(center=position)
                    self.screen.blit(glow, (glow_rect.x - i, glow_rect.y))
                    self.screen.blit(glow, (glow_rect.x + i, glow_rect.y))
                    self.screen.blit(glow, (glow_rect.x, glow_rect.y - i))
                    self.screen.blit(glow, (glow_rect.x, glow_rect.y + i))

        # Dessiner le texte principal
        self.screen.blit(text_surface, text_rect)

    def draw_grid_background(self):
        """Dessine une grille de fond style Tron"""
        # Paramètres de la grille
        grid_spacing = 50
        grid_color_base = (0, 20, 40)
        grid_color_bright = (0, 50, 100)

        # Animation de la grille
        offset = self.animation_frame % grid_spacing

        # Lignes horizontales
        for y in range(0, self.height + grid_spacing, grid_spacing):
            y_pos = y - offset
            intensity = abs((y_pos % (self.height * 2)) - self.height) / self.height
            color = (
                int(grid_color_base[0] + (grid_color_bright[0] - grid_color_base[0]) * intensity),
                int(grid_color_base[1] + (grid_color_bright[1] - grid_color_base[1]) * intensity),
                int(grid_color_base[2] + (grid_color_bright[2] - grid_color_base[2]) * intensity)
            )
            pygame.draw.line(self.screen, color, (0, y_pos), (self.width, y_pos), 1)

        # Lignes verticales
        for x in range(0, self.width + grid_spacing, grid_spacing):
            x_pos = x - offset
            intensity = abs((x_pos % (self.width * 2)) - self.width) / self.width
            color = (
                int(grid_color_base[0] + (grid_color_bright[0] - grid_color_base[0]) * intensity),
                int(grid_color_base[1] + (grid_color_bright[1] - grid_color_base[1]) * intensity),
                int(grid_color_base[2] + (grid_color_bright[2] - grid_color_base[2]) * intensity)
            )
            pygame.draw.line(self.screen, color, (x_pos, 0), (x_pos, self.height), 1)
