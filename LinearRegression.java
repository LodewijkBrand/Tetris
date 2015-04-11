import java.util.*;

public class LinearRegression extends TetrisBot{

    double[] theta;
    double alpha = 0.05;
    double epsilon = 1.0;
    double dFactor = 0.4;
    
    Random r;
    Contour c;
    int move_count;
    
    double[] prev_state;
    TetrisMove moveChoice;
    TetrisBoard prev_board;
    TetrisBoard curr_board;
    
    TetrisPiece[] pieces = new TetrisPiece[7];
    ArrayList<ArrayList<TetrisMove>> allPieceMoves;
    
    
    //before I did batch learning, but this will be iterative learning
    public LinearRegression() {
    	theta = new double[11];
    	r = new Random();
    	for(int i = 0; i < theta.length; i++) {
    	    theta[i] = r.nextInt(10);
    	}
        c = new Contour();
        pieces = getPieces();
        allPieceMoves = getAllPieceMoves();
    }

    public double predictedValue(double[] state) {
	    double value = 0;
    	for(int i = 0; i < state.length; i++) {
    	    value += theta[i] * state[i];
    	}
    	return value;
    }

    public double getReward() {
        TetrisBoard board = deepCopy(prev_board);
        int tempRow = board.linesEliminated;
        boolean alive = board.addPiece(moveChoice);
        board.eliminateRows();
        if (!alive) {
            return -1000;
        }
        else if(board.linesEliminated > tempRow) {
            System.out.println("\t\t\t\tReward!! " + (50 * board.linesEliminated - tempRow));
            return 50 * board.linesEliminated - tempRow;
        }
        else{
            return 0;
        }
    }

    public double getBestQ() {
        ArrayList<ArrayList<TetrisBoard>> futureBoards = getFutureBoards();
        double[] bestQs = new double[7];
        int piece_index = 0;
        for(ArrayList<TetrisBoard> futurePieceBoards : futureBoards){
            Double bestQ = -(Double.MAX_VALUE);
            for(TetrisBoard b : futurePieceBoards){
                double[] state = c.readContour(b);
                if(predictedValue(state) > bestQ){
                    bestQ = predictedValue(state);
                }
            }
            bestQs[piece_index] = bestQ;
            piece_index++;
        }
        Arrays.sort(bestQs);
        double min = bestQs[0];
        return min;
    }

    public ArrayList<ArrayList<TetrisBoard>> getFutureBoards() {
        ArrayList<ArrayList<TetrisBoard>> futureBoards = new ArrayList<>();
        for(int i = 0; i < allPieceMoves.size(); i++){
            ArrayList<TetrisBoard> pMoves = new ArrayList<>();
            for(int j = 0; j < allPieceMoves.get(i).size(); j++){
                TetrisMove m = allPieceMoves.get(i).get(j);
                TetrisBoard b = deepCopy(curr_board);
                b.addPiece(m);
                pMoves.add(b);
            }
            futureBoards.add(pMoves);
        }
        return futureBoards;     
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

    public void learn() {
        //do move that was previously chosen
    	double reward = getReward();
    	double gamma = reward + dFactor * getBestQ() - predictedValue(prev_state);
    	for(int i = 0; i < theta.length; i++) {
    	    theta[i] += alpha * gamma * prev_state[i];
    	}
    }

    public TetrisMove chooseMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        move_count++;
        double random = Math.random();
        if(move_count % 5000 == 0){
            epsilon *= 0.999;
            System.out.println("\t\t\tmove_count: " + move_count);
            System.out.println("\t\t\tepsilon: " + epsilon);
        }
            
        if(random < epsilon){
            moveChoice = chooseRandomMove(board, current_piece, next_piece);
            setVars(board);
            learn();
            return moveChoice;
        }
        else{
            moveChoice = getBestMove(board, current_piece);
            setVars(board);
            learn();
            return moveChoice;
        }
    }

    public TetrisMove chooseRandomMove(TetrisBoard board, TetrisPiece current_piece, TetrisPiece next_piece) {
        Random r = new Random();
        current_piece = current_piece.rotatePiece(r.nextInt(4));
        int col = r.nextInt(board.width-current_piece.width+1);
        return new TetrisMove(current_piece, col);
    }

    public void setVars(TetrisBoard board){
        prev_board = deepCopy(board);
        prev_state = c.readContour(prev_board);
        curr_board = deepCopy(board);
        curr_board.addPiece(moveChoice);
        double[] curr_state = c.readContour(curr_board);
        // for(int i = 0; i < prev_state.length; i++){
        //     System.out.print(curr_state[i] + "  ");
        // }
        // System.out.println("\n");
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

    public ArrayList<ArrayList<TetrisMove>> getAllPieceMoves(){
        ArrayList<ArrayList<TetrisMove>> allPMoves = new ArrayList<>();
        for(int i = 0; i < pieces.length; i++){
            allPMoves.add(getPieceMoves(pieces[i]));
        }
        return allPMoves;
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

}
