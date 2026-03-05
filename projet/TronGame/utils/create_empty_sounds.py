"""
Créer des fichiers sonores vides pour le jeu Tron
"""

import os

# Vérifier si le dossier sounds existe
sounds_dir = os.path.join("assets", "sounds")
os.makedirs(sounds_dir, exist_ok=True)

# Liste des fichiers audio à créer
sound_files = ["navigate.wav", "select.wav", "crash.wav", "move.wav"]

# Créer des fichiers vides
for sound_file in sound_files:
    file_path = os.path.join(sounds_dir, sound_file)
    with open(file_path, 'wb') as f:
        # Créer un fichier WAV minimal (en-tête WAV + silence)
        # En-tête WAV
        f.write(b'RIFF')
        f.write((36).to_bytes(4, byteorder='little'))  # Taille du fichier - 8
        f.write(b'WAVE')

        # Format chunk
        f.write(b'fmt ')
        f.write((16).to_bytes(4, byteorder='little'))  # Taille du chunk format
        f.write((1).to_bytes(2, byteorder='little'))   # Format PCM
        f.write((1).to_bytes(2, byteorder='little'))   # Mono
        f.write((44100).to_bytes(4, byteorder='little'))  # Taux d'échantillonnage
        f.write((44100 * 1 * 2).to_bytes(4, byteorder='little'))  # Débit d'octets
        f.write((2).to_bytes(2, byteorder='little'))   # Alignement des blocs
        f.write((16).to_bytes(2, byteorder='little'))  # Bits par échantillon

        # Data chunk
        f.write(b'data')
        f.write((0).to_bytes(4, byteorder='little'))  # Taille des données (vide)

print("Fichiers sons vides créés dans le dossier", sounds_dir)
