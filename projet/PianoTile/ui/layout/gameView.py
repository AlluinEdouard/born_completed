import pygame
from core.pageState import PageState
from ui.utils.piano import Piano

class GameView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager
        self.__padding_x = 100
        self.__padding_y = 80
        self.__screen_width = self.__windowManager.getScreenWidth()
        self.__screen_height = self.__windowManager.getScreenHeight()
        self.__play_width = self.__screen_width - 2 * self.__padding_x
        self.__play_height = self.__screen_height - self.__padding_y
        self.__piano = Piano(self)
        self.__line_y = self.__screen_height - 100
        self.__game_over = False
        self.__score = 0

    def getWindowManager(self):
        return self.__windowManager

    def getPlayWidth(self):
        return self.__play_width
    
    def getScore(self):
        return self.__score

    def getPlayHeight(self):
        return self.__play_height
    
    def getPiano(self):
        return self.__piano

    def getCurrentTime(self):
        return self.__piano.getCurrentTime()

    def setScore(self, score):
        self.__score = score
    
    def checkHit(self, column):
        closest_note = None
        tolerance = 200  # pixels au-dessus de la ligne_y

        for note in self.__piano.getNotes():
            if note.getColumn() == column and not note.isClicked():
                note_top = note.getRect().y

                if note_top >= self.getWindowManager().getScreenHeight() - tolerance and note_top <= self.getWindowManager().getScreenHeight():
                    # On prend la première note qui correspond aux critères
                    closest_note = note
                    break

        if closest_note:
            closest_note.setClicked(True)
            closest_note.setColor(self.getWindowManager().getColor().getRose())
            player = self.getWindowManager().getCurrentUser()
            self.setScore(self.__score + 1)
            player_name = player.getName()
            new_score = self.__score

            db = self.getWindowManager().getInterface().getGame().getDatabase()
            music_id = db.getMusicFromTitle(self.getWindowManager().getMusicSelect())[0][0]
            best_score = db.getBestScoreForUser(player_name, music_id)

            if new_score > best_score:
                db.setScores(player_name, music_id, new_score)
            return True
        return False

    def reset(self):
        self = GameView(self.getWindowManager())
        
    def isGameOver(self):
        return self.__game_over

    def setGameOver(self, over):
        self.__game_over = over

    def affichagePiano(self):
        screen = self.__windowManager.getWindow()
        col_width = self.__play_width / 4

        for i in range(4):
            x = self.__padding_x + i * col_width
            pygame.draw.rect(screen, self.__windowManager.getColor().getRose(), (x, self.__padding_y, col_width, self.__screen_height - self.__padding_y))
            pygame.draw.line(screen, self.__windowManager.getColor().getBlanc(), (x, self.__padding_y), (x, self.__screen_height), 1)

        pygame.draw.line(screen, (150, 100, 200, 100), (self.__padding_x, self.__line_y), (self.__screen_width - self.__padding_x, self.__line_y), 4)

        for note in self.__piano.getNotes():
            note.getRect().x = self.__padding_x + note.getColumn() * col_width
            note.getRect().width = col_width - 10
            if note.getRect().y < self.__screen_height:
                note.draw()

        # Ligne de validation principale (déjà existante)
        pygame.draw.line(screen, (150, 100, 200, 100), (self.__padding_x, self.__line_y), (self.__screen_width - self.__padding_x, self.__line_y), 4)

        # Affichage du score actuel en haut au centre
        score_text = self.__windowManager.getFontTall().render(
            f"Score : {self.__score}", True, self.__windowManager.getColor().getBlanc()
        )
        screen.blit(score_text, score_text.get_rect(center=(self.__screen_width // 2, 40)))

        pygame.display.flip()

    def update(self):
        if self.__game_over:
            return  # stop early si game over

        current_time = self.getCurrentTime()

        for note in self.__piano.getNotes():
            note.updatePosition(current_time)
            if note.isMissed():
                self.__game_over = True
                pygame.mixer.music.stop()
                return

