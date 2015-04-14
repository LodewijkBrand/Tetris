/**
 * This TDNetwork class creates a temporal difference neural network that will be used to train a bot to
 * play Tetris.
 * The pseudo code and concepts for this neural network came from: http://webdocs.cs.ualberta.ca/~sutton/td-backprop-pseudo-code.text
 * @author Lou Brand
 * @author Alan Yeung
 */
import java.lang.Math;
import java.util.Arrays;

public class TDNetwork {
    int    n, num_hidden, m; /* number of inputs, hidden, and output units */
    int    time_steps;       /* number of time steps to simulate */
    double BIAS;             /* strength of the bias (constant input) contribution */
    double ALPHA;            /* 1st layer learning rate (typically 1/n) */
    double BETA;             /* 2nd layer learning rate (typically 1/num_hidden) */
    double GAMMA;            /* discount-rate parameter (typically 0.9) */
    double LAMBDA;           /* trace decay parameter (should be <= gamma) */

    /* Network Data Structure: */

    double[]   x;            /* input data (units) */
    double[]   h;            /* hidden layer */
    double[]   y;            /* output layer */

    double[][] w;            /* edge weights from the hidden layer to output layer */
    double[][] mw;           /* momentum for w */
    double[][] pdw;          /* previous change in w */

    double[][] v;            /* edge weights from the input layer to hidden layer */
    double[][] mv;           /* momentum for v */
    double[][] pdv;          /* previous change in v */

    double[]  old_y;         /* old output layer */
    double[][][]  ev;        /* hidden trace */
    double[][]  ew;          /* output trace */
    double r;                /* reward */
    double[]  error;         /* error */

    /**
     * Initializes the TDNetwork
     * @param _n Number of input features
     * @param _num_hidden Number of hidden nodes
     * @param _m Number of output nodes
     * @param _BIAS The value for biased nodes
     * @param _ALPHA Learning rate for weights from input to hidden layer
     * @param _BETA Learing rate for weights from hidden to output layer
     * @param _GAMMA Future move discount
     * @param _LAMBDA Trace decay parameter
     */
    public TDNetwork(int _n, int _num_hidden, int _m,  double _BIAS, double _ALPHA, double _BETA, double _GAMMA, double _LAMBDA){
        /* Initialize all of our structural variables */
	n = _n;
        num_hidden = _num_hidden;
        m = _m;
        BIAS = _BIAS;
        ALPHA = _ALPHA;
        BETA = _BETA;
        GAMMA = _GAMMA;
        LAMBDA = _LAMBDA;
        
	/* Initialize all of our neural network arrays*/
        x = new double[n+1];
        h = new double[num_hidden+1];
        y = new double[m];

        w = new double[num_hidden+1][m];
        mw = new double[num_hidden+1][m];
        pdw = new double[num_hidden+1][m];

        v = new double[n+1][num_hidden+1];
        mv = new double[n+1][num_hidden+1];
        pdv = new double[n+1][num_hidden+1];

        old_y = new double[m];
        ev = new double[n+1][num_hidden+1][m];
        ew = new double[num_hidden+1][m];
        error = new double[m];
            
        initNetwork();
        response(); /* Just compute old response (old_y)... */

        for (int k = 0; k < m; k++) {
            old_y[k] = y[k];
        }

        updateElig(); /* ...and prepare the eligibilities */
    }

    /**
     * Feed forward an input vector of features (no learning involved)
     * @param features The current input vector
     * @return y The output vector
     */
    public double[] feedForward(double[] features){
	for (int i = 0; i < features.length; i++){
            x[i] = features[i];
	}
	response(); //Feed the current feature forward (NO LEARNING)
	return y; //Return current output
    }
    
    /**
     * Feed forward input vector of features and learn!
     * @param features The current input vector
     * @param _reward The current reward
     * @return y The output vector
     */
    int count = 0;
    double errorSum = 0;
    public void printAvg() {
        if (count % 100000 == 0) {
            System.out.println(errorSum/count);
            errorSum = 0;
            count = 0;
        }
    }
    
    public double[] timeStep(double[] features, double _reward){
        r = _reward;

        for (int i = 0; i < features.length; i++){
            x[i] = features[i];
        }
        response(); /* forward pass - compute activities */
        
	/* For each output node calculate the error */
        for (int k = 0; k < m; k++) {
            error[k] = r + GAMMA * y[k] - old_y[k]; /* form errors */
            errorSum += Math.abs(error[k]);
        }
        
        tdLearn(); /* backward pass - learning */
        response(); /* forward pass must be done twice to form TD errors */
        for (int k = 0; k < m; k++) {
            old_y[k] = y[k]; /* for use in next cycle's TD errors */
        }
        updateElig(); /* update eligibility traces */
        count++;
        printAvg();
        return y;
    }

    /**
     * Initialize the TDNetwork with random weights between nodes
     */
    public void initNetwork() {
        int s,j,k,i;
        //Set up the bias input node as the last node for each time step
        x[n] = BIAS;
        //The last hidden node should be set to the bias
        h[num_hidden]=BIAS;
        for (j=0;j<=num_hidden;j++) {
            for (k=0;k<m;k++) {
                //Make the random number between -1 and 1 for all weights of the hidden layers
                w[j][k]=(Math.random() * 2 - 1);
                mw[j][k]=1.0;  //Default momentum just 1.0
                pdw[j][k]=0.0; //No change in w yet
                ew[j][k]=0.0;
                old_y[k]=0.0;
            }
            for (i=0; i<=n; i++) {
                //Make the random number between -1 and 1 for all weights of the hidden layers
                v[i][j]=(Math.random() * 2 - 1);
                mv[i][j]=1.0;  //Default momentum is 1.0;
                pdv[i][j]=0.0; //No change in v yet
                for (k=0;k<m;k++) {
                    //For each input node, for each hidden node, for each output node, fill the ev array with 0.0
                    ev[i][j][k]=0.0;
                }
            }
        }
    }

