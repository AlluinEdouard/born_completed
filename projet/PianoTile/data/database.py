import sqlite3
import os
import datetime

class Database:
    def __init__(self, db_path="data/database.db"):
        self.__db_path = db_path
        db_exists = os.path.exists(db_path)

        if os.path.dirname(db_path):
            os.makedirs(os.path.dirname(db_path), exist_ok=True)

        self.__conn = sqlite3.connect(self.__db_path, check_same_thread=False)
        self.__conn.execute('PRAGMA journal_mode=WAL;')
        self.__cursor = self.__conn.cursor()

        if not db_exists:
            self.initialize()

    def create_tables(self):
        self.__cursor.execute("""
            CREATE TABLE IF NOT EXISTS players (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL UNIQUE
            );
        """)

        self.__cursor.execute("""
            CREATE TABLE IF NOT EXISTS music (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                genre TEXT NOT NULL,
                release_date DATE,
                difficulte TEXT NOT NULL,
                UNIQUE(title, artist)
            );
        """)

        self.__cursor.execute("""
            CREATE TABLE IF NOT EXISTS scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT NOT NULL,
                music_id INTEGER NOT NULL,
                score INTEGER NOT NULL,
                FOREIGN KEY (player_name) REFERENCES players(name),
                FOREIGN KEY (music_id) REFERENCES music(id),
                UNIQUE(player_name, music_id)
            );
        """)

        self.__cursor.execute("""
            CREATE TABLE IF NOT EXISTS playlists (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                description TEXT NOT NULL
            );
        """)

        self.__conn.commit()

    def insert_tables(self):
        self.__cursor.execute("""
            INSERT OR REPLACE INTO music (title, artist, genre, release_date, difficulte) VALUES
            ("Sunflower", "Post Malone & Swae Lee", "Hip Hop", "2018-10-18", "Hard"),
            ("Sweater Weather", "The Neighbourhood", "Alternative Rock", "2013-03-28", "Medium"),
            ("Believer", "Imagine Dragons", "Rock", "2017-02-01", "Easy"),
            ("Blinding Lights", "The Weekend", "Synthwave", "2019-11-29", "Medium")
        """)

        self.__cursor.execute("""
            INSERT OR REPLACE INTO players (name, password) VALUES
            ('Invité', 'invite'),
            ('Emma', 'admin123')
        """)

        for music_id in [1, 2, 3, 4]:
            self.__cursor.execute("""
                SELECT 1 FROM scores WHERE player_name = 'Invité' AND music_id = ?
            """, (music_id,))
            if not self.__cursor.fetchone():
                self.__cursor.execute("""
                    INSERT INTO scores (player_name, music_id, score) VALUES (?, ?, ?)
                """, ('Invité', music_id, 0))

        self.__conn.commit()


    def initialize(self):
        """Initialise la base de donnees."""
        self.create_tables()
        self.insert_tables()

    # ----------------------------------- Getter ----------------------------------- #

    def getMusic(self):
        """Getter de la musique."""
        self.__cursor.execute("SELECT * FROM music")
        return self.__cursor.fetchall()

    def getScores(self):
        """Getter des scores."""
        self.__cursor.execute("SELECT * FROM scores")
        return self.__cursor.fetchall()

    def getPlayers(self):
        """Getter des joueurs."""
        self.__cursor.execute("SELECT * FROM players")
        return self.__cursor.fetchall()

    def getPlaylists(self):
        """Getter des playlists."""
        self.__cursor.execute("SELECT * FROM playlists")
        return self.__cursor.fetchall()

    def getMusicFromTitle(self, title):
        """Recupere les musiques dont le titre contient une chaîne donnee."""
        self.__cursor.execute("SELECT * FROM music WHERE title LIKE ?", (f"%{title}%",))
        return self.__cursor.fetchall()
    
    def getMusicSorted(self, difficulte=None, annee=None):
        """Recupere les musiques triees par liste de difficultes et/ou annees."""
        query = "SELECT * FROM music WHERE 1=1"
        params = []

        if difficulte:
            placeholders = ','.join(['?'] * len(difficulte))
            query += f" AND difficulte IN ({placeholders})"
            params.extend(difficulte)

        if annee:
            placeholders = ','.join(['?'] * len(annee))
            query += f" AND strftime('%Y', release_date) IN ({placeholders})"
            params.extend(map(str, annee))

        query += " ORDER BY id ASC"
        self.__cursor.execute(query, tuple(params))
        return self.__cursor.fetchall()
    
    def getTotalScoresByPlayer(self):
        self.__cursor.execute("""
            SELECT player_name, SUM(score) as total_score
            FROM scores
            GROUP BY player_name
            ORDER BY total_score DESC
        """)
        return self.__cursor.fetchall() 

    def getScoresByMusic(self):
        self.__cursor.execute("""
            SELECT s.player_name, m.title, s.score
            FROM scores s
            JOIN music m ON s.music_id = m.id
            ORDER BY s.player_name, m.title
        """)
        return self.__cursor.fetchall()

    def getAllDifficultes(self):
        """Retourne toutes les difficultes distinctes."""
        self.__cursor.execute("SELECT DISTINCT difficulte FROM music")
        return [row[0] for row in self.__cursor.fetchall()]

    def getAllAnnees(self):
        self.__cursor.execute("SELECT DISTINCT strftime('%Y', release_date) FROM music WHERE release_date IS NOT NULL")
        years = [row[0] for row in self.__cursor.fetchall() if row[0]]
        return sorted(years)

    def getReleaseDateFormatted(self, music_id):
        """
        Retourne la date de sortie d'une musique au format '6 janvier 2017' (français),
        a partir du format ISO stocke.
        """
        self.__cursor.execute("SELECT release_date FROM music WHERE id = ?", (music_id,))
        row = self.__cursor.fetchone()
        if not row or not row[0]:
            return None

        iso_date = row[0]  # format YYYY-MM-DD
        try:
            dt = datetime.datetime.strptime(iso_date, "%Y-%m-%d")
        except ValueError:
            return iso_date  # Si format non conforme, renvoyer tel quel

        # Dictionnaire mois en français
        mois_fr = {
            1: "janvier", 2: "fevrier", 3: "mars", 4: "avril",
            5: "mai", 6: "juin", 7: "juillet", 8: "août",
            9: "septembre", 10: "octobre", 11: "novembre", 12: "decembre"
        }

        # Construire la date format français sans le zero initial dans le jour
        return f"{dt.day} {mois_fr[dt.month]} {dt.year}"

    def getBestScoreForUser(self, player_name, music_id):
        self.__cursor.execute("""
            SELECT MAX(score)
            FROM scores
            WHERE player_name = ? AND music_id = ?
        """, (player_name, music_id))
        row = self.__cursor.fetchone()
        return row[0] if row and row[0] is not None else 0

    def getBestScoreAllUsers(self, music_id):
        self.__cursor.execute("""
            SELECT MAX(score)
            FROM scores
            WHERE music_id = ?
        """, (music_id,))
        row = self.__cursor.fetchone()
        return row[0] if row and row[0] is not None else 0


    # ----------------------------------- Setter ----------------------------------- #

    def setMusic(self, title, artist, genre, release_date, difficulte):
        """Setter de la musique."""
        self.__cursor.execute(
            "INSERT OR REPLACE INTO music (title, artist, genre, release_date, difficulte) VALUES (?, ?, ?, ?, ?)",
            (title, artist, genre, release_date, difficulte)
        )
        self.__conn.commit()

    def setScores(self, player_name, music_id, score):
        """Setter des scores."""
        self.__cursor.execute(
            "INSERT OR REPLACE INTO scores (player_name, music_id, score) VALUES (?, ?, ?)",
            (player_name, music_id, score)
        )
        self.__conn.commit()

    def setPlayers(self, name, age, password):
        """Setter des joueurs."""
        self.__cursor.execute("INSERT OR REPLACE players (name, age, password) VALUES (?, ?, ?)", (name, age, password))
        self.__conn.commit()

    # ----------------------------------- Add ----------------------------------- #

    def addMusic(self, title, artist, genre, release_date, difficulte):
        """Ajoute une musique a la base de donnees."""
        self.setMusic(title, artist, genre, release_date, difficulte)
    
    def addScores(self, player_name, music_id, score):
        """Ajoute un score a la base de donnees."""
        self.setScores(player_name, music_id, score)

    def addPlayers(self, name, age, password):
        """Ajoute un joueur a la base de donnees."""
        self.setPlayers(name, age, password)
        # Récupérer toutes les musiques existantes
        self.__cursor.execute("SELECT id FROM music")
        musics = self.__cursor.fetchall()

        # Insérer un score de 0 pour chaque musique
        for (music_id,) in musics:
            self.__cursor.execute("""
                INSERT OR IGNORE INTO scores (player_name, music_id, score)
                VALUES (?, ?, 0)
            """, (name, music_id))

        self.__conn.commit()

    # ----------------------------------- Edit ----------------------------------- #

    def editMusic(self, music_id, title=None, artist=None, genre=None, release_date=None, difficulte=None):
        """Modifie une musique dans la base de donnees."""
        if title:
            self.__cursor.execute("UPDATE music SET title = ? WHERE id = ?", (title, music_id))
        if artist:
            self.__cursor.execute("UPDATE music SET artist = ? WHERE id = ?", (artist, music_id))
        if genre:
            self.__cursor.execute("UPDATE music SET genre = ? WHERE id = ?", (genre, music_id))
        if release_date:
            self.__cursor.execute("UPDATE music SET release_date = ? WHERE id = ?", (release_date, music_id))
        if difficulte:
            self.__cursor.execute("UPDATE music SET difficulte = ? WHERE id = ?", (difficulte, music_id))
        self.__conn.commit()

    def editScores(self, score_id, player_name=None, score=None):
        """Modifie un score dans la base de donnees."""
        if player_name:
            self.__cursor.execute("UPDATE scores SET player_name = ? WHERE id = ?", (player_name, score_id))
        if score:
            self.__cursor.execute("UPDATE scores SET score = ? WHERE id = ?", (score, score_id))
        self.__conn.commit()

    def editPlayers(self, player_id, name=None, age=None, password=None):
        """Modifie un joueur dans la base de donnees."""
        if name:
            self.__cursor.execute("UPDATE players SET name = ? WHERE id = ?", (name, player_id))
        if age:
            self.__cursor.execute("UPDATE players SET age = ? WHERE id = ?", (age, player_id))
        if password:
            self.__cursor.execute("UPDATE players SET password = ? WHERE id = ?", (password, player_id))
        self.__conn.commit()

    # ----------------------------------- Delete ----------------------------------- #

    def deleteMusic(self, music_id):
        """Supprime une musique de la base de donnees."""
        self.__cursor.execute("DELETE FROM music WHERE id = ?", (music_id,))
        self.__conn.commit()

    def deleteScores(self, score_id):
        """Supprime un score de la base de donnees."""
        self.__cursor.execute("DELETE FROM scores WHERE id = ?", (score_id,))
        self.__conn.commit()

    def deletePlayers(self, player_id):
        """Supprime un joueur de la base de donnees."""
        self.__cursor.execute("DELETE FROM players WHERE id = ?", (player_id,))
        self.__conn.commit()
