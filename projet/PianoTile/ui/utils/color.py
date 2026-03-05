class Color:
    """La classe Color est une classe qui permet de recuperer la couleur voulue grâce aux getters."""

    def __init__(self) -> None:
        """Initialisation des couleurs."""
        self.__BLANC = (255, 255, 255)
        self.__VIOLET = (238, 169, 239)
        self.__ROSE = (245, 203, 246)
        self.__BLEU = (196, 253, 251)
        self.__NOIR = (0, 0, 0)
        self.__ORANGE = (232, 171, 115)
        self.__VERT = (179, 218, 129)
        self.__ROUGE = (228, 124, 119)
        self.__JAUNE = (251, 243, 137)
        self.__GRIS = (203, 203, 203)
        self.__BRONZE = (227, 184, 140)

    def getBlanc(self):
        return self.__BLANC

    def getBleu(self):
        return self.__BLEU

    def getViolet(self):
        return self.__VIOLET

    def getRose(self):
        return self.__ROSE

    def getNoir(self):
        return self.__NOIR

    def getOrange(self):
        return self.__ORANGE

    def getVert(self):
        return self.__VERT

    def getRouge(self):
        return self.__ROUGE

    def getJaune(self):
        return self.__JAUNE

    def getGris(self):
        return self.__GRIS
    
    def getBronze(self):
        return self.__BRONZE
