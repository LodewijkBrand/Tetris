/**
 * An object that represents the OutputLayer of Neurons in our NeuralNetwork
 * @author Lou Brand
 */

import java.util.ArrayList;

public class OutputLayer implements java.io.Serializable{
    ArrayList<Double> expectedOutput;
    ArrayList<Neuron> outputNeurons;

    /**
    * Creates an OutputLayer given an expected Output object
    * @param The expected Output
    */
    public OutputLayer(Output output){
        outputNeurons = new ArrayList<Neuron>();
        expectedOutput = output.getOutputs();
        for (int i = 0; i < output.size(); i++){
            outputNeurons.add(new Neuron(0.0));
        }
    }

    /**
    * Gets all the Neurons in this OutputLayer
    * @return outputNeurons The Neurons in this layer
    */
    public ArrayList<Neuron> getNeurons(){
        return outputNeurons;
    }

    /**
    * Gets the expected output values
    * @return expectedOutput The expected output
    */
    public ArrayList<Double> getExpectedOutputs(){
        return expectedOutput;
    }

    /**
    * Resets the expected output values
    */
    public void setExpectedOutputs(Output newOutput){
        expectedOutput = newOutput.getOutputs();
    }

    /**
    * Gets the expected output located at a given index
    * @return The expected output at index i
    */
    public double getExpectedOutput(int i){
        return expectedOutput.get(i);
    }

    /**
    * Gets the Neuron located at the given index in the OutputLayer
    * @param i The index
    * @return The Neuron at index i
    */
    public Neuron get(int i){
        return outputNeurons.get(i);
    }

    /**
    * Gets the number of Neurons in this OutputLayer
    * @return The number of Neurons
    */
    public int size(){
        return outputNeurons.size();
    }
}
