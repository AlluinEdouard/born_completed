import math

import pygame

from constants import BULLET_EDGE, BULLET_MAIN, ENEMY_BAT, ENEMY_OUTLINE, ENEMY_SLIME, PLAYER_ACCENT
from constants import PLAYER_HAT, PLATFORM_HEIGHT
from helpers import clamp, lighten


class Player:
    def __init__(self, x, y, width, height):
        self.x = float(x)
        self.y = float(y)
        self.width = int(width)
        self.height = int(height)
        self.vy = 0.0
        self.facing = 1

    def get_rect(self):
        return pygame.Rect(int(self.x), int(self.y), self.width, self.height)

    def move_horizontal(self, direction, speed, dt_factor):
        self.x += direction * speed * dt_factor
        if direction > 0:
            self.facing = 1
        elif direction < 0:
            self.facing = -1

    def wrap_horizontally(self, screen_width):
        if self.x + self.width < 0:
            self.x = float(screen_width)
        elif self.x > screen_width:
            self.x = -float(self.width)

    def shift_y(self, delta):
        self.y += delta

    def draw(self, screen, body_color, outline_color):
        rect = self.get_rect()
        facing = self.facing

        shadow = pygame.Rect(rect.x + 10, rect.bottom - 6, rect.w - 20, 10)
        pygame.draw.ellipse(screen, lighten(body_color, -95), shadow)

        backpack_x = rect.x + 3 if facing > 0 else rect.right - 21
        backpack = pygame.Rect(backpack_x, rect.y + 23, 18, 22)
        pygame.draw.rect(screen, lighten(PLAYER_ACCENT, -30), backpack, border_radius=6)
        pygame.draw.rect(screen, PLAYER_ACCENT, backpack.inflate(-4, -4), border_radius=5)

        body = pygame.Rect(rect.x + 7, rect.y + 17, rect.w - 14, rect.h - 18)
        pygame.draw.ellipse(screen, body_color, body)
        pygame.draw.ellipse(screen, outline_color, body, 2)

        highlight = pygame.Rect(body.x + 8, body.y + 6, body.w // 2, body.h // 3)
        pygame.draw.ellipse(screen, lighten(body_color, 40), highlight)

        hat = pygame.Rect(rect.x + 8, rect.y + 7, rect.w - 16, 14)
        brim = pygame.Rect(rect.x + 4, rect.y + 15, rect.w - 8, 8)
        pygame.draw.rect(screen, PLAYER_HAT, hat, border_radius=7)
        pygame.draw.rect(screen, lighten(PLAYER_HAT, 48), hat, 2, border_radius=7)
        pygame.draw.rect(screen, lighten(PLAYER_HAT, -22), brim, border_radius=4)

        scarf_y = rect.y + 28
        tail_dir = -facing
        knot = (rect.centerx + 1, scarf_y)
        pygame.draw.circle(screen, PLAYER_ACCENT, knot, 7)
        tail = [
            (rect.centerx + 5, scarf_y + 2),
            (rect.centerx + 5 + tail_dir * 18, scarf_y + 8),
            (rect.centerx + 3 + tail_dir * 16, scarf_y + 17),
            (rect.centerx - 3, scarf_y + 8),
        ]
        pygame.draw.polygon(screen, PLAYER_ACCENT, tail)

        eye_offset = 4 if facing > 0 else -4
        eye_left = (rect.centerx - 11, rect.y + 34)
        eye_right = (rect.centerx + 11, rect.y + 34)
        for eye in (eye_left, eye_right):
            pygame.draw.circle(screen, (255, 255, 255), eye, 7)
            pygame.draw.circle(screen, outline_color, eye, 7, 2)
            pupil = (eye[0] + eye_offset // 2, eye[1] + 1)
            pygame.draw.circle(screen, (22, 22, 28), pupil, 2)

        pygame.draw.arc(screen, outline_color, (rect.centerx - 10, rect.y + 42, 20, 12), 0.2, 2.9, 2)

        foot_y = rect.bottom - 2
        left_shoe = pygame.Rect(rect.centerx - 18, foot_y - 5, 15, 8)
        right_shoe = pygame.Rect(rect.centerx + 3, foot_y - 5, 15, 8)
        pygame.draw.ellipse(screen, lighten(outline_color, 28), left_shoe)
        pygame.draw.ellipse(screen, lighten(outline_color, 28), right_shoe)


class Platform:
    def __init__(self, x, y, width, kind="normal", vx=0.0, move_range=0.0):
        self.x = float(x)
        self.y = float(y)
        self.width = int(width)
        self.height = PLATFORM_HEIGHT
        self.kind = kind
        self.vx = float(vx)
        self.active = True

        self.min_x = clamp(self.x - move_range, 0.0, float(self.x))
        self.max_x = self.x + move_range

    def get_rect(self):
        return pygame.Rect(int(self.x), int(self.y), self.width, self.height)

    def update(self, dt_factor, screen_width):
        if not self.active or self.kind != "moving":
            return

        self.x += self.vx * dt_factor

        max_allowed = min(float(screen_width - self.width), self.max_x)
        min_allowed = max(0.0, self.min_x)
        if self.x <= min_allowed:
            self.x = min_allowed
            self.vx = abs(self.vx)
        elif self.x >= max_allowed:
            self.x = max_allowed
            self.vx = -abs(self.vx)

    def shift_y(self, delta):
        self.y += delta

    def consume(self):
        self.active = False

    def draw(self, screen, normal_color, moving_color, boost_color, fragile_color):
        if not self.active:
            return

        colors = {
            "normal": normal_color,
            "moving": moving_color,
            "boost": boost_color,
            "fragile": fragile_color,
        }
        base = colors.get(self.kind, normal_color)
        rect = self.get_rect()

        pygame.draw.rect(screen, lighten(base, -45), rect.inflate(4, 6), border_radius=9)
        pygame.draw.rect(screen, base, rect, border_radius=8)
        pygame.draw.rect(screen, lighten(base, 52), rect, 2, border_radius=8)

        if self.kind == "boost":
            pygame.draw.circle(screen, (255, 240, 210), (rect.centerx, rect.centery), 5)
        elif self.kind == "fragile":
            pygame.draw.line(screen, (82, 48, 26), (rect.x + 10, rect.y + 3), (rect.right - 10, rect.bottom - 3), 2)
            pygame.draw.line(screen, (82, 48, 26), (rect.x + 16, rect.bottom - 3), (rect.right - 16, rect.y + 3), 2)


class Enemy:
    def __init__(self, x, y, width, height, kind="slime", vx=1.5, patrol_left=None, patrol_right=None):
        self.x = float(x)
        self.y = float(y)
        self.base_y = float(y)
        self.width = int(width)
        self.height = int(height)
        self.kind = kind
        self.vx = float(vx)
        self.active = True
        self.phase = (x * 0.13 + y * 0.07) % (2 * math.pi)

        if patrol_left is None:
            patrol_left = self.x - 40
        if patrol_right is None:
            patrol_right = self.x + 40
        self.patrol_left = float(min(patrol_left, patrol_right))
        self.patrol_right = float(max(patrol_left, patrol_right))

    def get_rect(self):
        return pygame.Rect(int(self.x), int(self.y), self.width, self.height)

    def shift_y(self, delta):
        self.base_y += delta
        self.y += delta

    def update(self, dt_factor):
        if not self.active:
            return

        self.x += self.vx * dt_factor
        if self.x <= self.patrol_left:
            self.x = self.patrol_left
            self.vx = abs(self.vx)
        elif self.x >= self.patrol_right:
            self.x = self.patrol_right
            self.vx = -abs(self.vx)

        self.phase += 0.07 * dt_factor
        if self.kind == "bat":
            self.y = self.base_y + math.sin(self.phase) * 6.0
        else:
            self.y = self.base_y + math.sin(self.phase * 0.75) * 2.5

    def draw(self, screen):
        if not self.active:
            return

        rect = self.get_rect()

        if self.kind == "bat":
            wing_left = [
                (rect.centerx - 6, rect.y + 20),
                (rect.x - 14, rect.y + 10),
                (rect.x - 8, rect.y + 27),
            ]
            wing_right = [
                (rect.centerx + 6, rect.y + 20),
                (rect.right + 14, rect.y + 10),
                (rect.right + 8, rect.y + 27),
            ]
            pygame.draw.polygon(screen, lighten(ENEMY_BAT, -18), wing_left)
            pygame.draw.polygon(screen, lighten(ENEMY_BAT, -18), wing_right)

            body = pygame.Rect(rect.x + 10, rect.y + 8, rect.w - 20, rect.h - 10)
            pygame.draw.ellipse(screen, ENEMY_BAT, body)
            pygame.draw.ellipse(screen, ENEMY_OUTLINE, body, 2)

            ear_left = [(body.x + 4, body.y + 7), (body.x + 11, body.y - 6), (body.x + 16, body.y + 9)]
            ear_right = [(body.right - 4, body.y + 7), (body.right - 11, body.y - 6), (body.right - 16, body.y + 9)]
            pygame.draw.polygon(screen, ENEMY_BAT, ear_left)
            pygame.draw.polygon(screen, ENEMY_BAT, ear_right)

        else:
            shadow = pygame.Rect(rect.x + 9, rect.bottom - 4, rect.w - 18, 7)
            pygame.draw.ellipse(screen, lighten(ENEMY_SLIME, -95), shadow)

            body = pygame.Rect(rect.x + 3, rect.y + 4, rect.w - 6, rect.h - 3)
            pygame.draw.ellipse(screen, ENEMY_SLIME, body)
            pygame.draw.ellipse(screen, ENEMY_OUTLINE, body, 2)

            shine = pygame.Rect(body.x + 7, body.y + 5, body.w // 3, body.h // 4)
            pygame.draw.ellipse(screen, lighten(ENEMY_SLIME, 45), shine)

        eye_left = (rect.centerx - 8, rect.y + 20)
        eye_right = (rect.centerx + 8, rect.y + 20)
        pygame.draw.circle(screen, (255, 255, 255), eye_left, 5)
        pygame.draw.circle(screen, (255, 255, 255), eye_right, 5)
        pygame.draw.circle(screen, ENEMY_OUTLINE, eye_left, 5, 2)
        pygame.draw.circle(screen, ENEMY_OUTLINE, eye_right, 5, 2)
        pygame.draw.circle(screen, (18, 15, 30), eye_left, 2)
        pygame.draw.circle(screen, (18, 15, 30), eye_right, 2)

        mouth = pygame.Rect(rect.centerx - 8, rect.y + 26, 16, 8)
        pygame.draw.arc(screen, ENEMY_OUTLINE, mouth, 0.2, 2.9, 2)


class Bullet:
    def __init__(self, x, y, radius, speed):
        self.x = float(x)
        self.y = float(y)
        self.radius = int(radius)
        self.vx = 0.0
        self.vy = -abs(float(speed))
        self.active = True

    def get_rect(self):
        r = self.radius
        return pygame.Rect(int(self.x - r), int(self.y - r), r * 2, r * 2)

    def shift_y(self, delta):
        self.y += delta

    def update(self, dt_factor):
        self.x += self.vx * dt_factor
        self.y += self.vy * dt_factor

    def draw(self, screen):
        if not self.active:
            return

        core = (int(self.x), int(self.y))
        pygame.draw.circle(screen, BULLET_EDGE, core, self.radius + 2)
        pygame.draw.circle(screen, BULLET_MAIN, core, self.radius)
        pygame.draw.circle(screen, (255, 255, 255), (core[0] - 1, core[1] - 1), max(1, self.radius // 2))
