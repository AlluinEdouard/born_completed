import os
import sqlite3

from constants import MAX_SCORES, NAME_LEN, SCORE_DB_PATH


def normalize_name(name):
    clean = "".join(ch for ch in name.upper() if ch.isalnum())
    if not clean:
        clean = "A" * NAME_LEN
    return (clean + ("A" * NAME_LEN))[:NAME_LEN]


def _read_legacy_highscores(path):
    scores = []
    if not os.path.exists(path):
        return scores

    try:
        with open(path, "r", encoding="utf-8") as file:
            for line in file:
                line = line.strip()
                if not line or "-" not in line:
                    continue
                name, score_str = line.split("-", 1)
                try:
                    value = int(score_str)
                except ValueError:
                    continue
                scores.append((normalize_name(name), max(0, value)))
    except OSError:
        return []

    return scores


def _init_score_db(path):
    try:
        with sqlite3.connect(SCORE_DB_PATH) as conn:
            conn.execute(
                """
                CREATE TABLE IF NOT EXISTS scores (
                    name TEXT PRIMARY KEY,
                    score INTEGER NOT NULL CHECK (score >= 0)
                )
                """
            )
            count = conn.execute("SELECT COUNT(*) FROM scores").fetchone()[0]
            if count == 0:
                for name, value in _read_legacy_highscores(path):
                    conn.execute(
                        """
                        INSERT INTO scores(name, score) VALUES (?, ?)
                        ON CONFLICT(name) DO UPDATE SET score = MAX(score, excluded.score)
                        """,
                        (normalize_name(name), max(0, int(value))),
                    )
            conn.commit()
    except sqlite3.Error:
        pass


def _sync_highscore_file(path, scores):
    try:
        with open(path, "w", encoding="utf-8") as file:
            top_scores = scores[:MAX_SCORES]
            for index, (entry_name, entry_score) in enumerate(top_scores):
                file.write(f"{entry_name}-{entry_score}")
                if index != len(top_scores) - 1:
                    file.write("\n")
    except OSError:
        pass


def load_highscores(path):
    _init_score_db(path)
    scores = []

    try:
        with sqlite3.connect(SCORE_DB_PATH) as conn:
            rows = conn.execute(
                "SELECT name, score FROM scores ORDER BY score DESC, name ASC LIMIT ?",
                (MAX_SCORES,),
            ).fetchall()
            scores = [(normalize_name(name), max(0, int(score))) for name, score in rows]
    except sqlite3.Error:
        scores = _read_legacy_highscores(path)

    scores.sort(key=lambda x: x[1], reverse=True)
    return scores[:MAX_SCORES]


def save_highscore(path, name, score):
    try:
        _init_score_db(path)
        clean_name = normalize_name(name)
        clean_score = max(0, int(score))

        with sqlite3.connect(SCORE_DB_PATH) as conn:
            current = conn.execute("SELECT score FROM scores WHERE name = ?", (clean_name,)).fetchone()
            if current is None:
                conn.execute("INSERT INTO scores(name, score) VALUES(?, ?)", (clean_name, clean_score))
            elif clean_score > int(current[0]):
                conn.execute("UPDATE scores SET score = ? WHERE name = ?", (clean_score, clean_name))
            conn.commit()
    except (OSError, sqlite3.Error):
        pass

    _sync_highscore_file(path, load_highscores(path))


def ensure_score_files(path):
    _sync_highscore_file(path, load_highscores(path))
