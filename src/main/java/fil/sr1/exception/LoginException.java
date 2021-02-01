/**
 * @author Pierre-Louis Virey
 * 28 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey
 * 28 janv. 2021
 * Raise this exception when your login failed.
 */
public class LoginException extends Exception {
	public LoginException(String msg) {
		super("Failed to send to the user to FTP server. Server reply : " + msg);
	}
}
