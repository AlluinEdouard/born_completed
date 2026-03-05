import pygame
from constantes import RED

class Bullet(pygame.sprite.Sprite):
    def __init__(self, x: int, y: int):
        super().__init__()
        self.image = pygame.Surface((10, 20))
        self.image.fill(RED)
        self.rect: pygame.Rect = self.image.get_rect()
        self.rect.bottom = y
        self.rect.centerx = x
        self.speed_y: int = 10

    def update(self):
        self.rect.y -= self.speed_y
        if self.rect.bottom < 0:
            self.kill()