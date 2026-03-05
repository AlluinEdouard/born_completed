import pygame

class Image: 
    """La classe Image est une classe qui permet de recuperer l'image voulue grâce au getter."""
    class Page:
        """La classe Page est une classe qui permet de recuperer l'image voulue grâce au getter."""
        PROFIL = pygame.image.load("./assets/img/page/ACCUEIL.png")
        FILTRER = pygame.image.load("./assets/img/page/ACCUEIL.png")
        AIDE = pygame.image.load("./assets/img/page/ACCUEIL.png")
        DETAIL = pygame.image.load("./assets/img/page/ACCUEIL.png")
        PLAY = pygame.image.load("./assets/img/page/ACCUEIL.png")
        ACCUEIL = pygame.image.load("./assets/img/page/ACCUEIL.png")
        MULTIJOUEUR = pygame.image.load("./assets/img/page/ACCUEIL.png")
        STATISTIQUE = pygame.image.load("./assets/img/page/ACCUEIL.png")
        QUITTER = pygame.image.load("./assets/img/page/ACCUEIL.png")
        
    class Cover:
        """La classe Cover est une classe qui permet de recuperer la cover d'un album d'une musique."""
        BLINDINGLIGHTS = pygame.image.load("./assets/img/music/blindinglights.png")
        SUNFLOWER = pygame.image.load("./assets/img/music/sunflower.png")
        SWEATERWEATHER = pygame.image.load("./assets/img/music/sweaterweather.png")
        BELIEVER = pygame.image.load("./assets/img/music/believer.png")