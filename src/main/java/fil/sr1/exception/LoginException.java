/**
 * @author Pierre-Louis Virey
 * 28 janv. 2021
 */
package fil.sr1.exception;

/**
 * @author Pierre-Louis Virey
 * 28 janv. 2021
 */
public class LoginException extends Exception {
	public LoginException(String msg) {
		super("Could not login to the FTP server : " + msg);
	}
}
