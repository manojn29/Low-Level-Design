from enum import Enum
from typing import Optional
from ConnectFour.Python.player import Player
from ConnectFour.Python.board import Board

class GameState(Enum):
    IN_PROGRESS = 'IN_PROGRESS'
    DRAW = 'DRAW'
    WON = 'WON'

class DiscColor(Enum):
    RED = 'R'
    YELLOW = 'Y'

class Game:
    def __init__(self, player1: Player, player2: Player, board: Board):
        self.player1 = player1
        self.player2 = player2
        self.current_player = player1
        self.board = board
        self.state = GameState.IN_PROGRESS
        self.winner = Optional[Player] = None
