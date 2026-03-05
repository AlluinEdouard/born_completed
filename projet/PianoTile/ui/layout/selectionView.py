import pygame
from core.pageState import PageState

class SelectionView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager
        self.__selections = {
            PageState.PROFIL: (
                (1, 0),
                self.create_PROFIL_selections()
            ),
            PageState.CONNEXION: (
                (1, 0),
                self.create_CONNEXION_selections()
            ),
            PageState.INSCRIPTION: (
                (1, 0),
                self.create_INSCRIPTION_selections()
            ),
            PageState.FILTRER: (
                (1, 0),
                self.create_FILTRER_selections()
            ),
            PageState.AIDE: (
                (1, 0),
                self.create_AIDE_selections()
            ),
            PageState.DETAIL: (
                (1, 0),
                self.create_DETAIL_selections()
            ),
            PageState.PLAY: (
                (1, 0),
                self.create_PLAY_selections()
            ),
            PageState.ACCUEIL: (
                (0, 0),
                self.create_ACCUEIL_selections()
            ),
            PageState.STATISTIQUE: (
                (1, 0),
                self.create_STATISTIQUE_selections()
            ),
            PageState.QUITTER: (
                (0, 1),
                self.create_QUITTER_selections()
            )
        }
        
    def getWindowManager(self):
        return self.__windowManager.getWindow()
    
    def getSelection(self):
        return self.__selections.get(self.__windowManager.getInterface().getPage())
    
    def getSelectionFull(self):
        page = self.__windowManager.getInterface().getPage()
        return self.__selections[page]
    
    def getPosition(self):
        page = self.__windowManager.getInterface().getPage()
        if page in self.__selections:
            return tuple(self.__selections[page][0])  # Cast en tuple
        return None

    def setPosition(self, position):
        """Met a jour la position de selection pour la page actuelle."""
        page = self.__windowManager.getInterface().getPage()
        if page in self.__selections:
            _, selection_dict = self.__selections[page]
            self.__selections[page] = (position, selection_dict)

    def setSelection(self, selection_dict):
        """Met a jour le dictionnaire de selection pour la page actuelle."""
        page = self.__windowManager.getInterface().getPage()
        if page in self.__selections:
            position, _ = self.__selections[page]
            self.__selections[page] = (position, selection_dict)

    def setSelectionFull(self, position, selection_dict):
        page = self.__windowManager.getInterface().getPage()
        self.__selections[page] = (position, selection_dict)

    def affichageSelection(self):
        selection = self.getSelectionFull()
        if not selection:
            return
        position_courante, dict_selection = selection
        for position, (shape, *params) in dict_selection.items():
            if tuple(position) == tuple(position_courante):
                if params[0] == "rectangle":
                    _, x, y, width, height, color = params
                    pygame.draw.rect(self.__windowManager.getWindow(), color, pygame.Rect(x, y, width, height), 5)
                elif params[0] == "cercle":
                    _, x, y, radius, color = params
                    pygame.draw.circle(self.__windowManager.getWindow(), color, (x, y), radius, 5)

    def create_PROFIL_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1), 
            (0, 2),
            (0, 3),
            (1, 3), 
            (2, 3),
            (3, 3)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0), (0, 1), (0, 2), (0, 3), (1, 3), (2, 3), (3, 3) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Se connecter", "rectangle", self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 - 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(0, 2)] = "S'inscrire", "rectangle",  self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 + 20, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(0, 3)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 3)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 3)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 3)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections
    
    def create_CONNEXION_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1), 
            (0, 2),
            (0, 3),
            (1, 3), 
            (2, 3),
            (3, 3)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0), (0, 1), (0, 2), (0, 3), (1, 3), (2, 3), (3, 3) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Nom d'utilisateur", "rectangle", self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 - 50, 400, 50, self.__windowManager.getColor().getBlanc()
            selections[(0, 2)] = "Mot de passe", "rectangle", self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 + 50, 400, 50, self.__windowManager.getColor().getBlanc()
            selections[(0, 3)] = "Valider", "rectangle",  self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 + 130, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(0, 4)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 4)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 4)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 4)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections
    
    def create_INSCRIPTION_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1), 
            (0, 2),
            (0, 3),
            (0, 4), 
            (0, 5), 
            (1, 5),
            (2, 5),
            (3, 5)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0), (0, 1), (0, 2), (0, 3), (0, 4), (0, 5), (1, 5), (2, 5), (3, 5) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Nom d'utilisateur", "rectangle", self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 - 130, 400, 50, self.__windowManager.getColor().getBlanc()
            selections[(0, 2)] = "Mot de passe", "rectangle", self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 - 30, 400, 50, self.__windowManager.getColor().getBlanc()
            selections[(0, 3)] = "Confirmer le mot de passe", "rectangle", self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 + 70, 400, 50, self.__windowManager.getColor().getBlanc()
            selections[(0, 4)] = "Valider", "rectangle",  self.__windowManager.getScreenWidth() // 2 - 200, self.__windowManager.getScreenHeight() // 2 + 150, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(0, 5)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 5)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 5)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 5)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections

    
    def create_FILTRER_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
        ]
        difficulte_list = ["Easy", "Medium", "Hard"]
        annee_list = self.__windowManager.getInterface().getGame().getDatabase().getAllAnnees()
        
        index = (0, 1)
        for _ in difficulte_list:
            positions_valides.append(index)
            index = (index[0] + 1, index[1])
        
        index = (0, 2)
        for _ in annee_list:
            positions_valides.append(index)
            index = (index[0] + 1, index[1])
        
        # Boutons du bas
        positions_valides.extend([
            (0, index[1] + 1),
            (1, index[1] + 1),
            (2, index[1] + 1),
            (3, index[1] + 1),
        ])

        selections = {}
        screen_w = self.__windowManager.getScreenWidth()
        screen_h = self.__windowManager.getScreenHeight()
        center_x = screen_w // 2

        if ((0, 0), (1, 0), (2, 0) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", center_x - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", screen_w - 120, 100, 40, self.__windowManager.getColor().getBlanc()

        # Difficulte
        start_x_d = center_x - (len(difficulte_list) - 1) * 220 // 2 - 100
        index = (0, 1)
        for i, difficulte in enumerate(difficulte_list):
            if index in positions_valides:
                selections[index] = (
                    f"Difficulte {difficulte}", "rectangle",
                    start_x_d + i * 220, screen_h // 2 - 100,
                    200, 50, self.__windowManager.getColor().getBlanc()
                )
                index = (index[0] + 1, index[1])

        # Annee
        start_x_a = center_x - (len(annee_list) - 1) * 160 // 2 - 75
        index = (0, 2)
        for i, annee in enumerate(annee_list):
            if index in positions_valides:
                selections[index] = (
                    f"Annee {annee}", "rectangle",
                    start_x_a + i * 160, screen_h // 2,
                    150, 50, self.__windowManager.getColor().getBlanc()
                )
                index = (index[0] + 1, index[1])

        # Bas de l'ecran
        index = (0, 3)
        selections.update({
            (0, index[1]): ("Accueil", "rectangle", 0, screen_h - 150, screen_w / 4, 150, self.__windowManager.getColor().getBlanc()),
            (1, index[1]): ("Multijoueur", "rectangle", screen_w / 4, screen_h - 150, screen_w / 4, 150, self.__windowManager.getColor().getBlanc()),
            (2, index[1]): ("Statistique", "rectangle", 2 * screen_w / 4, screen_h - 150, screen_w / 4, 150, self.__windowManager.getColor().getBlanc()),
            (3, index[1]): ("Quitter", "rectangle", 3 * screen_w / 4, screen_h - 150, screen_w / 4, 150, self.__windowManager.getColor().getBlanc()),
        })

        return selections

    def create_AIDE_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1),
            (1, 1), 
            (2, 1),
            (3, 1)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0), (0, 1), (1, 1), (2, 1), (3, 1) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 1)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 1)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 1)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections
    
    def create_DETAIL_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1),
            (1, 1),
            (2, 1),
            (3, 1)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0), (0,1), (1, 1), (2, 1), (3, 1) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 1)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 1)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 1)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections
    
    def create_PLAY_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0)
        ]
        
        selections = {}

        if ((0, 0), (1, 0) in positions_valides):
            selections[(0, 0)] = "Retour", "rectangle", 100, 20, 400, 50, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Play", "rectangle", self.__windowManager.getScreenWidth() - 500, 20, 400, 50, self.__windowManager.getColor().getBlanc()
            
        return selections
    
    def create_ACCUEIL_selections(self):
        """Cree les selections du tableau de demarrage en associant les positions valides aux selections."""
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
        ]
        index = (0, 0)
        for music in self.__windowManager.getInterface().getGame().getDatabase().getMusicSorted(self.__windowManager.getSorted().getDifficulte(), self.__windowManager.getSorted().getAnnee()):
            index = (index[0], index[1] + 1)
            positions_valides.append(index)  # Ajouter la position valide pour la musique
            index = (index[0] + 1, index[1])
            positions_valides.append(index)  # Ajouter la position valide pour l'element suivant
            index = (0, index[1])

        # Ajouter les positions des boutons en bas
        positions_valides.extend([
            (0, index[1] + 1),
            (1, index[1] + 1),
            (2, index[1] + 1),
            (3, index[1] + 1),
        ])

        selections = {}

        if ((0, 0), (1, 0), (2, 0) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Filtrer", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()

        # Configuration des musiques et autres elements...
        index = (0, 0)
        for music in self.__windowManager.getInterface().getGame().getDatabase().getMusicSorted(self.__windowManager.getSorted().getDifficulte(), self.__windowManager.getSorted().getAnnee()):
            index = (index[0], index[1] + 1)
            # Ajouter une selection pour chaque musique a une position valide
            if index in positions_valides:
                selections.update({
                    index: (f"Detail musique {music[1]}", "rectangle", 300, 125 + 210 * index[1] - self.__windowManager.getScrollOffset(), 200, 50, self.__windowManager.getColor().getBlanc()),
                })
            index = (index[0] + 1, index[1])
            if index in positions_valides:
                selections.update({
                    index: (f"Play musique {music[1]}", "rectangle", self.__windowManager.getScreenWidth() - 260, 60 + 210 * index[1] - self.__windowManager.getScrollOffset(), 200, 120, self.__windowManager.getColor().getBlanc()),
                })
            index = (0, index[1])

        # Ajouter les selections pour les positions en bas de l'ecran
        selections.update({
            (0, index[1] + 1): ("Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()),  # Rectangle
            (1, index[1] + 1): ("Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()),  # Rectangle
            (2, index[1] + 1): ("Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()),  # Rectangle
            (3, index[1] + 1): ("Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()),  # Rectangle
        })

        return selections
    
    def create_STATISTIQUE_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1),
            (1, 1), 
            (2, 1), 
            (3, 1)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0),  (0, 1), (1, 1), (2, 1), (3, 1) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Retour", "rectangle", self.__windowManager.getScreenWidth() / 2 - 200, 80, 400, 60, self.__windowManager.getColor().getBlanc()
            selections[(2, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 1)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 1)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 1)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections
    
    def create_QUITTER_selections(self):
        positions_valides = [
            (0, 0),
            (1, 0),
            (2, 0),
            (0, 1),
            (1, 1), 
            (0, 2), 
            (1, 2), 
            (2, 2), 
            (3, 2)
        ]
        
        selections = {}

        if ((0, 0), (1, 0), (2, 0),  (0, 1), (1, 1), (0, 2), (1, 2), (2, 2), (3, 2) in positions_valides):
            selections[(0, 0)] = "Profil", "cercle", 100, 100, 75, self.__windowManager.getColor().getBlanc()
            selections[(1, 0)] = "Aide", "cercle", self.__windowManager.getScreenWidth() - 120, 100, 40, self.__windowManager.getColor().getBlanc()
            selections[(0, 1)] = "Non", "rectangle",  self.__windowManager.getScreenWidth() // 2 - 210, self.__windowManager.getScreenHeight() // 2, 180, 60, self.__windowManager.getColor().getBlanc()
            selections[(1, 1)] = "Oui", "rectangle", self.__windowManager.getScreenWidth() // 2 + 30, self.__windowManager.getScreenHeight() // 2, 180, 60, self.__windowManager.getColor().getBlanc()
            selections[(0, 2)] = "Accueil", "rectangle", 0, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(1, 2)] = "Multijoueur", "rectangle", self.__windowManager.getScreenWidth() / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(2, 2)] = "Statistique", "rectangle", self.__windowManager.getScreenWidth() / 2, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()
            selections[(3, 2)] = "Quitter", "rectangle", self.__windowManager.getScreenWidth() * 3 / 4, self.__windowManager.getScreenHeight() - 150, self.__windowManager.getScreenWidth() / 4, 150, self.__windowManager.getColor().getBlanc()

        return selections
    
    def update_selection(self, direction):
        """Met a jour la selection pour la page donnee a la position specifiee."""
        selection_dict = self.getSelection()[1].copy()

        selection = selection_dict.copy()
        for key in selection_dict:
            if key[1] == 0 or key[1] == len (self.__windowManager.getInterface().getGame().getDatabase().getMusicSorted(self.__windowManager.getSorted().getDifficulte(), self.__windowManager.getSorted().getAnnee())) + 1:
                selection.pop(key)

        # Direction vers le haut
        scroll_up = False
        if direction == (0, -1):
            y_first = selection[next(iter(selection))][3] 
            y_attendu = 335 + 210 * (len (self.__windowManager.getInterface().getGame().getDatabase().getMusicSorted(self.__windowManager.getSorted().getDifficulte(), self.__windowManager.getSorted().getAnnee())) - 2)
            for i in range (1, self.__windowManager.getScrollOffset() // 210 + 1):
                y_attendu -= i * 210
            y_attendu -= 514
            if y_first == 335 or y_first == y_attendu:
                for pos, forme in selection.items():
                    if pos == tuple(self.getSelection()[0]):
                        if forme[3] < self.__windowManager.getAreaMusic().top:
                            scroll_up = True
            else:
                for pos, forme in selection.items():
                    if pos == tuple(self.getSelection()[0]):
                        if forme[3] < self.__windowManager.getAreaMusic().top:
                            scroll_up = True

            if scroll_up:
                for pos, forme in selection.items():
                    if forme[1] == "rectangle":
                        y = forme[3]
                        if (self.__windowManager.getScrollOffset() // 210 >= 0):
                            y += 210
                        else:
                            y += self.__windowManager.getScrollOffset()
                        selection_dict[pos] = (forme[0], "rectangle", forme[2], y, forme[4], forme[5], forme[6])
                if (self.__windowManager.getScrollOffset() // 210 < 0):
                    self.__windowManager.setScrollOffset(-self.__windowManager.getScrollOffset())
                else:
                    self.__windowManager.setScrollOffset(-210)

        # Direction vers le bas
        scroll_down = False
        if direction == (0, 1):
            y_first = selection[next(iter(selection))][3] 
            y_attendu = 335 + 210 * (len (self.__windowManager.getInterface().getGame().getDatabase().getMusicSorted(self.__windowManager.getSorted().getDifficulte(), self.__windowManager.getSorted().getAnnee())) - 2)
            for i in range (1, self.__windowManager.getScrollOffset() // 210 + 1):
                y_attendu += i * 210
            y_attendu += 514
            if y_first == 335 or y_first == y_attendu:
                for pos, forme in selection.items():
                    if pos == tuple(self.getSelection()[0]):
                        if forme[3] + forme[5] > self.__windowManager.getAreaMusic().top + self.__windowManager.getAreaMusic().height:
                            scroll_down = True
            else:
                for pos, forme in selection.items():
                    if pos == tuple(self.getSelection()[0]):
                        if forme[3] + forme[5] > self.__windowManager.getAreaMusic().top + self.__windowManager.getAreaMusic().height:
                            scroll_down = True

            if scroll_down:
                for pos, forme in selection.items():
                    if forme[1] == "rectangle":
                        y = forme[3]
                        if (self.__windowManager.getScrollOffset() // 210 >= 0):
                            y -= 210
                        else:
                            y -= self.__windowManager.getScrollOffset()
                        selection_dict[pos] = (forme[0], "rectangle", forme[2], y, forme[4], forme[5], forme[6])
                if (self.__windowManager.getScrollOffset() // 210 < 0):
                    self.__windowManager.setScrollOffset(self.__windowManager.getScrollOffset())
                else:
                    self.__windowManager.setScrollOffset(210)
        
        if scroll_down or scroll_up:
            self.setSelection(selection_dict)
            
    def updatePosition(self, direction):
        """Met a jour la position sur le tableau en fonction de la direction, en respectant les positions valides."""
        dx, dy = direction
        x, y = self.getPosition()
        # Si deplacement horizontal (haut ou bas)
        if dy != 0 and dx == 0:
            nouvelle_col = y + dy
            candidats = [(i, j) for (i, j) in self.getSelection()[1].keys() if j == nouvelle_col]
            if candidats:
                # Trouver la ligne la plus proche de la position actuelle
                candidats.sort(key=lambda pos: abs(pos[0] - x))
                self.setPosition(list(candidats[0]))
        
        # Si deplacement vertical (gauche ou droite)
        elif dx != 0 and dy == 0:
            nouvelle_ligne = x + dx
            if (nouvelle_ligne, y) in self.getSelection()[1].keys():
                self.setPosition((nouvelle_ligne, y))
        
        if self.__windowManager.getInterface().getPage() == PageState.ACCUEIL:
            self.update_selection(direction)

