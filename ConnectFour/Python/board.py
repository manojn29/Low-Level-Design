from ConnectFour.Python.game import DiscColor

class Board:
    def __init__(self, rows: int = 6, cols = 7):
        self.rows = rows
        self.cols = cols
        self.grid = [[DiscColor for _ in range(cols)] for _ in range(rows)]