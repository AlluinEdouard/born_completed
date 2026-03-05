#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Jeu Tron pour borne d'arcade
Jouable en solo contre une IA ou à deux joueurs
"""

import pygame
import sys
import os
from menu_main import Menu
from game_main import Game
from config import *  # Import all constants from config.py

# Highscore helpers (format: NAME-SCORE)
def _normalize_name(name):
    name = "".join(ch for ch in name.upper() if ch.isalnum())
    if not name:
        name = "AAA"
    return (name + "AAA")[:3]


def _load_highscores(path):
    scores = []
    try:
        with open(path, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if not line:
                    continue
                parts = line.rsplit("-", 1)
                if len(parts) != 2:
                    continue
                name, score_str = parts
                try:
                    score_val = int(score_str)
                except ValueError:
                    continue
                scores.append((name, score_val))
    except FileNotFoundError:
        pass
    return scores


def _save_highscore(path, name, score, limit=10):
    name = _normalize_name(name)
    scores = _load_highscores(path)
    scores.append((name, max(0, int(score))))
    scores.sort(key=lambda x: x[1], reverse=True)
    scores = scores[:limit]
    with open(path, "w", encoding="utf-8") as f:
        for i, (n, s) in enumerate(scores):
            f.write(f"{n}-{s}")
            if i != len(scores) - 1:
                f.write("\n")

# Initialisation de Pygame
pygame.init()
pygame.mixer.init()  # Pour les effets sonores et musiques

class TronGame:
    def __init__(self):
        global SCREEN_WIDTH, SCREEN_HEIGHT
        
        # Configuration de l'écran
        if FULLSCREEN:
            info = pygame.display.Info()
            self.screen = pygame.display.set_mode((info.current_w, info.current_h), pygame.FULLSCREEN)
            SCREEN_WIDTH = info.current_w
            SCREEN_HEIGHT = info.current_h
        else:
            self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        
        pygame.display.set_caption(TITLE)
        self.clock = pygame.time.Clock()

        # États du jeu
        self.running = True
        self.current_state = "menu"
        self.difficulty = "moyen"
        self.move_delay = MOVE_DELAY
        self.fullscreen_mode = FULLSCREEN
        
        # Composants
        self.menu = Menu(self.screen)
        self.game = None
        self.options = None
        self.score_screen = None

        # Lancer la musique du menu
        self.current_music = None
        self.play_music("./assets/sounds/music_menu.wav")
        
    def play_music(self, filepath, fade_ms=1000):
        """Jouer une musique en boucle avec fondu de transition"""
        if self.current_music == filepath:
            return  # Ne rien faire si la même musique est déjà en cours
            
        try:
            pygame.mixer.music.fadeout(fade_ms)
            pygame.time.delay(fade_ms)  # Attendre la fin du fondu avant de jouer la nouvelle
            pygame.mixer.music.load(filepath)
            # Version compatible avec pygame 1.9.4 (sans fade_ms)
            pygame.mixer.music.play(-1)  # Jouer en boucle (-1)
            self.current_music = filepath
        except pygame.error as e:
            print(f"Erreur lors du chargement de la musique : {e}")

    def toggle_fullscreen(self):
        """Fonction conservée pour compatibilité mais désactivée"""
        # La fonctionnalité de plein écran a été désactivée
        pass

    def run(self):
        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False
                elif event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE or event.key == pygame.K_f:  # ESCAPE ou touche f
                        if self.current_state in ["game", "options", "score_screen"]:
                            self.current_state = "menu"
                            self.play_music("./assets/sounds/music_menu.wav")
                        else:
                            self.running = False
                    # Fonctionnalité de plein écran retirée

                # Transmettre les événements à l'état courant
                if self.current_state == "menu":
                    action = self.menu.handle_event(event)
                    if action:
                        self.process_menu_action(action)
                elif self.current_state == "game" and self.game:
                    self.game.handle_event(event)
                elif self.current_state == "options" and self.options:
                    action = self.options.handle_event(event)
                    if action:
                        self.current_state = action
                elif self.current_state == "score_screen" and self.score_screen:
                    action = self.score_screen.handle_event(event)
                    if action:
                        if action == "replay":
                            self.game = Game(self.screen, mode=self.game.mode, difficulty=self.difficulty, move_delay=self.move_delay)
                            self.play_music("./assets/sounds/music_game.wav")
                            self.current_state = "game"
                        elif action == "menu":
                            self.current_state = "menu"
                            self.play_music("./assets/sounds/music_menu.wav")

            # Mise à jour
            if self.current_state == "menu":
                self.menu.update()
            elif self.current_state == "game" and self.game:
                game_over = self.game.update()
                if game_over:
                    self.save_highscore()
                    from score_screen import ScoreScreen
                    self.score_screen = ScoreScreen(self.screen, self.game.get_game_stats())
                    self.current_state = "score_screen"
            elif self.current_state == "options" and self.options:
                self.options.update()
            elif self.current_state == "score_screen" and self.score_screen:
                self.score_screen.update()

            # Rendu
            self.screen.fill(BLACK)

            if self.current_state == "menu":
                self.menu.draw()
            elif self.current_state == "game" and self.game:
                self.game.draw()
            elif self.current_state == "options" and self.options:
                self.options.draw()
            elif self.current_state == "score_screen" and self.score_screen:
                self.score_screen.draw()

            pygame.display.flip()
            self.clock.tick(FPS)

    def process_menu_action(self, action):
        if action == "single_player":
            self.game = Game(self.screen, mode="single", difficulty=self.difficulty, move_delay=self.move_delay)
            self.play_music("./assets/sounds/music_game.wav")
            self.current_state = "game"
        elif action == "two_players":
            self.game = Game(self.screen, mode="multi", move_delay=self.move_delay)
            self.play_music("./assets/sounds/music_game.wav")
            self.current_state = "game"
        elif action == "options":
            from options_main import Options
            self.options = Options(self.screen, self)
            self.current_state = "options"
        elif action == "quit":
            self.running = False

    def save_highscore(self):
        winner = self.game.winner
        p1_len = len(self.game.player1.positions)
        p2_len = len(self.game.player2.positions)
        if winner == "Joueur 1":
            name = "J1"
            score = p1_len
        elif winner == "Joueur 2":
            name = "J2"
            score = p2_len
        else:
            name = "NUL"
            score = max(p1_len, p2_len)
        _save_highscore("highscore", name, score)

if __name__ == "__main__":
    game = TronGame()
    game.run()
    pygame.quit()
    sys.exit()
