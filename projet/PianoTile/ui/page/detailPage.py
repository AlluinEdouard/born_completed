import pygame
from ui.page.basePage import BasePage

class DetailPage(BasePage):
    def affichagePage(self):
        # Taille de la surface
        x, y = 50, 200
        w, h = self._windowManager.getScreenWidth() - 100, self._windowManager.getScreenHeight() - 375

        # Capturer le fond actuel a cet endroit
        fond_rect = pygame.Rect(x, y, w, h)
        fond_capture = self._windowManager.getWindow().subsurface(fond_rect).copy()

        # Appliquer un flou en downscalant/upscalant
        small = pygame.transform.smoothscale(fond_capture, (w // 10, h // 10))
        blurry = pygame.transform.smoothscale(small, (w, h))

        # Creer une surface finale transparente
        surface_finale = pygame.Surface((w, h), pygame.SRCALPHA)
        surface_finale.blit(blurry, (0, 0))

        # Ajouter un voile semi-transparent violet
        voile = pygame.Surface((w, h), pygame.SRCALPHA)
        voile.fill((150, 100, 200, 100))  # (R, G, B, alpha)
        surface_finale.blit(voile, (0, 0))

        # Dessiner le tout sur la fenetre
        self._windowManager.getWindow().blit(surface_finale, (x, y))

        # Requete dans la base
        music_info = self._windowManager.getInterface().getGame().getDatabase().getMusicFromTitle(self._windowManager.getMusicSelect())

        if music_info:  # Verifie qu'on a bien un resultat
            music = music_info[0]  # (id, title, artist, genre, release_date)

            titre_text = self._windowManager.getFontTall().render(
                f"Titre : {music[1]}",
                True,
                self._windowManager.getColor().getBlanc()
            )
            self._windowManager.getWindow().blit(
                titre_text, 
                titre_text.get_rect(center=(self._windowManager.getScreenWidth() // 2, y + 100))
            )

            artiste_text = self._windowManager.getFontTall().render(
                f"Artiste : {music[2]}",
                True,
                self._windowManager.getColor().getBlanc()
            )
            self._windowManager.getWindow().blit(
                artiste_text, 
                artiste_text.get_rect(center=(self._windowManager.getScreenWidth() // 2, y + 200))
            )

            genre_text = self._windowManager.getFontTall().render(
                f"Genre : {music[3]}",
                True,
                self._windowManager.getColor().getBlanc()
            )
            self._windowManager.getWindow().blit(
                genre_text, 
                genre_text.get_rect(center=(self._windowManager.getScreenWidth() // 2, y + 300))
            )

            date_text = self._windowManager.getFontTall().render(
                f"Sortie : {self._windowManager.getInterface().getGame().getDatabase().getReleaseDateFormatted(music[0])}",
                True,
                self._windowManager.getColor().getBlanc()
            )
            self._windowManager.getWindow().blit(
                date_text, 
                date_text.get_rect(center=(self._windowManager.getScreenWidth() // 2, y + 400))
            )
        else:
            erreur_text = self._windowManager.getFontTall().render(
                "Musique introuvable",
                True,
                self._windowManager.getColor().getBlanc()
            )
            self._windowManager.getWindow().blit(
                erreur_text,
                erreur_text.get_rect(center=(self._windowManager.getScreenWidth() // 2, y + 100))
            )
