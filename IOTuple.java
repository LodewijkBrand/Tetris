/**
 * A class that conveniently holds a pair of Input and Output objects
 * @author Lou Brand
 */

public class IOTuple implements java.io.Serializable{
	private Input myInput;
	private Output myOutput;
	
    /**
     * Creates an IOTuple object with an Input and an Output
     * @param input The Input
     * @param output The Output
     */
	public IOTuple (Input input, Output output){
		myInput = input;
		myOutput = output;
	}
	
    /**
     * Gets the Input of this IOTuple object
     * @return myInput This Input
     */
	public Input getInput(){
		return myInput;
	}
	
    /**
     * Gets the Output of this IOTuple object
     * @return myOutput This Output
     */
	public Output getOutput(){
		return myOutput;
	}
}
