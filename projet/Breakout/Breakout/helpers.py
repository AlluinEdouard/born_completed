import pygame


def make_vertical_gradient(width, height, c1, c2):
    surface = pygame.Surface((width, height))
    span = max(1, height - 1)
    for y in range(height):
        ratio = y / span
        color = (
            int(c1[0] + (c2[0] - c1[0]) * ratio),
            int(c1[1] + (c2[1] - c1[1]) * ratio),
            int(c1[2] + (c2[2] - c1[2]) * ratio),
        )
        pygame.draw.line(surface, color, (0, y), (width, y))
    return surface


def circle_intersects_rect(cx, cy, radius, rect):
    nearest_x = max(rect.left, min(cx, rect.right))
    nearest_y = max(rect.top, min(cy, rect.bottom))
    dx = cx - nearest_x
    dy = cy - nearest_y
    return dx * dx + dy * dy <= radius * radius


def clamp(value, min_value, max_value):
    return max(min_value, min(value, max_value))


def lighten(color, amount):
    return (
        clamp(color[0] + amount, 0, 255),
        clamp(color[1] + amount, 0, 255),
        clamp(color[2] + amount, 0, 255),
    )