    /**
     * Calculate the response of the neural network after the input vector has been set
     */
    public void response() {
        int i,j,k;
        //Set the last hidden nodes bias
        h[num_hidden]=BIAS;
        //Set the last input nodes bias
        x[n]=BIAS;
        for (j=0;j<num_hidden;j++) {
            //Reset the hidden node values to 0.0
            h[j]=0.0;
            //For each input node
            for (i=0;i<=n;i++) {
                //Calculate the value of that node to be the input * the input weight
                h[j]+=x[i]*v[i][j];
            }
            //Turn the output of each hidden layer node into the sigmoid of the input * the input weight
            h[j]=1.0/(1.0+ Math.exp(-h[j])); /* asymmetric sigmoid */
        }
        //For each output node
        for (k=0;k<m;k++) {
            //Reset the output layer values to 0.0
            y[k] = 0.0;
            //For each hidden node
            for (j = 0; j <= num_hidden; j++) {
                //Calculate the value of the output node to be the hidden node output * the hidden layer weight
                y[k] += h[j] * w[j][k];
            }
            //Turn the ouput of each node in the ouput layer to be the sigmoid of the hidden node output * the hidden layer weight
            y[k] = 1.0 / (1.0 + Math.exp(-y[k])); /* asymmetric sigmoid (OPTIONAL) */
        }
    }

    /**
     * Updates the weight vectors based on backpropagation and the eligibility traces
     */
    public void tdLearn() {
        int i, j, k;
        // For each output layer node
        for (k = 0; k < m; k++) {
            //For each hidden layer node
            for (j = 0; j <= num_hidden; j++) {
                //Update the weight as BETA * error at ouput * output trace for hidden node j and output node k
                double dw = BETA * error[k] * ew[j][k];
                w[j][k] += applyMomentum(mw, dw, pdw, j, k);
                //For each input node
                for (i = 0; i <= n; i++) {
                    //Update the weight as ALPHA * error at input * hidden trace for input node i, hidden node j, and output node k
                    double dv = ALPHA * error[k] * ev[i][j][k];
                    v[i][j] += applyMomentum(mv, dv, pdv, i, j);
                }
            }
        }
    }

    /**
     * Applies momentum to a change in weight
     * @param m Momentum array
     * @param dw The change in weight (before momentum applied)
     * @param pdw Previous change in weight
     * @param i, j Current indices
     * @return The applied momentum
     */
    public double applyMomentum(double[][] m, double delta, double[][] pd, int i, int j){
        if ((delta > 0 && pd[i][j] > 0) | (delta < 0 && pd[i][j] < 0)){
            m[i][j] *= 1.05; //Aggressive cat!
            //System.out.println("APPLYING MOMENTUM!!!! \ndw: " + delta + "\npdw: " + pd[i][j]);
        } else{
            m[i][j] *= .95; //Slow down!
            //System.out.println("SLOW DOWN CAT!!!!! \ndw: " + delta + "\npdw: " + pd[i][j]);
        }
        pd[i][j] = delta;
        return delta * m[i][j]; //Apply momentum
        //return delta;
    }

    /**
     * Update the input and hidden traces
     */
    public void updateElig() {
        int i, j, k;
        double[] temp = new double[m];
        // For each output node
        for (k = 0; k < m; k++) {
            // Set temp at output node to be output of node k * 1-output of node k
            //The derivative of the sigmoid
            temp[k] = y[k] * (1 - y[k]);
        }
        //For each hidden node
        for (j = 0; j <= num_hidden; j++) {
            //For each output node
            for (k = 0; k < m; k++) {
                //Change the output trace at hidden node j and output node k to be LAMBDA * old output trace at that location + the temp of that
                //output node * the hidden node (j) output
                //Basically LAMBDA is how much previous value should impact current trace, where temp[k] * h[j] is the current trace value (similar to the delta)
                ew[j][k] = LAMBDA * ew[j][k] + temp[k] * h[j];
                for (i = 0; i <= n; i++) {
                    //Set the hidden trace with some affect of the old hidden trace, and the current trace
                    ev[i][j][k] = LAMBDA * ev[i][j][k] + temp[k] * w[j][k] * h[j] * (1 - h[j]) * x[i];
                }
            }
        }
    }
}	

/**
 * A Note From the Pseudocode Author:
 *
 * Although the detailed behavior of this algorithm is complicated, an intuitive understanding
 * is possible. The algorithm uses a prediction of a later quantity, Pt(xt+1) , to update a 
 * prediction of an earlier quantity, Pt(xt) , where each prediction is computed via the same
 * prediction function, Pt. This may seem like a futile process since neither prediction need
 * be accurate, but in the course of the algorithm's operation, later predictions tend to
 * become accurate sooner than earlier ones, so there tends to be an overall error reduction
 * as learning proceeds. This depends on the learning system receiving an input sequence with
 * sufficient regularity to make predicting possible. In formal treatments, the inputs xt 
 * represent states of a Markov chain and the y values are given by a function of these states.
 * This makes it possible to form accurate predictions of the expected values of the discounted
 * sums Yt.
*/
