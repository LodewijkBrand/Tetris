/**
 * An object that represents the HiddenLayer of Neurons in our NeuralNetwork
 * @author Lou Brand
 */

import java.util.ArrayList;

public class HiddenLayer implements java.io.Serializable{
    private ArrayList<Neuron> hiddenNeurons;

    /**
    * Initializes a given number of hidden Neurons in our HiddenLayer
    * @param numNeurons The number of neurons we want in our HiddenLayer
    */
    public HiddenLayer(int numNeurons){
        hiddenNeurons = new ArrayList<Neuron>();

        for (int i = 0; i < numNeurons; i++){
            hiddenNeurons.add(new Neuron(0.0));
        }
    }

    /**
    * Gets all the Neurons contained in this HiddenLayer
    */
    public ArrayList<Neuron> getNeurons(){
        return hiddenNeurons;
    }
}
