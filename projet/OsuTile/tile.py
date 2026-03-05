import pygame
from config import SCREEN_HEIGHT, TILE_HEIGHT, FALL_TIME


class Tile:
    def __init__(self, lane, time):
        self.lane = lane
        self.time = time  # en ms
        self.hit = False

    def get_y(self, current_time):
        time_diff = (current_time - self.time) / 1000
        return time_diff / FALL_TIME * SCREEN_HEIGHT

    def draw(self, surface, x, width, current_time, color):
        y = self.get_y(current_time)
        if y > SCREEN_HEIGHT:
            return
        pygame.draw.rect(surface, color, (x, y, width, TILE_HEIGHT))
