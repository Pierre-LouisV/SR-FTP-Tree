package fil.sr1;

import fil.sr1.exception.WrongArgumentsException;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class Main {
	
	/**
	 * Main class that uses the tree command parser to launch the tree command. 
	 */
	public static void main(String[] args) {
		TreeCommandParser tcp = new TreeCommandParser(args);
		try {
			tcp.parseLaunchTree();
		} catch (WrongArgumentsException e) {
			throw new RuntimeException(e);
		}
	}
}
