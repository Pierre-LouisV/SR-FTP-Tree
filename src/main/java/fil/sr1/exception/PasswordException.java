/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 * Raise this exception when your password isn't working.
 */
public class PasswordException extends Exception {
	public PasswordException(String msg) {
		super("Failed to login to the FTP server with the password. Server reply : " + msg);
	}
}
