import java.util.Arrays;

public class Contour{

    /*@param: takes in a tetris board
      @return: a contour object representing the contour of the tetris blocks
     */

    public static double[] readContour(TetrisBoard t_board){
      double[] contour = new double[t_board.width + 1];
      contour[0] = 1;
    	double firstBlock = t_board.height;
    	//find the first block in column 0 
    	//note that a Tetrisboard.board is stored as [row][column]
    	for(int j = 0; j < t_board.height; j++){
    	    if(t_board.board[j][0] != 0){
    		    firstBlock = j;
    		    break;
    	    }
      }	

      //represent every other block relative to the starting block

      
      for(int i = 1; i < contour.length; i++){
        for(int j = 0; j < t_board.height; j++){
          if (t_board.board[j][i - 1] !=0){
    		    // System.out.println("j: " + j);
    		    if ((firstBlock - j) > 4){
              contour[i] = 4;
            }
            else if ((firstBlock - j) < -4){
              contour[i] = -4;
            }
            else{
              contour[i] = firstBlock - j;
            }
            firstBlock = j;
          //   System.out.println("firstBlock: " + firstBlock);
    		    // System.out.println("contour[i]: " + contour[i]);
    		    break;
		      }
        }
      }

      return contour;
    }

}

    
