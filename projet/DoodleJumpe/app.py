import os
import random
import sys

import pygame

from constants import ALERT, BG_BOTTOM, BG_TOP, BULLET_RADIUS, BULLET_SPEED, CAMERA_TARGET_Y, CARD, CARD_BORDER, COVER_PATH
from constants import ENEMY_H, ENEMY_MOVE_SPEED_MAX, ENEMY_MOVE_SPEED_MIN, ENEMY_SCORE, ENEMY_SPAWN_BASE
from constants import ENEMY_SPAWN_MAX, ENEMY_W, FAIL_Y_MARGIN, FPS, GRAVITY, HIGHLIGHT, HIGHSCORE_PATH
from constants import JUMP_SPEED, KEY_BACK, KEY_BACK_MENU, KEY_CONFIRM, KEY_DOWN, KEY_LEFT, KEY_PAUSE
from constants import KEY_RIGHT, KEY_SHOOT, KEY_UP, MAX_SCORES, NAME_LEN, PLAYER_BODY, PLAYER_H
from constants import PLAYER_MOVE_SPEED, PLAYER_OUTLINE, PLAYER_START_Y, PLAYER_W, PLATFORM_BOOST
from constants import PLATFORM_FRAGILE, PLATFORM_GAP_MAX, PLATFORM_GAP_MIN, PLATFORM_MAX_W, PLATFORM_MIN_W
from constants import PLATFORM_MOVE_RANGE, PLATFORM_MOVE_SPEED_MAX, PLATFORM_MOVE_SPEED_MIN, PLATFORM_MOVING
from constants import PLATFORM_NORMAL, SCORE_PER_PIXEL, SCREEN_H, SCREEN_W, SHOOT_COOLDOWN_MS
from constants import START_PLATFORM_Y, SUPER_JUMP_SPEED, TEXT_DIM, TEXT_MAIN
from entities import Bullet, Enemy, Platform, Player
from helpers import clamp, make_vertical_gradient
from storage import ensure_score_files, load_highscores, save_highscore


