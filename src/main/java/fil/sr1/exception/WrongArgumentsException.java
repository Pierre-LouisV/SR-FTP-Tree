/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
public class WrongArgumentsException extends Exception{
	public WrongArgumentsException() {
		super("Incorrect options please refer to the following pattern :\n"
				+"java -jar TreeFtp.jar server_adress [[-u user] [-p password]] [-d directory] [-L level]");
	}
}
