import math
import os
import random
import sys

import pygame

from constants import ALERT, BALL_BASE_SPEED, BALL_COLOR, BALL_MAX_SPEED, BALL_RADIUS, BONUS_COLORS
from constants import BONUS_DROP_CHANCE, BONUS_SPEED, BRICK_BREAK_SCORE, BRICK_COLS, BRICK_GAP
from constants import BRICK_H, BRICK_HIT_SCORE, BRICK_PALETTE, BRICK_ROWS_BASE, CARD, CARD_BORDER
from constants import COVER_PATH, HIGHLIGHT, HIGHSCORE_PATH, HUD_BG, KEY_BACK, KEY_BACK_MENU
from constants import KEY_CONFIRM, KEY_DOWN, KEY_LEFT, KEY_PAUSE, KEY_RIGHT, KEY_UP, LEVEL_CLEAR_SCORE
from constants import MAX_SCORES, NAME_LEN, PADDLE_BASE_W, PADDLE_EDGE, PADDLE_H, PADDLE_MAIN, PADDLE_SPEED
from constants import PADDLE_TOUCH_RESET_COMBO, PADDLE_WIDE_W, PADDLE_Y, PLAY_BOTTOM, PLAY_TOP
from constants import SCREEN_H, SCREEN_W, START_LIVES, TEXT_DIM, TEXT_MAIN, WIDE_DURATION_MS
from entities import Brick
from helpers import clamp, circle_intersects_rect, lighten, make_vertical_gradient
from storage import ensure_score_files, load_highscores, save_highscore


