#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe Options du jeu Tron
Définit le menu des options du jeu
"""

import pygame
import os
from option_item import OptionItem
from config import *

class Options:
    def __init__(self, screen, game_ref):
        self.screen = screen
        self.game_ref = game_ref
        self.width, self.height = screen.get_size()
        self.items = []
        self.selected_index = 0
        self.animation_frame = 0
        self.title_font = pygame.font.Font(None, 80)
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
            # Continuer sans les images

        # Création des options
        center_x = self.width // 2
        title_offset = 150
        spacing = 80

        # Difficulté
        self.difficulty_item = OptionItem(
            "Difficulté IA",
            (center_x, title_offset + spacing),
            ["Facile", "Moyen", "Difficile"],
            1  # Par défaut: Moyen
        )

        # Vitesse
        self.speed_item = OptionItem(
            "Vitesse",
            (center_x, title_offset + spacing * 2),
            ["Lente", "Normale", "Rapide"],
            1  # Par défaut: Normale
        )

        # Son
        self.sound_item = OptionItem(
            "Son",
            (center_x, title_offset + spacing * 3),
            ["Désactivé", "Activé"],
            1  # Par défaut: Activé
        )

        # Retour
        self.back_item = OptionItem(
            "Retour au menu",
            (center_x, title_offset + spacing * 4),
            [""],
            0
        )

        # Ajouter les options à la liste
        self.items = [
            self.difficulty_item,
            self.speed_item,
            self.sound_item,
            self.back_item
        ]

        # Sélectionner le premier élément par défaut
        self.items[self.selected_index].selected = True

        # Charger les effets sonores
        self.sound_select = None
        self.sound_navigate = None
        try:
            self.sound_select = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_SELECT))
            self.sound_navigate = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_NAVIGATE))
        except Exception as e:
            print(f"Warning: Impossible de charger les effets sonores: {e}")

    def handle_event(self, event):
        """Gère les événements du menu des options"""
        if event.type == pygame.KEYDOWN:
            # Navigation verticale (changer d'option)
            if event.key in [pygame.K_UP, pygame.K_w]:
                self.items[self.selected_index].selected = False
                self.selected_index = (self.selected_index - 1) % len(self.items)
                self.items[self.selected_index].selected = True
                if self.sound_navigate:
                    self.sound_navigate.play()

            elif event.key in [pygame.K_DOWN, pygame.K_s]:
                self.items[self.selected_index].selected = False
                self.selected_index = (self.selected_index + 1) % len(self.items)
                self.items[self.selected_index].selected = True
                if self.sound_navigate:
                    self.sound_navigate.play()

            # Navigation horizontale (changer la valeur de l'option)
            elif event.key == pygame.K_LEFT:
                if self.items[self.selected_index].prev_option():
                    if self.sound_navigate:
                        self.sound_navigate.play()
                    self.apply_option_change()

            elif event.key == pygame.K_RIGHT:
                if self.items[self.selected_index].next_option():
                    if self.sound_navigate:
                        self.sound_navigate.play()
                    self.apply_option_change()            # Sélection / Confirmation            elif event.key in [pygame.K_RETURN, pygame.K_SPACE, pygame.K_r]:
                if self.sound_select:
                    self.sound_select.play()

                # Si c'est l'option "Retour au menu"
                if self.selected_index == len(self.items) - 1:
                    return "menu"
                    
            # Retour au menu avec la touche F (comme Escape)
            elif event.key in [pygame.K_ESCAPE, pygame.K_f]:
                if self.sound_select:
                    self.sound_select.play()
                return "menu"

        return None

    def apply_option_change(self):
        """Applique les changements d'options"""
        # Difficulté
        if self.selected_index == 0:
            difficulty = self.difficulty_item.get_current_option().lower()
            self.game_ref.difficulty = difficulty

        # Vitesse
        elif self.selected_index == 1:
            speed = self.speed_item.get_current_option()
            if speed == "Lente":
                self.game_ref.move_delay = MOVE_DELAY * 1.5
            elif speed == "Normale":
                self.game_ref.move_delay = MOVE_DELAY
            elif speed == "Rapide":
                self.game_ref.move_delay = MOVE_DELAY * 0.7

        # Son
        elif self.selected_index == 2:
            sound_enabled = self.sound_item.get_current_option() == "Activé"
            if sound_enabled:
                pygame.mixer.unpause()
            else:
                pygame.mixer.pause()

    def update(self):
        """Met à jour les animations du menu des options"""
        self.animation_frame = (self.animation_frame + 1) % 60

    def draw(self):
        """Dessine le menu des options"""
        # Arrière-plan
        self.screen.fill(BLACK)

        # Animation de grid de fond style Tron
        self.draw_grid_background()

        # Titre
        title_text = "OPTIONS"
        title = self.title_font.render(title_text, True, CYAN)
        title_rect = title.get_rect(center=(self.width // 2, 80))
        self.screen.blit(title, title_rect)        # Dessiner les options
        for item in self.items:
            item.draw(self.screen)
            
        # Afficher les touches de la borne d'arcade
        if hasattr(self, 'key_r_img') and hasattr(self, 'key_f_img') and self.key_r_img and self.key_f_img:
            # Position des touches
            key_y = self.height - 50
            key_size = 32
            
            # Touche R (ENTER)
            self.screen.blit(self.key_r_img, (20, key_y))
            r_text = " Sélectionner"
            r_render = self.small_font.render(r_text, True, WHITE)
            self.screen.blit(r_render, (60, key_y + 8))  # +8 pour centrer verticalement avec l'image
            
            # Touche F (ESCAPE)
            self.screen.blit(self.key_f_img, (250, key_y))
            f_text = " Retour"
            f_render = self.small_font.render(f_text, True, WHITE)
            self.screen.blit(f_render, (250 + 40, key_y + 8))
        else:
            # Instructions textuelles si les images ne sont pas disponibles
            instructions_font = pygame.font.Font(None, 24)
            instructions = instructions_font.render("↑/↓: Naviguer   ←/→: Changer   R: Sélectionner   F: Retour", True, WHITE)
            instructions_rect = instructions.get_rect(center=(self.width // 2, self.height - 50))
            self.screen.blit(instructions, instructions_rect)

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
