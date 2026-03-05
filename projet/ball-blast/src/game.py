from ball import Ball
from bullet import Bullet
from player import Player
from constantes import WHITE, BLACK, RED, GREEN, BLUE, SCREEN_WIDTH, SCREEN_HEIGHT, FONT, FIRERATE, BALL_EQUIVALENT, FONT_SCORE

import pygame
import random


class Game():
    def __init__(self, screen: pygame.Surface):
        self.screen: pygame.Surface = screen
        self.level = 0
        # Création des variables de jeu
        self.ball_level = [[BLACK, 50], [RED, 40], [GREEN, 33], [BLUE, 25]]
        self.ballEquivalents = [10,7,3,1]
        self.ballsToSpawn = BALL_EQUIVALENT
        self.perdu: bool = False
        self.shootCD: int = 0
        self.texture: pygame.Surface = pygame.transform.scale(
            pygame.image.load('./assets/bg_pxl.jpg').convert(), (SCREEN_WIDTH, SCREEN_HEIGHT))

        # Compteurs de frames pour laisser du temps avant de passer à la suite
        self.frameNumberLoseAnim: int = 0
        self.frameNumberWinAnim : int = 0
        self.frameNumberSpawnBalls : int = 0
        self.frameNumberBeginLevel: int = 0
        
        # Sons
        # self.sonExplosion = sonExplosion
        # self.sonWin = sonWin
        # self.sonPop = sonPop
        
        self.shootCD: int = 0
        self.path: str = "./assets/explosion_frames/frame-"
        self.perdu = False

        # Initialize player, balls, and bullets
        self.player = Player()
        wheels = self.player.getWheels()
        self.playerGroup = pygame.sprite.Group()
        self.playerGroup.add(self.player)
        self.balls = pygame.sprite.Group()
        self.bullets = pygame.sprite.Group()
        self.all_sprites = pygame.sprite.Group()
        self.all_sprites.add(self.playerGroup)
        self.all_sprites.add(wheels[0])
        self.all_sprites.add(wheels[1])
    
    #Crée toutes les boules et les ajoutes dans le jeu
    def createBalls(self):
        while True:
            ballType : int = random.randint(0,len(self.ball_level)-1)
            if self.ballEquivalents[ballType] <= self.ballsToSpawn:
                newball = Ball(random.randint(100, SCREEN_WIDTH-100),
                        random.randint(-100, -40), self.ball_level[ballType][1], ballType, self.ball_level[ballType][0])
                self.balls.add(newball)
                self.all_sprites.add(newball)
                self.ballsToSpawn -= self.ballEquivalents[ballType]
                return
       
    #Passe au niveau suivant     
    def nextLevel(self):
        self.level += 1
        self.ballsToSpawn = BALL_EQUIVALENT + self.level * 5
        
        self.frameNumberWinAnim : int = 0
        self.frameNumberSpawnBalls : int = 0
        self.frameNumberBeginLevel = 0

    def showGame(self, events=None):
        if events is None:
            events = pygame.event.get()
        
        if self.frameNumberBeginLevel < 60:
            self.frameNumberBeginLevel += 1
            self.screen.blit(self.texture, (0,0))
            self.screen.blit(FONT.render('NIVEAU ' + str(self.level), True, (0, 0, 0)),(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2))
            return False, False
            
        for event in events:
            if event.type == pygame.QUIT:
                return False, True
            if event.type == pygame.KEYDOWN:
                if event.key in (pygame.K_f, pygame.K_ESCAPE, pygame.K_q):
                    if self.perdu:
                        return True, False
                    return False, True

        self.shootCD += 1
        
        if self.ballsToSpawn > 0:
            if self.frameNumberSpawnBalls % 20 == 0:
                self.createBalls()
            self.frameNumberSpawnBalls += 1

        # Toutes les 10 frames, on tire
        if self.shootCD == FIRERATE and not self.perdu:
            self.shootCD = 0
            bullet = Bullet(self.player.rect.centerx, self.player.rect.top)
            self.all_sprites.add(bullet)
            self.bullets.add(bullet)

        # Update
        self.all_sprites.update()

        # Check for collisions
        hitBalls = pygame.sprite.groupcollide(
            self.balls, self.bullets, False, True)
        for hit in hitBalls:

            destroyed: bool = hit.take_damage()
            # Si la boule touchée n'est pas la plus petite
            if destroyed:
                self.player.score += hit.base_life_points
                if hit.level < len(self.ball_level)-1:
                    #pygame.mixer.Sound.play(self.sonPop)
                    # On crée une boule aux mêmes co que la boule détruite
                    ball1 = Ball(
                        hit.rect.x, hit.rect.y, self.ball_level[hit.level+1][1], hit.level+1, self.ball_level[hit.level+1][0])

                    # on la décale à droite
                    ball1.decale(10)

                    # Ajout au groupe de collision des boules
                    self.balls.add(ball1)

                    # Ajout au groupe de tout les sprites
                    self.all_sprites.add(ball1)

                    # On crée une autre boule
                    ball2 = Ball(
                        hit.rect.x, hit.rect.y, self.ball_level[hit.level+1][1], hit.level+1, self.ball_level[hit.level+1][0])

                    # On la décale à gauche
                    ball2.decale(-10)

                    # Ajout au groupe de collision des boules
                    self.balls.add(ball2)

                    # Ajout au groupe de tout les sprites
                    self.all_sprites.add(ball2)
                hit.kill()

        # Draw / render

        # On render le fond en premier sinon tout est derrière
        self.screen.blit(self.texture, (0,0))

        self.all_sprites.draw(self.screen)

        # On affiche le score
        self.score_box = pygame.Surface((150, 50), pygame.SRCALPHA)
        pygame.draw.rect(self.score_box, (255, 255, 255, 180), self.score_box.get_rect())
        self.score_texte = FONT_SCORE.render("Score : " + str(self.player.score), True, (0, 0, 0))
        self.score_box.blit(self.score_texte, (10, 10))
        self.screen.blit(self.score_box, (10, 10))

        hitPlayer = pygame.sprite.groupcollide(
            self.balls, self.playerGroup, False, False)

        # Si le perso a été touché par une balle, il perd
        if hitPlayer:
            self.perdu = True
            self.player.kill()

        if self.perdu:
            text_surface = FONT.render('PERDUUUUUUU', False, (0, 0, 0))
            self.screen.blit(
                text_surface, (SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2))
            if self.frameNumberLoseAnim == 0:
                #pygame.mixer.Sound.play(self.sonExplosion)
                pygame.mixer.music.load("./assets/sound//musicdeath.mp3")
                pygame.mixer.music.play()

            if self.frameNumberLoseAnim < 17:
                self.frameNumberLoseAnim += 1
                if self.frameNumberLoseAnim <= 9:
                    deathImage = pygame.image.load(
                        self.path + "0" + str(self.frameNumberLoseAnim) + ".png")
                else:
                    deathImage = pygame.image.load(
                        self.path + str(self.frameNumberLoseAnim) + ".png")
                self.screen.blit(deathImage, (self.player.rect.left -
                                              20, self.player.rect.top-80))

        # Si il n'y a plus aucune balle, il gagne
        if len(self.balls.sprites()) == 0 and not self.perdu:
            text_surface = FONT.render('GAGNÉ', False, (0, 0, 0))
            self.screen.blit(
                text_surface, (SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2))
            
            #if self.frameNumberWinAnim == 0 and not self.perdu:
                #pygame.mixer.Sound.play(self.sonWin)
            
            if self.frameNumberWinAnim == 240 and not self.perdu:
                self.nextLevel()
            self.frameNumberWinAnim += 1

        return False, False

    def registerScore(self):
        """Affiche une page pour saisir un pseudo de 3 caractères avec les flèches directionnelles"""
        # Alphabet disponible
        alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        
        # Pseudo de 3 caractères, initialisé à "AAA"
        pseudo_chars = [0, 0, 0]  # Index dans l'alphabet
        current_position = 0  # Position actuelle (0, 1 ou 2)
        
        input_active = True
        cursor_visible = True
        cursor_timer = 0
        
        while input_active:
            cursor_timer += 1
            if cursor_timer >= 20:  # Clignote toutes les 20 frames
                cursor_visible = not cursor_visible
                cursor_timer = 0
            
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    input_active = False
                    pygame.quit()
                    break
                
                if event.type == pygame.KEYDOWN:
                    if event.key in (pygame.K_r, pygame.K_RETURN, pygame.K_SPACE):
                        # Valider le pseudo et enregistrer le score
                        pseudo = ''.join([alphabet[i] for i in pseudo_chars])
                        self._saveScore(pseudo)
                        input_active = False
                    elif event.key in (pygame.K_f, pygame.K_ESCAPE, pygame.K_q):
                        # Annuler la saisie
                        input_active = False
                    elif event.key == pygame.K_UP:
                        # Lettre suivante dans l'alphabet
                        pseudo_chars[current_position] = (pseudo_chars[current_position] + 1) % len(alphabet)
                    elif event.key == pygame.K_DOWN:
                        # Lettre précédente dans l'alphabet
                        pseudo_chars[current_position] = (pseudo_chars[current_position] - 1) % len(alphabet)
                    elif event.key == pygame.K_LEFT:
                        # Position précédente
                        current_position = (current_position - 1) % 3
                    elif event.key == pygame.K_RIGHT:
                        # Position suivante
                        current_position = (current_position + 1) % 3
            
            # Affichage
            self.screen.blit(self.texture, (0,0))
            
            # Titre
            title_text = FONT.render("ENREGISTRER LE SCORE !", True, WHITE)
            title_rect = title_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 - 100))
            self.screen.blit(title_text, title_rect)
            
            # Score
            score_text = FONT.render(f"Score: {self.player.score}", True, WHITE)
            score_rect = score_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 - 60))
            self.screen.blit(score_text, score_rect)
            
            # Instructions
            instruction_text = FONT_SCORE.render("Entrez votre pseudo (3 lettres):", True, WHITE)
            instruction_rect = instruction_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 - 20))
            self.screen.blit(instruction_text, instruction_rect)
            
            # Affichage des 3 caractères
            char_spacing = 60
            start_x = SCREEN_WIDTH // 2 - char_spacing
            
            for i in range(3):
                # Boîte pour chaque caractère
                char_box = pygame.Surface((50, 50), pygame.SRCALPHA)
                
                # Couleur différente pour la position actuelle
                if i == current_position:
                    pygame.draw.rect(char_box, (255, 255, 0, 200), char_box.get_rect())  # Jaune
                    pygame.draw.rect(char_box, BLACK, char_box.get_rect(), 3)
                else:
                    pygame.draw.rect(char_box, (255, 255, 255, 200), char_box.get_rect())  # Blanc
                    pygame.draw.rect(char_box, BLACK, char_box.get_rect(), 2)
                
                # Lettre
                letter = alphabet[pseudo_chars[i]]
                letter_surface = FONT.render(letter, True, BLACK)
                letter_rect = letter_surface.get_rect(center=(25, 25))
                char_box.blit(letter_surface, letter_rect)
                
                # Position de la boîte
                box_rect = char_box.get_rect(center=(start_x + i * char_spacing, SCREEN_HEIGHT // 2 + 20))
                self.screen.blit(char_box, box_rect)
                
                # Curseur clignotant sous la position actuelle
                if i == current_position and cursor_visible:
                    cursor_y = SCREEN_HEIGHT // 2 + 50
                    pygame.draw.line(self.screen, WHITE, 
                                   (start_x + i * char_spacing - 15, cursor_y), 
                                   (start_x + i * char_spacing + 15, cursor_y), 3)
            
            # Instructions de contrôle
            controls_text = [
                "↑↓ : Changer la lettre",
                "←→ : Changer de position", 
                "R : Valider",
                "F : Annuler"
            ]
            
            for j, text in enumerate(controls_text):
                control_surface = FONT_SCORE.render(text, True, WHITE)
                control_rect = control_surface.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 + 90 + j * 20))
                self.screen.blit(control_surface, control_rect)
            
            pygame.display.flip()
            pygame.time.Clock().tick(40)
        
        return False,False
    
    def _saveScore(self, pseudo):
        """Sauvegarde le score avec le pseudo dans le fichier highscore, trié par score décroissant"""
        try:
            # Lire les scores existants
            scores = []
            try:
                with open("highscore", "r", encoding="utf-8") as file:
                    for line in file:
                        line = line.strip()
                        parts = line.split('-')
                        pseudo_existing = parts[0]
                        score_existing = int(parts[1])
                        scores.append((pseudo_existing, score_existing))
            except FileNotFoundError:
                # Le fichier n'existe pas encore, on commence avec une liste vide
                pass
            
            # Ajouter le nouveau score
            scores.append((pseudo, self.player.score))
            
            # Trier par score décroissant (du meilleur au plus petit)
            scores.sort(key=lambda x: x[1], reverse=True)
            
            # Réécrire le fichier avec tous les scores triés
            with open("highscore", "w", encoding="utf-8") as file:
                for pseudo_score, score in scores:
                    file.write(f"{pseudo_score}-{score}\n")
                    
        except Exception as e:
            print(f"Erreur lors de la sauvegarde du score: {e}")
        
