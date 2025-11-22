
import java.util.Random;



class Board {       // TODO does this have to be named Shared and implemented as in the assignment spec???
    // singleton shared board instance

    private static Board instance = null;

    private char board[];
    private int turn;

    private static final char PLAYER1_MOVE = 'X';
    private static final char PLAYER2_MOVE = 'O';
    private static final char EMPTY = '-';

    private Board() {
        if (instance != null) {
            throw new IllegalStateException("Use getInstance() to create Board instance");
        }

        turn = 0;
        board = new char[9];

        for (int i = 0; i < 9; i++) {
            board[i] = EMPTY;
        }
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }

    public void printBoard() {
        System.out.println("TODO HEADER HERE");
        System.out.println("-------------");
        System.out.println("| " + board[0] + " | " + board[1] + " | " + board[2] + " |");
        System.out.println("-------------");
        System.out.println("| " + board[3] + " | " + board[4] + " | " + board[5] + " |");
        System.out.println("-------------"); 
        System.out.println("| " + board[6] + " | " + board[7] + " | " + board[8] + " |");
        System.out.println("-------------");
    }

    public synchronized void setTurn(int turn) {
        this.turn = turn;
    }

    public synchronized int getTurn() {
        return turn;
    }

    public int checkBoard() {
        /*
        Checks the board for a winner

        returns
        -1 if there is a draw
        0 if the game is still ongoing
        1 if the X player won
        2 if the O player won
        */
    }

    public synchronized int makeMove(int position) {
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
    private Board sharedBoard;
    private final int NUM_MOVES = 9;

    public Player(int id, Board sharedBoard) {
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

            randMove = randomSequence.nextInt(NUM_MOVES + 1); 
            if (sharedBoard.makeMove(randMove) == 0)  moveSuccessful = true;
        } while (!moveSuccessful);
    }

    @Override
    public void run() {
        boolean interrupted = false;


        while (sharedBoard.getTurn() != ID && !interrupted) {
            if (interrupted) {
                // TODO If game ending, break
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

        Board board = Board.getInstance();
        Player player1 = new Player(1);
        Player player2 = new Player(2);
        
        board.setTurn(0); 
        player1.start();
        player2.start();

        // game loop
        while (true) { 

            // wait for the thread's turn
            while (board.getTurn() != 0) {
                // TODO 
            }

            board.printBoard();      

            // TODO Check winner

        }

    }
}