import pygame

class Note:
    def __init__(self, gameview, position, timestamp):
        self.__gameview = gameview
        self.__timestamp = timestamp 
        self.__width = (gameview.getPlayWidth() // 2 + 100) / 4
        self.__height = (gameview.getPlayHeight() // 2 + 100)  / 4
        self.__column = ["left", "middle", "right", "top"].index(position)
        self.__x = self.__column * self.__width
        self.__y = -self.__height
        self.__rect = pygame.Rect(int(self.__x), int(self.__y), int(self.__width), int(self.__height))
        self.__clicked = False
        self.__color = gameview.getWindowManager().getColor()

    def getColumn(self):
        return self.__column
    
    def getTimeStamp(self):
        return self.__timestamp

    def getRect(self):
        return self.__rect

    def isClicked(self):
        return self.__clicked

    def setClicked(self, clicked=True):
        self.__clicked = clicked
    
    def setColor(self, color):
        self.__color = color

    def updatePosition(self, current_time):
        time_diff = current_time - self.__timestamp
        total_fall_duration = 2.0  # secondes
        screen_height = self.__gameview.getWindowManager().getScreenHeight()
        end_y = screen_height + self.__rect.height
        start_y = -self.__rect.height
        distance = end_y - start_y

        if time_diff >= 0:
            ratio = min(time_diff / total_fall_duration, 1.0)
            self.__rect.y = int(start_y + ratio * distance)
        else:
            self.__rect.y = start_y


    def isMissed(self):
        if self.getRect().y > self.__gameview.getWindowManager().getScreenHeight() and not self.isClicked():
            return True
        else:
            return False

    def draw(self):
        surface = self.__gameview.getWindowManager().getWindow()

        if self.__clicked:
            note_surface = pygame.Surface((self.__rect.width, self.__rect.height), pygame.SRCALPHA)
            note_surface.fill(self.__gameview.getWindowManager().getColor().getRose())
            surface.blit(note_surface, self.__rect.topleft)
        else:
            note_surface = pygame.Surface((self.__rect.width, self.__rect.height), pygame.SRCALPHA)
            note_surface.fill(self.__gameview.getWindowManager().getColor().getNoir())
            surface.blit(note_surface, self.__rect.topleft)