class BreakoutApp:
    def __init__(self):
        pygame.init()
        self.screen = pygame.display.set_mode((SCREEN_W, SCREEN_H))
        pygame.display.set_caption("Breakout - Borne Arcade")
        self.clock = pygame.time.Clock()

        self.font_title = pygame.font.SysFont("Bauhaus 93", 64)
        self.font_big = pygame.font.SysFont("Agency FB", 44, bold=True)
        self.font = pygame.font.SysFont("Trebuchet MS", 30, bold=True)
        self.font_small = pygame.font.SysFont("Consolas", 20, bold=True)

        self.running = True
        self.state = "menu"
        self.menu_index = 0
        self.pause_index = 0
        self.name_chars = [0] * NAME_LEN
        self.name_index = 0

        self.move_left = False
        self.move_right = False

        self.cover = self.load_cover()
        self.bg_game = make_vertical_gradient(SCREEN_W, SCREEN_H, (14, 21, 48), (7, 10, 25))
        self.bg_menu = make_vertical_gradient(SCREEN_W, SCREEN_H, (22, 29, 56), (5, 8, 19))

        star_rng = random.Random(2026)
        self.bg_stars = [
            (star_rng.randint(0, SCREEN_W - 1), star_rng.randint(0, SCREEN_H - 1), star_rng.randint(1, 3))
            for _ in range(150)
        ]

        self.pause_started_ms = 0
        self.menu_input_lock_until_ms = 0
        ensure_score_files(HIGHSCORE_PATH)
        self.reset_full_game()

    def load_cover(self):
        if not os.path.exists(COVER_PATH):
            return None
        try:
            image = pygame.image.load(COVER_PATH).convert()
            return pygame.transform.smoothscale(image, (360, 220))
        except pygame.error:
            return None

    def reset_full_game(self):
        self.level = 1
        self.score = 0
        self.lives = START_LIVES
        self.combo = 0
        self.status_text = ""
        self.status_color = HIGHLIGHT
        self.status_until_ms = 0
        self.wide_until_ms = 0
        self.bonuses = []

        self.bricks = []
        self.paddle = pygame.Rect(0, PADDLE_Y, PADDLE_BASE_W, PADDLE_H)
        self.paddle.centerx = SCREEN_W // 2

        self.ball_x = float(self.paddle.centerx)
        self.ball_y = float(self.paddle.top - BALL_RADIUS - 1)
        self.ball_vx = 0.0
        self.ball_vy = 0.0
        self.ball_stuck = True

        self.name_chars = [0] * NAME_LEN
        self.name_index = 0
        self.state = "menu"

        self.load_level()

    def start_new_run(self):
        self.level = 1
        self.score = 0
        self.lives = START_LIVES
        self.combo = 0
        self.status_text = ""
        self.status_until_ms = 0
        self.wide_until_ms = 0
        self.name_chars = [0] * NAME_LEN
        self.name_index = 0
        self.load_level()
        self.state = "game"

    def set_status(self, text, color=HIGHLIGHT, duration_ms=1600):
        self.status_text = text
        self.status_color = color
        self.status_until_ms = pygame.time.get_ticks() + duration_ms

    def go_to_menu(self, lock_ms=240):
        self.state = "menu"
        self.menu_input_lock_until_ms = pygame.time.get_ticks() + lock_ms

    def make_bricks(self):
        self.bricks = []
        rows = min(9, BRICK_ROWS_BASE + (self.level - 1) // 2)
        margin_x = 42
        width_available = SCREEN_W - margin_x * 2 - (BRICK_COLS - 1) * BRICK_GAP
        brick_w = width_available // BRICK_COLS
        top_offset = PLAY_TOP + 28

        for row in range(rows):
            hp = min(3, 1 + (row + self.level - 1) // 4)
            color = BRICK_PALETTE[row % len(BRICK_PALETTE)]
            for col in range(BRICK_COLS):
                x = margin_x + col * (brick_w + BRICK_GAP)
                y = top_offset + row * (BRICK_H + BRICK_GAP)
                self.bricks.append(Brick(pygame.Rect(x, y, brick_w, BRICK_H), hp, color))

    def reset_ball_on_paddle(self):
        self.ball_stuck = True
        self.ball_x = float(self.paddle.centerx)
        self.ball_y = float(self.paddle.top - BALL_RADIUS - 1)
        self.ball_vx = 0.0
        self.ball_vy = 0.0

    def launch_ball(self):
        if not self.ball_stuck:
            return
        speed = BALL_BASE_SPEED + min(1.8, (self.level - 1) * 0.2)
        horizontal = random.uniform(0.35, 0.8)
        direction = -1 if random.random() < 0.5 else 1
        self.ball_vx = speed * horizontal * direction
        self.ball_vy = -math.sqrt(max(1.0, speed * speed - self.ball_vx * self.ball_vx))
        self.ball_stuck = False

    def load_level(self):
        self.make_bricks()
        self.bonuses = []
        self.paddle.width = PADDLE_BASE_W
        self.paddle.height = PADDLE_H
        self.paddle.centerx = SCREEN_W // 2
        self.paddle.y = PADDLE_Y
        self.wide_until_ms = 0
        self.reset_ball_on_paddle()
        self.set_status(f"Niveau {self.level}", HIGHLIGHT, 1700)

    def apply_wide_paddle(self):
        center = self.paddle.centerx
        self.paddle.width = PADDLE_WIDE_W
        self.paddle.centerx = clamp(center, self.paddle.width // 2, SCREEN_W - self.paddle.width // 2)
        self.wide_until_ms = pygame.time.get_ticks() + WIDE_DURATION_MS
        self.set_status("Bonus: barre large", BONUS_COLORS["WIDE"])

    def apply_bonus(self, bonus_type):
        if bonus_type == "WIDE":
            self.apply_wide_paddle()
            self.score += 80
        elif bonus_type == "LIFE":
            self.lives = min(9, self.lives + 1)
            self.score += 140
            self.set_status("Bonus: +1 vie", BONUS_COLORS["LIFE"])
        else:
            self.ball_vx *= 0.68
            self.ball_vy *= 0.68
            self.score += 95
            self.set_status("Bonus: balle ralentie", BONUS_COLORS["SLOW"])

    def spawn_bonus(self, x, y):
        if random.random() > BONUS_DROP_CHANCE:
            return

        choices = ["WIDE", "LIFE", "SLOW"]
        weights = [0.5, 0.2, 0.3]
        bonus_type = random.choices(choices, weights=weights, k=1)[0]
        self.bonuses.append(
            {
                "type": bonus_type,
                "x": float(x),
                "y": float(y),
                "size": 22,
                "speed": BONUS_SPEED,
            }
        )

    def update_paddle(self, dt_factor):
        movement = 0
        if self.move_left:
            movement -= 1
        if self.move_right:
            movement += 1

        self.paddle.x += int(movement * PADDLE_SPEED * dt_factor)
        self.paddle.x = clamp(self.paddle.x, 0, SCREEN_W - self.paddle.width)

        if self.ball_stuck:
            self.ball_x = float(self.paddle.centerx)
            self.ball_y = float(self.paddle.top - BALL_RADIUS - 1)

    def ball_speed_up(self, factor):
        speed = math.hypot(self.ball_vx, self.ball_vy)
        if speed <= 0.01:
            return
        new_speed = min(BALL_MAX_SPEED + self.level * 0.2, speed * factor)
        ratio = new_speed / speed
        self.ball_vx *= ratio
        self.ball_vy *= ratio

    def handle_walls(self):
        if self.ball_x - BALL_RADIUS <= 0:
            self.ball_x = BALL_RADIUS
            self.ball_vx = abs(self.ball_vx)

        if self.ball_x + BALL_RADIUS >= SCREEN_W:
            self.ball_x = SCREEN_W - BALL_RADIUS
            self.ball_vx = -abs(self.ball_vx)

        if self.ball_y - BALL_RADIUS <= PLAY_TOP:
            self.ball_y = PLAY_TOP + BALL_RADIUS
            self.ball_vy = abs(self.ball_vy)

    def handle_paddle_collision(self):
        if self.ball_vy <= 0:
            return

        if not circle_intersects_rect(self.ball_x, self.ball_y, BALL_RADIUS, self.paddle):
            return

        self.ball_y = self.paddle.top - BALL_RADIUS - 1
        relative = (self.ball_x - self.paddle.centerx) / max(1, self.paddle.width / 2)
        relative = clamp(relative, -1.0, 1.0)

        speed = max(BALL_BASE_SPEED + (self.level - 1) * 0.2, math.hypot(self.ball_vx, self.ball_vy))
        self.ball_vx = relative * speed * 1.06
        self.ball_vy = -abs(speed * (0.95 + abs(relative) * 0.1))

        if abs(self.ball_vx) < 1.2:
            self.ball_vx = 1.2 if self.ball_vx >= 0 else -1.2

        if PADDLE_TOUCH_RESET_COMBO:
            self.combo = 0

    def handle_brick_collision(self, prev_x, prev_y):
        for idx, brick in enumerate(self.bricks):
            if not circle_intersects_rect(self.ball_x, self.ball_y, BALL_RADIUS, brick.rect):
                continue

            from_top = prev_y + BALL_RADIUS <= brick.rect.top and self.ball_y + BALL_RADIUS >= brick.rect.top
            from_bottom = prev_y - BALL_RADIUS >= brick.rect.bottom and self.ball_y - BALL_RADIUS <= brick.rect.bottom
            from_left = prev_x + BALL_RADIUS <= brick.rect.left and self.ball_x + BALL_RADIUS >= brick.rect.left
            from_right = prev_x - BALL_RADIUS >= brick.rect.right and self.ball_x - BALL_RADIUS <= brick.rect.right

            if from_top:
                self.ball_vy = -abs(self.ball_vy)
            elif from_bottom:
                self.ball_vy = abs(self.ball_vy)
            elif from_left:
                self.ball_vx = -abs(self.ball_vx)
            elif from_right:
                self.ball_vx = abs(self.ball_vx)
            else:
                self.ball_vy *= -1

            self.score += BRICK_HIT_SCORE
            destroyed = brick.hit()
            if destroyed:
                self.combo += 1
                combo_mult = min(8, 1 + self.combo // 2)
                self.score += BRICK_BREAK_SCORE * combo_mult
                self.spawn_bonus(brick.rect.centerx, brick.rect.centery)
                self.bricks.pop(idx)
            else:
                self.combo = max(0, self.combo - 1)

            self.ball_speed_up(1.006)
            return

    def lose_life(self):
        self.lives -= 1
        self.combo = 0

        if self.lives <= 0:
            self.state = "name_input"
            return

        self.paddle.width = PADDLE_BASE_W
        self.wide_until_ms = 0
        self.paddle.centerx = SCREEN_W // 2
        self.reset_ball_on_paddle()
        self.set_status("Balle perdue", ALERT, 1400)

    def update_bonuses(self, dt_factor):
        alive_bonuses = []
        for bonus in self.bonuses:
            bonus["y"] += bonus["speed"] * dt_factor
            size = bonus["size"]
            rect = pygame.Rect(0, 0, size, size)
            rect.center = (int(bonus["x"]), int(bonus["y"]))

            if rect.colliderect(self.paddle):
                self.apply_bonus(bonus["type"])
                continue

            if bonus["y"] > PLAY_BOTTOM + 30:
                continue

            alive_bonuses.append(bonus)

        self.bonuses = alive_bonuses

    def update_gameplay(self, now, dt):
        dt_factor = max(0.6, min(1.7, dt * 60.0))

        self.update_paddle(dt_factor)

        if self.wide_until_ms and now >= self.wide_until_ms:
            center = self.paddle.centerx
            self.paddle.width = PADDLE_BASE_W
            self.paddle.centerx = clamp(center, self.paddle.width // 2, SCREEN_W - self.paddle.width // 2)
            self.wide_until_ms = 0

        if self.ball_stuck:
            self.update_bonuses(dt_factor)
            return

        prev_x = self.ball_x
        prev_y = self.ball_y

        self.ball_x += self.ball_vx * dt_factor
        self.ball_y += self.ball_vy * dt_factor

        self.handle_walls()
        self.handle_paddle_collision()
        self.handle_brick_collision(prev_x, prev_y)

        self.update_bonuses(dt_factor)

        if self.ball_y - BALL_RADIUS > PLAY_BOTTOM:
            self.lose_life()
            return

        if not self.bricks:
            self.level += 1
            self.score += LEVEL_CLEAR_SCORE + self.lives * 45
            if self.level % 3 == 0:
                self.lives = min(9, self.lives + 1)
                self.set_status("Niveau termine: +1 vie", BONUS_COLORS["LIFE"], 1900)
            self.load_level()

    def draw_background(self, game_mode=True):
        self.screen.blit(self.bg_game if game_mode else self.bg_menu, (0, 0))
        for x, y, radius in self.bg_stars:
            col = (56, 90, 156) if game_mode else (70, 108, 180)
            self.screen.fill(col, (x, y, radius, radius))

    def draw_ball(self):
        center = (int(self.ball_x), int(self.ball_y))
        pygame.draw.circle(self.screen, lighten(BALL_COLOR, -55), center, BALL_RADIUS + 4)
        pygame.draw.circle(self.screen, BALL_COLOR, center, BALL_RADIUS)

    def draw_paddle(self):
        glow = self.paddle.inflate(10, 10)
        pygame.draw.rect(self.screen, lighten(PADDLE_MAIN, -70), glow, border_radius=12)
        pygame.draw.rect(self.screen, PADDLE_MAIN, self.paddle, border_radius=10)
        pygame.draw.rect(self.screen, PADDLE_EDGE, self.paddle, 2, border_radius=10)

    def draw_bonuses(self):
        for bonus in self.bonuses:
            x = int(bonus["x"])
            y = int(bonus["y"])
            size = bonus["size"]
            color = BONUS_COLORS[bonus["type"]]
            pygame.draw.circle(self.screen, color, (x, y), size // 2)
            pygame.draw.circle(self.screen, lighten(color, 65), (x, y), size // 2 + 2, 2)

            label = bonus["type"][0]
            text = self.font_small.render(label, True, (20, 26, 50))
            self.screen.blit(text, (x - text.get_width() // 2, y - text.get_height() // 2))

    def draw_hud(self):
        hud = pygame.Rect(0, 0, SCREEN_W, PLAY_TOP)
        pygame.draw.rect(self.screen, HUD_BG, hud)
        pygame.draw.line(self.screen, CARD_BORDER, (0, PLAY_TOP), (SCREEN_W, PLAY_TOP), 2)

        top_line = f"Score {self.score}    Vies {self.lives}    Niveau {self.level}"
        info = self.font.render(top_line, True, TEXT_MAIN)
        self.screen.blit(info, (20, 14))

        bricks_left = self.font_small.render(f"Briques restantes: {len(self.bricks)}", True, TEXT_DIM)
        self.screen.blit(bricks_left, (20, 56))

        combo_text = f"Combo x{max(1, self.combo)}"
        combo_color = HIGHLIGHT if self.combo >= 2 else TEXT_DIM
        combo = self.font_small.render(combo_text, True, combo_color)
        self.screen.blit(combo, (350, 56))

        if self.wide_until_ms > pygame.time.get_ticks():
            wide = self.font_small.render("Barre large active", True, BONUS_COLORS["WIDE"])
            self.screen.blit(wide, (520, 56))

        tip = self.font_small.render("Fleches/QD: deplacer | F: lancer | T: pause | Y: menu", True, TEXT_DIM)
        self.screen.blit(tip, (20, SCREEN_H - 30))

        if self.ball_stuck and self.state == "game":
            message = self.font_small.render("Appuyez sur F pour lancer la balle", True, HIGHLIGHT)
            self.screen.blit(message, (SCREEN_W // 2 - message.get_width() // 2, PLAY_BOTTOM - 40))

        if self.status_text and pygame.time.get_ticks() < self.status_until_ms:
            status = self.font_small.render(self.status_text, True, self.status_color)
            self.screen.blit(status, (SCREEN_W // 2 - status.get_width() // 2, 58))

    def draw_game(self):
        self.draw_background(game_mode=True)

        for brick in self.bricks:
            brick.draw(self.screen)

        self.draw_bonuses()
        self.draw_paddle()
        self.draw_ball()
        self.draw_hud()

    def draw_menu(self):
        self.draw_background(game_mode=False)

        panel = pygame.Rect(28, 28, SCREEN_W - 56, SCREEN_H - 56)
        pygame.draw.rect(self.screen, CARD, panel, border_radius=16)
        pygame.draw.rect(self.screen, CARD_BORDER, panel, 3, border_radius=16)

        title = self.font_title.render("BREAKOUT", True, HIGHLIGHT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, 52))

        sub = self.font_small.render("Version borne arcade", True, TEXT_DIM)
        self.screen.blit(sub, (SCREEN_W // 2 - sub.get_width() // 2, 122))

        if self.cover:
            self.screen.blit(self.cover, (SCREEN_W // 2 - self.cover.get_width() // 2, 160))
        else:
            missing = self.font_small.render("Image introuvable: img/image.png", True, TEXT_MAIN)
            self.screen.blit(missing, (SCREEN_W // 2 - missing.get_width() // 2, 236))

        options = ["Jouer", "Highscores", "Quitter"]
        for i, text in enumerate(options):
            color = HIGHLIGHT if i == self.menu_index else TEXT_MAIN
            line = self.font.render(text, True, color)
            self.screen.blit(line, (SCREEN_W // 2 - line.get_width() // 2, 414 + i * 52))

        rules_1 = self.font_small.render("Cassez toutes les briques avec la balle. Ne laissez pas la balle tomber.", True, TEXT_DIM)
        rules_2 = self.font_small.render("Bonus: W barre large, L vie, S ralentit la balle.", True, TEXT_DIM)
        self.screen.blit(rules_1, (SCREEN_W // 2 - rules_1.get_width() // 2, SCREEN_H - 100))
        self.screen.blit(rules_2, (SCREEN_W // 2 - rules_2.get_width() // 2, SCREEN_H - 74))

        tip = self.font_small.render("Haut/Bas: menu   F/Entree: valider   Y/Echap: quitter", True, TEXT_DIM)
        self.screen.blit(tip, (SCREEN_W // 2 - tip.get_width() // 2, SCREEN_H - 46))

    def draw_highscores(self):
        self.draw_background(game_mode=False)
        title = self.font_big.render("HIGHSCORES", True, HIGHLIGHT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, 46))

        scores = load_highscores(HIGHSCORE_PATH)
        if not scores:
            text = self.font.render("Aucun score enregistre", True, TEXT_MAIN)
            self.screen.blit(text, (SCREEN_W // 2 - text.get_width() // 2, 190))
        else:
            for i in range(MAX_SCORES):
                if i < len(scores):
                    name, score = scores[i]
                    line = f"{i + 1:>2}. {name}  -  {score}"
                else:
                    line = f"{i + 1:>2}. {'-' * NAME_LEN}  -  0"
                text = self.font.render(line, True, TEXT_MAIN)
                self.screen.blit(text, (SCREEN_W // 2 - 175, 132 + i * 42))

        tip = self.font_small.render("F/Entree pour valider   Y/Echap pour retour", True, TEXT_DIM)
        self.screen.blit(tip, (SCREEN_W // 2 - tip.get_width() // 2, SCREEN_H - 56))

    def draw_pause(self):
        self.draw_game()

        overlay = pygame.Surface((SCREEN_W, SCREEN_H), pygame.SRCALPHA)
        overlay.fill((0, 0, 0, 175))
        self.screen.blit(overlay, (0, 0))

        card = pygame.Rect(SCREEN_W // 2 - 185, SCREEN_H // 2 - 130, 370, 250)
        pygame.draw.rect(self.screen, CARD, card, border_radius=14)
        pygame.draw.rect(self.screen, CARD_BORDER, card, 3, border_radius=14)

        title = self.font_big.render("PAUSE", True, HIGHLIGHT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, SCREEN_H // 2 - 104))

        options = ["Reprendre", "Retour menu"]
        for i, text in enumerate(options):
            color = HIGHLIGHT if i == self.pause_index else TEXT_MAIN
            line = self.font.render(text, True, color)
            self.screen.blit(line, (SCREEN_W // 2 - line.get_width() // 2, SCREEN_H // 2 - 28 + i * 56))

    def draw_name_input(self):
        self.draw_background(game_mode=False)

        title = self.font_big.render("FIN DE PARTIE", True, ALERT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, 58))

        score_text = self.font.render(f"Score: {self.score}", True, TEXT_MAIN)
        self.screen.blit(score_text, (SCREEN_W // 2 - score_text.get_width() // 2, 122))

        sub = self.font_small.render("Entrez votre pseudo (4 lettres)", True, TEXT_MAIN)
        self.screen.blit(sub, (SCREEN_W // 2 - sub.get_width() // 2, 170))

        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        box_w = 72
        box_gap = 18
        total_w = NAME_LEN * box_w + (NAME_LEN - 1) * box_gap
        start_x = SCREEN_W // 2 - total_w // 2
        for i in range(NAME_LEN):
            box = pygame.Rect(start_x + i * (box_w + box_gap), 226, box_w, 88)
            fill = (235, 225, 98) if i == self.name_index else (232, 239, 255)
            pygame.draw.rect(self.screen, fill, box, border_radius=8)
            pygame.draw.rect(self.screen, (14, 20, 44), box, 3, border_radius=8)

            letter = alphabet[self.name_chars[i]]
            txt = self.font_big.render(letter, True, (14, 20, 44))
            self.screen.blit(txt, (box.centerx - txt.get_width() // 2, box.y + 14))

        tip = self.font_small.render("Haut/Bas: lettre | Gauche/Droite: position | F/Entree: valider", True, TEXT_MAIN)
        tip2 = self.font_small.render("Y/Echap: ignorer et retour menu", True, TEXT_DIM)
        self.screen.blit(tip, (SCREEN_W // 2 - tip.get_width() // 2, 374))
        self.screen.blit(tip2, (SCREEN_W // 2 - tip2.get_width() // 2, 404))

    def handle_event_menu(self, key):
        if key in KEY_UP:
            self.menu_index = (self.menu_index - 1) % 3
        elif key in KEY_DOWN:
            self.menu_index = (self.menu_index + 1) % 3
        elif key in KEY_CONFIRM:
            if self.menu_index == 0:
                self.start_new_run()
            elif self.menu_index == 1:
                self.state = "highscores"
            else:
                self.running = False
        elif key in KEY_BACK_MENU:
            self.running = False

    def handle_event_game_down(self, key):
        if key in KEY_LEFT:
            self.move_left = True
        if key in KEY_RIGHT:
            self.move_right = True

        if key in KEY_CONFIRM:
            self.launch_ball()
        elif key in KEY_PAUSE:
            self.pause_index = 0
            self.pause_started_ms = pygame.time.get_ticks()
            self.state = "pause"
        elif key in KEY_BACK:
            self.go_to_menu()

    def handle_event_game_up(self, key):
        if key in KEY_LEFT:
            self.move_left = False
        if key in KEY_RIGHT:
            self.move_right = False

    def resume_after_pause(self):
        now = pygame.time.get_ticks()
        delta = now - self.pause_started_ms
        if self.status_until_ms:
            self.status_until_ms += delta
        if self.wide_until_ms:
            self.wide_until_ms += delta
        self.state = "game"

    def handle_event_pause(self, key):
        if key in KEY_UP or key in KEY_DOWN:
            self.pause_index = 1 - self.pause_index
        elif key in KEY_CONFIRM:
            if self.pause_index == 0:
                self.resume_after_pause()
            else:
                self.go_to_menu()
        elif key in KEY_BACK:
            self.resume_after_pause()

    def handle_event_name_input(self, key):
        alphabet_len = 26
        if key in KEY_LEFT:
            self.name_index = (self.name_index - 1) % NAME_LEN
        elif key in KEY_RIGHT:
            self.name_index = (self.name_index + 1) % NAME_LEN
        elif key in KEY_UP:
            self.name_chars[self.name_index] = (self.name_chars[self.name_index] + 1) % alphabet_len
        elif key in KEY_DOWN:
            self.name_chars[self.name_index] = (self.name_chars[self.name_index] - 1) % alphabet_len
        elif key in KEY_CONFIRM:
            alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            name = "".join(alphabet[idx] for idx in self.name_chars)
            save_highscore(HIGHSCORE_PATH, name, self.score)
            self.state = "highscores"
        elif key in KEY_BACK:
            self.go_to_menu()

    def run(self):
        while self.running:
            dt = self.clock.tick(60) / 1000.0
            now = pygame.time.get_ticks()

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False
                elif event.type == pygame.KEYDOWN:
                    key = event.key
                    if self.state == "menu" and now < self.menu_input_lock_until_ms:
                        continue
                    if self.state == "menu":
                        self.handle_event_menu(key)
                    elif self.state == "game":
                        self.handle_event_game_down(key)
                    elif self.state == "pause":
                        self.handle_event_pause(key)
                    elif self.state == "highscores":
                        if key in KEY_CONFIRM or key in KEY_BACK_MENU:
                            self.go_to_menu()
                    elif self.state == "name_input":
                        self.handle_event_name_input(key)
                elif event.type == pygame.KEYUP:
                    if self.state == "game":
                        self.handle_event_game_up(event.key)

            if self.state == "game":
                self.update_gameplay(now, dt)
                self.draw_game()
            elif self.state == "pause":
                self.draw_pause()
            elif self.state == "highscores":
                self.draw_highscores()
            elif self.state == "name_input":
                self.draw_name_input()
            else:
                self.draw_menu()

            pygame.display.flip()

        pygame.quit()
        sys.exit(0)


def run_game():
    BreakoutApp().run()
