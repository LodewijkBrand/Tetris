
import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.awt.event.*;
import java.lang.reflect.*;


class TetrisBoardViewer extends JComponent {

    TetrisBoard b = null;
    TetrisPiece incomingPiece = null;
    int incomingColPosition = 0;
    Font font;
    
    public TetrisBoardViewer(TetrisBoard _b) {
        super();
        b = _b;
        
        font = new Font("Verdana", Font.BOLD, 24);
    }
    
    public void setIncomingPiece(TetrisPiece _p, int colPosition) {
        incomingPiece = _p;
        incomingColPosition = colPosition;
    }

    public void paintComponent(Graphics g) {
        int squareWidth = getWidth() / b.width;
        int squareHeight= getHeight() / b.height;

        for (int r = 0; r < b.height; r++) {
            for (int c = 0; c < b.width; c++) {
                if (b.board[r][c] != 0) {
                    // black piece
                    //g.setColor(Color.BLACK);
                    Color color = new Color(100, Math.min(b.board[r][c]+50, 255), 100);
                    g.setColor(color);
                    g.fillRect(c * squareWidth , r * squareHeight, squareWidth, squareHeight);
                }
                else {
                    // white piece
                    g.setColor(Color.WHITE);
                    g.fillRect(c * squareWidth, r * squareHeight, squareWidth, squareHeight);
                }
            }
        }
        
        // paint incoming piece
        if (incomingPiece != null) {
            for (int r = 0; r < incomingPiece.height; r++) {
                for (int c = 0; c < incomingPiece.width; c++) {
                    if (incomingPiece.blocks[r][c] != 0) {
                        Color color = new Color(233, 20, 10);
                        g.setColor(color);
                        g.fillRect((incomingColPosition + c) * squareWidth , r * squareHeight, squareWidth, squareHeight);
                    }
                }
            }
        }
        
        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(font);

        g.drawString("" + b.blocksPlaced, 10, 20);
        
    }
}

class TetrisMove {
    
    public TetrisPiece piece;
    public int boardCol;
    
    public TetrisMove(TetrisPiece _p, int _boardCol) {
        piece = _p;
        boardCol = _boardCol;
    }
    
}




class TetrisPiece {
    
    public int width;
    public int height;
    
    public int[][] blocks;
    
