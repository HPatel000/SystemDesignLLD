import java.util.*;

public class TicTacToe {
    public static void main(String[] args) {
        System.out.println("hello");
        Game game = new Game(3, List.of(
                new Player("John", new Piece(PieceType.X)),
                new Player("Jack", new Piece(PieceType.O))
            )
        );
        game.play();
    }
}

class Game {
    private final List<Player> players;
    private final Board board;

    Game(int size, List<Player> players) {
        this.players = players;
        board = new Board(size);
    }

    void play() {
        boolean isNotFinished = true;
        Queue<Player> playerQueue = new LinkedList<>(players);
        Scanner scanner = new Scanner(System.in);
        while(isNotFinished) {
            Player currPlayer = playerQueue.poll();
            if(currPlayer == null) continue;
            System.out.println(currPlayer.getPiece().getPieceType()+"'S Turn, Enter row and col: ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            if(board.addPiece(currPlayer.getPiece(), row, col)) {
                board.printBoard();
                if (board.checkWin(currPlayer.getPiece(), row, col)) {
                    System.out.println("Congratulations!! "+currPlayer.getName()+" WON THE GAME");
                    isNotFinished = false;
                }
                if(board.checkTie()) {
                    System.out.println("It's a draw!");
                    isNotFinished = false;
                }
            }
            playerQueue.add(currPlayer);
        }
        scanner.close();
    }

}

class Board {
    private final int size;
    private final PieceType[][] board;
    private int totalMoves = 0;

    Board(int size) {
        this.size = size;
        this.totalMoves = size*size;
        board = new PieceType[size][size];
    }

    boolean addPiece(Piece piece, int row, int col) {
        if(row < 0 || row >= size || col < 0 || col >= size) {
            System.out.println("Please Enter Valid row and column");
            return false;
        }
        if(board[row][col] != null) {
            System.out.println("Already occupied, please enter another row col");
            return false;
        }
        board[row][col] = piece.getPieceType();
        totalMoves--;
        return true;
    }

    boolean checkWin(Piece piece, int row, int col) {
        int rowCount = 0, colCount = 0, diaOneCount = 0, diaTwoCount = 0;
        for(int i=0; i<size; i++) {
            if(board[i][col] != null && board[i][col] == piece.getPieceType()) colCount++;
            if(board[row][i] != null && board[row][i] == piece.getPieceType()) rowCount++;
            if(row == col && board[i][i] != null && board[i][i] == piece.getPieceType()) diaOneCount++;
            if(row+col == size-1 && board[i][size-i-1] != null && board[i][size-i-1] == piece.getPieceType()) diaTwoCount++;
        }
        return rowCount == size || colCount == size || diaOneCount == size || diaTwoCount == size;
    }

    boolean checkTie() {
        return totalMoves == 0;
    }

    void printBoard() {
        for(PieceType[] row: board) {
            System.out.println(Arrays.toString(row));
        }
    }
}

class Player {
    private final String name;
    private final Piece piece;

    public String getName() {
        return name;
    }

    public Piece getPiece() {
        return piece;
    }

    Player(String name, Piece piece) {
        this.name = name;
        this.piece = piece;
    }
}

class Piece {
    private final PieceType pieceType;

    public PieceType getPieceType() {
        return pieceType;
    }

    Piece(PieceType pieceType) {
        this.pieceType = pieceType;
    }
}

enum PieceType {
    X, O;
}
