/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1;

import fil.sr1.exception.ConnexionException;
import fil.sr1.exception.LoginException;
import fil.sr1.exception.PasswordException;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
public interface FTPClient {
	
	/**
	 * Connects anonymously to the ftp server with the adress.
	 * @throws PasswordException 
	 * @throws LoginException 
	 * @throws ConnexionException 
	 */
	public void connectAnon(String adress, int port) throws ConnexionException, LoginException, PasswordException;

	/**
	 * Connects anon to the ftp server with the adress
	 * @throws PasswordException 
	 * @throws LoginException 
	 * @throws ConnexionException 
	 */
	public void connectLogin(String adress, int port, String user, String passwd) throws ConnexionException, LoginException, PasswordException;
	
}
