import pygame, sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from core.pageState import PageState
from ui.interface import Interface
from core.logic import Logic
from data.database import Database

class Game:
    def __init__(self):
        self.__database: Database = Database()
        self.__interface: Interface = Interface(self)
        self.__logic: Logic = Logic(self)

# ----------------------------------- Getter ----------------------------------- #

    def getDatabase(self):
        """Getter de la base de donnees."""
        return self.__database

    def getInterface(self): 
        """Getter de l'interface."""
        return self.__interface
    
    def getLogic(self):
        """Getter de la logique."""
        return self.__logic

# ----------------------------------- Setter ----------------------------------- #

    def PageProfil(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageProfil()
        self.getLogic().actionPageProfil()

    def PageConnexion(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageConnexion()
        self.getLogic().actionPageConnexion()

    def PageInscription(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageInscription()
        self.getLogic().actionPageInscription()

    def PageFiltrer(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageFiltrer()
        self.getLogic().actionPageFiltrer()
    
    def PageAide(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageAide()
        self.getLogic().actionPageAide()
    
    def PageDetail(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageDetail()
        self.getLogic().actionPageDetail()
    
    def PagePlay(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePagePlay()
        self.getLogic().actionPagePlay()
    
    def PageAccueil(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageAccueil()
        self.getLogic().actionPageAccueil()

    def PageMultijoueur(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageMultijoueur()
        self.getLogic().actionPageMultijoueur()

    def PageStatistique(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageStatistique()
        self.getLogic().actionPageStatistique()

    def PageQuitter(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageQuitter()
        self.getLogic().actionPageQuitter()

    def PageFinGagne(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageFinGagne()
        self.getLogic().actionPageFinGagne()

    def PageFinPerdu(self):
        if (self.getInterface().getUpdate()):
            self.getInterface().affichagePageFinPerdu()
        self.getLogic().actionPageFinPerdu()

if __name__ == "__main__":
    pygame.init()
    
    Game = Game()

    while Game.getInterface().getPage() not in [PageState.FERMER]:
        if Game.getInterface().getPage() is PageState.PROFIL:
            Game.PageProfil()

        elif Game.getInterface().getPage() is PageState.CONNEXION:
            Game.PageConnexion()

        elif Game.getInterface().getPage() is PageState.INSCRIPTION:
            Game.PageInscription()

        elif Game.getInterface().getPage() is PageState.FILTRER:
            Game.PageFiltrer()

        elif Game.getInterface().getPage() is PageState.AIDE:
            Game.PageAide()

        elif Game.getInterface().getPage() is PageState.DETAIL:
            Game.PageDetail()

        elif Game.getInterface().getPage() is PageState.PLAY:
            Game.PagePlay()

        elif Game.getInterface().getPage() is PageState.ACCUEIL:
            Game.PageAccueil()

        elif Game.getInterface().getPage() is PageState.MULTIJOUEUR:
            Game.PageMultijoueur()
        
        elif Game.getInterface().getPage() is PageState.STATISTIQUE:
            Game.PageStatistique()

        elif Game.getInterface().getPage() is PageState.QUITTER:
            Game.PageQuitter()
        
        elif Game.getInterface().getPage() is PageState.FINGAGNE:
            Game.PageFinGagne()

        elif Game.getInterface().getPage() is PageState.FINPERDU:
            Game.PageFinPerdu() 

        # Mettre a jour l'affichage
        pygame.display.update()

