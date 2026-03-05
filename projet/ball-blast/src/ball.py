import random
import pygame
import math
from constantes import RED, SCREEN_WIDTH, SCREEN_HEIGHT, WHITE, BALL_SPEED_X, BALL_SPEED_FALL, BALL_TOP_BOUNCE, BALL_BOTTOM_BOUNCE

# Font
pygame.init()
font = pygame.font.SysFont('Arial', 24)

class Ball(pygame.sprite.Sprite):
    def __init__(self, x: int, y: int, radius: int, level: int = 0, color = RED):
        super().__init__()
        # Créer une surface transparente
        self.image = pygame.Surface((radius*2, radius*2), pygame.SRCALPHA)
        self.level: int = level
        self.life: int = 3
        self.color = color
        self.radius: int = radius
        
        # Générer des points pour créer un polygone irrégulier
        self.points = self._generate_rock_shape()
        
        # Dessiner le polygone sur la surface
        pygame.draw.polygon(self.image, self.color, self.points)
        
        # Dessiner les points de vie
        self.base_life_points:int = random.randint(1, round(radius*0.75))
        self.life_points:int = self.base_life_points
        self.life_points_surface = font.render(str(self.life_points), True, WHITE)
        self.life_points_surface_rect = self.life_points_surface.get_rect()
        self.life_points_surface_rect.center = (self.radius, self.radius)
        self.image.blit(self.life_points_surface, self.life_points_surface_rect)
        
        # Créer un masque de collision pour une détection plus précise
        self.mask = pygame.mask.from_surface(self.image)
        
        self.rect: pygame.Rect = self.image.get_rect()
        self.rect.x = x
        self.rect.y = y
        
        # Vitesse de déplacement
        self.speed_y: int = -2
        self.speed_x: int = random.randint(1*BALL_SPEED_X, 2*BALL_SPEED_X) * (-1)**random.randint(1, 2)
    
    def _generate_rock_shape(self):
        """Génère des points pour créer une forme de roche irrégulière"""
        points = []
        num_points = random.randint(6, 8)  # Nombre de points du polygone
        
        for i in range(num_points):
            angle = (2 * math.pi * i) / num_points
            # Ajouter une variation aléatoire au rayon
            x = self.radius + self.radius * math.cos(angle)
            y = self.radius + self.radius * math.sin(angle)
            points.append((int(x), int(y)))
            
        return points
    
    def update(self):
        self.rect.y += self.speed_y
        self.rect.x += self.speed_x
        
        self.speed_x = -self.speed_x if (self.rect.left < 0 and self.speed_x < 0) or (self.rect.right > SCREEN_WIDTH and self.speed_x > 0) else self.speed_x 
        self.speed_y = random.randint(BALL_TOP_BOUNCE, BALL_BOTTOM_BOUNCE) if self.rect.bottom > SCREEN_HEIGHT else self.speed_y + BALL_SPEED_FALL
        
    def take_damage(self) -> bool:
        self.life_points -= 1
        
        self.life_points_surface = font.render(str(self.life_points), True, WHITE)
        self.life_points_surface_rect = self.life_points_surface.get_rect()
        self.life_points_surface_rect.center = (self.radius, self.radius)
        
        self.image = pygame.Surface((self.image.get_rect().width, self.image.get_rect().width), pygame.SRCALPHA)
        
        pygame.draw.polygon(self.image, self.color, self.points)
        self.image.blit(self.life_points_surface, self.life_points_surface_rect)
        
        # Mettre à jour le masque de collision
        self.mask = pygame.mask.from_surface(self.image)
        return self.life_points == 0
        
        
    def decale(self, decale: int):
        self.rect.x += decale
        
        if self.speed_x * decale < 0:
            self.speed_x *= -1
    
        
    def level(self)->int:
        return self.level