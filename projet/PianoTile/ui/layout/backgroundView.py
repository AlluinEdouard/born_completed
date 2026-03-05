import pygame

class BackgroundView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager
        
    def affichageFondEcran(self, ImageFond):
        """
            La fonction affichageFondEcran permet d'affichage l'illustration de fond du jeu
            :param ImageFond: L'image de fond à affichage (par defaut, None).
        """
        screen_width = self.__windowManager.getScreenWidth()
        screen_height = self.__windowManager.getScreenHeight()

        fond_redimensionne = pygame.transform.scale(ImageFond, (screen_width, screen_height))

        self.__windowManager.getWindow().blit(fond_redimensionne, (0, 0))
