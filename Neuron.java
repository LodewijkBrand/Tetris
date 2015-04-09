/**
 * An object that represents a Neuron. The basic building-block of a Neural Network
 * @author Lou Brand
 */

import java.util.ArrayList;

public class Neuron implements java.io.Serializable{
    private ArrayList<Synapse> inputs;
    private ArrayList<Synapse> outputs;
    private double myValue;
    private boolean biased;

    /**
    * Basic constructor that generates a Neuron object with an initial value
    * @param value The initial value of this Neuron
    */
    public Neuron(double value){
        inputs = new ArrayList<Synapse>();
        outputs = new ArrayList<Synapse>();
        setValue(value);
        biased = false;
    }

    /**
    * Adds incoming Synapse object (edge) to this Neuron that connects it to another Neuron
    * @param newInput The new edge
    */
    public void addInput(Synapse newInput){
        if (newInput.getDestination() == this && !biased){
            inputs.add(newInput);
        } else {throw new IllegalArgumentException("The Synapse Destination Doesn't Match or This Neuron is BIASED!");}
    }

    /**
    * Creates on outgoing Synapse object (edge) from this Neuron to another Neuron
    * @param newOutput The new edge
    */
    public void addOutput(Synapse newOutput){
        if (newOutput.getOrigin() == this){
            outputs.add(newOutput);
        } else {throw new IllegalArgumentException("The Synapse Origin Doesn't Match!");}
    }

    /**
    * Gets the list of Synapses (edges) that point to this Neuron
    * @return inputs The list of edges pointing to this Neuron (can be an empty ArrayList<Synapse> if this Neuron is biased)
    */
    public ArrayList<Synapse> getInputSynapses(){
        return inputs;
    }

    /**
    * Gets the Synapse (edge) that goes out of this Neuron
    * @return output The outgoing Syanpse
    */
    public ArrayList<Synapse> getOutputSynapses(){
        return outputs;
    }

    /**
    * Gets the current value of this Neuron
    * @return myValue Value of this Neuron
    */
    public double getValue(){
        return myValue;
    }

    /**
    * Resets the value of this Neuron
    * @param newValue The new value of this Neuron
    */
    public void setValue(double newValue){
        if (biased){
            throw new IllegalArgumentException("This Neuron is BIASED and its value cannot be changed!.");
        }

        if (newValue <= 1.0 && newValue >= -1.0){
            myValue = newValue;
        } else {throw new IllegalArgumentException("The value of this Neuron (" + newValue + ") is NOT valid.");}
    }

    /**
    * Makes this Neuron baised
    */
    public void makeBias(){
        inputs = new ArrayList<Synapse>();
        biased = true;
    }

    /**
    * Checks whether this Neuron is biased
    * @return biased Is this neuron biased?
    */
    public boolean isBiased(){
        return biased;
    }
}
