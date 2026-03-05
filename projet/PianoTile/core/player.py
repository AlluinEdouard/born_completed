class Player:
    def __init__(self, player_id, name, password):
        self.__id = player_id
        self.__name = name
        self.__password = password

    def getName(self):
        return self.__name