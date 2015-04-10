import java.util.Arrays;


public class Contour{
    int width;
    double[] contour;

    public Contour(int _width){
	width = _width;
	contour = new double[width];
    }

    /*@param: takes in a tetris board
      @return: a contour object representing the contour of the tetris blocks
     */
    public static Contour readContour(TetrisBoard t_board){

	Contour b_contour = new Contour(t_board.width);
	double firstBlock = 0;
	//find the first block in column 0 
	//note that a Tetrisboard.board is stored as [row][column]
	for(int j=0; j<t_board.height;j++){
	    if(t_board.board[j][0] !=0){
		firstBlock=j;
		break;
	    }
	}	
	//represent every other block relative to the starting block

	for(int i=0; i<t_board.width; i++){
	    for(int j=0; j<t_board.height; j++){
		if (t_board.board[j][i] !=0){
		    System.out.println("j: " + j);
		    b_contour.contour[i]=j-firstBlock;
		    System.out.println("contour[i]: " + b_contour.contour[i]);
		    break;
		}
	    }
	}

	return b_contour;
    }


    /*this method takes in a contour and breaks it into 7 chunks of size 4
      @param: the contour to break up
      @return: an array with a chunk (an int[] of size 4) at each index.
     */
    /*public int[][] contourChunks(Contour c){
	int CHUNKSIZE = 4;
	int NUMCHUNKS = 7;
	int chunk_number = 0;
	int[][] chunks = new int[NUMCHUNKS][CHUNKSIZE];
	for(int i = 0; i<NUMCHUNKS; i++){
	    int[] chunk = new int[CHUNKSIZE];
	    for(int j = 0; j<CHUNKSIZE; j++){
		chunk[j] = c.contour[chunk_number+j];
	    }
	    chunks[i]=chunk;
	    chunk_number++;
	}
	return chunks;
	}*/



    /*@return: a list of lists of all the possible moves per piece
     */
    /*    public ArrayList<ArrayList<TetrisMove>> generatePossibleMoves(TetrisPiece currentPiece){

	ArrayList<ArrayList<TetrisMove>> globalLegalMoves = new ArrayList<>();
	int NUM_OF_PIECES = 7;
	int NUM_OF_ROTATIONS = 4;

	for(int j=0; j<NUM_OF_PIECES;j++){

	    ArrayList<TetrisMove> moves_for_piece = new ArrayList<>();	
	    //if the piece is a square piece
	    if(currentPiece.blocks[0][0]==1 && currentPiece.blocks[0][1]==1 && currentPiece.blocks[1][0]==1 && currentPiece.blocks[1][1]==1){
		//don't create the rotations, just every possible col value
		for(int k=0; k<width-current_piece.width+1; k++){
		    TetrisMove possibleMove= new TetrisMove(current_piece, k);
		    moves_for_piece.add(possibleMove); 
		}
	    }
	    //if the piece is an S piece, a Z piece or a line piece
	    else if((currentPiece.blocks[0][0]==0 && currentPiece.blocks[0][1]==1 && currentPiece.blocks[0][2]==1 && currentPiece.blocks[1][0]==1 && currentPiece.blocks[1][1]==1 && currentPiece.blocks[1][2]==0)||
		    (currentPiece.blocks[0][0]==1 && currentPiece.blocks[0][1]==1 && currentPiece.blocks[0][2]==0 && currentPiece.blocks[1][0]==0 && currentPiece.blocks[1][1]==1 && currentPiece.blocks[1][2]==1)||
		    (currentPiece.blocks[0][0]==1 && currentPiece.blocks[1][0]==1 && currentPiece.blocks[2][0]==1 && currentPiece.blocks[3][0]==1)){
		//only create 2 rotations, in every possible col value
		TetrisPiece rotated_once = currentPiece.rotatePiece(1);
		TetrisPiece rotated_twice = currentPiece.rotatePiece(2);		
		for(int k=0; k<width-current_piece.width+1;k++){
		    TetrisMove possibleMoveOne = new TetrisMove(rotated_once, k);
		    TetrisMove possibleMoveTwo = new TetrisMove(rotated_twice, k);
		    moves_for_piece.add(possibleMoveOne);
		    moves_for_piece.add(possibleMoveTwo);
		}
	    }
	    //if the piece is an T piece, a right L piece or a left L piece
	    else if((currentPiece.blocks[0][0]==0 && currentPiece.blocks[0][1]==1 && currentPiece.blocks[0][2]==0 && currentPiece.blocks[1][0]==1 && currentPiece.blocks[1][1]==1 && currentPiece.blocks[1][2]==1)||
		     (currentPiece.blocks[0][0]==1 && currentPiece.blocks[0][1]==0 && currentPiece.blocks[1][0]==1 && currentPiece.blocks[1][1]==0 && currentPiece.blocks[2][0]==1 && currentPiece.blocks[2][1]==1)||
		     (currentPiece.blocks[0][0]==0 && currentPiece.blocks[0][1]==1 && currentPiece.blocks[1][0]==0 && currentPiece.blocks[1][1]==1 &&  currentPiece.blocks[2][0]==1 &&  currentPiece.blocks[2][1]==1)){
			//create all 4 rotations, in every possible col val
			TetrisPiece rotated_once = currentPiece.rotatePiece(1);
			TetrisPiece rotated_twice = currentPiece.rotatePiece(2);		
			TetrisPiece rotated_thrice = currentPiece.rotatePiece(3);
			TetrisPiece rotated_four = currentPiece.rotatePiece(4);		
			
			for(int k=0; k<width-current_piece.width+1;k++){
			    TetrisMove possibleMoveOne = new TetrisMove(rotated_once, k);
			    TetrisMove possibleMoveTwo = new TetrisMove(rotated_twice, k);
			    TetrisMove possibleMoveThree = new TetrisMove(rotated_thrice, k);
			    TetrisMove possibleMoveFour = new TetrisMove(rotated_four, k);
			    moves_for_piece.add(possibleMoveOne);
			    moves_for_piece.add(possibleMoveTwo);
			    moves_for_piece.add(possibleMoveThree);
			    moves_for_piece.add(possibleMoveFour);

			}


		    }
		    globalLegalMoves.addAll(moves_for_piece);
	}
 
	
	}*/
}
    
