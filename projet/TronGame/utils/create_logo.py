"""
Créer un logo Tron simple pour le jeu
"""

import os
import pygame

# Initialiser pygame
pygame.init()

# Vérifier si le dossier images existe
images_dir = os.path.join("assets", "images")
os.makedirs(images_dir, exist_ok=True)

# Dimensions du logo
logo_width = 600
logo_height = 200

# Créer une surface pour le logo
logo = pygame.Surface((logo_width, logo_height), pygame.SRCALPHA)

# Couleurs
BLACK = (0, 0, 0, 0)  # Transparent
BLUE = (0, 150, 255)
CYAN = (0, 255, 255)
WHITE = (255, 255, 255)

# Remplir avec du noir transparent
# logo.fill(BLUE)

# Texte "TRON"
font_size = 150
font = pygame.font.Font(None, font_size)
text = font.render("TRON", True, WHITE)
text_rect = text.get_rect(center=(logo_width // 2, logo_height // 2))

# Effet de glow
glow_sizes = [10, 6, 3]
for size in glow_sizes:
    glow_surface = pygame.Surface((text_rect.width + size * 2, text_rect.height + size * 2), pygame.SRCALPHA)
    glow_color = (0, 150 + size * 10, 255)  # RGB sans alpha
    pygame.draw.rect(glow_surface, glow_color, glow_surface.get_rect(), 0)
    logo.blit(glow_surface, (text_rect.x - size, text_rect.y - size))

# Texte principal
logo.blit(text, text_rect)

# Ajouter une grille style Tron derrière le texte
grid_spacing = 20
grid_color = (0, 100, 200, 100)

# Lignes horizontales
# for y in range(0, logo_height, grid_spacing):
#     pygame.draw.line(logo, grid_color, (0, y), (logo_width, y), 1)

# # Lignes verticales
# for x in range(0, logo_width, grid_spacing):
#     pygame.draw.line(logo, grid_color, (x, 0), (x, logo_height), 1)

# contour
pygame.draw.rect(logo, CYAN, (0, 0, logo_width, logo_height), 2)

#save l'image
pygame.image.save(logo, os.path.join(images_dir, "tron_logo.png"))

print(f"Logo créé avec succès: {os.path.join(images_dir, 'tron_logo.png')}")
