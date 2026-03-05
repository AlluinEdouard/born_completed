import pygame
from ui.page.basePage import BasePage

class HelpPage(BasePage):
    def affichagePage(self):
        screen = self._windowManager.getWindow()
        font = self._windowManager.getFontSmall()
        font_tall = self._windowManager.getFontTall()  # police pour les titres
        color = self._windowManager.getColor()
        button_descriptions = {
            "joueur1": {
                pygame.K_r: "Piano colonne 1",
                pygame.K_t: "Piano colonne 2",
                pygame.K_y: "Piano colonne 3",
                pygame.K_f: "Piano colonne 4",
                pygame.K_g: "Aucune action",
                pygame.K_h: "Valider",
            },
            "joueur2": {
                pygame.K_a: "Piano colonne 1",
                pygame.K_z: "Piano colonne 2",
                pygame.K_e: "Piano colonne 3",
                pygame.K_q: "Piano colonne 4",
                pygame.K_s: "Aucune action",
                pygame.K_d: "Valider",
            }
        }

        start_x_j1 = self._windowManager.getScreenWidth() // 2 - 400
        start_x_j2 = self._windowManager.getScreenWidth() // 2 + 100
        start_y = 300
        spacing_y = 60

        # Titres joueurs
        title_j1 = font_tall.render("Joueur 1", True, color.getBlanc())
        title_j2 = font_tall.render("Joueur 2", True, color.getBlanc())
        screen.blit(title_j1, (start_x_j1, start_y - 70))
        screen.blit(title_j2, (start_x_j2, start_y - 70))

        def draw_joystick_direction(surface, rect, direction, color_fg):
            pygame.draw.rect(surface, color_fg, rect, 3)
            cx, cy = rect.center
            offset = 12
            # Dessin simple de fleche selon direction
            if direction == "up":
                points = [(cx, cy - offset), (cx - 7, cy + 7), (cx + 7, cy + 7)]
            elif direction == "down":
                points = [(cx, cy + offset), (cx - 7, cy - 7), (cx + 7, cy - 7)]
            elif direction == "left":
                points = [(cx - offset, cy), (cx + 7, cy - 7), (cx + 7, cy + 7)]
            elif direction == "right":
                points = [(cx + offset, cy), (cx - 7, cy - 7), (cx - 7, cy + 7)]
            else:
                points = []
            if points:
                pygame.draw.polygon(surface, color_fg, points)

        # Pour chaque joueur, colonne gauche/droite
        for start_x, joueur in [(start_x_j1, "joueur1"), (start_x_j2, "joueur2")]:
            for i, (key, desc) in enumerate(button_descriptions[joueur].items()):
                y = start_y + i * spacing_y
                rect_button = pygame.Rect(start_x, y, 50, 50)

                # fond carre contour violet
                pygame.draw.rect(screen, color.getViolet(), rect_button, 3)

                # Pour les touches 1-4 : dessiner joystick direction
                if key in [pygame.K_r, pygame.K_t, pygame.K_y, pygame.K_f]:
                    # Definir la direction selon touche
                    if key == [pygame.K_r]:
                        direction = "up"
                    elif key == [pygame.K_t]:
                        direction = "down"
                    elif key == [pygame.K_y]:
                        direction = "left"
                    elif key == [pygame.K_f]:
                        direction = "right"
                    else:
                        direction = None
                    draw_joystick_direction(screen, rect_button, direction, color.getBlanc())
                else:
                    # Sinon texte touche normal
                    key_name = pygame.key.name(key).upper().replace("[", "").replace("]", "")
                    key_text = font.render(key_name, True, color.getBlanc())
                    key_rect = key_text.get_rect(center=rect_button.center)
                    screen.blit(key_text, key_rect)

                # Description a droite
                desc_text = font.render(desc, True, color.getBlanc())
                screen.blit(desc_text, (start_x + 70, y + 15))
