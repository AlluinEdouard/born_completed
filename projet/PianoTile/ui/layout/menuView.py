import pygame
from core.pageState import PageState
from ui.page.connexionPage import ConnexionPage
from ui.page.detailPage import DetailPage
from ui.page.filtrerPage import FiltrerPage
from ui.page.gamePage import GamePage
from ui.page.helpPage import HelpPage
from ui.page.inscriptionPage import InscriptionPage
from ui.page.profilPage import ProfilPage
from ui.page.quitterPage import QuitterPage
from ui.page.statistiquePage import StatistiquePage 

class MenuView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager
        self.__pageViews = {
            PageState.PROFIL: ProfilPage(windowManager),
            PageState.INSCRIPTION: InscriptionPage(windowManager),
            PageState.CONNEXION: ConnexionPage(windowManager),
            PageState.FILTRER: FiltrerPage(windowManager),
            PageState.AIDE: HelpPage(windowManager),
            PageState.DETAIL: DetailPage(windowManager),
            PageState.PLAY: GamePage(windowManager),
            # PageState.MULTIJOUEUR: MultijoueurPage(windowManager),
            PageState.STATISTIQUE: StatistiquePage(windowManager),
            PageState.QUITTER: QuitterPage(windowManager),
        }

    def getPage(self):
        if self.__windowManager.getInterface().getPage() != PageState.ACCUEIL:
            return self.__pageViews[self.__windowManager.getInterface().getPage()]

    def affichageMenu(self):
        window = self.__windowManager.getWindow()
        page = self.__windowManager.getInterface().getPage()

        if page != PageState.PLAY:
            center = (100, 100)
            color = self.__windowManager.getColor()

            # Cercles concentriques
            pygame.draw.circle(window, color.getViolet(), center, 80)
            pygame.draw.circle(window, color.getRose(), center, 75)
            pygame.draw.circle(window, color.getViolet(), center, 70)

            # Texte centré dans le cercle
            font = self.__windowManager.getFontTall()
            name = self.__windowManager.getCurrentUser().getName()
            text_surface = font.render(name, True, color.getBlanc())
            text_rect = text_surface.get_rect(center=center)
            window.blit(text_surface, text_rect)

            # Cercle icône aide
            pygame.draw.circle(self.__windowManager.getWindow(), self.__windowManager.getColor().getViolet(), (self.__windowManager.getScreenWidth() - 120, 100), 45)
            pygame.draw.circle(self.__windowManager.getWindow(), self.__windowManager.getColor().getRose(), (self.__windowManager.getScreenWidth() - 120, 100), 40)
            pygame.draw.circle(self.__windowManager.getWindow(), self.__windowManager.getColor().getViolet(), (self.__windowManager.getScreenWidth() - 120, 100), 35)

            # Boutons du haut (sauf pour la page "PROFIL")
            if page == PageState.ACCUEIL:
                rect = pygame.draw.rect(self.__windowManager.getWindow(), self.__windowManager.getColor().getViolet(), pygame.Rect(self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60))
                filter_filtres = self.__windowManager.getFontSmall().render("Filtrer", True, self.__windowManager.getColor().getBlanc())
                self.__windowManager.getWindow().blit(filter_filtres, filter_filtres.get_rect(center=rect.center))

            # Boutons du bas (global)
            rect_bouton = pygame.Rect(0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth(), 150)
            pygame.draw.rect(self.__windowManager.getWindow(), self.__windowManager.getColor().getViolet(), rect_bouton)

            texts = ["Accueil", "Multijoueur", "Statistique", "Quitter"]
            for i, text in enumerate(texts):
                x_center = self.__windowManager.getScreenWidth() * (i + 0.5) / 4
                text_surface = self.__windowManager.getFontTall().render(text, True, self.__windowManager.getColor().getBlanc())
                text_rect = text_surface.get_rect(center=(x_center, self.__windowManager.getScreenHeight() - 75))
                self.__windowManager.getWindow().blit(text_surface, text_rect)

            # Separateurs verticaux
            for i in range(1, 4):
                x = self.__windowManager.getScreenWidth() * i / 4
                pygame.draw.line(
                    self.__windowManager.getWindow(),
                    self.__windowManager.getColor().getRose(),
                    (x, self.__windowManager.getScreenHeight() - 150),
                    (x, self.__windowManager.getScreenHeight()),
                    5
                )
            
            # Cas special : different de ACCUEIL et SUITTER
            if page != PageState.ACCUEIL and page != PageState.QUITTER:
                rect_retour = pygame.draw.rect(self.__windowManager.getWindow(), self.__windowManager.getColor().getViolet(), pygame.Rect(self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60))
                texte_retour = self.__windowManager.getFontSmall().render("Retour", True, self.__windowManager.getColor().getBlanc())
                self.__windowManager.getWindow().blit(texte_retour, texte_retour.get_rect(center=rect_retour.center))

        # Appeler la methode d'affichage de la page courante
        if page in self.__pageViews.keys():
            self.__pageViews[page].affichagePage()
            
        elif page != PageState.ACCUEIL:
            # Par defaut, afficher un message ou rien
            font = self.__windowManager.getFontTall()
            color = self.__windowManager.getColor().getBlanc()
            text = font.render("Page inconnue", True, color)
            rect = text.get_rect(center=(self.__windowManager.getScreenWidth() // 2, 200))
            window.blit(text, rect)