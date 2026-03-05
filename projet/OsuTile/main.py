import os
import subprocess
from menu import run_menu


def ensure_maps_exported():
    osu_folder = "beatmaps"
    map_folder = "maps"

    os.makedirs(map_folder, exist_ok=True)
    for filename in os.listdir(osu_folder):
        if filename.endswith(".osu"):
            map_name = os.path.splitext(filename)[0]
            osu_path = os.path.join(osu_folder, filename)
            py_path = os.path.join(map_folder, f"{map_name}.py")

            if not os.path.exists(py_path):
                print(f"Génération de {py_path}")
                subprocess.run(["python3", "tools/export_map.py", osu_path, py_path])


if __name__ == "__main__":
    ensure_maps_exported()
    run_menu()
