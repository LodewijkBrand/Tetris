import java.util.ArrayList;

public class NNBot {
    NerualNetwork myNN;
    final double GAMMA = .5;
    final double LEARNING_RATE = .7;

    public NNBot(){
	myNN = new NeuralNetwork();
    }

    //Returns an integer array 
    public static int[] contour(TetrisBoard board){
	int[] contour = new int[board.width];
	int lowestBlock = 0;
	//Find the heights of all of the columns
	//note that a Tetrisboard.board is stored as [row][column]
	for (int c = 0; c<board.width; c++){
	    height = 0
	    for(int r = 0; r<t_board.height;r++){
		if (board[r][c] != 0) {
		    height++;
		}
		else {
		    contour[col] = height;
		    break;
		}
	    }
	}
    }

    //Finds the lowest value in the array and subtracts that value from all other value in the array and returns it
    public void format(int[] contour) {







	
    }

    public findLowest() {





	
    }


    //LOU WORK BELOW HERE!!!

    public TetrisMove chooseMove(TestrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece){
	Input features = new Input(contour(board));
	Output out = new Output(null);
	
    }
}
