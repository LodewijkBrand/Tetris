/**
 * An object that represents the InputLayer of Neurons in our NeuralNetwork
 * @author Lou Brand
 */

import java.util.ArrayList;

public class InputLayer implements java.io.Serializable{
    private ArrayList<Neuron> inputNeurons;

    /**
    * Creates an InputLayer of Neurons given an Input
    * @param inputValues The Input
    */
    public InputLayer(Input inputValues){
        inputNeurons = new ArrayList<Neuron>();

        for (Double currentValue : inputValues.getInputs()){
            inputNeurons.add(new Neuron(currentValue));
        }
        Neuron bias = new Neuron(-1.0);
        bias.makeBias();
        inputNeurons.add(bias);
    }

    /**
    * Gets the Neurons that make up this layer
    * @param inputNeurons The Neurons that make up this layer
    */
    public ArrayList<Neuron> getNeurons(){
        return inputNeurons;
    }
}
