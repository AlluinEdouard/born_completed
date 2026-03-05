#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe OptionItem du jeu Tron
Définit un élément d'option dans le menu des options
"""

import pygame
from config import *

class OptionItem:
    def __init__(self, text, position, options, current_index=0, font_size=36):
        self.text = text
        self.position = position
        self.options = options
        self.current_index = current_index
        self.font = pygame.font.Font(None, font_size)
        self.option_font = pygame.font.Font(None, font_size - 8)
        self.selected = False
        self.hover_animation = 0
        self.color = NEON_BLUE

    def draw(self, surface):
        # Animation de sélection
        color = self.color
        size_offset = 0

        if self.selected:
            self.hover_animation = min(1.0, self.hover_animation + 0.05)
            color = NEON_PINK
            size_offset = int(self.hover_animation * 8)
        else:
            self.hover_animation = max(0.0, self.hover_animation - 0.05)
            size_offset = int(self.hover_animation * 8)

        # Texte principal (titre de l'option)
        label_text = f"{self.text}: "
        rendered_label = self.font.render(label_text, True, WHITE)
        label_rect = rendered_label.get_rect(midright=(self.position[0] - 20, self.position[1]))
        surface.blit(rendered_label, label_rect)

        # Valeur de l'option
        current_option = self.options[self.current_index]
        rendered_option = self.option_font.render(current_option, True, color)
        option_rect = rendered_option.get_rect(midleft=(self.position[0] + 20, self.position[1]))
        surface.blit(rendered_option, option_rect)

        # Flèches pour indiquer qu'on peut changer l'option
        if self.selected:
            # Flèche gauche
            if self.current_index > 0:
                left_arrow = self.option_font.render("◄", True, color)
                left_rect = left_arrow.get_rect(midright=(option_rect.left - 10, option_rect.centery))
                surface.blit(left_arrow, left_rect)

            # Flèche droite
            if self.current_index < len(self.options) - 1:
                right_arrow = self.option_font.render("►", True, color)
                right_rect = right_arrow.get_rect(midleft=(option_rect.right + 10, option_rect.centery))
                surface.blit(right_arrow, right_rect)

    def next_option(self):
        """Passe à l'option suivante"""
        if self.current_index < len(self.options) - 1:
            self.current_index += 1
            return True
        return False

    def prev_option(self):
        """Passe à l'option précédente"""
        if self.current_index > 0:
            self.current_index -= 1
            return True
        return False

    def get_current_option(self):
        """Renvoie l'option actuellement sélectionnée"""
        return self.options[self.current_index]
