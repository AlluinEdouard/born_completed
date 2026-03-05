import pygame
from core.player import Player
from ui.layout.gameView import GameView
from ui.layout.timerView import TimerView
from ui.utils.color import Color
from core.pageState import PageState
from core.button import Button

class Logic:
    def __init__(self, game) -> None:
        """Initialisation de l'interface."""
        self.__game = game
        self.__interface = game.getInterface()
        self.__color: Color = Color()
        self.__button: Button = Button()

    def getGame(self):
        """Game du jeu."""
        return self.__game
    
    def getColor(self):
        """Getter de la color."""
        return self.__color
    
    def getInterface(self):
        """Getter de l'interface."""
        return self.__interface
    
    def getButton(self):
        """Getter du bouton."""
        return self.__button
    
    def actionPageProfil(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:    
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Se connecter"):
                        self.getInterface().setPage(PageState.CONNEXION)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "S'inscrire"):
                        self.getInterface().setPage(PageState.INSCRIPTION)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):            
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageInscription(self):
        # ✅ Ne continue que si la page courante est bien CONNEXION
        if self.getInterface().getPage() != PageState.INSCRIPTION:
            return

        # ✅ Récupère la page Connexion
        page = self.getInterface().getWindowManager().getMenu().getPage()
        if page is None:
            return  # Sécurité : évite crash si getPage() retourne None

        self.getInterface().setUpdate(False)

        for event in pygame.event.get():
            # Toujours transmettre les événements à la page
            page.handle_event(event)

            username_active = page.input_username.active
            password_active = page.input_password.active
            password_confirm_active = page.input_confirmPassword.active

            # ✅ Si un champ est actif, gérer les touches
            if username_active or password_active or password_confirm_active:
                if event.type == pygame.KEYDOWN and event.key in [pygame.K_UP, pygame.K_DOWN, pygame.K_LEFT, pygame.K_RIGHT]:
                    # ⬅️➡️⬆️⬇️ désactivent la saisie
                    page.input_username.active = False
                    page.input_password.active = False
                    page.input_confirmPassword.active = False
                    page.input_username.color = page.input_username.color_inactive
                    page.input_password.color = page.input_password.color_inactive
                    page.input_confirmPassword.color = page.input_confirmPassword.color_inactive

                self.getInterface().setUpdate(True)
                continue  # ⛔ Ignore le reste (navigation)

            # 🎮 Navigation clavier
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    selection = self.getInterface().getWindowManager().getSelection()
                    current_item = selection.getSelection()[1][selection.getPosition()][0]

                    if current_item == "Retour":
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())

                    elif current_item == "Valider":
                        username = page.input_username.get_text()
                        password = page.input_password.get_text()
                        confirmPassword = page.input_confirmPassword.get_text()

                        db = self.getInterface().getGame().getDatabase()
                        users = db.getPlayers()

                        matched_user = next(
                            (u for u in users if u[1] == username and u[2] == password), None
                        )

                        if matched_user and confirmPassword == password:
                            page.erreur_inscription = False

                            player_id, name, password = matched_user
                            player_obj = Player(player_id, name, password)

                            self.getInterface().getWindowManager().setCurrentUser(player_obj)
                            self.getInterface().setPage(PageState.ACCUEIL)
                        else:
                            page.erreur_inscription = True

                        self.getInterface().setUpdate(True)

                    elif current_item == "Profil":
                        self.getInterface().setPage(PageState.PROFIL)
                    elif current_item == "Accueil":
                        self.getInterface().setPage(PageState.ACCUEIL)
                    elif current_item == "Nom d'utilisateur":
                        page.input_username.active = True
                        page.input_password.active = False
                        page.input_confirmPassword.active = False
                        page.input_username.color = page.input_username.color_active
                        page.input_password.color = page.input_password.color_inactive
                        page.input_confirmPassword.color = page.input_password.color_inactive
                    elif current_item == "Mot de passe":
                        page.input_password.active = True
                        page.input_username.active = False
                        page.input_confirmPassword.active = False
                        page.input_password.color = page.input_password.color_active
                        page.input_username.color = page.input_username.color_inactive
                        page.input_confirmPassword.color = page.input_confirmPassword.color_inactive
                    elif current_item == "Confirmer le mot de passe":
                        page.input_confirmPassword.active = True
                        page.input_password.active = False
                        page.input_username.active = False
                        page.input_confirmPassword.color = page.input_confirmPassword.color_active
                        page.input_password.color = page.input_password.color_inactive
                        page.input_username.color = page.input_username.color_inactive
                    elif current_item == "Multijoueur":
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                    elif current_item == "Statistique":
                        self.getInterface().setPage(PageState.STATISTIQUE)
                    elif current_item == "Quitter":
                        self.getInterface().setPage(PageState.QUITTER)

                    self.getInterface().setUpdate(True)

                elif isinstance(direction, tuple):
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)

            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageConnexion(self):
        # ✅ Ne continue que si la page courante est bien CONNEXION
        if self.getInterface().getPage() != PageState.CONNEXION:
            return

        # ✅ Récupère la page Connexion
        page = self.getInterface().getWindowManager().getMenu().getPage()
        if page is None:
            return  # Sécurité : évite crash si getPage() retourne None

        self.getInterface().setUpdate(False)

        for event in pygame.event.get():
            # Toujours transmettre les événements à la page
            page.handle_event(event)

            username_active = page.input_username.active
            password_active = page.input_password.active

            # ✅ Si un champ est actif, gérer les touches
            if username_active or password_active:
                if event.type == pygame.KEYDOWN and event.key in [pygame.K_UP, pygame.K_DOWN, pygame.K_LEFT, pygame.K_RIGHT]:
                    # ⬅️➡️⬆️⬇️ désactivent la saisie
                    page.input_username.active = False
                    page.input_password.active = False
                    page.input_username.color = page.input_username.color_inactive
                    page.input_password.color = page.input_password.color_inactive

                self.getInterface().setUpdate(True)
                continue  # ⛔ Ignore le reste (navigation)

            # 🎮 Navigation clavier
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    selection = self.getInterface().getWindowManager().getSelection()
                    current_item = selection.getSelection()[1][selection.getPosition()][0]

                    if current_item == "Retour":
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())

                    elif current_item == "Valider":
                        username = page.input_username.get_text()
                        password = page.input_password.get_text()

                        db = self.getInterface().getGame().getDatabase()
                        users = db.getPlayers()

                        matched_user = next(
                            (u for u in users if u[1] == username and u[2] == password), None
                        )

                        if matched_user:
                            page.erreur_connexion = False

                            player_id, name, password = matched_user
                            player_obj = Player(player_id, name, password)

                            self.getInterface().getWindowManager().setCurrentUser(player_obj)
                            self.getInterface().setPage(PageState.ACCUEIL)
                        else:
                            page.erreur_connexion = True

                        self.getInterface().setUpdate(True)

                    elif current_item == "Profil":
                        self.getInterface().setPage(PageState.PROFIL)
                    elif current_item == "Accueil":
                        self.getInterface().setPage(PageState.ACCUEIL)
                    elif current_item == "Nom d'utilisateur":
                        page.input_username.active = True
                        page.input_password.active = False
                        page.input_username.color = page.input_username.color_active
                        page.input_password.color = page.input_password.color_inactive
                    elif current_item == "Mot de passe":
                        page.input_password.active = True
                        page.input_username.active = False
                        page.input_password.color = page.input_password.color_active
                        page.input_username.color = page.input_username.color_inactive
                    elif current_item == "Multijoueur":
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                    elif current_item == "Statistique":
                        self.getInterface().setPage(PageState.STATISTIQUE)
                    elif current_item == "Quitter":
                        self.getInterface().setPage(PageState.QUITTER)

                    self.getInterface().setUpdate(True)

                elif isinstance(direction, tuple):
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)

            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageFiltrer(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif "Difficulte " in self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0]:
                        self.getInterface().getWindowManager().getSorted().changeFilter(self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0])
                        self.getInterface().setUpdate(True)
                    elif "Annee " in self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0]:
                        self.getInterface().getWindowManager().setMusicSelect(self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0].replace("Detail musique ", ""))
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):            
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageAide(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)                
                elif isinstance(direction, tuple):            
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()
                
    def actionPageDetail(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):         
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPagePlay(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction == "enter":
                selection = self.getInterface().getWindowManager().getSelection()
                item = selection.getSelection()[1][selection.getPosition()][0]
                if item == "Retour":
                    self.getInterface().setPage(self.getInterface().getPagePrecedente())
                    self.getInterface().setUpdate(True)
                elif item == "Profil":
                    self.getInterface().setPage(PageState.PROFIL)
                    self.getInterface().setUpdate(True)
                elif item == "Accueil":
                    self.getInterface().setPage(PageState.ACCUEIL)
                    self.getInterface().setUpdate(True)
                elif item == "Multijoueur":
                    self.getInterface().setPage(PageState.MULTIJOUEUR)
                    self.getInterface().setUpdate(True)
                elif item == "Statistique":
                    self.getInterface().setPage(PageState.STATISTIQUE)
                    self.getInterface().setUpdate(True)
                elif item == "Quitter":
                    self.getInterface().setPage(PageState.QUITTER)
                    self.getInterface().setUpdate(True)
                elif item == "Play":
                    timer = TimerView(self.getInterface().getWindowManager())
                    timer.start()
                    clock = pygame.time.Clock()
                    running = True
                    game_started = False

                    # Recréer un nouveau GameView
                    new_game_view = GameView(self.getInterface().getWindowManager())
                    self.getInterface().getWindowManager().setGame(new_game_view)

                    game_view = new_game_view
                    piano = game_view.getPiano()

                    while running:
                        for event in pygame.event.get():
                            if event.type == pygame.QUIT:
                                pygame.quit()
                                exit()

                        keys = pygame.key.get_pressed()
                        for key in self.getButton().getAll().keys():
                            if keys[key]:
                                game_view.checkHit(self.getButton().update(
                                    pygame.event.Event(pygame.KEYDOWN, key=key, unicode=pygame.key.name(key))))

                        self.getInterface().getWindowManager().getWindow().fill((30, 30, 30))

                        if not game_started:
                            timer.draw()
                            if timer.getFinished():
                                piano.play()
                                game_started = True
                                self.getInterface().setUpdate(True)
                        else:
                            game_view.update()
                            game_view.affichagePiano()
                            self.getInterface().setUpdate(True)

                            if game_view.isGameOver():
                                pygame.mixer.music.stop()
                                running = False
                                self.getInterface().setUpdate(True)

                        pygame.display.flip()
                        clock.tick(60)

            elif isinstance(direction, tuple):
                self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                self.getInterface().setUpdate(True)

            elif event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageAccueil(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Filtrer"):
                        self.getInterface().setPage(PageState.FILTRER)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Aide"):
                        self.getInterface().setPage(PageState.AIDE)
                        self.getInterface().setUpdate(True)
                    elif "Detail " in self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0]:
                        self.getInterface().getWindowManager().setMusicSelect(self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0].replace("Detail musique ", ""))
                        self.getInterface().setPage(PageState.DETAIL)
                        self.getInterface().setUpdate(True)
                    elif "Play " in self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0]:
                        self.getInterface().getWindowManager().setMusicSelect(self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0].replace("Play musique ", ""))
                        self.getInterface().setPage(PageState.PLAY)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                
                elif isinstance(direction, tuple):            
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()
    
    def actionPageMultijoueur(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:    
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):  
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()
    
    def actionPageStatistique(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):  
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageQuitter(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:  
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Non"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Oui"):
                        self.getInterface().setPage(PageState.FERMER)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):  
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageFinGagne(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):  
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()

    def actionPageFinPerdu(self):
        self.getInterface().setUpdate(False)
        for event in pygame.event.get():
            direction = self.getButton().update(event)
            if direction:
                if direction == "enter":
                    if (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Retour"):
                        self.getInterface().setPage(self.getInterface().getPagePrecedente())
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Profil"):
                        self.getInterface().setPage(PageState.PROFIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Accueil"):
                        self.getInterface().setPage(PageState.ACCUEIL)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Multijoueur"):
                        self.getInterface().setPage(PageState.MULTIJOUEUR)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Statistique"):
                        self.getInterface().setPage(PageState.STATISTIQUE)
                        self.getInterface().setUpdate(True)
                    elif (self.getInterface().getWindowManager().getSelection().getSelection()[1][self.getInterface().getWindowManager().getSelection().getPosition()][0] == "Quitter"):
                        self.getInterface().setPage(PageState.QUITTER)
                        self.getInterface().setUpdate(True)
                elif isinstance(direction, tuple):  
                    self.getInterface().getWindowManager().getSelection().updatePosition(direction)
                    self.getInterface().setUpdate(True)
            if event.type == pygame.QUIT:
                pygame.quit()
                exit()