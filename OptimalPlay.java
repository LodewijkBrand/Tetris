import java.util.*;
import java.io.*;

public vlass OptimalPlay() {

    double theta;
    Contour c;
    TetrisPiece[] pieces;
    ArrayList<ArrayList<TetrisMove>> allPieceMoves;

    public OptimalPlay() {
        double[] theta = new double[11];
        c = new Contour();
        pieces = getPieces();
        allPieceMoves = getAllPieceMoves();
        try {
            File in = new File("theta.txt");
            Scanner s = new Scanner(in);
            int counter = 0;
            while(s.hasNextLine()) {
                double i = s.nextDouble();
                theta[couter] = i;
                counter++;
            }
            s.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        return getBestMove(board, current_piece);
    }

    public TetrisMove getBestMove(TetrisBoard b, TetrisPiece p) {
        ArrayList<TetrisMove> moves = getPieceMoves(p);
        double bestQ = -(Double.MAX_VALUE);
        TetrisMove bestMove = moves.get(0);
        for(TetrisMove m : moves) {
            TetrisBoard temp = deepCopy(b);
            temp.addPiece(m);
            double[] state = c.readContour(temp);
                if (predictedValue(state) > bestQ) {
                    bestQ = predictedValue(state);
                    bestMove = m;
                }
        }
        return bestMove;
    }

    public ArrayList<TetrisMove> getPieceMoves(TetrisPiece piece){
        ArrayList<TetrisMove> pieceMoves = new ArrayList<>();
        ArrayList<TetrisPiece> rotatedPieces = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            rotatedPieces.add(piece.rotatePiece(i));
            for(int j = 0; j < 10 - rotatedPieces.get(i).width + 1; j++){
                TetrisMove move = new TetrisMove(rotatedPieces.get(i), j);
                pieceMoves.add(move);
            }
        }
        return pieceMoves;
    }

    public TetrisBoard deepCopy(TetrisBoard _t) {
        TetrisBoard t = new TetrisBoard(_t.width, _t.height, false);
        t.blocksPlaced = _t.blocksPlaced;
        for (int r = 0; r < _t.board.length; r++) {
            for (int c = 0; c <_t.board[0].length; c++) {
                t.board[r][c] = _t.board[r][c];
            }
        }
        t.linesEliminated = _t.linesEliminated;
        //not messing with _t.tbv
        return t;
    }

    public TetrisPiece[] getPieces(){
        TetrisPiece[] pieces = new TetrisPiece[7];
        pieces[0] = TetrisPiece.buildSquarePiece();
        pieces[1] = TetrisPiece.buildSPiece();
        pieces[2] = TetrisPiece.buildZPiece();
        pieces[3] = TetrisPiece.buildTPiece();
        pieces[4] = TetrisPiece.buildRightLPiece();
        pieces[5] = TetrisPiece.buildLeftLPiece();
        pieces[6] = TetrisPiece.buildLinePiece();
        return pieces;
    }

    public ArrayList<TetrisMove> getPieceMoves(TetrisPiece piece){
        ArrayList<TetrisMove> pieceMoves = new ArrayList<>();
        ArrayList<TetrisPiece> rotatedPieces = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            rotatedPieces.add(piece.rotatePiece(i));
            for(int j = 0; j < 10 - rotatedPieces.get(i).width + 1; j++){
                TetrisMove move = new TetrisMove(rotatedPieces.get(i), j);
                pieceMoves.add(move);
            }
        }
        return pieceMoves;
    }
}
