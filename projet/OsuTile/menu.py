import pygame
import os
import sys
from config import (
    SCREEN_WIDTH,
    SCREEN_HEIGHT,
    BACKGROUND_COLOR,
    TEXT_COLOR,
    HIGHLIGHT_COLOR,
    FPS,
    BEATMAP_FOLDER,
    MENU_TITLE,
    SELECT_PROMPT,
    MENU_UP_KEY,
    MENU_DOWN_KEY,
    MENU_SELECT_KEY,
    MENU_QUIT_KEY,
    FULLSCREEN,
)
from game import play_map


def draw_gradient_background(screen, color1, color2):
    """Dessine un fond dégradé vertical."""
    for y in range(SCREEN_HEIGHT):
        ratio = y / SCREEN_HEIGHT
        r = int(color1[0] * (1 - ratio) + color2[0] * ratio)
        g = int(color1[1] * (1 - ratio) + color2[1] * ratio)
        b = int(color1[2] * (1 - ratio) + color2[2] * ratio)
        pygame.draw.line(screen, (r, g, b), (0, y), (SCREEN_WIDTH, y))


def neon_text(surface, text, font, pos, main_color, glow_color, glow_size=4):
    """Affiche un texte avec effet néon."""
    base = font.render(text, True, main_color)
    for dx in range(-glow_size, glow_size + 1):
        for dy in range(-glow_size, glow_size + 1):
            if dx * dx + dy * dy <= glow_size * glow_size:
                glow = font.render(text, True, glow_color)
                surface.blit(glow, (pos[0] + dx, pos[1] + dy))
    surface.blit(base, pos)


def run_menu():
    pygame.init()
    flags = pygame.FULLSCREEN if FULLSCREEN else 0
    screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT),flags)
    pygame.display.set_caption("Sélection de musique")
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("Arial Black", 48, bold=True)
    small_font = pygame.font.SysFont("Arial", 28, bold=True)

    beatmaps = [f for f in os.listdir(BEATMAP_FOLDER) if f.endswith(".osu")]
    selected_index = 0

    # Couleurs arcade flashy
    BG_TOP = (20, 20, 60)
    BG_BOTTOM = (60, 0, 60)
    NEON = (0, 255, 255)
    NEON_GLOW = (0, 180, 255)
    WHITE = (255, 255, 255)
    YELLOW = (255, 255, 0)
    SELECT_BG = (40, 0, 80)

    running = True
    while running:
        draw_gradient_background(screen, BG_TOP, BG_BOTTOM)

        # Titre avec effet néon
        title_text = MENU_TITLE
        title_pos = (SCREEN_WIDTH // 2 - font.size(title_text)[0] // 2, 60)
        neon_text(screen, title_text, font, title_pos, NEON, NEON_GLOW, 8)

        # Rectangle d'encadrement style arcade
        margin_x, margin_y = 120, 170
        rect_width = SCREEN_WIDTH - 2 * margin_x
        rect_height = 60 * len(beatmaps) + 40
        border_rect = pygame.Rect(margin_x, margin_y, rect_width, rect_height)
        pygame.draw.rect(screen, NEON, border_rect, 6)
        pygame.draw.rect(screen, WHITE, border_rect, 2)

        # Affichage des beatmaps
        for i, beatmap in enumerate(beatmaps):
            # Affichage sans extension et avec espaces à la place des underscores
            display_name = os.path.splitext(beatmap)[0].replace("_", " ")
            y = margin_y + 20 + i * 60
            x = SCREEN_WIDTH // 2
            if i == selected_index:
                # Fond de sélection flashy
                select_rect = pygame.Rect(margin_x + 10, y - 8, rect_width - 20, 54)
                pygame.draw.rect(screen, SELECT_BG, select_rect)
                pygame.draw.rect(screen, YELLOW, select_rect, 3)
                neon_text(
                    screen,
                    display_name,
                    small_font,
                    (x - small_font.size(display_name)[0] // 2, y),
                    YELLOW,
                    NEON_GLOW,
                    4,
                )
            else:
                neon_text(
                    screen,
                    display_name,
                    small_font,
                    (x - small_font.size(display_name)[0] // 2, y),
                    WHITE,
                    NEON_GLOW,
                    2,
                )

        # Prompt en bas
        prompt = SELECT_PROMPT + "  (H pour quitter)"
        prompt_pos = (
            SCREEN_WIDTH // 2 - small_font.size(prompt)[0] // 2,
            SCREEN_HEIGHT - 60,
        )
        neon_text(screen, prompt, small_font, prompt_pos, NEON, NEON_GLOW, 3)

        pygame.display.flip()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == MENU_UP_KEY:
                    selected_index = (selected_index - 1) % len(beatmaps)
                elif event.key == MENU_DOWN_KEY:
                    selected_index = (selected_index + 1) % len(beatmaps)
                elif event.key == MENU_SELECT_KEY:
                    result = play_map(beatmaps[selected_index])
                    if result == "quit":
                        running = False
                elif event.key == MENU_QUIT_KEY:
                    running = False

        clock.tick(FPS)

    pygame.quit()
    sys.exit()
