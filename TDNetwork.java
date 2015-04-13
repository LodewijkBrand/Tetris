/*
 * The pseudo code and concepts for this neural network came from: http://webdocs.cs.ualberta.ca/~sutton/td-backprop-pseudo-code.text
*/
import java.lang.Math;
import java.util.Arrays;

public class TDNetwork {
    /* Experimental Parameters: */
    //TODO: Set these parameters..., perhaps mark next pieces (1-7) and use them as input to the network? Or this is probably already done by finding the possible contours of the next state...
    //TODO: Save the network... (perhaps just the weight array), perhaps add an input for average line height
    int    n, num_hidden, m; /* number of inputs, hidden, and output units */
    int    time_steps;  /* number of time steps to simulate */
    double  BIAS;   /* strength of the bias (constant input) contribution */
    double  ALPHA;  /* 1st layer learning rate (typically 1/n) */
    double  BETA;   /* 2nd layer learning rate (typically 1/num_hidden) */
    double  GAMMA;  /* discount-rate parameter (typically 0.9) */
    double  LAMBDA; /* trace decay parameter (should be <= gamma) */

    /* Network Data Structure: */

    double[] x; /* input data (units) */
    double[]  h;
    double[]  y;
    double[][]  w;
    double[][]  v;

    double[]  old_y;
    double[][][]  ev;
    double[][]  ew; 
    double r;
    double[]  error;

    public TDNetwork(int _n, int _num_hidden, int _m,  double _BIAS, double _ALPHA, double _BETA, double _GAMMA, double _LAMBDA){
        n = _n;
        num_hidden = _num_hidden;
        m = _m;
        BIAS = _BIAS;
        ALPHA = _ALPHA;
        BETA = _BETA;
        GAMMA = _GAMMA;
        LAMBDA = _LAMBDA;
        
        x = new double[n+1]; /* input data (units) */
        h = new double[num_hidden+1]; /* hidden layer */
        y = new double[m]; /* output layer */
        w = new double[num_hidden+1][m]; /* weights for the hidden layer*/
        v = new double[n+1][num_hidden+1]; /* weights for the input layer*/
        old_y = new double[m];
        ev = new double[n+1][num_hidden+1][m]; /* hidden trace */
        ew = new double[num_hidden+1][m]; /* output trace */
        error = new double[m];  /* TD error */
            
        initNetwork();
        response(); /* Just compute old response (old_y)... */

	int k;
        for (k = 0; k < m; k++) {
            old_y[k] = y[k];
        }

        updateElig(); /* ...and prepare the eligibilities */
    }

    public double[] feedForward(double[] features){
	for (int i = 0; i < features.length; i++){
            x[i] = features[i];
        }
	response(); //Feed the current feature forward (NO LEARNING)
	return y; //Return current output
    }

    public double timeStep(double[] features, double _reward){
        int k;
        double qValue = 0.0;
        r = _reward;
        //System.out.println(Arrays.toString(features));
        for (int i = 0; i < features.length; i++){
            x[i] = features[i];
        }
        response(); /* forward pass - compute activities */
        //For each output node
        for (k = 0; k < m; k++) {
            //Calculate the error as: the reward + GAMMA * output - old output (error is 0 if Gamma*y[k] + r == old_y[k])
            error[k] = r + GAMMA * y[k] - old_y[k]; /* form errors */
        }
        tdLearn(); /* backward pass - learning */
        response(); /* forward pass must be done twice to form TD errors */
        for (k = 0; k < m; k++) {
            old_y[k] = y[k]; /* for use in next cycle's TD errors */
            //FIX THIS LATER, there may be more than one output node
            qValue = y[k];
        }
        updateElig(); /* update eligibility traces */
        //System.out.println(Arrays.deepToString(w));
        return qValue;
    }

    public void initNetwork() {
        int s,j,k,i;
        //Set up the bias input node as the last node for each time step
        x[n] = BIAS;
        //The last hidden node should be set to the bias
        h[num_hidden]=BIAS;
        for (j=0;j<=num_hidden;j++) {
            for (k=0;k<m;k++) {
                //Make the random number between -1 and 1 for all weights of the hidden layers
                w[j][k]= (Math.random() * 2 - 1);
                ew[j][k]=0.0;
                old_y[k]=0.0;
            }
            for (i=0; i<=n; i++) {
                //Make the random number between -1 and 1 for all weights of the hidden layers
                v[i][j]= (Math.random() * 2 - 1);
                for (k=0;k<m;k++) {
                    //For each input node, for each hidden node, for each output node, fill the ev array with 0.0
                    ev[i][j][k]=0.0;
                }
            }
        }
    }
    /*****
     * Response()
     *
     * Compute hidden layer and output predictions
     *
     *****/

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

    /*****
     * TDlearn()
     *
     * Update weight vectors
     *
     *****/

    public void tdLearn() {
        int i, j, k;
        // For each output layer node
        for (k = 0; k < m; k++) {
            //For each hidden layer node
            //System.out.println("PRINTING WEIGHT CHANGE");
            for (j = 0; j <= num_hidden; j++) {
                //Update the weight as BETA * error at ouput * output trace for hidden node j and output node k
                w[j][k] += BETA * error[k] * ew[j][k];
                //System.out.println(BETA * error[k] * ew[j][k]);
                //System.out.println(error[k]);
                //For each input node
                for (i = 0; i <= n; i++) {
                    //Update the weight as ALPHA * error at input * hidden trace for input node i, hidden node j, and output node k
                    v[i][j] += ALPHA * error[k] * ev[i][j][k];
                }
            }
        }
    }

    /*****
     * UpdateElig()
     *
     * Calculate new weight eligibilities
     *
     *****/

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
    //TODO: Set up a function that populates the x array with inputs each iteration of the game

}	

/*
  Although the detailed behavior of this algorithm is complicated, an intuitive understanding is possible. 
  The algorithm uses a prediction of a later quantity, Pt(xt+1) , to update a prediction of an earlier quantity, Pt(xt) , where each prediction is computed via the same prediction function, Pt . 
  This may seem like a futile process since neither prediction need be accurate, but in the course of the algorithm's operation, later predictions tend to become accurate sooner than earlier ones,
  so there tends to be an overall error reduction as learning proceeds. This depends on the learning system receiving an input sequence with sufficient regularity to make predicting possible. 
  In formal treatments, the inputs xt represent states of a Markov chain and the y values are given by a function of these states. This makes it possible to form accurate predictions of the expected 
  values of the discounted sums Yt .


 */
