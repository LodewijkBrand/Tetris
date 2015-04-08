import java.util.Arrays;


public class Contour{

    /*@param: takes in a tetris board
      @return: an int[] representing the contour of the tetris blocks
     */
    public static int[] readContour(TetrisBoard t_board){

	int[] contour = new int[t_board.width];
	int firstBlock = 0;
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
		    contour[i]=j-firstBlock;
		    System.out.println("contour[i]: " + contour[i]);
		    break;
		}
	    }
	}

	return contour;
    }


    /*this method takes in a contour and breaks it into 7 chunks of size 4
      @param: the contour to break up
      @return: an array with a chunk (an int[] of size 4) at each index.
     */
    public int[][] contourChunks(int[] contour){
	int CHUNKSIZE = 4;
	int NUMCHUNKS = 7;
	int chunk_number = 0;
	int[][] chunks = new int[NUMCHUNKS][CHUNKSIZE];
	for(int i = 0; i<NUMCHUNKS; i++){
	    int[] chunk = new int[CHUNKSIZE];
	    for(int j = 0; j<CHUNKSIZE; j++){
		chunk[j] = contour[chunk_number+j];
	    }
	    chunks[i]=chunk;
	    chunk_number++;
	}
	return chunks;
    }

    


}