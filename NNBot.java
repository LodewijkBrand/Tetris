import java.util.Arrays;
import java.util.ArrayList;

public class NNBot extends TetrisBot{
    TDNetwork myNN;
    final double GAMMA = .5;
    final double LEARNING_RATE = .7;
    double ETA = .5;

    public NNBot(){
        //EXPERIMENT ON THESE!
        System.out.println("INITIALIZING NEURAL NETWORK BAD!");
        myNN = new TDNetwork(10, 3, 1, 1, LEARNING_RATE, LEARNING_RATE, .9, .5);
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
        TetrisPiece piece = TetrisPiece.buildLinePiece();
        piece = piece.rotatePiece(1);
        //System.out.println(getLegalMoves(board, piece).size());

        TetrisPiece piece2 = TetrisPiece.buildZPiece();
        //System.out.println(getLegalMoves(board, piece2).size());

        TetrisMove move = new TetrisMove(piece, 0);
        TetrisMove move2 = new TetrisMove(piece2, 7);
        board.addPiece(move);
        board.addPiece(move2);
        board.addPiece(move2);
        System.out.println(board);
        System.out.println(Arrays.toString(bot.contour(board, false)));
        System.out.println(findHighest(bot.contour(board, false)));
        System.out.println(bot.getReward(board));
    }

    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece){
        TetrisBoard currentBoard;
        ArrayList<TetrisMove> moves = getLegalMoves(board, current_piece); 
        //System.out.println(ETA);
        double output;    
        double best = Double.MIN_VALUE;
        TetrisMove bestMove = new TetrisMove(current_piece, 0);
        if (Math.random() > ETA) {
            for(TetrisMove move : moves){
                currentBoard = deepCopy(board);
                currentBoard.addPiece(move);
                //Pick the move with the highest outputx[t][n]=BIAS; with chance ETA
                //There will only be one output
                output = myNN.feedForward(contour(currentBoard, true))[0];
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
            ETA -= .000001;
            System.out.println(ETA);
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
    
    public void learn(TetrisBoard board, TetrisMove bestMove, TetrisPiece next_piece) {
        TetrisBoard currentBoard = deepCopy(board);
        currentBoard.addPiece(bestMove);
        myNN.timeStep(contour(currentBoard, true), getReward(currentBoard));
    }

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

    //TODO: TEST THIS, The reward is 100 for each line completed, perhaps just give a reward for completing any lines???
/*    public double getReward(TetrisBoard board, TetrisPiece current_piece){
        double reward = 0.0;

        for (int r = 0; r < board.height; r++) {
            if (board.checkEliminate(r) == true) {
                reward += .25;
            }
        }

        //If there are no legal moves left (you've lost) and the reward is zero (you are not about to clear any rows)
        if (reward == 0 && getLegalMoves(board, current_piece).size()==0) {
            return -1;
        }
        return reward;
    }
*/
    //TODO: TEST THIS, The reward is 100 for each line completed, perhaps just give a reward for completing any lines???
    public double getReward(TetrisBoard board){
        board.eliminateRows();
        double highest = findHighest(contour(board, false));
        return (1.0-(highest/(double)board.height));
    }
}
