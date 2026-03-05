import pygame
import time

class TimerView:
    def __init__(self, windowManager):
        self.__windowManager = windowManager
        self.__fontTall = windowManager.getFontTall()
        self.__duration = 5 
        self.__start_time = None
        self.__finished = False

    def getFinished(self):
        return self.__finished

    def start(self):
        self.__start_time = time.time()
        self.__finished = False

    def update(self):
        if self.__finished:
            return None

        elapsed = time.time() - self.__start_time
        remaining = self.__duration - int(elapsed)

        if remaining > 0:
            return str(remaining)
        elif remaining == 0:
            return "GO"
        else:
            self.__finished = True
            return None

    def draw(self):
        text = self.update()
        if text is not None:
            text_surface = self.__fontTall.render(text, True, (255, 255, 255))
            rect = text_surface.get_rect(center= (self.__windowManager.getWindow().get_width() // 2, self.__windowManager.getWindow().get_height() // 2))
            self.__windowManager.getWindow().blit(text_surface, rect)
