import random, pygame
from core.pageState import PageState
from ui.layout.selectionView import SelectionView
from ui.manager.windowManager import WindowManager
from ui.utils.image import Image

class Interface:
    def __init__(self, game) -> None:
        """Initialisation de l'interface."""
        self.__game = game
        self.__update: bool = True
        self.__page: PageState = PageState.ACCUEIL
        self.__pagePrecedente: PageState = PageState.ACCUEIL
        self.__pagePrecedenteQuitter: PageState = PageState.ACCUEIL
        self.__windowManager: WindowManager = WindowManager(self)
    
# ----------------------------------- Getter ----------------------------------- #
    
    def getGame(self):
        """Game du jeu."""
        return self.__game
    
    def getWindowManager(self):
        """Retourne la fenetre."""
        return self.__windowManager
    
    def getPage(self):
        """Retourne la page de l'interface."""
        return self.__page
    
    def getPagePrecedente(self):
        """Retourne la page precedente de l'interface."""
        return self.__pagePrecedente

    def getPagePrecedenteQuitter(self):
        """Retourne la page precedente de l'interface."""
        return self.__pagePrecedenteQuitter
    
    def getUpdate(self):
        """Retourne le booleen concernant la mise a jour de l'interface."""
        return self.__update
    
# ----------------------------------- Setter des elements ----------------------------------- #

    def setPage(self, page):
        """ Met à jour la page """
        if page != PageState.QUITTER:
            if (self.__page == PageState.QUITTER):
                self.setPagePrecedente(self.getPagePrecedenteQuitter())
                self.setPagePrecedenteQuitter(self.getPagePrecedente())
                self.__page = page
            else:
                self.setPagePrecedente(self.__page)
                self.__page = page
        else:
            self.setPagePrecedenteQuitter(self.getPagePrecedente())
            self.setPagePrecedente(self.__page)
            self.__page = page

    def setPagePrecedente(self, pagePrecedente):
        """ Met a jour la page precedente'. """
        self.__pagePrecedente = pagePrecedente
    
    def setPagePrecedenteQuitter(self, pagePrecedenteQuitter):
        """ Met a jour la page precedente quitter'. """
        self.__pagePrecedenteQuitter = pagePrecedenteQuitter

    def setUpdate(self, update):
        """ Met a jour l'interface'. """
        self.__update = update

# ----------------------------------- Affichage ----------------------------------- #

    def affichagePageProfil(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.PROFIL)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
    
    def affichagePageConnexion(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.PROFIL)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
    
    def affichagePageInscription(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.PROFIL)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()

    def affichagePageFiltrer(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.FILTRER)
        self.getWindowManager().getMusic().affichageListeMusique()
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSorted().affichageSorted()
        self.getWindowManager().getSelection().affichageSelection()

    def affichagePageAide(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.AIDE)   
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection() 

    def affichagePageDetail(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.DETAIL)
        self.getWindowManager().getMusic().affichageListeMusique()
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
        
    def affichagePagePlay(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.PLAY)
        self.getWindowManager().getGame().affichagePiano()
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
        
    def affichagePageAccueil(self): 
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.ACCUEIL) 
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getMusic().affichageListeMusique()
        self.getWindowManager().getSelection().affichageSelection()

    def affichagePageMultijoueur(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.MULTIJOUEUR)   
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection() 

    def affichagePageStatistique(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.STATISTIQUE)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
        
    def affichagePageQuitter(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.QUITTER)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
        
    def affichagePageFinPerdu(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.FIN_JEU_PERTE)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
                        
    def affichagePageFinGagne(self):
        self.getWindowManager().getBackground().affichageFondEcran(Image.Page.FIN_JEU_GAIN)
        self.getWindowManager().getMenu().affichageMenu()
        self.getWindowManager().getSelection().affichageSelection()
