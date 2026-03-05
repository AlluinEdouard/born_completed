#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe MenuItem du jeu Tron
Définit un élément de menu
"""

import pygame
from config import *

class MenuItem:
    def __init__(self, text, position, action, font_size=48):
        self.text = text
        self.position = position
        self.action = action
        self.font = pygame.font.Font(None, font_size)
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

        # Effet de "glow" pour style arcade
        glow_size = 3 + size_offset // 2
        glow_surface = self.font.render(self.text, True, color)

        for i in range(glow_size):
            alpha = 150 - i * 40
            if alpha > 0:
                glow = self.font.render(self.text, True, color)
                glow.set_alpha(alpha)
                glow_rect = glow.get_rect(center=(self.position[0], self.position[1]))
                surface.blit(glow, (glow_rect.x - i, glow_rect.y))
                surface.blit(glow, (glow_rect.x + i, glow_rect.y))
                surface.blit(glow, (glow_rect.x, glow_rect.y - i))
                surface.blit(glow, (glow_rect.x, glow_rect.y + i))

        # Texte principal
        rendered_text = self.font.render(self.text, True, WHITE)
        text_rect = rendered_text.get_rect(center=self.position)
        surface.blit(rendered_text, text_rect)        # Rectangle de sélection
        if self.selected:
            padding = 20 + size_offset
            pygame.draw.rect(
                surface,
                color,
                (text_rect.left - padding // 2, text_rect.top - padding // 2, text_rect.width + padding, text_rect.height + padding), 2
            )