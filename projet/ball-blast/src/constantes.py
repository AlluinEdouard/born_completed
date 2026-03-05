import pygame.font

# Screen dimensions
SCREEN_WIDTH = 1240 #1024 
SCREEN_HEIGHT = 1024 #768 

# Colors
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
RED = (255, 0, 0)
GREEN = (0,255,0)
BLUE = (0,0,255)

PLAYER_SPEED = 10

BALL_SPEED_X = 2
BALL_SPEED_FALL = 0.15
#BALL_TOP_BOUNCE = -11
BALL_TOP_BOUNCE = -17
#BALL_BOTTOM_BOUNCE = -9
BALL_BOTTOM_BOUNCE = -14
BALL_EQUIVALENT = 14
FIRERATE = 7

pygame.font.init()

#Fonts
FONT = pygame.font.SysFont('Comic Sans MS', 30)
FONT_SCORE = pygame.font.SysFont('Comic Sans MS', 18)