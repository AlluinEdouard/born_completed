import pygame

from core.pageState import PageState
from ui.layout.selectionView import SelectionView

class SortedView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager
        self.__difficulte_list = [[diff, True] for diff in ["Easy", "Medium", "Hard"]]
        self.__annee_list = [[annee, True] for annee in self.__windowManager.getInterface().getGame().getDatabase().getAllAnnees()]
        self.__sorted = [self.__difficulte_list, self.__annee_list]  # Valeurs selectionnees par defaut
    
    def getWindowManager(self):
        return self.__windowManager
    
    def getSorted(self):
        return self.__sorted
    
    def getDifficulte(self):
        return [d[0] for d in self.__difficulte_list if d[1]]

    def getAnnee(self):
        return [a[0] for a in self.__annee_list if a[1]]

    def setSorted(self, sorted):
        """ Met à jour le filtre des musiques. """
        if (sorted):
            self.__sorted = sorted
    
    def changeFilter(self, name):
        """Met à False la selection d'un filtre (difficulte ou annee) selon son nom."""
        
        if name.startswith("Difficulte "):
            valeur = name[len("Difficulte "):]  # Extrait "Medium" par exemple
            for item in self.__difficulte_list:
                if item[0] == valeur:
                    if (item[1] == False):
                        item[1] = True
                    else:
                        item[1] = False
                    self.__windowManager.setSelection(SelectionView(self.__windowManager))
                    return


        elif name.startswith("Annee "):
            valeur = name[len("Annee "):]  # Extrait "2013" par exemple
            for item in self.__annee_list:
                if item[0] == valeur:
                    if (item[1] == False):
                        item[1] = True
                    else:
                        item[1] = False
                    self.__windowManager.setSelection(SelectionView(self.__windowManager))
                    return

    def affichageSorted(self):
        screen = self.__windowManager.getWindow()
        screen_width = self.__windowManager.getScreenWidth()
        screen_height = self.__windowManager.getScreenHeight()
        font = self.__windowManager.getFontSmall()
        color = self.__windowManager.getColor()
        center_x = screen_width // 2

        # Affichage des boutons de difficulte
        start_x_d = center_x - (len(self.__difficulte_list) - 1) * 220 // 2 - 100
        for i, (diff, selected) in enumerate(self.__difficulte_list):
            rect = pygame.Rect(start_x_d + i * 220, screen_height // 2 - 100, 200, 50)
            couleur = color.getVert() if selected else color.getRouge()
            pygame.draw.rect(screen, couleur, rect)
            texte = font.render(diff, True, color.getBlanc())
            screen.blit(texte, texte.get_rect(center=rect.center))

        # Affichage des boutons d'annee
        start_x_a = center_x - (len(self.__annee_list) - 1) * 160 // 2 - 75
        for i, (annee, selected) in enumerate(self.__annee_list):
            rect = pygame.Rect(start_x_a + i * 160, screen_height // 2, 150, 50)
            couleur = color.getVert() if selected else color.getRouge()
            pygame.draw.rect(screen, couleur, rect)
            texte = font.render(str(annee), True, color.getBlanc())
            screen.blit(texte, texte.get_rect(center=rect.center))

