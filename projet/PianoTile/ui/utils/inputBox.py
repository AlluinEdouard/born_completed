import pygame
import hashlib

class InputBox:
    def __init__(self, x, y, w, h, font, color_inactive, color_active, text='', is_password=False):
        self.rect = pygame.Rect(x, y, w, h)
        self.color_inactive = color_inactive
        self.color_active = color_active
        self.color = color_inactive
        self.text = text
        self.font = font
        self.is_password = is_password
        self.txt_surface = self.font.render(self.get_display_text(), True, (0, 0, 0))
        self.active = False

    def handle_event(self, event):
        if event.type == pygame.MOUSEBUTTONDOWN:
            self.active = self.rect.collidepoint(event.pos)
            self.color = self.color_active if self.active else self.color_inactive

        if event.type == pygame.KEYDOWN and self.active:
            if event.key == pygame.K_RETURN:
                return self.text
            elif event.key == pygame.K_BACKSPACE:
                self.text = self.text[:-1]
            else:
                self.text += event.unicode
            self.txt_surface = self.font.render(self.get_display_text(), True, (0, 0, 0))

    def get_display_text(self):
        return '●' * len(self.text) if self.is_password else self.text

    def draw(self, screen):
        pygame.draw.rect(screen, self.color, self.rect)
        screen.blit(self.txt_surface, (self.rect.x+5, self.rect.y+5))
        pygame.draw.rect(screen, (0, 0, 0), self.rect, 2)

    def get_text(self):
        return self.text

    def get_hashed_password(self):
        if not self.is_password:
            return None
        return hashlib.sha256(self.text.encode('utf-8')).hexdigest()
