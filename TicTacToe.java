

class Board {       // TODO does this have to be named Shared and implemented as in the assignment spec???
    // singleton shared board instance

    private static Board instance = null;

    private char board[];
    private int turn;

    private static final char X = 'X';
    private static final char O = 'O';
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
}

class Player extends Thread {

    private int ID;

    public Player(int id) {
        this.ID = id;
        // TODO
    }

    @Override
    public void run() {
        // TODO
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