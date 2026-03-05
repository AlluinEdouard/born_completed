"""
Utilitaire pour générer des sons simples pour le jeu Tron
"""

import os
import pygame
import pygame.mixer
import numpy as np
from scipy.io import wavfile

# Initialisation de pygame
pygame.init()
pygame.mixer.init()

# Vérifier si le dossier sounds existe
sounds_dir = os.path.join("assets", "sounds")
os.makedirs(sounds_dir, exist_ok=True)

def generate_tone(freq, duration, volume=0.5, sample_rate=44100):
    """Génère un son pur à la fréquence donnée"""
    t = np.linspace(0, duration, int(sample_rate * duration), False)
    tone = np.sin(freq * t * 2 * np.pi)

    # Ajouter une enveloppe pour éviter les clics
    fade = 0.05  # 50ms fade in/out
    fade_samples = int(fade * sample_rate)

    # Fade in
    if fade_samples > 0:
        tone[:fade_samples] *= np.linspace(0, 1, fade_samples)

    # Fade out
    if fade_samples > 0:
        tone[-fade_samples:] *= np.linspace(1, 0, fade_samples)

    # Ajuster le volume
    tone *= volume

    # Convertir en int16
    tone = (tone * 32767).astype(np.int16)

    return tone

def generate_noise(duration, volume=0.5, sample_rate=44100):
    """Génère du bruit blanc"""
    noise = np.random.uniform(-1, 1, int(sample_rate * duration))

    # Ajouter une enveloppe pour éviter les clics
    fade = 0.05  # 50ms fade in/out
    fade_samples = int(fade * sample_rate)

    # Fade in
    if fade_samples > 0:
        noise[:fade_samples] *= np.linspace(0, 1, fade_samples)

    # Fade out
    if fade_samples > 0:
        noise[-fade_samples:] *= np.linspace(1, 0, fade_samples)

    # Ajuster le volume
    noise *= volume

    # Convertir en int16
    noise = (noise * 32767).astype(np.int16)

    return noise

def sweep_tone(start_freq, end_freq, duration, volume=0.5, sample_rate=44100):
    """Génère un balayage de fréquence"""
    t = np.linspace(0, duration, int(sample_rate * duration), False)

    # Calculer la phase instantanée en fonction du temps
    phase = 2 * np.pi * (start_freq * t + (end_freq - start_freq) * t ** 2 / (2 * duration))

    # Générer le signal
    sweep = np.sin(phase)

    # Ajouter une enveloppe pour éviter les clics
    fade = 0.05  # 50ms fade in/out
    fade_samples = int(fade * sample_rate)

    # Fade in
    if fade_samples > 0:
        sweep[:fade_samples] *= np.linspace(0, 1, fade_samples)

    # Fade out
    if fade_samples > 0:
        sweep[-fade_samples:] *= np.linspace(1, 0, fade_samples)

    # Ajuster le volume
    sweep *= volume

    # Convertir en int16
    sweep = (sweep * 32767).astype(np.int16)

    return sweep

# Son de navigation dans le menu
navigate_sound = generate_tone(440, 0.1, 0.3)  # La 440Hz
wavfile.write(os.path.join(sounds_dir, "navigate.wav"), 44100, navigate_sound)

# Son de sélection dans le menu
select_sound = sweep_tone(440, 880, 0.2, 0.4)  # La 440Hz à La 880Hz
wavfile.write(os.path.join(sounds_dir, "select.wav"), 44100, select_sound)

# Son de crash
crash_sound = np.concatenate([
    generate_noise(0.05, 0.8),
    generate_tone(110, 0.3, 0.6),  # La 110Hz
])
wavfile.write(os.path.join(sounds_dir, "crash.wav"), 44100, crash_sound)

# Son de mouvement
move_sound = generate_tone(220, 0.05, 0.1)  # La 220Hz
wavfile.write(os.path.join(sounds_dir, "move.wav"), 44100, move_sound)

print("Sons générés avec succès dans le dossier", sounds_dir)
