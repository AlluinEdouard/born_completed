import pygame

from constantes import WHITE, BLACK, RED, GREEN, BLUE, SCREEN_WIDTH, SCREEN_HEIGHT, FONT


class Menu():
    def __init__(self, screen: pygame.Surface):
        self.screen: pygame.Surface = screen
        self.selectedOption: int = 0
        self.texture: pygame.Surface = pygame.transform.scale(
            pygame.image.load('./assets/bg_pxl.jpg').convert(), (SCREEN_WIDTH, SCREEN_HEIGHT))
        #self.sonMenu = sonMenu

    def showMenu(self, keyEvent, pause: bool = False) -> bool:
        newGame: bool = False
        credits: bool = False
        if pause:
            numberOfOptions = 3
        else:
            numberOfOptions = 2

        goTogame: bool = False
        for event in keyEvent:
            if event.type == pygame.KEYDOWN:
                if event.key in (pygame.K_DOWN, pygame.K_s):
                    #pygame.mixer.Sound.play(self.sonMenu)
                    if self.selectedOption == numberOfOptions:
                        self.selectedOption = 0
                    else:
                        self.selectedOption += 1
                if event.key in (pygame.K_UP, pygame.K_z):
                    #pygame.mixer.Sound.play(self.sonMenu)
                    if self.selectedOption == 0:
                        self.selectedOption = numberOfOptions
                    else:
                        self.selectedOption -= 1

                if event.key in (pygame.K_r, pygame.K_RETURN, pygame.K_SPACE):
                    #pygame.mixer.Sound.play(self.sonMenu)
                    if pause:
                        if self.selectedOption == 0:
                            goTogame = True
                        elif self.selectedOption == 1:
                            newGame = True
                            goTogame = True
                        elif self.selectedOption == 2:
                            credits = True
                        elif self.selectedOption == 3:
                            pygame.quit()
                            exit(0)
                    else:
                        if self.selectedOption == 0:
                            goTogame = True
                            newGame = True
                        elif self.selectedOption == 1:
                            credits = True
                        elif self.selectedOption == 2:
                            pygame.quit()
                            exit(0)

                if event.key in (pygame.K_q, pygame.K_ESCAPE, pygame.K_f):
                    pygame.quit()
                    exit(0)

        self.screen.blit(self.texture, (0, 0))

        delta = 100 if pause else 0

        if pause:
            text_surface = FONT.render('REPRENDRE', False, (0, 0, 0))
            self.screen.blit(
                text_surface, (SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2))

            text_surface = FONT.render('NOUVELLE PARTIE', False, (0, 0, 0))
            self.screen.blit(text_surface, (SCREEN_WIDTH //
                                            2, SCREEN_HEIGHT // 2 + 100))
        else:
            text_surface = FONT.render('COMMENCER', False, (0, 0, 0))
            self.screen.blit(
                text_surface, (SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2))
            
        text_surface = FONT.render(
            'BALL BLAST', False, (0, 0, 0))
        
        self.screen.blit(text_surface, (SCREEN_WIDTH //
                                        2, (SCREEN_HEIGHT // 2) - 200))

        text_surface = FONT.render(
            'CRÉDITS', False, (0, 0, 0))  # Merci
        self.screen.blit(text_surface, (SCREEN_WIDTH //
                                        2, (SCREEN_HEIGHT // 2) + 100 + delta))

        text_surface = FONT.render('QUITTER', False, (0, 0, 0))
        self.screen.blit(text_surface, (SCREEN_WIDTH //
                                        2, (SCREEN_HEIGHT // 2) + 200 + delta))

        pygame.draw.circle(self.screen, WHITE, ((SCREEN_WIDTH // 2) - 50,
                           (SCREEN_HEIGHT // 2) + 100 * self.selectedOption + 25), 5)

        return goTogame, newGame, credits
    
    def showCredits(self):
        """Affiche l'écran des crédits"""
        self.screen.blit(self.texture, (0, 0))
        
        # Titre "PRODUIT PAR:"
        title_text = FONT.render('PRODUIT PAR:', False, (0, 0, 0))
        title_rect = title_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 - 50))
        self.screen.blit(title_text, title_rect)
        
        # Premier développeur
        dev1_text = FONT.render('Justin FONTAINE', False, (0, 0, 0))
        dev1_rect = dev1_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 + 20))
        self.screen.blit(dev1_text, dev1_rect)
        
        # Deuxième développeur
        dev2_text = FONT.render('Arnaud WISSOCQ', False, (0, 0, 0))
        dev2_rect = dev2_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 + 70))
        self.screen.blit(dev2_text, dev2_rect)
        
        # Instruction pour revenir
        back_text = FONT.render('Appuyez sur Q pour revenir', False, (0, 0, 0))
        back_rect = back_text.get_rect(center=(SCREEN_WIDTH // 2, SCREEN_HEIGHT // 2 + 150))
        self.screen.blit(back_text, back_rect)
        
        if (pygame.key.get_pressed()[pygame.K_q]
            or pygame.key.get_pressed()[pygame.K_ESCAPE]
            or pygame.key.get_pressed()[pygame.K_f]
            or pygame.key.get_pressed()[pygame.K_RETURN]):
            return False
        
        return True
        
