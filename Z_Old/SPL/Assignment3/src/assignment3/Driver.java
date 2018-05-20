package assignment3;

import java.io.IOException;
import java.util.logging.Logger;

import com.vogella.logger.MyLogger;


/**
 * runs the file.
 */
public class Driver {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Print this as a string.
     * @return
     */
    public String toString() {
        return "The Driver Class";
    }
    
    public static void main(String[] args) {


    	try {
    		MyLogger.setup();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
		LOGGER.info("begining parsing");
		Management management = Parser.createManagement(args[0], args[2], args[1], args[3]);
		management.startSimulation();

    }
}

