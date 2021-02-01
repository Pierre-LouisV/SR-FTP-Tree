/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 * Raise this exception when the PASV connexion failed.
 */
public class PASVException extends Exception {
	public PASVException(String msg) {
		super("Failed to open a PASV connection. Server reply : " + msg);
	}
}
