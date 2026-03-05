import pygame
from ui.page.basePage import BasePage

class StatistiquePage(BasePage):
    def getTotalClassement(self):
        return self._windowManager.getInterface().getGame().getDatabase().getTotalScoresByPlayer()

    def getScoresParMusiqueTrie(self):
        raw_scores = self._windowManager.getInterface().getGame().getDatabase().getScoresByMusic()
        scores_par_musique = {}
        for player_name, music_title, score in raw_scores:
            scores_par_musique.setdefault(music_title, []).append((player_name, score))
        for music_title in scores_par_musique:
            scores_par_musique[music_title].sort(key=lambda x: x[1], reverse=True)
        return scores_par_musique

    def drawPodiumIcon(self, screen, x, y, radius, position):
        colors = [self._windowManager.getColor().getJaune(), self._windowManager.getColor().getGris(), self._windowManager.getColor().getBronze()]
        color = colors[position]
        pygame.draw.circle(screen, color, (x, y), radius)
        font = self._windowManager.getFontTall()
        text_surface = font.render(str(position + 1), True, (255, 255, 255))
        text_rect = text_surface.get_rect(center=(x, y))
        screen.blit(text_surface, text_rect)

    def affichagePage(self):
        screen = self._windowManager.getWindow()
        font = self._windowManager.getFontSmall()
        font_tall = self._windowManager.getFontTall()
        color = self._windowManager.getColor()

        screen_width = self._windowManager.getScreenWidth()
        screen_height = self._windowManager.getScreenHeight()

        white = color.getBlanc()
        violet = color.getViolet()

        # --- Podium total ---
        podium_y = 250
        podium_width = 350
        podium_height = 60
        spacing_x = 40

        classement_total = self.getTotalClassement()
        total_podium_width = podium_width * 3 + spacing_x * 2
        start_x_podium = (screen_width - total_podium_width) // 2

        for i, (player_name, total_score) in enumerate(classement_total[:3]):
            x = start_x_podium + i * (podium_width + spacing_x)
            rect = pygame.Rect(x, podium_y, podium_width, podium_height)
            pygame.draw.rect(screen, violet, rect)

            # Texte du joueur
            text_str = f"{player_name} : {total_score} pts"
            text_surface = font.render(text_str, True, white)
            text_width = text_surface.get_width()
            icon_radius = 20
            icon_margin = 10  # Espace entre l’icône et le texte
            total_width = icon_radius * 2 + icon_margin + text_width

            # Centrage horizontal du bloc (icône + texte)
            center_x = rect.centerx
            start_x = center_x - total_width // 2

            # Position icône
            icon_x = start_x + icon_radius
            icon_y = rect.centery
            self.drawPodiumIcon(screen, icon_x, icon_y, icon_radius, i)

            # Position texte
            text_x = icon_x + icon_radius + icon_margin
            text_y = rect.centery - text_surface.get_height() // 2
            screen.blit(text_surface, (text_x, text_y))

        # --- Scores par musique ---
        scores_par_musique = self.getScoresParMusiqueTrie()
        start_y = podium_y + podium_height + 50

        num_columns = 2
        spacing_x = 40  # Espace entre les colonnes
        col_width = (screen_width - spacing_x * (num_columns - 1)) // num_columns - 10

        # Calcule la largeur totale et ajuste start_x pour centrer
        total_width = col_width * num_columns + spacing_x * (num_columns - 1)
        start_x = (screen_width - total_width) // 2

        row_height = 200
        spacing_y = 20

        for idx, (music_title, players_scores) in enumerate(scores_par_musique.items()):
            col = idx % num_columns
            row = idx // num_columns

            x = start_x + col * (col_width + spacing_x)
            y = start_y + row * (row_height + spacing_y)

            # Fond musique
            header_rect = pygame.Rect(x, y - 10, col_width, row_height)
            pygame.draw.rect(screen, violet, header_rect)

            # Titre musique centre
            header_text = font.render(music_title, True, white)
            header_text_rect = header_text.get_rect(center=(header_rect.centerx, y + 15))
            screen.blit(header_text, header_text_rect)

            # Podium top 3
            center_x = header_rect.centerx - 100
            top_y = y + 80
            side_y = top_y + 70
            icon_radius = 20
            icon_margin = 10

            positions = [
                (center_x, top_y),             # 🥇
                (center_x - 150, side_y),      # 🥈
                (center_x + 150, side_y)       # 🥉
            ]

            for i, (player_name, score) in enumerate(players_scores[:3]):
                icon_x, icon_y = positions[i]
                self.drawPodiumIcon(screen, icon_x, icon_y, icon_radius, i)

                # Texte centre horizontalement a droite de l'icône
                text_str = f"{player_name} : {score} pts"
                text_surface = font.render(text_str, True, white)
                text_rect = text_surface.get_rect()
                text_rect.midleft = (icon_x + icon_radius + icon_margin, icon_y)
                screen.blit(text_surface, text_rect)
