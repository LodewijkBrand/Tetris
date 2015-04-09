/**
 * An Output object that holds the Output information for a specific individual in the NeuralNetwork
 * @author Lou Brand
 */

import java.util.ArrayList;

public class Output implements java.io.Serializable{
    ArrayList<Double> myOutputs;

    /**
    * Initializes an Output object with a list of Doubles
    * @param inputs The list of outputs that will make up a Neural layer
    */
    public Output(ArrayList<Double> outputs){
        myOutputs = outputs;
    }

    /**
    * Normalizes the output
    * @param min Minimum value in the dataset for a specific output data-point
    * @param max Maximum value in the dataset for a specific output data-point
    * @param index The index of the specific data-point
    */
    public void normalizeValue(double min, double max, int index){
        double range = max - min;
        double oldValue = myOutputs.get(index);
        double newValue = (oldValue - min)/range;
        myOutputs.set(index, newValue);
    }

    /**
    * Gets the list of Outputs
    * @return myOutputs The outputs
    */
    public ArrayList<Double> getOutputs(){
        return myOutputs;
    }

    /**
    * Gets the number of outputs in Output
    * @return The size
    */
    public int size(){
        return myOutputs.size();
    }
}
