import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.*;
import java.lang.reflect.*;


public class RandomBot extends TetrisBot {
    System.out.println("JUNKITY JUNK");
    // return the left-most column of where you want to play
    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        Random r = new Random();

        current_piece = current_piece.rotatePiece(r.nextInt(4));
        
        int col = r.nextInt(board.width-current_piece.width+1);
        return new TetrisMove(current_piece, col);
    }
}

