def parse_osu_file(filepath, lane_count=4):
    notes = []
    with open(filepath, "r", encoding="utf-8", errors="ignore") as f:
        lines = f.readlines()

    in_hit_objects = False
    for line in lines:
        line = line.strip()
        if line == "[HitObjects]":
            in_hit_objects = True
            continue
        if in_hit_objects:
            if not line or line.startswith("["):
                break
            parts = line.split(",")
            if len(parts) >= 3:
                x = int(parts[0])
                time = int(parts[2])
                lane = x * lane_count // 512
                notes.append({"time": time, "lane": lane})
    return notes
