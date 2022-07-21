import java.util.*;

public class Othello {
    private int[][] board;
    private int numTurns;
    private boolean player1;
    private int pieces1;
    private int pieces2;
    private Map<Integer, Set<Integer>> possibleMoves;
    private boolean gameOver;

    /**
     * Constructor sets up game state.
     */
    public Othello() {
        reset();
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public Object[] playTurn(int r, int c) { 
        if (!possibleMoves.containsKey(10 * r + c) || gameOver) {
            return new Object[]{false};
        }
        Set<Integer> flipped = possibleMoves.get(10 * r + c);
        for (Integer i : flipped) {
            int rVal = i / 10;
            int cVal = i % 10;
            if (player1) {
                board[rVal][cVal] = 1;
            } else {
                board[rVal][cVal] = 2;
            }
        }

        if (player1) {
            board[r][c] = 1;
            pieces1 += flipped.size() + 1;
            pieces2 -= flipped.size();
        } else {
            board[r][c] = 2;
            pieces2 += flipped.size() + 1;
            pieces1 -= flipped.size();
        }

        numTurns++;
        player1 = !player1;
        getMoves();
        if (possibleMoves.isEmpty()) { //If one player can't make a move, go to the other and see if they can.
            player1 = !player1;
            getMoves();
            if (possibleMoves.isEmpty()) {
                gameOver = true;
            }
        }
        if (player1 || gameOver) {
            return new Object[]{true, null};
        } else {
            int[] newMove = minimax(board, possibleMoves, player1, pieces2 - pieces1, 10 * r + c, 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
            System.out.println((int) newMove[1] / 10 + "" + (int) newMove[1] % 10);
            playTurn((int) newMove[1] / 10, (int) newMove[1] % 10);
            return new Object[]{true, newMove[1]};
        } 
    }

    public int[] minimax(int[][] boardCopy, Map<Integer, Set<Integer>> possibleMovesCopy, boolean player1Copy, int diff, int currMove, int depth, int alpha, int beta) {
        if (depth == 0) {
            return new int[]{diff, currMove};
        }
        if (possibleMovesCopy.size() == 0) {
            if (diff > 0) {
                return new int[]{Integer.MAX_VALUE - 1, currMove};
            } else {
                return new int[]{Integer.MIN_VALUE + 1, currMove};
            }
        }

        if (!player1Copy) {
            int maxEval = Integer.MIN_VALUE;
            int bestMove = -2;
            for (int move: possibleMovesCopy.keySet()) {
                int[][] boardCopy2 = new int[8][8];
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        boardCopy2[i][j] = boardCopy[i][j];
                    }
                }
    
                for (int flipped: possibleMovesCopy.get(move)) {
                    int rVal = flipped / 10;
                    int cVal = flipped % 10;
                    boardCopy2[rVal][cVal] = 2;
                }
    
                Map<Integer, Set<Integer>> possibleMovesCopy2 = getMovesForMinimax(boardCopy2, player1Copy);
                int[] res = minimax(boardCopy2, possibleMovesCopy2, !player1Copy, diff + 2 * possibleMovesCopy.get(move).size() + 1, move, depth - 1, alpha, beta);
                if (res[0] > maxEval) {
                    maxEval = res[0];
                    bestMove = move;
                }
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha) {
                    break;
                }
            }

            return new int[]{maxEval, bestMove};

        } else {
            int minEval = Integer.MAX_VALUE;
            int bestMove = -2;
            for (int move: possibleMovesCopy.keySet()) {
                int[][] boardCopy2 = boardCopy.clone();

                for (int flipped: possibleMovesCopy.get(move)) {
                    int rVal = flipped / 10;
                    int cVal = flipped % 10;
                    boardCopy2[rVal][cVal] = 1;
                }

                Map<Integer, Set<Integer>> possibleMovesCopy2 = getMovesForMinimax(boardCopy2, player1Copy);
                int[] res = minimax(boardCopy2, possibleMovesCopy2, !player1Copy, diff - 2 * possibleMovesCopy.get(move).size() - 1, move, depth - 1, alpha, beta);
                if (res[0] < minEval) {
                    minEval = res[0];
                    bestMove = move;
                }
                beta = Math.min(beta, minEval);
                if (beta <= alpha) {
                    break;
                }
            }

            return new int[]{minEval, bestMove};
        }
    }

    public Map<Integer, Set<Integer>> getMovesForMinimax(int[][] theBoard, boolean currPlayer) {
        Map<Integer, Set<Integer>> possibleMovesCopy2 = new TreeMap<>();
        for (int i = 0; i < theBoard.length; i++) {
            for (int j = 0; j < theBoard[0].length; j++) {
                if (theBoard[i][j] > 0) {
                    continue;
                }

                Set<Integer> piecesThatChange = new TreeSet<>();
                int otherSide;
                if (currPlayer) {
                    otherSide = 1;
                } else {
                    otherSide = 2;
                }

                int newi;
                int newj;
                Set<Integer> oneD;
                if (i > 0 && theBoard[i - 1][j] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i - 1;
                    while (newi >= 0 && theBoard[newi][j] == otherSide) {
                        oneD.add(10 * newi + j);
                        newi -= 1;
                    }

                    if (newi >= 0 && theBoard[newi][j] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i < theBoard.length - 1 && theBoard[i + 1][j] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i + 1;
                    while (newi <= theBoard.length - 1 && theBoard[newi][j] == otherSide) {
                        oneD.add(10 * newi + j);
                        newi += 1;
                    }

                    if (newi <= theBoard.length - 1 && theBoard[newi][j] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (j > 0 && theBoard[i][j - 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newj = j - 1;
                    while (newj >= 0 && theBoard[i][newj] == otherSide) {
                        oneD.add(10 * i + newj);
                        newj -= 1;
                    }

                    if (newj >= 0 && theBoard[i][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (j < theBoard[0].length - 1 && theBoard[i][j + 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newj = j + 1;
                    while (newj <= theBoard[0].length - 1 && theBoard[i][newj] == otherSide) {
                        oneD.add(10 * i + newj);
                        newj += 1;
                    }

                    if (newj <= theBoard.length - 1 && theBoard[i][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }

                if (i > 0 && j > 0 && theBoard[i - 1][j - 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i - 1;
                    newj = j - 1;
                    while (newi >= 0 && newj >= 0 && theBoard[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi -= 1;
                        newj -= 1;
                    }

                    if (newi >= 0 && newj >= 0 && theBoard[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i > 0 && j < theBoard[0].length - 1 && theBoard[i - 1][j + 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i - 1;
                    newj = j + 1;
                    while (newi >= 0 && newj <= theBoard[0].length - 1
                            && theBoard[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi -= 1;
                        newj += 1;
                    }

                    if (newi >= 0 && newj <= theBoard[0].length - 1
                            && theBoard[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i < theBoard.length - 1 && j > 0 && theBoard[i + 1][j - 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i + 1;
                    newj = j - 1;
                    while (newi <= theBoard.length - 1 && newj >= 0
                            && theBoard[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi += 1;
                        newj -= 1;
                    }

                    if (newi <= theBoard.length - 1 && newj >= 0
                            && theBoard[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i < theBoard.length - 1 && j < theBoard[0].length - 1
                        && theBoard[i + 1][j + 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i + 1;
                    newj = j + 1;
                    while (newi <= theBoard.length - 1 && newj <= theBoard[0].length - 1
                            && theBoard[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi += 1;
                        newj += 1;
                    }

                    if (newi <= theBoard.length - 1 && newj <= theBoard[0].length - 1
                            && theBoard[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (!piecesThatChange.isEmpty()) {
                    possibleMovesCopy2.put(10 * i + j, piecesThatChange);
                }
            }
        
        }
        return possibleMovesCopy2;
    }

    /**
     * getMoves() is called after every turn to update possibleMoves with the
     * location of every possible legal move
     * for the next turn. For each legal move, possibleMoves also stores what pieces
     * would need to be flipped.
     */
    public void getMoves() {
        possibleMoves = new TreeMap<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] > 0) {
                    continue;
                }

                Set<Integer> piecesThatChange = new TreeSet<>();
                int otherSide;
                if (player1) {
                    otherSide = 2;
                } else {
                    otherSide = 1;
                }

                int newi;
                int newj;
                Set<Integer> oneD;
                if (i > 0 && board[i - 1][j] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i - 1;
                    while (newi >= 0 && board[newi][j] == otherSide) {
                        oneD.add(10 * newi + j);
                        newi -= 1;
                    }

                    if (newi >= 0 && board[newi][j] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i < board.length - 1 && board[i + 1][j] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i + 1;
                    while (newi <= board.length - 1 && board[newi][j] == otherSide) {
                        oneD.add(10 * newi + j);
                        newi += 1;
                    }

                    if (newi <= board.length - 1 && board[newi][j] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (j > 0 && board[i][j - 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newj = j - 1;
                    while (newj >= 0 && board[i][newj] == otherSide) {
                        oneD.add(10 * i + newj);
                        newj -= 1;
                    }

                    if (newj >= 0 && board[i][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (j < board[0].length - 1 && board[i][j + 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newj = j + 1;
                    while (newj <= board[0].length - 1 && board[i][newj] == otherSide) {
                        oneD.add(10 * i + newj);
                        newj += 1;
                    }

                    if (newj <= board.length - 1 && board[i][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }

                if (i > 0 && j > 0 && board[i - 1][j - 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i - 1;
                    newj = j - 1;
                    while (newi >= 0 && newj >= 0 && board[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi -= 1;
                        newj -= 1;
                    }

                    if (newi >= 0 && newj >= 0 && board[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i > 0 && j < board[0].length - 1 && board[i - 1][j + 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i - 1;
                    newj = j + 1;
                    while (newi >= 0 && newj <= board[0].length - 1
                            && board[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi -= 1;
                        newj += 1;
                    }

                    if (newi >= 0 && newj <= board[0].length - 1
                            && board[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i < board.length - 1 && j > 0 && board[i + 1][j - 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i + 1;
                    newj = j - 1;
                    while (newi <= board.length - 1 && newj >= 0
                            && board[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi += 1;
                        newj -= 1;
                    }

                    if (newi <= board.length - 1 && newj >= 0
                            && board[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (i < board.length - 1 && j < board[0].length - 1
                        && board[i + 1][j + 1] == otherSide) {
                    oneD = new TreeSet<>();
                    newi = i + 1;
                    newj = j + 1;
                    while (newi <= board.length - 1 && newj <= board[0].length - 1
                            && board[newi][newj] == otherSide) {
                        oneD.add(10 * newi + newj);
                        newi += 1;
                        newj += 1;
                    }

                    if (newi <= board.length - 1 && newj <= board[0].length - 1
                            && board[newi][newj] == (3 - otherSide)) {
                        piecesThatChange.addAll(oneD);
                    }
                }
                if (!piecesThatChange.isEmpty()) {
                    possibleMoves.put(10 * i + j, piecesThatChange);
                }
            }
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {
        if (!gameOver) {
            return 0;
        }
        if (pieces1 > pieces2) {
            return 1;
        }
        if (pieces2 > pieces1) {
            return 2;
        }

        return 3;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        System.out.println("Black (1): " + pieces1 + " ——— White (2): " + pieces2);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 7) {
                    System.out.print(" | ");
                }
            }
            if (i < 7) {
                System.out.println("\n------------------------------");
            }
        }
        System.out.println();
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        board = new int[8][8];
        board[3][4] = 1;
        board[4][3] = 1;
        board[3][3] = 2;
        board[4][4] = 2;
        numTurns = 0;
        pieces1 = 2;
        pieces2 = 2;
        player1 = true;
        gameOver = false;
        getMoves();
    }

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     *
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int r, int c) {
        return board[r][c];
    }

    public int getBlackPieces() {
        return pieces1;
    }

    public int getWhitePieces() {
        return pieces2;
    }

    public static void main(String[] args) {
    }
}