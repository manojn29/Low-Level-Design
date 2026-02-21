public class Game {
    Board board = new Board();
    Player player1;
    Player player2;
    GameState state;
    Player currentPlayer;
    Player winner;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.state = GameState.INPROGRESS;
        this.winner = null;
    }

    public boolean makeMove(Player player, int col){
        if(this.state != GameState.INPROGRESS) {
            return false;
        }
        if(player != currentPlayer) {
            return false;
        }
        int row = board.placeDisc(col, player.getColor());
        if (row == -1) {
            return false;
        }
        if (board.checkWinner(row, col, player.getColor())) {
            this.state = GameState.WON;
            this.winner = currentPlayer;
        }
        else if (board.isFull()) {
            this.state = GameState.DRAW;
        }
        return true;   
    }

    public Player getWinner() {
        return this.winner;
    }

    public GameState getState() {
        return this.state;
    }
}

enum GameState {
    DRAW,
    INPROGRESS,
    OVER
}

enum Disc {
    RED,
    YELLOW
}

class Player {
    private String name;
    private Disc disc;
    
    public Player(String name, Disc disc) {
        this.name = name;
        this.disc = disc;
    }

    public String getName() {
        return this.name;
    }

    public Disc getColor() {
        return this.disc;
    }
}

class Board {
    private Disc[][] board;

    public Board() {
        board = new Disc[6][7];
    }

    private boolean canPlace(int col) {
        for (int i = 0; i < 6; i++) {
            if (board[i][col] == null) {
                return true;
            }
        }
        return false;
    }

    public int placeDisc(int col, Disc discColor) {
        if (!canPlace(col)) {
            return -1;
        }

        for (int i = 5; i >= 0; i--) {
            if (board[i][col] == null) {
                board[i][col] = discColor;
                return i;
            }
        }
        return -1;
    }

    public boolean checkWinner(int row, int col, Disc disc){
        int[][] directions = new int[][] {
            {0, 1},
            {1, 0},
            {1, 1},
            {1, -1}
        };
        int count;

        for (int[] dir: directions) {
            count = 1;
            count += countInDirection(row, col, dir[0], dir[1], disc);
            count += countInDirection(row, col, -dir[0], -dir[1], disc);
            if (count >= 4) {
                return true;
            }
        }
        return false;
    }

    private Disc getCell(int row, int col) {
        return this.board[row][col];
    }

    private int countInDirection(int row, int col, int dr, int dc, Disc disc) {
        row = row + dr;
        col = col + dc;
        int count = 0;

        while (row >= 0 && row < 6 && col >= 0 && col < 7 && getCell(row, col) == disc) {
            count += 1;
            row += dr;
            col += dr;
        }
        return count;
    }

    public Disc[][] getBoard() {
        return this.board;
    }

}