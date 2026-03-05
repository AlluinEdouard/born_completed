#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe Menu du jeu Tron
Définit le menu principal du jeu
"""

import pygame
import os
from menu_item import MenuItem
from config import *

class Menu:
    def __init__(self, screen):
        self.screen = screen
        self.width, self.height = screen.get_size()
        self.items = []
        self.selected_index = 0
        self.animation_frame = 0
        self.title_font = pygame.font.Font(None, 120)
        self.small_font = pygame.font.Font(None, 24)

        # Création des éléments du menu
        center_x = self.width // 2
        title_offset = 100
        spacing = 100

        # Initialisation des éléments du menu
        self.items.append(MenuItem("1 JOUEUR", (center_x, title_offset + spacing * 2), "single_player"))
        self.items.append(MenuItem("2 JOUEURS", (center_x, title_offset + spacing * 3), "two_players"))
        self.items.append(MenuItem("OPTIONS", (center_x, title_offset + spacing * 4), "options"))
        self.items.append(MenuItem("QUITTER", (center_x, title_offset + spacing * 5), "quit"))

        # Sélectionner le premier élément par défaut
        self.items[self.selected_index].selected = True        # Charger les effets sonores
        self.sound_select = None
        self.sound_navigate = None
        try:
            self.sound_select = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_SELECT))
            self.sound_navigate = pygame.mixer.Sound(os.path.join(SOUNDS_DIR, SOUND_NAVIGATE))
        except Exception as e:
            print(f"Warning: Impossible de charger les effets sonores: {e}")

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

        # Essayer de charger l'image du logo
        try:
            self.logo = pygame.image.load(os.path.join(IMAGES_DIR, LOGO_IMAGE))
            self.logo = pygame.transform.scale(self.logo, (600, 200))
            self.has_logo = True
        except Exception as e:
            print(f"Warning: Impossible de charger le logo: {e}")
            self.has_logo = False

    def handle_event(self, event):
        """Gère les événements du menu et retourne l'action sélectionnée le cas échéant"""
        if event.type == pygame.KEYDOWN:
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

            elif event.key in [pygame.K_RETURN, pygame.K_SPACE, pygame.K_r]:
                if self.sound_select:
                    self.sound_select.play()
                return self.items[self.selected_index].action

        return None

    def update(self):
        """Met à jour les animations du menu"""
        self.animation_frame = (self.animation_frame + 1) % 60

    def draw(self):
        """Dessine le menu"""
        # Arrière-plan
        self.screen.fill(BLACK)

        # Animation de grid de fond style Tron
        self.draw_grid_background()

        # Logo ou titre
        if self.has_logo:
            logo_rect = self.logo.get_rect(center=(self.width // 2, 120))
            self.screen.blit(self.logo, logo_rect)
        else:
            self.draw_title()        # Dessiner les éléments du menu
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
            f_text = " Quitter"
            f_render = self.small_font.render(f_text, True, WHITE)
            self.screen.blit(f_render, (250 + 40, key_y + 8))

    def draw_title(self):
        """Dessine le titre du jeu s'il n'y a pas de logo"""
        # Texte principal
        title_text = "TRON"
        title = self.title_font.render(title_text, True, CYAN)
        title_rect = title.get_rect(center=(self.width // 2, 100))

        # Dessiner un simple glow
        for offset in [6, 4, 2]:
            shadow_rect = title_rect.copy()
            shadow_rect.x -= offset
            shadow_rect.y -= offset
            shadow = self.title_font.render(title_text, True, BLUE)
            self.screen.blit(shadow, shadow_rect)

            shadow_rect = title_rect.copy()
            shadow_rect.x += offset
            shadow_rect.y += offset
            self.screen.blit(shadow, shadow_rect)

        # Texte principal au-dessus du glow
        self.screen.blit(title, title_rect)

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