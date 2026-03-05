import pygame

from ui.page.basePage import BasePage

class ProfilPage(BasePage):
    def affichagePage(self):
        window = self._windowManager.getWindow()
        font = self._windowManager.getFontTall()
        color = self._windowManager.getColor().getBlanc()

        center_x = self._windowManager.getScreenWidth() // 2
        
        # Boutons connexion / inscription
        rect_connexion = pygame.Rect(center_x - 200, self._windowManager.getScreenHeight() // 2 - 80, 400, 60)
        pygame.draw.rect(window, self._windowManager.getColor().getViolet(), rect_connexion)
        texte_connexion = font.render("Se connecter", True, color)
        window.blit(texte_connexion, texte_connexion.get_rect(center=rect_connexion.center))

        rect_inscription = pygame.Rect(center_x - 200, self._windowManager.getScreenHeight() // 2 + 20, 400, 60)
        pygame.draw.rect(window, self._windowManager.getColor().getViolet(), rect_inscription)
        texte_inscription = font.render("S'inscrire", True, color)
        window.blit(texte_inscription, texte_inscription.get_rect(center=rect_inscription.center))
