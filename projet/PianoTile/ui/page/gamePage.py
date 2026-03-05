import pygame

from ui.page.basePage import BasePage

class GamePage(BasePage):
    def affichagePage(self):
        rect_retour = pygame.draw.rect(self._windowManager.getWindow(), self._windowManager.getColor().getViolet(), pygame.Rect(100, 20, 400, 50))
        texte_retour = self._windowManager.getFontSmall().render("Retour", True, self._windowManager.getColor().getBlanc())
        self._windowManager.getWindow().blit(texte_retour, texte_retour.get_rect(center=rect_retour.center))
        
        rect_play = pygame.draw.rect(self._windowManager.getWindow(), self._windowManager.getColor().getViolet(), pygame.Rect(self._windowManager.getScreenWidth() - 500, 20, 400, 50))
        texte_play = self._windowManager.getFontSmall().render("Play", True, self._windowManager.getColor().getBlanc())
        self._windowManager.getWindow().blit(texte_play, texte_play.get_rect(center=rect_play.center)) 

        # Centre de la zone de jeu
        play_width = self._windowManager.getGame().getPlayWidth()
        play_height = self._windowManager.getGame().getPlayHeight()
        padding_x = 100
        padding_y = 80
        surface = self._windowManager.getWindow()

        center_x = padding_x + play_width // 2
        center_y = padding_y + play_height // 2

        # --- Récupérer la musique sélectionnée ---
        selected_title = self._windowManager.getMusicSelect().replace("Play musique ", "")
        music_info = None
        for music in self._windowManager.getInterface().getGame().getDatabase().getMusicFromTitle(selected_title):
            if music[1].lower() in selected_title.lower():
                music_info = music
                break

        if music_info:
            # Taille et position de la surface floue
            w, h = 700, 300
            x = center_x - w // 2
            y = center_y - h // 2

            # Capturer le fond actuel à cet endroit
            fond_rect = pygame.Rect(x, y, w, h)
            fond_capture = surface.subsurface(fond_rect).copy()

            # Appliquer un flou léger (downscale puis upscale)
            small = pygame.transform.smoothscale(fond_capture, (w // 10, h // 10))
            blurry = pygame.transform.smoothscale(small, (w, h))

            # Créer une surface finale transparente
            surface_finale = pygame.Surface((w, h), pygame.SRCALPHA)
            surface_finale.blit(blurry, (0, 0))

            # Ajouter un voile violet semi-transparent
            voile = pygame.Surface((w, h), pygame.SRCALPHA)
            voile.fill((150, 100, 200, 100))
            surface_finale.blit(voile, (0, 0))

            # Blitter le tout
            surface.blit(surface_finale, (x, y))

            # --- Affichage des infos musicales avec les mêmes positions qu'avant ---
            titre_artiste = self._windowManager.getFontSmall().render(f"{music_info[1]} - {music_info[2]}", True, self._windowManager.getColor().getBlanc())
            surface.blit(titre_artiste, titre_artiste.get_rect(center=(center_x, center_y - 100)))

            difficulte = music_info[5]
            if difficulte == "Easy":
                diff_color = self._windowManager.getColor().getVert()
            elif difficulte == "Medium":
                diff_color = self._windowManager.getColor().getOrange()
            elif difficulte == "Hard":
                diff_color = self._windowManager.getColor().getRouge()
            else:
                diff_color = self._windowManager.getColor().getBlanc()

            rect_diff = pygame.Rect(0, 0, 180, 50)
            rect_diff.center = (center_x, center_y - 30)
            pygame.draw.rect(surface, diff_color, rect_diff)
            diff_text = self._windowManager.getFontSmall().render(difficulte, True, self._windowManager.getColor().getBlanc())
            surface.blit(diff_text, diff_text.get_rect(center=rect_diff.center))

            score_joueur = self._windowManager.getInterface().getGame().getDatabase().getBestScoreForUser(self._windowManager.getCurrentUser().getName(), music_info[0])

            score_global = self._windowManager.getInterface().getGame().getDatabase().getBestScoreAllUsers(music_info[0])
            if not score_joueur:
                score_text_1 = self._windowManager.getFontSmall().render("Ton meilleur score : 0", True, self._windowManager.getColor().getBlanc())
            else:
                score_text_1 = self._windowManager.getFontSmall().render(f"Ton meilleur score : {score_joueur}", True, self._windowManager.getColor().getBlanc())
            if not score_global:
                score_text_2 = self._windowManager.getFontSmall().render(f"Score max global : 0", True, self._windowManager.getColor().getBlanc())
            else:
                score_text_2 = self._windowManager.getFontSmall().render(f"Score max global : {score_global}", True, self._windowManager.getColor().getBlanc())
            
            surface.blit(score_text_1, score_text_1.get_rect(center=(center_x, center_y + 30)))
            surface.blit(score_text_2, score_text_2.get_rect(center=(center_x, center_y + 80)))