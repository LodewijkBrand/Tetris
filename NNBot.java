import java.util.Arrays;
import java.util.ArrayList;

public class NNBot {
    NeuralNetwork myNN;
    final double GAMMA = .5;
    final double LEARNING_RATE = .7;

    public NNBot(){
	myNN = new NeuralNetwork();
    }

    //Returns an integer array 
    public static int[] contour(TetrisBoard tBoard){
	int[][] board = tBoard.board;
	int[] contour = new int[tBoard.width];
	int lowestBlock = 0;
	//Find the heights of all of the columns
	//note that a Tetrisboard.board is stored as [row][column]
	for (int c = 0; c<tBoard.width; c++){
	    int height = 0;
	    for(int r = 0; r<tBoard.height;r++){
		if (board[r][c] != 0) {
		    height++;
		}
		else {
		    contour[c] = height;
		    break;
		}
	    }
	}
	format(contour);
	return contour;
    }

    //Finds the lowest value in the array and subtracts that value from all other value in the array and returns it
    public static void format(int[] contour) {
	int lowest = findLowest(contour);
	for (int i = 0; i < contour.length; i++) {
	    contour[i] -= lowest;
	}
    }

    //Find the lowest number in an array and return it
    public static int findLowest(int[] contour) {
	int lowest = contour[0];
	for (int i = 1; i < contour.length; i++) {
	    if (contour[i] < lowest) {
		lowest = contour[i];
	    }
	}
	return lowest;
    }

    public TetrisBoard deepCopy(TetrisBoard board) {
	return null;
    }

    public static void main(String[] args) {
	TetrisBoard board = new TetrisBoard(10, 20, false);
	System.out.println(board);
	System.out.println(Arrays.toString(contour(board)));
    }

    //LOU WORK BELOW HERE!!!

    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece){
	for(TetrisMove mov : getLegalMoves(board)){
	    TetrisBoard currentBoard = deepCopy(board);
	    double reward = getReward(currentBoard, mov);

	}
	return null;
    }

    public ArrayList<TetrisMove> getLegalMoves(TetrisBoard board){
	return null;
    }

    public double getReward(TetrisBoard board, TetrisMove move){
	double reward = 0.0;
	return reward;
    }
}
