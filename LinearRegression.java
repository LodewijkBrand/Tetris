import java.util.*;

public class LinearRegression {

    double[] theta;
    double alpha = 0.05;
    double dFactor = 0.4;
    Random r;
    Contour c;
    
    
    //before I did batch learning, but this will be iterative learning
    public LinearRegression(int vector_size) {
	theta = new double[vector_size];
	r = new Random();
	for(int i=0;i<vector_size;i++) {
	    theta[i] = r.nextInt(10);
	}
	c = new Contour(vector_size - 1);
    }

    public double predictedValue(double[] state) {
	double total = theta[0];
	for(int i=1;i<data.length;i++) {
	    total = theta[i]*state[i-1];
	}
	return total;
    }

    public double getReward() {
	return 0.;
    }
    
    public double getValue(TetrisBoard b) {
        //evaluate the value of a board state
    }

    public double getBestQ(TetrisBoard t, TetrisPiece p) {
        ArrayList<TetrisBoard> futureBoards = getFutureBoards(t, p);
        double highestQ = 0;
	for(TetrisBoard b : futureBoards) {
	    c.readBoard(t);
            if (predictedValue(b) > highestQ) {
                highestQ = predictedValue();
            }
	    
	}
        return highestQ;
    }
    
    public ArrayList<TetrisBoard> getFutureBoards(TetrisBoard t, TetrisPiece _p) {
        ArrayList<TetrisBoard> futureBoards = new ArrayList<TetrisBoard>();
        for (int i=0;i<4;i++) {
            for (int j=0;j<t.width;j++) {
               TetrisPiece p = _p.rotatePiece(i);
               TetrisMove m = new TetrisMove(p, j);
               TetrisBoard b = deepCopy(t);
               b.addPiece(m);
               futureBoards.add(b);
            }
        }
        return futureBoards;
            
    }

    public void learn(double[] state) {
	//do move that was previously chosen
	double reward = getReward();
	double gamma = reward + dFactor * getBestQ(state) - predictedValue(state);
	for(int i=1;i<theta.length;i++) {
	    theta[i] += alpha * gamma * state[i];
	}
    }

    public TetrisBoard deepCopy(TetrisBoard _t) {
        TetrisBoard t = new TetrisBoard(_t.width, _t.height, false);
        t.blocksPlaced = _t.blocksPlaced;
        for (int r=0;r<_t.board.length;r++) {
            for (int c=0;c<_t.board[0].length;c++) {
                t.board[r][c] = _t.board[r][c];
            }
        }
        t.linesEliminated = _t.linesEliminated;
        //not messing with _t.tbv
        return t;
    }
}
