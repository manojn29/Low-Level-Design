from ConnectFour.Python.disc_color import DiscColor

class Player:
    def __init__(self, name: str, disc_color: DiscColor):
        self.name = name
        self.disc_color = disc_color