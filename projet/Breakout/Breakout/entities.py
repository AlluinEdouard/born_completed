import pygame

from helpers import lighten


class Brick:
    _hp_font = None

    def __init__(self, rect, hp, color):
        self.rect = rect
        self.hp = hp
        self.max_hp = hp
        self.base_color = color

    @classmethod
    def _get_hp_font(cls):
        if cls._hp_font is None:
            cls._hp_font = pygame.font.SysFont("Consolas", 18, bold=True)
        return cls._hp_font

    def draw(self, screen):
        shade = (self.max_hp - self.hp) * 24
        body = lighten(self.base_color, -shade)
        border = lighten(self.base_color, 52)
        pygame.draw.rect(screen, body, self.rect, border_radius=8)
        pygame.draw.rect(screen, border, self.rect, 2, border_radius=8)

        if self.hp > 1:
            text = self._get_hp_font().render(str(self.hp), True, (16, 24, 50))
            screen.blit(
                text,
                (self.rect.centerx - text.get_width() // 2, self.rect.centery - text.get_height() // 2),
            )

    def hit(self):
        self.hp -= 1
        return self.hp <= 0
