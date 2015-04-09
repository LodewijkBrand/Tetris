import java.util.*;

public class LinearRegression {

    double[] theta;
    double alpha = 0.05;
    double dFactor = 0.4;
    Random r;
    
    
    //before I did batch learning, but this will be iterative learning
    public LinearRegression(int vector_size) {
	theta = new double[vector_size];
	r = new Random();
	for(int i=0;i<vector_size;i++) {
	    theta[i] = r.nextInt(10);
	}
    }

    public double predictedValue(double[] state) {
	double total = theta[0];
	for(int i=1;i<data.length;i++) {
	    total = theta[1]*data[i-1];
	}
	return total;
    }
    //NOT DONE
    /*public double slopeCalc(double[] data, int index) {
	//we need some value for the next move so we can aim for it
	double returnable = alpha*(predictedValue(data) - "value of move")*data[index-1];
	return returnable;
    }
    //NOT DONE
    public double interceptCalc(double[] data) {
	double returnable = alpha*(predictedValue(data) - "value of move");
	return returnable;
	}*/

    public double getReward() {
	return 0.;
    }

    public double getBestQ() {
	for(move m : moves[state[state.length - 1]]) {
	    //copy state and do move
	    
	}
	return 0.;
    }

    public void learn(double[] state) {
	//do move that was previously chosen
	double reward = getReward();
	double gamma = reward + dFactor * getBestQ(state) - predictedValue(state);
	for(int i=1;i<theta.length;i++) {
	    theta[i] += alpha * gamma * state[i];
	}
    }
}