    public TetrisPiece(int[][] pieceBlocks) {
        height = pieceBlocks.length;
        width = pieceBlocks[0].length;
        blocks = pieceBlocks;
    }
    
    
    public static TetrisPiece buildSquarePiece() {
        int[][] newBlocks = new int[2][2];
        newBlocks[0][0] = 1;
        newBlocks[0][1] = 1;
        newBlocks[1][0] = 1;
        newBlocks[1][1] = 1;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildSPiece() {
        int[][] newBlocks = new int[2][3];
        newBlocks[0][0] = 0;
        newBlocks[0][1] = 1;
        newBlocks[0][2] = 1;
        newBlocks[1][0] = 1;
        newBlocks[1][1] = 1;
        newBlocks[1][2] = 0;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildZPiece() {
        int[][] newBlocks = new int[2][3];
        newBlocks[0][0] = 1;
        newBlocks[0][1] = 1;
        newBlocks[0][2] = 0;
        newBlocks[1][0] = 0;
        newBlocks[1][1] = 1;
        newBlocks[1][2] = 1;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildTPiece() {
        int[][] newBlocks = new int[2][3];
        newBlocks[0][0] = 0;
        newBlocks[0][1] = 1;
        newBlocks[0][2] = 0;
        newBlocks[1][0] = 1;
        newBlocks[1][1] = 1;
        newBlocks[1][2] = 1;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildRightLPiece() {
        int[][] newBlocks = new int[3][2];
        newBlocks[0][0] = 1;
        newBlocks[0][1] = 0;
        newBlocks[1][0] = 1;
        newBlocks[1][1] = 0;
        newBlocks[2][0] = 1;
        newBlocks[2][1] = 1;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildLeftLPiece() {
        int[][] newBlocks = new int[3][2];
        newBlocks[0][0] = 0;
        newBlocks[0][1] = 1;
        newBlocks[1][0] = 0;
        newBlocks[1][1] = 1;
        newBlocks[2][0] = 1;
        newBlocks[2][1] = 1;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildLinePiece() {
        int[][] newBlocks = new int[4][1];
        newBlocks[0][0] = 1;
        newBlocks[1][0] = 1;
        newBlocks[2][0] = 1;
        newBlocks[3][0] = 1;
        return new TetrisPiece(newBlocks);
    }
    
    public static TetrisPiece buildRandomPiece() {
        Random r = new Random();
        int ran = r.nextInt(7);
        if (ran == 0) return buildSquarePiece();
        else if (ran == 1) return buildTPiece();
        else if (ran == 2) return buildSPiece();
        else if (ran == 3) return buildZPiece();
        else if (ran == 4) return buildLinePiece();
        else if (ran == 5) return buildRightLPiece();
        else if (ran == 6) return buildLeftLPiece();
        else {
            return buildSquarePiece();
        }
        
    }
    
    public int getPieceSink(int offset) {
        for (int r = height-1; r >= 0; r--) {
            if (blocks[r][offset] != 0) {
                //System.out.println("SINK:" + (height - r - 1));
                return height - r - 1;
            }
        }
        return 0;
    }
    
    public int getBottom(int offset){
        for (int r = height-1; r >= 0; r--) {
            if (blocks[r][offset] != 0) {
                return r;
            }
        }
        return 0;
    }
    
    public TetrisPiece rotatePieceHelper() {
        // perform a single 90 degree clockwise rotation
        
        // Flip the dimensions
        int w = this.height;
        int h = this.width;
        
        // Copy the data
        int[][] newBlocks = new int[h][w];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                //System.out.println(row + ", " + col + " : " + w + ", " + h);
                newBlocks[col][w-row-1] = blocks[row][col];   
            }
        }
        return new TetrisPiece(newBlocks);
    }
    
    // rotate this piece n times clockwise 90 degrees
    public TetrisPiece rotatePiece(int n) {
        TetrisPiece current = this;
        for (int i = 0; i < n; i++) {
            current = current.rotatePieceHelper();
        }
        return current;
    }
    
    public String toString() {
        String result = "";
        for (int row = 0; row < height; row++) {
            for (int col = 0; col< width; col++) {
                if (blocks[row][col] > 0) {
                    result += "*";
                }
                else {
                    result += " ";
                }
            }
            result += "\n";
        }
        return result;
    }

    public static int whatPiece(TetrisPiece piece){
	if (piece.blocks.length == 2 && piece.blocks[0].length == 2){
	    return 0; //Square Piece
	}
	if(piece.blocks.length == 4 && piece.blocks[0].length == 1){
	    return 4; //Line Piece
	}
	if (piece.blocks.length == 2 && piece.blocks[0].length == 3){
	    if (piece.blocks[0][0] == 0 && piece.blocks[0][1] == 1 && piece.blocks[0][2] == 0 && piece.blocks[1][0] == 1 && piece.blocks[1][1] == 1 && piece.blocks[1][2] == 1){
		return 1; //T Piece
	    }
	    if(piece.blocks[0][0] == 0 && piece.blocks[0][1] == 1 && piece.blocks[0][2] == 1 && piece.blocks[1][0] == 1 && piece.blocks[1][1] == 1 && piece.blocks[1][2] == 0){
		return 2; //S Piece
	    }
	    if(piece.blocks[0][0] == 1 && piece.blocks[0][1] == 1 && piece.blocks[0][2] == 0 && piece.blocks[1][0] == 0 && piece.blocks[1][1] == 1 && piece.blocks[1][2] == 1){
		return 3; //Z Piece
	    }
	}
	if (piece.blocks.length == 3 && piece.blocks[0].length == 2){
	    if(piece.blocks[0][0] == 1 && piece.blocks[0][1] == 0 && piece.blocks[1][0] == 1 && piece.blocks[1][1] == 0 && piece.blocks[2][0] == 1 && piece.blocks[2][1] == 1){
		return 5; //Right L Piece
	    }
	    if(piece.blocks[0][0] == 0 && piece.blocks[0][1] == 1 && piece.blocks[1][0] == 0 && piece.blocks[1][1] == 1 && piece.blocks[2][0] == 1 && piece.blocks[2][1] == 1){
		return 6; //Left L Piece
	    }
	}
	return -1; //Invalid Piece
    }
}


class TetrisBoard {
    
    public int[][] board;
    public int height;
    public int width;
    
    public int linesEliminated;
    public int blocksPlaced;
    
    public boolean hasViewWindow;
    JFrame viewer = null;
    TetrisBoardViewer tbv = null;
    
    public static final int WIDTH = 400;
    public static final int HEIGHT = 800;
    
    public TetrisBoard(int _width, int _height, boolean _hasViewWindow) {
        linesEliminated = 0;
        blocksPlaced = 0;
        width = _width;
        height = _height;
        board = new int[height][width];  // rows then columns
        
        hasViewWindow = _hasViewWindow;
        if (hasViewWindow) {
            viewer = new JFrame();
            viewer.setTitle("Tetris Viewer");
            viewer.setSize(WIDTH + 50, HEIGHT + 50);
            tbv = new TetrisBoardViewer(this);
            viewer.add(tbv);
            viewer.setVisible(true);
            viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
    
    public void addHumanPlayerKeyListener(HumanPlayer p) {
        if (hasViewWindow) {
            viewer.addKeyListener(p);
        }
        else {
            System.out.println("No view window, no key listener for you!");
        }  
    }
    
    

    public void viewIncomingPiece(TetrisPiece p, int colPosition) {
        if (hasViewWindow) {
            tbv.setIncomingPiece(p, colPosition);
            repaint();
        }
    }
    
    public void repaint() {
        viewer.repaint();
    }

    
    // return the row of the highest point in the given column
    public int getHighestPoint(int col) {
        for (int row = 0; row < height; row++) {
            if (board[row][col] != 0){
                return row;
            }
        }
        //System.out.println("HIT POINT:" + (height));
        return height;
    }
    
    
    public void copyPieceToLocation(TetrisPiece p, int row, int col) {
        Random ra = new Random();
        int rColor = 1 + ra.nextInt(254);
        for (int r = 0; r < p.height; r++) {
            for (int c = 0; c < p.width; c++) {
                if (p.blocks[r][c] != 0) {
                    board[row+r][col+c] = rColor;
                }
            }
        }
    }
    
    public boolean checkEliminate(int row) {
        for (int col = 0; col < width; col++) {
            if (board[row][col] == 0) {
                return false;
            }
        }
        return true;
    }
    
    public void eliminateRows() {
        for (int row = 0; row < height; row++) {
            if (checkEliminate(row)) {
                eliminateRow(row);
            }
        }
    }
    
    public void aboveFallsDown(int row, int col) {
        for (int currentRow = row; currentRow > 0 ; currentRow--) {
            board[currentRow][col] = board[currentRow-1][col];
        }
        board[0][col] = 0;
    }
    
    public void eliminateRow(int row) {
        linesEliminated++;
        for (int col = 0; col < width; col++) {
            board[row][col] = 0;
            int fallRow = row;
            while (fallRow+1 < height && board[fallRow+1][col] == 0) {
                fallRow++;
            }
            aboveFallsDown(fallRow, col);
        }
    }
    
    
    public boolean addPiece(TetrisMove m) {
        
        blocksPlaced++;
        
        int leftCol = m.boardCol;
        TetrisPiece piece = m.piece;
        
        
        if (leftCol < 0 || leftCol + piece.width > width) {
            return false;
        }
        
        for (int row = 0; row < height; row++) {
            for (int offset = 0; offset < piece.width; offset++) {
                int ledge = row + piece.getBottom(offset) + 1;

                if (ledge >= height || board[ledge][leftCol + offset] != 0) {
                    copyPieceToLocation(piece, row, leftCol);
                    return row > 0;
                }
            }
        }
        
        return false;
    }
    

    public String toString() {
        String result = "";
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col< width; col++) {
                result += board[row][col] + " ";
            }
            result += "\n";
        }
        return result;
    }
    
    
}


class TetrisBot {
    
    // override this method
    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        return new TetrisMove(current_piece, 0);
    }
}


class TetrisGame {
    
    public static int playGame(int boardWidth, int boardHeight, TetrisBot player, boolean viewPlayback, int playbackDelay) {
        TetrisBoard b = new TetrisBoard(boardWidth, boardHeight, viewPlayback);
        //RandomBot player = new RandomBot();
        //HeuristicBot player = new HeuristicBot();

        if (player.getClass().getSimpleName().equals("HumanPlayer")) {
            b.addHumanPlayerKeyListener((HumanPlayer)player);    
        }
        
        boolean alive = true;
        TetrisPiece current_piece = TetrisPiece.buildRandomPiece();
        TetrisPiece next_piece = TetrisPiece.buildRandomPiece();
        while (alive) {
                       
            //TetrisPiece p = TetrisPiece.buildRandomPiece();
            TetrisMove m = player.chooseMove(b, current_piece, next_piece);

            alive = b.addPiece(m);           

            if (b.hasViewWindow) {
                b.repaint();
            }

            try {
                Thread.sleep(playbackDelay);
            } catch (InterruptedException e) {
                System.out.println("Execution interrupted!");
            }
            
            b.eliminateRows();
            
            if (b.hasViewWindow) {
                b.repaint();
            }
            
            try {
                Thread.sleep(playbackDelay);
            } catch (InterruptedException e) {
                System.out.println("Execution interrupted!");
            }
            
            current_piece = next_piece;
            next_piece = TetrisPiece.buildRandomPiece();
        }
        
        return b.blocksPlaced;
    }
    
    
    
    
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if (args.length < 6) {
            System.out.println("USAGE:\n\t java TetrisGame BotClassName BOARD_WIDTH BOARD_HEIGHT NUMBER_GAMES PLAYBACK_DISPLAY PLAYBACK_DELAY\n");
            System.out.println("EXAMPLE:\n\t java TetrisGame RandomBot 10 20 10 0 0");
            System.exit(1);
        }
        
        String botClassName = args[0];

        int BOARD_WIDTH = Integer.parseInt(args[1]);
        int BOARD_HEIGHT = Integer.parseInt(args[2]);
        
        long NUMBER_GAMES = Long.parseLong(args[3]);
        boolean viewPlayback = Integer.parseInt(args[4]) > 0;
        int PLAYBACK_DELAY = Integer.parseInt(args[5]);      
        
        //Class c1 = Class.forName(botClassName);
        //Constructor con1 = c1.getConstructor();
        //TetrisBot player = (TetrisBot)con1.newInstance();
        
        TetrisBot player = (TetrisBot) Class.forName(botClassName).newInstance();
        
        int totalScore = 0;
        int myScore = 0;
        for (int i = 0; i < NUMBER_GAMES; i++) {
            int gameScore = playGame(BOARD_WIDTH, BOARD_HEIGHT, player, viewPlayback, PLAYBACK_DELAY);
            //System.out.println("Game score: " + gameScore);
            totalScore += gameScore;
            myScore += gameScore;
            if (i%50000==0) {
                System.out.println(botClassName + " achieved average score: " + ((double)myScore / 50000));
                myScore = 0;
            }

        }
        System.out.println(botClassName + " achieved average score: " + ((double)totalScore / NUMBER_GAMES));
    }    
}
