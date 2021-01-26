/**
 * @author Pierre-Louis Virey
 * 26 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey 26 janv. 2021
 */
public class ConnexionException extends Exception { // ou RuntimeException

	public ConnexionException(String msg) {
		super("Could not connect to the FTP server.\n" + msg);
	}
}
