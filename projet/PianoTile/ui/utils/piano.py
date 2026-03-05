import pygame, librosa, random
from ui.utils.note import Note

class Piano:
    def __init__(self, gameview):
        self.__gameView = gameview
        self.__filepath = "./assets/music/" + self.__gameView.getWindowManager().getMusicSelect().lower().replace('play musique ', '').replace(' ', '').replace("'", '').replace(',', '') + ".mp3"
        self.__difficulty = 1
        self.__notes = self.generate_notes()

    def getNotes(self):
        return self.__notes

    def increaseDifficulty(self):
        self.__difficulty += 1

    def play(self):
        pygame.mixer.init()
        pygame.mixer.music.load(self.__filepath)
        pygame.mixer.music.play()

    def pause(self):
        pygame.mixer.music.pause()

    def generate_notes(self):
        print("Génération des notes à partir du fichier :", self.__filepath)
        notes = []

        # Charger le fichier en mono, à faible sample rate (optimisation mémoire)
        y, sr = librosa.load(self.__filepath, sr=22050, mono=True)

        # Analyse du rythme
        tempo, beat_frames = librosa.beat.beat_track(y=y, sr=sr)
        beat_times = librosa.frames_to_time(beat_frames, sr=sr)

        for time in beat_times:
            nb_notes = min(self.__difficulty, random.randint(1, 4))
            for _ in range(nb_notes):
                position = random.choice(["left", "middle", "right", "top"])
                note = Note(gameview=self.__gameView, position=position, timestamp=time)
                notes.append(note)

        print(f"{len(notes)} notes générées.")
        return notes

    def getCurrentTime(self):
        return pygame.mixer.music.get_pos() / 1000.0