class DoodleJumpeApp:
    def __init__(self):
        pygame.init()
        self.screen = pygame.display.set_mode((SCREEN_W, SCREEN_H))
        pygame.display.set_caption("Doodle Jumpe - Borne Arcade")
        self.clock = pygame.time.Clock()

        self.font_title = pygame.font.SysFont("Impact", 68)
        self.font_big = pygame.font.SysFont("Agency FB", 46, bold=True)
        self.font = pygame.font.SysFont("Trebuchet MS", 32, bold=True)
        self.font_small = pygame.font.SysFont("Consolas", 22, bold=True)

        self.running = True
        self.state = "menu"
        self.menu_index = 0
        self.pause_index = 0
        self.move_left = False
        self.move_right = False

        self.name_chars = [0] * NAME_LEN
        self.name_index = 0

        self.score = 0
        self.distance = 0.0
        self.player = Player(SCREEN_W // 2 - PLAYER_W // 2, PLAYER_START_Y, PLAYER_W, PLAYER_H)
        self.platforms = []
        self.enemies = []
        self.bullets = []
        self.shoot_cooldown_until_ms = 0

        self.status_text = ""
        self.status_color = HIGHLIGHT
        self.status_until_ms = 0

        self.pause_started_ms = 0
        self.menu_input_lock_until_ms = 0

        self.rng = random.Random()
        self.cover = self.load_cover()

        self.bg_game = make_vertical_gradient(SCREEN_W, SCREEN_H, BG_TOP, BG_BOTTOM)
        self.bg_menu = make_vertical_gradient(SCREEN_W, SCREEN_H, (190, 241, 255), (241, 252, 255))

        self.clouds = self.build_clouds()

        ensure_score_files(HIGHSCORE_PATH)
        self.reset_run_state()

    def load_cover(self):
        if not os.path.exists(COVER_PATH):
            return None
        try:
            image = pygame.image.load(COVER_PATH).convert()
            return pygame.transform.smoothscale(image, (340, 208))
        except pygame.error:
            return None

    def build_clouds(self):
        clouds = []
        seed = random.Random(20260305)
        for _ in range(16):
            clouds.append(
                {
                    "x": seed.randint(-180, SCREEN_W + 120),
                    "y": seed.randint(30, SCREEN_H - 60),
                    "size": seed.randint(52, 120),
                    "speed": seed.uniform(0.05, 0.22),
                }
            )
        return clouds

    def reset_run_state(self):
        self.score = 0
        self.distance = 0.0
        self.status_text = ""
        self.status_until_ms = 0

        self.move_left = False
        self.move_right = False

        self.player = Player(SCREEN_W // 2 - PLAYER_W // 2, PLAYER_START_Y, PLAYER_W, PLAYER_H)
        self.player.vy = -9.2

        self.platforms = []
        self.enemies = []
        self.bullets = []
        self.shoot_cooldown_until_ms = 0

    def set_status(self, text, color=HIGHLIGHT, duration_ms=1200):
        self.status_text = text
        self.status_color = color
        self.status_until_ms = pygame.time.get_ticks() + duration_ms

    def difficulty_ratio(self):
        return clamp(self.score / 14500.0, 0.0, 1.0)

    def enemy_spawn_chance(self):
        ratio = self.difficulty_ratio()
        return ENEMY_SPAWN_BASE + (ENEMY_SPAWN_MAX - ENEMY_SPAWN_BASE) * ratio

    def choose_platform_kind(self, force_normal=False):
        if force_normal:
            return "normal"

        ratio = self.difficulty_ratio()
        boost_chance = 0.1
        moving_chance = 0.1 + 0.18 * ratio
        fragile_chance = 0.08 + 0.18 * ratio

        roll = self.rng.random()
        if roll < boost_chance:
            return "boost"
        if roll < boost_chance + moving_chance:
            return "moving"
        if roll < boost_chance + moving_chance + fragile_chance:
            return "fragile"
        return "normal"

    def platform_gap(self):
        ratio = self.difficulty_ratio()
        low = PLATFORM_GAP_MIN + int(10 * ratio)
        high = PLATFORM_GAP_MAX + int(26 * ratio)
        return self.rng.randint(low, high)

    def make_platform(self, y, force_normal=False):
        width = self.rng.randint(PLATFORM_MIN_W, PLATFORM_MAX_W)
        x = self.rng.randint(0, SCREEN_W - width)
        kind = self.choose_platform_kind(force_normal=force_normal)

        vx = 0.0
        move_range = 0.0
        if kind == "moving":
            speed = self.rng.uniform(PLATFORM_MOVE_SPEED_MIN, PLATFORM_MOVE_SPEED_MAX)
            vx = speed if self.rng.random() < 0.5 else -speed
            move_range = float(PLATFORM_MOVE_RANGE + self.rng.randint(-25, 45))

        return Platform(x, y, width, kind=kind, vx=vx, move_range=move_range)

    def maybe_spawn_enemy_for_platform(self, platform):
        if platform.kind != "normal":
            return
        if platform.width < ENEMY_W + 18:
            return
        if platform.y > SCREEN_H - 180:
            return
        if self.rng.random() > self.enemy_spawn_chance():
            return

        left = platform.x + 6
        right = platform.x + platform.width - ENEMY_W - 6
        if right <= left:
            return

        ratio = self.difficulty_ratio()
        speed = self.rng.uniform(ENEMY_MOVE_SPEED_MIN, ENEMY_MOVE_SPEED_MAX) * (1.0 + 0.35 * ratio)
        if self.rng.random() < 0.5:
            speed *= -1

        kind = "bat" if self.rng.random() < (0.3 + 0.35 * ratio) else "slime"
        x = self.rng.uniform(left, right)
        enemy = Enemy(
            x,
            platform.y - ENEMY_H - 4,
            ENEMY_W,
            ENEMY_H,
            kind=kind,
            vx=speed,
            patrol_left=left,
            patrol_right=right,
        )
        self.enemies.append(enemy)

    def setup_initial_platforms(self):
        base_width = 188
        base_x = SCREEN_W // 2 - base_width // 2
        base = Platform(base_x, START_PLATFORM_Y, base_width, kind="normal")

        self.platforms = [base]
        self.player.x = float(base_x + base_width // 2 - self.player.width // 2)
        self.player.y = float(START_PLATFORM_Y - self.player.height)
        self.player.vy = -10.5

        y = START_PLATFORM_Y
        index = 0
        while y > -SCREEN_H:
            y -= self.platform_gap()
            platform = self.make_platform(y, force_normal=index < 3)
            self.platforms.append(platform)
            if index >= 5:
                self.maybe_spawn_enemy_for_platform(platform)
            index += 1

    def replenish_platforms(self):
        self.platforms = [p for p in self.platforms if p.active and p.y < SCREEN_H + 80]

        if not self.platforms:
            rescue = Platform(SCREEN_W // 2 - 80, START_PLATFORM_Y, 160, kind="normal")
            self.platforms = [rescue]

        top_y = min(platform.y for platform in self.platforms)
        while top_y > -140:
            new_y = top_y - self.platform_gap()
            platform = self.make_platform(new_y)
            self.platforms.append(platform)
            self.maybe_spawn_enemy_for_platform(platform)
            top_y = new_y

    def start_new_run(self):
        self.reset_run_state()
        self.setup_initial_platforms()
        self.name_chars = [0] * NAME_LEN
        self.name_index = 0
        self.state = "game"

    def go_to_menu(self, lock_ms=220):
        self.state = "menu"
        self.menu_input_lock_until_ms = pygame.time.get_ticks() + lock_ms

    def begin_name_input(self):
        self.name_chars = [0] * NAME_LEN
        self.name_index = 0
        self.state = "name_input"

    def shoot(self):
        now = pygame.time.get_ticks()
        if now < self.shoot_cooldown_until_ms:
            return

        self.shoot_cooldown_until_ms = now + SHOOT_COOLDOWN_MS
        x = self.player.x + self.player.width * 0.5
        y = self.player.y + 10
        self.bullets.append(Bullet(x, y, BULLET_RADIUS, BULLET_SPEED))

    def handle_platform_collisions(self, previous_rect):
        if self.player.vy <= 0:
            return

        player_rect = self.player.get_rect()
        feet = pygame.Rect(player_rect.x + 8, player_rect.bottom - 5, max(4, player_rect.w - 16), 10)

        candidates = sorted(self.platforms, key=lambda p: p.y)
        for platform in candidates:
            if not platform.active:
                continue
            rect = platform.get_rect()
            if previous_rect.bottom > rect.top + 8:
                continue
            if not feet.colliderect(rect):
                continue

            self.player.y = float(rect.top - self.player.height)
            if platform.kind == "boost":
                self.player.vy = SUPER_JUMP_SPEED
                self.set_status("Super saut", PLATFORM_BOOST, 900)
            else:
                self.player.vy = JUMP_SPEED

            if platform.kind == "fragile":
                platform.consume()
                self.set_status("Plateforme fragile", PLATFORM_FRAGILE, 900)
            return

    def update_enemies(self, dt_factor):
        for enemy in self.enemies:
            enemy.update(dt_factor)

    def update_bullets(self, dt_factor):
        for bullet in self.bullets:
            bullet.update(dt_factor)

    def handle_bullet_enemy_collisions(self):
        if not self.bullets or not self.enemies:
            return

        for bullet in self.bullets:
            if not bullet.active:
                continue
            bullet_rect = bullet.get_rect()
            for enemy in self.enemies:
                if not enemy.active:
                    continue
                if not bullet_rect.colliderect(enemy.get_rect()):
                    continue

                bullet.active = False
                enemy.active = False
                self.score += ENEMY_SCORE
                self.set_status("Ennemi detruit +{}".format(ENEMY_SCORE), HIGHLIGHT, 700)
                break

    def handle_player_enemy_collision(self):
        player_hitbox = self.player.get_rect().inflate(-16, -10)
        for enemy in self.enemies:
            if not enemy.active:
                continue
            if player_hitbox.colliderect(enemy.get_rect().inflate(-6, -6)):
                self.begin_name_input()
                return True
        return False

    def cleanup_dynamic_objects(self):
        self.bullets = [
            b
            for b in self.bullets
            if b.active and -40 <= b.y <= SCREEN_H + 100 and -50 <= b.x <= SCREEN_W + 50
        ]
        self.enemies = [e for e in self.enemies if e.active and e.y <= SCREEN_H + 120]

    def update_gameplay(self, dt):
        dt_factor = clamp(dt * FPS, 0.55, 1.85)

        direction = 0
        if self.move_left:
            direction -= 1
        if self.move_right:
            direction += 1

        self.player.move_horizontal(direction, PLAYER_MOVE_SPEED, dt_factor)
        self.player.wrap_horizontally(SCREEN_W)

        for platform in self.platforms:
            platform.update(dt_factor, SCREEN_W)

        previous_rect = self.player.get_rect()

        self.player.vy += GRAVITY * dt_factor
        self.player.vy = min(18.0, self.player.vy)
        self.player.y += self.player.vy * dt_factor

        self.handle_platform_collisions(previous_rect)

        if self.player.y < CAMERA_TARGET_Y and self.player.vy < 0:
            shift = CAMERA_TARGET_Y - self.player.y
            self.player.y = float(CAMERA_TARGET_Y)
            for platform in self.platforms:
                platform.shift_y(shift)
            for enemy in self.enemies:
                enemy.shift_y(shift)
            for bullet in self.bullets:
                bullet.shift_y(shift)
            self.distance += shift
            self.score = max(self.score, int(self.distance * SCORE_PER_PIXEL))

        self.replenish_platforms()
        self.update_enemies(dt_factor)
        self.update_bullets(dt_factor)
        self.handle_bullet_enemy_collisions()
        self.cleanup_dynamic_objects()

        if self.handle_player_enemy_collision():
            return

        if self.player.y > SCREEN_H + FAIL_Y_MARGIN:
            self.begin_name_input()

    def draw_cloud(self, x, y, size, color):
        w = size
        h = int(size * 0.56)
        pygame.draw.ellipse(self.screen, color, (x, y, w, h))
        pygame.draw.ellipse(self.screen, color, (x + w * 0.22, y - h * 0.25, w * 0.5, h * 0.65))
        pygame.draw.ellipse(self.screen, color, (x + w * 0.52, y - h * 0.1, w * 0.43, h * 0.6))

    def draw_background(self, game_mode=True):
        self.screen.blit(self.bg_game if game_mode else self.bg_menu, (0, 0))
        now = pygame.time.get_ticks()

        for cloud in self.clouds:
            drift = int((now * cloud["speed"]) % (SCREEN_W + 280))
            x = cloud["x"] + drift - 160
            y = cloud["y"]
            color = (255, 255, 255) if game_mode else (246, 252, 255)
            self.draw_cloud(x, y, cloud["size"], color)

    def draw_platforms(self):
        for platform in self.platforms:
            platform.draw(
                self.screen,
                normal_color=PLATFORM_NORMAL,
                moving_color=PLATFORM_MOVING,
                boost_color=PLATFORM_BOOST,
                fragile_color=PLATFORM_FRAGILE,
            )

    def draw_enemies(self):
        for enemy in self.enemies:
            enemy.draw(self.screen)

    def draw_bullets(self):
        for bullet in self.bullets:
            bullet.draw(self.screen)

    def draw_hud(self):
        hud = pygame.Rect(0, 0, SCREEN_W, 90)
        pygame.draw.rect(self.screen, (255, 255, 255), hud)
        pygame.draw.line(self.screen, CARD_BORDER, (0, 90), (SCREEN_W, 90), 2)

        text = self.font.render("Score {}".format(self.score), True, TEXT_MAIN)
        self.screen.blit(text, (20, 14))

        altitude = int(self.distance)
        alt_text = self.font_small.render("Altitude {} px".format(altitude), True, TEXT_DIM)
        self.screen.blit(alt_text, (23, 58))

        enemy_text = self.font_small.render("Ennemis {}".format(len(self.enemies)), True, TEXT_DIM)
        self.screen.blit(enemy_text, (280, 58))

        shoot_ready = pygame.time.get_ticks() >= self.shoot_cooldown_until_ms
        shoot_color = HIGHLIGHT if shoot_ready else TEXT_DIM
        shoot_state = "Tir F: pret" if shoot_ready else "Tir F: recharge"
        shoot_text = self.font_small.render(shoot_state, True, shoot_color)
        self.screen.blit(shoot_text, (462, 58))

        tips = self.font_small.render("Gauche/Droite ou Q/D: deplacer   F: tirer   T: pause   Y: menu", True, TEXT_DIM)
        self.screen.blit(tips, (SCREEN_W // 2 - tips.get_width() // 2, SCREEN_H - 34))

        if self.status_text and pygame.time.get_ticks() < self.status_until_ms:
            status = self.font_small.render(self.status_text, True, self.status_color)
            self.screen.blit(status, (SCREEN_W // 2 - status.get_width() // 2, 58))

    def draw_game(self):
        self.draw_background(game_mode=True)
        self.draw_platforms()
        self.draw_enemies()
        self.draw_bullets()
        self.player.draw(self.screen, PLAYER_BODY, PLAYER_OUTLINE)
        self.draw_hud()

    def draw_menu(self):
        self.draw_background(game_mode=False)

        panel = pygame.Rect(26, 26, SCREEN_W - 52, SCREEN_H - 52)
        pygame.draw.rect(self.screen, CARD, panel, border_radius=18)
        pygame.draw.rect(self.screen, CARD_BORDER, panel, 3, border_radius=18)

        title = self.font_title.render("DOODLE JUMPE", True, HIGHLIGHT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, 44))

        subtitle = self.font_small.render("Version borne arcade", True, TEXT_DIM)
        self.screen.blit(subtitle, (SCREEN_W // 2 - subtitle.get_width() // 2, 116))

        if self.cover:
            self.screen.blit(self.cover, (SCREEN_W // 2 - self.cover.get_width() // 2, 154))

        options = ["Jouer", "Highscores", "Quitter"]
        for index, label in enumerate(options):
            color = HIGHLIGHT if self.menu_index == index else TEXT_MAIN
            line = self.font.render(label, True, color)
            self.screen.blit(line, (SCREEN_W // 2 - line.get_width() // 2, 398 + index * 52))

        line_1 = self.font_small.render("Montez le plus haut possible en sautant de plateforme en plateforme.", True, TEXT_DIM)
        line_2 = self.font_small.render("F pour tirer en l'air et eliminer les ennemis.", True, TEXT_DIM)
        line_3 = self.font_small.render("Plateformes: orange=boost, bleue=mouvante, marron=fragile.", True, TEXT_DIM)
        self.screen.blit(line_1, (SCREEN_W // 2 - line_1.get_width() // 2, SCREEN_H - 120))
        self.screen.blit(line_2, (SCREEN_W // 2 - line_2.get_width() // 2, SCREEN_H - 94))
        self.screen.blit(line_3, (SCREEN_W // 2 - line_3.get_width() // 2, SCREEN_H - 68))

        tip = self.font_small.render("Haut/Bas: menu   F/Entree: valider   Y/Echap: quitter", True, TEXT_DIM)
        self.screen.blit(tip, (SCREEN_W // 2 - tip.get_width() // 2, SCREEN_H - 40))

    def draw_highscores(self):
        self.draw_background(game_mode=False)
        title = self.font_big.render("HIGHSCORES", True, HIGHLIGHT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, 50))

        scores = load_highscores(HIGHSCORE_PATH)
        if not scores:
            text = self.font.render("Aucun score enregistre", True, TEXT_MAIN)
            self.screen.blit(text, (SCREEN_W // 2 - text.get_width() // 2, 198))
        else:
            for i in range(MAX_SCORES):
                if i < len(scores):
                    name, value = scores[i]
                    line = "{:>2}. {}  -  {}".format(i + 1, name, value)
                else:
                    line = "{:>2}. {}  -  0".format(i + 1, "-" * NAME_LEN)
                txt = self.font.render(line, True, TEXT_MAIN)
                self.screen.blit(txt, (SCREEN_W // 2 - 176, 138 + i * 42))

        tip = self.font_small.render("F/Entree pour valider   Y/Echap pour retour", True, TEXT_DIM)
        self.screen.blit(tip, (SCREEN_W // 2 - tip.get_width() // 2, SCREEN_H - 56))

    def draw_pause(self):
        self.draw_game()

        overlay = pygame.Surface((SCREEN_W, SCREEN_H), pygame.SRCALPHA)
        overlay.fill((0, 0, 0, 150))
        self.screen.blit(overlay, (0, 0))

        card = pygame.Rect(SCREEN_W // 2 - 196, SCREEN_H // 2 - 134, 392, 258)
        pygame.draw.rect(self.screen, CARD, card, border_radius=14)
        pygame.draw.rect(self.screen, CARD_BORDER, card, 3, border_radius=14)

        title = self.font_big.render("PAUSE", True, HIGHLIGHT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, SCREEN_H // 2 - 106))

        options = ["Reprendre", "Retour menu"]
        for i, label in enumerate(options):
            color = HIGHLIGHT if self.pause_index == i else TEXT_MAIN
            txt = self.font.render(label, True, color)
            self.screen.blit(txt, (SCREEN_W // 2 - txt.get_width() // 2, SCREEN_H // 2 - 28 + i * 58))

    def draw_name_input(self):
        self.draw_background(game_mode=False)

        title = self.font_big.render("FIN DE PARTIE", True, ALERT)
        self.screen.blit(title, (SCREEN_W // 2 - title.get_width() // 2, 54))

        score_text = self.font.render("Score: {}".format(self.score), True, TEXT_MAIN)
        self.screen.blit(score_text, (SCREEN_W // 2 - score_text.get_width() // 2, 118))

        sub = self.font_small.render("Entrez votre pseudo (4 lettres)", True, TEXT_MAIN)
        self.screen.blit(sub, (SCREEN_W // 2 - sub.get_width() // 2, 170))

        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        box_w = 72
        box_gap = 18
        total_w = NAME_LEN * box_w + (NAME_LEN - 1) * box_gap
        start_x = SCREEN_W // 2 - total_w // 2

        for i in range(NAME_LEN):
            box = pygame.Rect(start_x + i * (box_w + box_gap), 226, box_w, 88)
            fill = (248, 233, 140) if i == self.name_index else (235, 242, 252)
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

        if key in KEY_SHOOT:
            self.shoot()
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
            name = "".join(alphabet[index] for index in self.name_chars)
            save_highscore(HIGHSCORE_PATH, name, self.score)
            self.state = "highscores"
        elif key in KEY_BACK:
            self.go_to_menu()

    def run(self):
        while self.running:
            dt = self.clock.tick(FPS) / 1000.0
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
                self.update_gameplay(dt)
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
    DoodleJumpeApp().run()
