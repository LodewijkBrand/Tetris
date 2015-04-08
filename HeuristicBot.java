
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.*;
import java.lang.reflect.*;

public class HeuristicBot extends TetrisBot {
    
    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        
        int highest = 0;
        int highestCol = 0;
                
        // Choose highest row (lowest zone on the board)
        for (int col = 0; col < board.width - current_piece.width + 1; col++) {
            if (board.getHighestPoint(col) > highest) {
                highest = board.getHighestPoint(col);
                highestCol = col;
            }
        }
        
        return new TetrisMove(current_piece, highestCol);
    }
}
