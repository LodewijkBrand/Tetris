//THINGS TO TRY: Erase eligibility traces after each game
import java.util.Arrays;
import java.util.ArrayList;

public class NNBot extends TetrisBot{
    TDNetwork myNN;
    final double GAMMA = .9;
    final double LAMBDA = .5;
    final double ALPHA = .08333;
    final double BETA = .33333;
    final double BIAS = 1;
    double ETA = 0.1;
    int inputNodes = 23;

    public NNBot(){
        //EXPERIMENT ON THESE!
        System.out.println("INITIALIZING NEURAL NETWORK BAD!");
        //int _n, int _num_hidden, int _m,  double _BIAS, double _ALPHA, double _BETA, double _GAMMA, double _LAMBDA
        myNN = new TDNetwork(inputNodes, 2, 1, BIAS, ALPHA, BETA, GAMMA, LAMBDA);
    }

    //Returns an integer array 
    public double[] contour(TetrisBoard tBoard, Boolean format){
        int[][] board = tBoard.board;
        double[] contour = new double[tBoard.width];
        //Find the heights of all of the columns
        //note that a Tetrisboard.board is stored as [row][column]
        for (int c = 0; c<tBoard.width; c++){
            for(int r = 0; r<tBoard.height; r++){
                if (board[r][c] != 0) {
                    contour[c] = (double)(tBoard.height-r);
                    break;
                }
                else if (r == tBoard.height-1 && board[r][c] == 0) {
                    contour[c] = (0.0);
                }
            }
        }
        if (format) {
            format(contour);
        }
        return contour;
    }

    //Finds the lowest value in the array and subtracts that value from all other value in the array and returns it
    public static void format(double[] contour) {
        double lowest = findLowest(contour);
        for (int i = 0; i < contour.length; i++) {
            contour[i] = (contour[i]-lowest);
        }
    }
    
    //Find the highest number in an array and return it
    public static double findHighest(double[] contour) {
        double highest = contour[0];
        for (int i = 1; i < contour.length; i++) {
            if (contour[i] > highest) {
                highest = contour[i];
            }
        }
        return highest;
    }

    //Find the lowest number in an array and return it
    public static double findLowest(double[] contour) {
        double lowest = contour[0];
        for (int i = 1; i < contour.length; i++) {
            if (contour[i] < lowest) {
                lowest = contour[i];
            }
        }
        return lowest;
    }

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

    public static void main(String[] args) {
        NNBot bot = new NNBot();
        TetrisBoard board = new TetrisBoard(10, 20, false);
        TetrisPiece piece = TetrisPiece.buildLeftLPiece();
        //piece = piece.rotatePiece(1);
        //System.out.println(getLegalMoves(board, piece).size());

        TetrisPiece piece2 = TetrisPiece.buildLinePiece();
        //System.out.println(getLegalMoves(board, piece2).size());
	//System.out.println("HELLO!: " + TetrisPiece.whatPiece(piece));
        TetrisMove move = new TetrisMove(piece, 0);
        TetrisMove move2 = new TetrisMove(piece2, 7);
        board.addPiece(move);
        board.addPiece(move2);
        board.addPiece(move2);
        System.out.println(board);
        System.out.println(Arrays.toString(bot.contour(board, false)));
        System.out.println(findHighest(bot.contour(board, false)));
        //System.out.println(bot.getReward(board));
        double[] input = new double[12];
        //input = bot.getInput(board, piece);
        System.out.println(Arrays.toString(input));
    }

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
            ETA -= ETA * .999999;
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
    
    //Return the input as an double array of the contour, highest point, and next_piece
    public double[] getInput(TetrisBoard board, TetrisPiece next_piece) {
        double[] input = new double[inputNodes];
        for (int r = 0; r < 4; r++) {
            for (int c=0;c<4;c++) {
                if (board.board[r][c] != 0) {
                    input[c + r * 4] = 1;
                }
            }
        }
        //double[] cont = contour(board, true);   
/*        for (int i = 0; i < cont.length; i++) {
            input[i] = cont[i];
        }*/
        //DOES THE CONTOUR CHANGE???
        board.eliminateRows();
        //input[inputNodes-2] = findHighest(contour(board, false));
        int piece = TetrisPiece.whatPiece(next_piece);
        input[16 + piece] = 1;
        if (piece == -1) {
            System.out.println("IM MAD");

        }
        return input;
    }
    
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
