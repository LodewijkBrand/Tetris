/**
 * An Input object that holds the Input information for a specific individual in the NeuralNetwork
 * @author Lou Brand
 */

import java.util.ArrayList;

public class Input implements java.io.Serializable{
	private ArrayList<Double> myInputs;
    
	/**
	 * Initializes an Input object with a list of Doubles
	 * @param inputs The list of inputs that will make up a Neural layer
	 */
	public Input(ArrayList<Double> inputs){
		myInputs = inputs;
	}
    
	/**
	 * Normalizes the input
	 * @param min Minimum value in the dataset for a specific input data-point
	 * @param max Maximum value in the dataset for a specific input data-point
	 * @param index The index of the specific data-point
	 */
	public void normalizeValue(double min, double max, int index){
		double range = max - min;
		double oldValue = myInputs.get(index);
		double newValue = (oldValue - min)/range;
		myInputs.set(index, newValue);
	}
	
	/**
	 * Gets the list of Input data-points
	 * @return myInputs The data-points in this Input
	 */
	public ArrayList<Double> getInputs(){
		return myInputs;
	}
	
	/**
	 * Gets the number of inputs in the Input
	 * @return The size
	 */
	public int size(){
		return myInputs.size();
	}
}
