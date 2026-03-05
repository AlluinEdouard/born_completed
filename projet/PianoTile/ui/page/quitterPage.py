import pygame
from ui.page.basePage import BasePage

class QuitterPage(BasePage):
    def affichagePage(self):
        center_x = self._windowManager.getScreenWidth() // 2
        center_y = self._windowManager.getScreenHeight() // 2

        # Texte de confirmation
        texte_quitter = self._windowManager.getFontTall().render(
            "Êtes-vous sûr de vouloir quitter le jeu ?", True, self._windowManager.getColor().getBlanc()
        )
        self._windowManager.getWindow().blit(
            texte_quitter, texte_quitter.get_rect(center=(center_x, center_y - 100))
        )

        # Bouton Non (rouge)
        rect_non = pygame.Rect(center_x - 210, center_y, 180, 60)
        pygame.draw.rect(self._windowManager.getWindow(), self._windowManager.getColor().getRouge(), rect_non)
        texte_non = self._windowManager.getFontTall().render("Non", True, self._windowManager.getColor().getBlanc())
        self._windowManager.getWindow().blit(texte_non, texte_non.get_rect(center=rect_non.center))

        # Bouton Oui (vert)
        rect_oui = pygame.Rect(center_x + 30, center_y, 180, 60)
        pygame.draw.rect(self._windowManager.getWindow(), self._windowManager.getColor().getVert(), rect_oui)
        texte_oui = self._windowManager.getFontTall().render("Oui", True, self._windowManager.getColor().getBlanc())
        self._windowManager.getWindow().blit(texte_oui, texte_oui.get_rect(center=rect_oui.center))
