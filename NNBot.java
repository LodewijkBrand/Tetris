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
    public static ArrayList<Double> contour(TetrisBoard tBoard){
	int[][] board = tBoard.board;
	ArrayList<Double> contour = new ArrayList<Double>();
	int lowestBlock = 0;
	//Find the heights of all of the columns
	//note that a Tetrisboard.board is stored as [row][column]
	for (int c = 0; c<tBoard.width; c++){
	    for(int r = 0; r<tBoard.height; r++){
		if (board[r][c] != 0) {
		    contour.add((double)tBoard.height-r);
		    break;
		}
		else if (r == tBoard.height-1 && board[r][c] == 0) {
		    contour.add(0.0);
		}
	    }
	}
	System.out.println(contour);
	format(contour);
	return contour;
    }

    //Finds the lowest value in the array and subtracts that value from all other value in the array and returns it
    public static void format(ArrayList<Double> contour) {
	double lowest = findLowest(contour);
	for (int i = 0; i < contour.size(); i++) {
	    contour.set(i, contour.get(i)-lowest);
	}
    }

    //Find the lowest number in an array and return it
    public static double findLowest(ArrayList<Double> contour) {
	double lowest = contour.get(0);
	for (int i = 1; i < contour.size(); i++) {
	    if (contour.get(i) < lowest) {
		lowest = contour.get(i);
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
	TetrisBoard board = new TetrisBoard(10, 20, false);
	TetrisPiece piece = TetrisPiece.buildLinePiece();
	piece = piece.rotatePiece(1);
	System.out.println(getLegalMoves(board, piece).size());

	TetrisPiece piece2 = TetrisPiece.buildZPiece();
	System.out.println(getLegalMoves(board, piece2).size());

	TetrisMove move = new TetrisMove(piece, 0);
	TetrisMove move2 = new TetrisMove(piece2, 7);
	board.addPiece(move);
	System.out.println(board.addPiece(move2));
	System.out.println(board);	
	System.out.println(contour(board));
    }

    //LOU WORK BELOW HERE!!!

    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece){
	for(TetrisMove mov : getLegalMoves(board, current_piece)){
	    TetrisBoard currentBoard = deepCopy(board);
	    double reward = getReward(currentBoard, mov);
	}
	return null;
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

    public double getReward(TetrisBoard board, TetrisPiece current_piece){
	double reward = 0.0;
	
	if (getLegalMoves(board, current_piece).size()==0) {
	    return -100000000;
	}
	if (board.addPiece(move)){
	    
	}else{
	    return -1000000000;
	}
	return reward;
    }
}
