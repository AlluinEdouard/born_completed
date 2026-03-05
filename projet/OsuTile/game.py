import pygame
import time
import os
import importlib.util
from config import (
    SCREEN_WIDTH,
    SCREEN_HEIGHT,
    BACKGROUND_COLOR,
    TILE_COLOR,
    HIT_LINE_Y,
    FPS,
    LANE_COUNT,
    ASSETS_FOLDER,
    KEY_MAPPING,
    PAUSE_KEY,
    HIT_BOX_PIXEL,
    TILE_HEIGHT,
    MENU_RESUME_KEY,
    MENU_QUIT_KEY,
    MENU_BACK_TO_MENU_KEY,
    MENU_RETRY_KEY,
    FULLSCREEN,
)
from tile import Tile


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


def load_beatmap(filename):
    map_name = os.path.splitext(filename)[0]
    map_path = os.path.join("maps", f"{map_name}.py")
    spec = importlib.util.spec_from_file_location("beatmap_module", map_path)
    beatmap_module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(beatmap_module)
    return beatmap_module.beatmap


def draw_pause_menu(screen, font):
    screen.fill(BACKGROUND_COLOR)
    text = font.render("Pause", True, (255, 255, 255))
    resume = font.render("Entrée : reprendre", True, (200, 200, 200))
    quit_ = font.render("Q : quitter", True, (200, 200, 200))
    screen.blit(text, (SCREEN_WIDTH // 2 - text.get_width() // 2, 300))
    screen.blit(resume, (SCREEN_WIDTH // 2 - resume.get_width() // 2, 400))
    screen.blit(quit_, (SCREEN_WIDTH // 2 - quit_.get_width() // 2, 450))
    pygame.display.flip()


def draw_scene(screen, font, tiles, current_time, score, combo, feedbacks):
    screen.fill(BACKGROUND_COLOR)
    pygame.draw.line(
        screen, (255, 0, 0), (0, HIT_LINE_Y), (SCREEN_WIDTH, HIT_LINE_Y), 3
    )
    for tile in tiles:
        if tile.hit:
            continue
        y = tile.get_y(current_time)
        if y > SCREEN_HEIGHT - 60:
            continue
        x = tile.lane * (SCREEN_WIDTH // LANE_COUNT)
        tile.draw(screen, x, SCREEN_WIDTH // LANE_COUNT, current_time, TILE_COLOR)
    # Affichage score et combo
    screen.blit(
        font.render(f"Score : {score}", True, (255, 255, 255)), (10, SCREEN_HEIGHT - 80)
    )
    screen.blit(
        font.render(f"Combo : {combo}", True, (200, 200, 200)), (10, SCREEN_HEIGHT - 40)
    )
    for fb in feedbacks[:]:
        text, t0, lane = fb
        if current_time - t0 < 300:
            color = (255, 255, 255) if text == "Perfect" else (255, 50, 50)
            surf = font.render(text, True, color)
            x = lane * (SCREEN_WIDTH // LANE_COUNT) + 20
            screen.blit(surf, (x, HIT_LINE_Y - 40))
        else:
            feedbacks.remove(fb)
    pygame.display.flip()


def countdown(screen, font, tiles, current_time, score, combo, feedbacks):
    for i in range(3, 0, -1):
        draw_scene(screen, font, tiles, current_time, score, combo, feedbacks)
        txt = font.render(str(i), True, (255, 255, 255))
        screen.blit(txt, (SCREEN_WIDTH // 2 - txt.get_width() // 2, SCREEN_HEIGHT // 2))
        pygame.display.flip()
        time.sleep(1)


def end_screen(screen, font, score, total_notes, max_combo):
    screen.fill(BACKGROUND_COLOR)
    title = font.render("Fin de la partie", True, (255, 255, 255))
    score_txt = font.render(f"Score final : {score}", True, (255, 255, 255))
    percent = (score / total_notes) * 100 if total_notes > 0 else 0
    percent_txt = font.render(f"Précision : {percent:.1f}%", True, (0, 255, 255))
    combo_txt = font.render(f"Combo max : {max_combo}", True, (255, 255, 0))
    retry = font.render("Entrée : Rejouer", True, (200, 200, 200))
    menu = font.render("M : Menu", True, (200, 200, 200))
    quit_ = font.render("Q : Quitter", True, (200, 200, 200))
    screen.blit(title, (SCREEN_WIDTH // 2 - title.get_width() // 2, 180))
    screen.blit(score_txt, (SCREEN_WIDTH // 2 - score_txt.get_width() // 2, 250))
    screen.blit(percent_txt, (SCREEN_WIDTH // 2 - percent_txt.get_width() // 2, 300))
    screen.blit(combo_txt, (SCREEN_WIDTH // 2 - combo_txt.get_width() // 2, 350))
    screen.blit(retry, (SCREEN_WIDTH // 2 - retry.get_width() // 2, 420))
    screen.blit(menu, (SCREEN_WIDTH // 2 - menu.get_width() // 2, 470))
    screen.blit(quit_, (SCREEN_WIDTH // 2 - quit_.get_width() // 2, 520))
    pygame.display.flip()

    _save_highscore("highscore", "PLY", score)

    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return "quit"
            elif event.type == pygame.KEYDOWN:
                if event.key == MENU_RETRY_KEY:
                    return "retry"
                elif event.key == MENU_BACK_TO_MENU_KEY:
                    return "menu"
                elif event.key == MENU_QUIT_KEY:
                    return "quit"


def play_map(filename):
    beatmap = load_beatmap(filename)
    tiles = [Tile(lane, time) for lane, time in beatmap]
    audio_file = os.path.join(ASSETS_FOLDER, os.path.splitext(filename)[0] + ".mp3")

    flags = pygame.HWSURFACE | pygame.DOUBLEBUF
    if FULLSCREEN :
        flags |= pygame.FULLSCREEN
    screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT), flags)
    clock = pygame.time.Clock()
    font = pygame.font.SysFont(None, 48)

    pygame.mixer.init()
    if os.path.exists(audio_file):
        pygame.mixer.music.load(audio_file)
        pygame.mixer.music.play()
    else:
        print(f" Audio manquant : {audio_file}")

    start_time = time.time()
    paused = False
    score, combo = 0, 0
    max_combo = 0
    feedbacks = []
    running = True

    total_notes = len(tiles)

    while running:
        current_time = (time.time() - start_time) * 1000 if not paused else current_time
        screen.fill(BACKGROUND_COLOR)

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                return "quit"
            elif event.type == pygame.KEYDOWN:
                if event.key == PAUSE_KEY:
                    paused = True
                    pygame.mixer.music.pause()
                    draw_pause_menu(screen, font)
                    while paused:
                        for pe in pygame.event.get():
                            if pe.type == pygame.QUIT:
                                return "quit"
                            elif pe.type == pygame.KEYDOWN:
                                if pe.key == MENU_RESUME_KEY:
                                    countdown(
                                        screen,
                                        font,
                                        tiles,
                                        current_time,
                                        score,
                                        combo,
                                        feedbacks,
                                    )
                                    start_time = time.time() - (current_time / 1000)
                                    pygame.mixer.music.unpause()
                                    paused = False
                                elif pe.key == MENU_QUIT_KEY:
                                    return "menu"
                else:
                    for lane, key in KEY_MAPPING.items():
                        if key is not None and event.key == key:
                            for tile in tiles:
                                if tile.hit:
                                    continue
                                tile_y = tile.get_y(current_time)
                                top = tile_y
                                bottom = tile_y + TILE_HEIGHT
                                if tile.lane == lane and (
                                    top <= HIT_LINE_Y + HIT_BOX_PIXEL
                                    and bottom >= HIT_LINE_Y - HIT_BOX_PIXEL
                                ):
                                    tile.hit = True
                                    score += 1
                                    combo += 1
                                    if combo > max_combo:
                                        max_combo = combo
                                    feedbacks.append(("Perfect", current_time, lane))
                                    break
                            else:
                                combo = 0
                                feedbacks.append(("Miss", current_time, lane))

        # Combo reset si une tuile sort de l'écran
        for tile in tiles:
            if not tile.hit and tile.get_y(current_time) > SCREEN_HEIGHT:
                tile.hit = True
                combo = 0
                feedbacks.append(("Miss", current_time, tile.lane))

        draw_scene(screen, font, tiles, current_time, score, combo, feedbacks)

        if all(tile.hit for tile in tiles):
            pygame.mixer.music.stop()
            result = end_screen(screen, font, score, total_notes, max_combo)
            return result

        clock.tick(FPS)

    pygame.quit()
