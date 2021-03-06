//THINGS TO TRY: Erase eligibility traces after each game
import java.util.Arrays;
import java.util.ArrayList;

public class NNBot extends TetrisBot{
    TDNetwork myNN;
    final double GAMMA = .9;
    final double LAMBDA = .5;
    double ALPHA = .08333;
    double BETA = .33333;
    final double BIAS = 1;
    double ETA = 0.1;
    int pieceNodes = 7;
    int inputNodes = 23;
    int hiddenNodes = 2;
    int outputNodes = 1;

    public NNBot(){
        System.out.println("INITIALIZING NEURAL NETWORK BAD!");
        //int _n, int _num_hidden, int _m,  double _BIAS, double _ALPHA, double _BETA, double _GAMMA, double _LAMBDA
        myNN = new TDNetwork(inputNodes, 2, 1, BIAS, ALPHA, BETA, GAMMA, LAMBDA);
    }
    
    public void setNetwork(int BOARD_WIDTH, int BOARD_HEIGHT) {
        inputNodes = BOARD_WIDTH * BOARD_HEIGHT + pieceNodes;
        hiddenNodes = (int)((2.0/3.0)* inputNodes);
        ALPHA = 1.0/inputNodes;
        BETA = 1.0/hiddenNodes;
        myNN = new TDNetwork(inputNodes, hiddenNodes, outputNodes, BIAS, ALPHA, BETA, GAMMA, LAMBDA);
    }

    /**
     * Returns a deepcopy of a Tetris board object
     * @param board the original Tetris board
     * @return the new Tetris board object
     */
    //Return a deepcopy of a TetrisBoard object
    public static TetrisBoard deepCopy(TetrisBoard tBoard) {
        int[][] original = tBoard.board;
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }

        TetrisBoard newTBoard =  new TetrisBoard(tBoard.width, tBoard.height, false);
        newTBoard.board = result;
        return newTBoard;
    }

    /**
     * Returns a Tetris move to be made in the Tetris game. This program
     * selects a random move to train the neural network on with percent
     * ETA (where ETA decreases over time).
     * @param board the current board state
     * @param current_piece the current piece to be played on the board
     * @return next_piece the next piece to be played on the board after current piece
     */
    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece){
        TetrisBoard currentBoard;
        ArrayList<TetrisMove> moves = getLegalMoves(board, current_piece); 
        //System.out.println(ETA);
        double output;    
        double best = Double.MIN_VALUE;
        TetrisMove bestMove = new TetrisMove(current_piece, 0);
        double[] input;
        //System.out.println(board);
        if (Math.random() > ETA) {
            for(TetrisMove move : moves){
                currentBoard = deepCopy(board);
                currentBoard.addPiece(move);
                //Pick the move with the highest outputx[t][n]=BIAS; with chance ETA
                //There will only be one output
                input = getInput(currentBoard, next_piece);
                output = myNN.feedForward(input)[0];
                if (output > best) {
                    best = output;
                    bestMove = move;
                }
                //backprop(target, output);
            }            
            learn(board, bestMove, next_piece);
            return bestMove;
        }
        else {
            ETA = ETA * .999999;
            //System.out.println(ETA);
            if (moves.size()>0) {
                bestMove = moves.get((int)(Math.random() * (moves.size()-1)));
                learn(board, bestMove, next_piece);
                return bestMove;
            }
            
            //Every move will lose the game, so choose the first move
            learn(board, bestMove, next_piece);
            return bestMove;
        }
    }
    
    /**
     * Returns the input to the neural netowork as a double array with
     * each position of the board as either a 0 (not filled) or a 1 (filled)
     * and 1 of 7 inputs as a 1 representing the next piece 
     * @param board a copy of the Tetris board with a piece placed on it
     * @param next_piece the next piece that will be placed on the board
     * @return the input double array for the neural network
     */
    public double[] getInput(TetrisBoard board, TetrisPiece next_piece) {
        double[] input = new double[inputNodes];
        for (int r = 0; r < board.height; r++) {
            for (int c = 0; c < board.width; c++) {
                if (board.board[r][c] != 0) {
                    input[c + r * board.width] = 1;
                }
            }
        }
        //DOES THE CONTOUR CHANGE???
        board.eliminateRows();
        int piece = TetrisPiece.whatPiece(next_piece);
        input[inputNodes - pieceNodes + piece] = 1;
        if (piece == -1) {
            System.out.println("IM MAD");
        }
        return input;
    }
    
    /**
     * Feeds the current board through the network and 
     * runs the backpropagation algorithm for the network
     * @param board the original Tetris board
     * @param bestMove the bestMove that will be placed on the board
     */
    public void learn(TetrisBoard board, TetrisMove bestMove, TetrisPiece next_piece) {
        TetrisBoard currentBoard = deepCopy(board);
        currentBoard.addPiece(bestMove);
        double[] input = getInput(currentBoard, next_piece);
        myNN.timeStep(input, getReward(currentBoard, next_piece));
    }

    /**
     * Gets all the legal moves given a board and a piece to place.
     * @param board A deep copy of the current board
     * @param current_piece The current piece we are trying to place
     * @return legalMoves All the legal moves available
     */
    public static  ArrayList<TetrisMove> getLegalMoves(TetrisBoard board, TetrisPiece current_piece){
        ArrayList<TetrisMove> legalMoves = new ArrayList<TetrisMove>();
        for (int i = 0; i < 4; i++){
            current_piece = current_piece.rotatePiece(1);
            for (int col = 0; col < board.width; col++){
                TetrisBoard currentBoard = deepCopy(board);
                TetrisMove mov = new TetrisMove(current_piece, col);
                if (currentBoard.addPiece(mov)){
                    legalMoves.add(mov);
                }
            }
        }
        return legalMoves;
    }

    /**
     * Only returns a negative reward if the game is lost. We give no other information
     * about the game. Not even lines cleared!
     * @param board A deep copy of the current board
     * @param current_piece The next piece that could be placed
     * @return reward Are we going to lose or not?
     */
    public double getReward(TetrisBoard board, TetrisPiece current_piece){
        double reward = 0;

        if (getLegalMoves(board, current_piece).size()==0) {
            return -1;
        }
        return reward;
    }
}
