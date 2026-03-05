#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Module pour la classe Direction du jeu Tron
Définit les directions possibles pour les joueurs
"""

from enum import Enum

class Direction(Enum):
    UP = (0, -1)
    DOWN = (0, 1)
    LEFT = (-1, 0)
    RIGHT = (1, 0)
