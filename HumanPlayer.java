
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.*;
import java.lang.reflect.*;

public class HumanPlayer extends TetrisBot implements KeyListener  {
    
    int numberRotations;
    int colPosition;
    TetrisPiece currPiece;
    TetrisBoard currBoard;
    boolean waitForPlay = true;
    
    public HumanPlayer() {
        numberRotations = 0;
        colPosition = 0;
    }
    
    public void keyPressed(KeyEvent e){
        char key = e.getKeyChar();
        if (e.getKeyCode() == 38) {
            currPiece = currPiece.rotatePiece(1);
            System.out.println(currPiece);
            if (currBoard.width - currPiece.width < colPosition) {
                colPosition--;
            }
        }
        else if (e.getKeyCode() == 40) {
            //System.out.println("Pressed down: LAUNCH");
        }
        else if (e.getKeyCode() == 37) {
            if (colPosition > 0) {
                colPosition--;
            }
        }
        else if (e.getKeyCode() == 39) {
            if (colPosition + currPiece.width < currBoard.width) {
                colPosition++;
            }
        }
        currBoard.viewIncomingPiece(currPiece, colPosition);
    }
    
    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == 40) {
            waitForPlay = false;
            
        }
    }
    public void keyTyped(KeyEvent e){}
    
    
    // return the left-most column of where you want to play
    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        
        currPiece = current_piece;
        currBoard = board;
        
        currBoard.viewIncomingPiece(current_piece, colPosition);

        while (waitForPlay) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Execution interrupted!");
            }
        }
        
        waitForPlay = true;
        int oldPosition = colPosition;
        colPosition = board.width / 2;

        currBoard.viewIncomingPiece(null, 0);
        
        return new TetrisMove(currPiece, oldPosition);
    }
}
