import os

import pygame

SCREEN_W = 960
SCREEN_H = 700
PLAY_TOP = 90
PLAY_BOTTOM = SCREEN_H - 10

PADDLE_BASE_W = 140
PADDLE_WIDE_W = 220
PADDLE_H = 18
PADDLE_Y = SCREEN_H - 48
PADDLE_SPEED = 12.8

BALL_RADIUS = 10
BALL_BASE_SPEED = 4.7
BALL_MAX_SPEED = 8.8

BRICK_ROWS_BASE = 5
BRICK_COLS = 11
BRICK_H = 30
BRICK_GAP = 8

BONUS_DROP_CHANCE = 0.2
BONUS_SPEED = 3.6
WIDE_DURATION_MS = 14000

START_LIVES = 3
MAX_SCORES = 10
NAME_LEN = 4

LEVEL_CLEAR_SCORE = 500
BRICK_HIT_SCORE = 12
BRICK_BREAK_SCORE = 60
PADDLE_TOUCH_RESET_COMBO = True

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
HIGHSCORE_PATH = os.path.join(ROOT_DIR, "highscore")
SCORE_DB_PATH = os.path.join(ROOT_DIR, "highscores.db")
COVER_PATH = os.path.join(ROOT_DIR, "img", "image.png")

BG_TOP = (14, 21, 48)
BG_BOTTOM = (7, 10, 25)
HUD_BG = (20, 27, 59)
CARD = (22, 33, 70)
CARD_BORDER = (97, 166, 255)
TEXT_MAIN = (240, 246, 255)
TEXT_DIM = (170, 186, 226)
HIGHLIGHT = (98, 236, 255)
ALERT = (255, 108, 133)
BALL_COLOR = (255, 241, 199)
PADDLE_MAIN = (108, 239, 255)
PADDLE_EDGE = (221, 252, 255)
BRICK_PALETTE = [
    (255, 102, 120),
    (255, 150, 84),
    (255, 214, 92),
    (109, 226, 145),
    (103, 205, 255),
    (173, 153, 255),
]
BONUS_COLORS = {
    "WIDE": (109, 255, 202),
    "LIFE": (255, 136, 161),
    "SLOW": (151, 168, 255),
}

KEY_UP = {pygame.K_UP, pygame.K_z}
KEY_DOWN = {pygame.K_DOWN, pygame.K_s}
KEY_LEFT = {pygame.K_LEFT, pygame.K_q}
KEY_RIGHT = {pygame.K_RIGHT, pygame.K_d}
KEY_CONFIRM = {pygame.K_f, pygame.K_RETURN, pygame.K_SPACE}
KEY_PAUSE = {pygame.K_p, pygame.K_t}
KEY_BACK = {pygame.K_y, pygame.K_ESCAPE}
KEY_BACK_MENU = {pygame.K_ESCAPE}
