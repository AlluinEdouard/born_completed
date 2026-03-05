import pygame
from ui.utils.image import Image

class MusicView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager

    def affichageListeMusique(self):
        """Affiche une liste defilante de musiques dans une zone scrollable."""

        surface = pygame.Surface(
            (self.__windowManager.getAreaMusic().width, self.__windowManager.getAreaMusic().height),
            pygame.SRCALPHA  # Active le canal alpha
        )
        surface.fill((0, 0, 0, 0))  # Transparent total : RGBA avec alpha = 0
        
        for index, music in enumerate(self.__windowManager.getInterface().getGame().getDatabase().getMusicSorted(self.__windowManager.getSorted().getDifficulte(), self.__windowManager.getSorted().getAnnee())):
            top_y = index * 210 - self.__windowManager.getScrollOffset()
            if top_y + 200 < 0 or top_y > self.__windowManager.getAreaMusic().height:
                continue  # hors de la zone visible

            # Rectangle global
            rect = pygame.Rect(0, top_y, self.__windowManager.getAreaMusic().width, 200)
            pygame.draw.rect(surface, self.__windowManager.getColor().getRose(), rect)

            # Couverture
            cover = music[1].upper().replace(" ", "").replace("'", "").replace(",", "").replace("e", "e")
            surface.blit(getattr(Image.Cover, cover), getattr(Image.Cover, cover).get_rect(left=rect.left, centery=rect.centery))

            # Titre
            title = self.__windowManager.getFontSmall().render(f"{music[1]} - {music[2]}", True, self.__windowManager.getColor().getBlanc())
            surface.blit(title, title.get_rect(left=rect.left + 250, centery=rect.top + 60))

            # Bouton Details
            rect_bouton = pygame.Rect(rect.left + 250, rect.top + 135, 200, 50)
            pygame.draw.rect(surface, self.__windowManager.getColor().getViolet(), rect_bouton)
            detail_button_text = self.__windowManager.getFontSmall().render("Details", True, self.__windowManager.getColor().getBlanc())
            surface.blit(detail_button_text, detail_button_text.get_rect(center=rect_bouton.center))

            # Difficulte
            # Choisir la couleur selon la difficulte
            if music[5] == "Easy":
                color = self.__windowManager.getColor().getVert()  # Vert
            elif music[5] == "Medium":
                color = self.__windowManager.getColor().getOrange()  # Orange (ajoute cette couleur si elle n’existe pas)
            elif music[5] == "Hard":
                color = self.__windowManager.getColor().getRouge()  # Rouge
            else:
                color = self.__windowManager.getColor().getBlanc()  # Par defaut

            pygame.draw.polygon(surface, color, [(rect.right - 210, rect.top), (rect.right - 250, rect.top), (rect.right - 210, rect.top + 49)])
            rect_diff = pygame.Rect(rect.right - 210, rect.top, 210, 50)
            pygame.draw.rect(surface, color, rect_diff)
            surface.blit(self.__windowManager.getFontSmall().render(f"{music[5]}", True, self.__windowManager.getColor().getBlanc()), self.__windowManager.getFontSmall().render(f"{music[5]}", True, self.__windowManager.getColor().getRose()).get_rect(center=rect_diff.center))

            # Bouton Play
            rect_play = pygame.Rect(rect.right - 210, rect.top + 70, 200, 115)
            pygame.draw.rect(surface, self.__windowManager.getColor().getViolet(), rect_play)
            surface.blit(self.__windowManager.getFontSmall().render("Play", True, self.__windowManager.getColor().getBlanc()), self.__windowManager.getFontSmall().render("Play", True, self.__windowManager.getColor().getRose()).get_rect(center=rect_play.center))

        self.__windowManager.getWindow().blit(surface, self.__windowManager.getAreaMusic().topleft, area=pygame.Rect(0, 0, self.__windowManager.getAreaMusic().width, self.__windowManager.getAreaMusic().height))
