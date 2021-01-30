/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
public class PASVException extends Exception {
	public PASVException(String msg) {
		super("Failed to open a PASV connection. Server reply : " + msg);
	}
}
