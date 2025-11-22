
import java.util.Random;



class Shared {       // TODO does this have to be named Shared and implemented as in the assignment spec???
    // singleton shared board instance

    private static Shared instance = null;

    private char board[];
    private int turn;

    private static final char PLAYER1_MOVE = 'X';
    private static final char PLAYER2_MOVE = 'O';
    private static final char EMPTY = '-';

    static {
        instance = new Shared();
    }

    private Shared() {
        /*
        Private constructor for singleton pattern. Initializes the board and turn.
        */
        if (instance != null) {
            throw new IllegalStateException("Use getInstance() to create Board instance");
        }

        turn = 0;
        board = new char[9];

        for (int i = 0; i < 9; i++) {
            board[i] = EMPTY;
        }
    }

    public static Shared getInstance() {
        /*
        Returns the singleton instance of the shared board.
        @return The singleton instance of the shared board.
        */

        if (instance == null) {
            instance = new Shared();
        }

        return instance;
    }

    public synchronized void printBoard(int turnPlayer) {
        /*
        Prints the current state of the board.
        @param turnPlayer The player whose turn it is (for the header)
        */

        // print header
        if (boardEmpty()) {
            System.out.println("Initial Board:");
        } 
        else if (turnPlayer == 1) {
            System.out.println("Player " + PLAYER1_MOVE + "'s Turn:");
        }
        else if (turnPlayer == 2) {
            System.out.println("Player " + PLAYER2_MOVE + "'s Turn:");
        }

        // print board
        System.out.println("-------------");
        System.out.println("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        System.out.println("-------------");
        System.out.println("| " + board[3] + " | " + board[4] + " | " + board[5] + " |");
        System.out.println("-------------"); 
        System.out.println("| " + board[6] + " | " + board[7] + " | " + board[8] + " |");
        System.out.println("-------------\n");
    }

    public synchronized void setTurn(int turn) {
        /*
        Sets the current turn to the given player ID.
        @param turn The value to set turn to
        */

        this.turn = turn;
    }

    public synchronized int getTurn() {
        /* 
        Gets the current turn.
        @return The value of turn
        */
        return turn;
    }

    public synchronized boolean boardEmpty () {
        /*
        Checks if the board is empty.
        @return true if the board is empty, false otherwise.
        */

        for (int i = 0; i < 9; i++) {
            if (board[i] != EMPTY) {
                return false;
            }
        }

        return true;
    }

    public synchronized int checkBoard() {
        /*
        Checks the board for a winner

        returns
        -1 if there is a draw
        0 if the game is still ongoing
        1 if the X player won
        2 if the O player won

        @return The game state as described above.
        */

        // Check rows and columns for a player 1 win
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i * 3 + 0] == PLAYER1_MOVE && board[i * 3 + 1] == PLAYER1_MOVE && board[i * 3 + 2] == PLAYER1_MOVE) {
                return 1;
            }

            // Check columns
            if (board[0 * 3 + i] == PLAYER1_MOVE && board[1 * 3 + i] == PLAYER1_MOVE && board[2 * 3 + i] == PLAYER1_MOVE) {                return 1;
            }
        }

        // Check rows and columns for a player 2 win
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i * 3 + 0] == PLAYER2_MOVE && board[i * 3 + 1] == PLAYER2_MOVE && board[i * 3 + 2] == PLAYER2_MOVE) {
                return 2;
            }

            // Check columns
            if (board[0 * 3 + i] == PLAYER2_MOVE && board[1 * 3 + i] == PLAYER2_MOVE && board[2 * 3 + i] == PLAYER2_MOVE) {
                return 2;
            }
        }

        // Check diagonals for a player 1 win
        if (board[0] == PLAYER1_MOVE && board[4] == PLAYER1_MOVE && board[8] == PLAYER1_MOVE) {
            return 1;
        }

        if (board[2] == PLAYER1_MOVE && board[4] == PLAYER1_MOVE && board[6] == PLAYER1_MOVE) {
            return 1;
        }

        // Check diagonals for a player 2 win
        if (board[0] == PLAYER2_MOVE && board[4] == PLAYER2_MOVE && board[8] == PLAYER2_MOVE) {
            return 2;
        }

        if (board[2] == PLAYER2_MOVE && board[4] == PLAYER2_MOVE && board[6] == PLAYER2_MOVE) {
            return 2;
        }

        // Check if board is full for a draw
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                return 0;   // Game is still ongoing
            }
        }

        // No empty spaces and no winner, so it's a draw
        return -1;
    }

    public synchronized int makeMove(int position, int playerID) {
        /*
        Makes a move on the board at the given position for the given player.
        @param position The position to make the move (0-9).
        @return 0 if the move was successful, 1 if the move was already taken, and -1 if the move is invalid.
        */

        // Validate inputs
        if (position < 0 || position > 9) {
            return -1;
        }

        if (playerID != 1 && playerID != 2) {
            return -1;
        }

        // Check if position is already occupied
        if (board[position] != EMPTY) {
            return 1;
        }

        // Make the move
        if (turn == 1) {
            board[position] = PLAYER1_MOVE;
        } 
        else if (turn == 2) {
            board[position] = PLAYER2_MOVE;
        } 

        return 0;
    }
}

class Player extends Thread {

    private int ID;
    private Shared sharedBoard;
    private final int NUM_MOVES = 9;

    public Player(int id, Shared sharedBoard) {
        this.ID = id;
        this.sharedBoard = sharedBoard;
    }

    private void decideMove() {
        /*
        Pick a random move and submit it to the shared board
        */

        int randMove = 0;
        Random randomSequence = new Random();
        boolean moveSuccessful = false;

        // Repeat until a valid move is made
        do {

            randMove = randomSequence.nextInt(NUM_MOVES); 
            if (sharedBoard.makeMove(randMove, ID) == 0)  moveSuccessful = true;
        } while (!moveSuccessful);
    }

    @Override
    public void run() {
        /*
        Main player thread loop
        */

        while (true) {

            // Wait while it's not this thread's turn and not interrupted
            while (sharedBoard.getTurn() != ID && !Thread.currentThread().isInterrupted()) {}
            
            // If game ending, break
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            // Make a move
            decideMove();

            // Set turn back to main thread
            sharedBoard.setTurn(0); 
        }
    }
}

class TicTacToe {



    public static void main(String args[]) {

        Shared shared = Shared.getInstance();
        Player player1 = new Player(1, shared);
        Player player2 = new Player(2, shared);

        int gameState;
        int turnPlayer = 1;
        boolean gameOver = false;
        
        // initialize game
        shared.setTurn(0); 
        player1.start();
        player2.start();

        // game loop
        while (!gameOver) { 

            // busy wait for the thread's turn
            while (shared.getTurn() != 0) {}

            shared.printBoard(3 - turnPlayer);

            // Check if game is over
            gameState = shared.checkBoard();
            if (gameState != 0) {
                if (gameState > 0)  System.out.println("WINNER: Player " + gameState + " wins!  :D\n");
                else                System.out.println("DRAW!\n");

                player1.interrupt();
                player2.interrupt();
                gameOver = true;
            }

            // If game is ongoing, set the turn to the next player
            else {
                shared.setTurn(turnPlayer);

                if (turnPlayer == 1)  turnPlayer = 2;
                else                  turnPlayer = 1;
            }
        }
    }
}