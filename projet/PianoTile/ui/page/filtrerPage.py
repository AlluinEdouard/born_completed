import pygame
from ui.page.basePage import BasePage

class FiltrerPage(BasePage):
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