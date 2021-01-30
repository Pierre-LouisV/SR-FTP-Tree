/**
 * @author Pierre-Louis Virey
 * 25 janv. 2021
 */
package fil.sr1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fil.sr1.exception.ConnexionException;
import fil.sr1.exception.LoginException;
import fil.sr1.exception.PasswordException;

/**
 * @author Pierre-Louis Virey
 * 25 janv. 2021
 */
public class FTPClientTest {
	
	private FTPClient ftp;
	
	@Before
	public void init() {
		ftp = new FTPClient();
    }
	
    @Test
    public void testGetFileNameReturnTheGoodResult() {
    	String line = "drwxr-xr-x   31 997      997          4096 Jan 25 18:47 cdimage"; //example of line we can expect.
    	
    	String name = ftp.getFileName(line);
    	assertEquals(name,"cdimage");
    }
    
    @Test
    public void testIsDirectoryReturnTrueWhenDirectory() {
    	String directory = "drwxr-xr-x   31 997      997          4096 Jan 25 18:47 cdimage"; //example of directory we can expect.
    	assertTrue(ftp.isDirectory(directory));
    }
    
    @Test
    public void testIsDirectoryReturnFalseWhenFile() {
    	String directory = "-rw-r--r-- 1 pl pl  131 janv. 26 13:03 README.md"; //example of file we can expect.
    	assertFalse(ftp.isDirectory(directory));
    }
    
    @Test(expected=PasswordException.class)
    public void testLoginShouldFailWhenGivenWrongPassword() throws ConnexionException, LoginException, PasswordException {
    	//Test stupide car on ne fais pas un mock du serveur et il peux s'agit d'un faute du serveur néenmoins je souhatais tester une exception.
    	ftp.connectLogin("vps-e0d81721.vps.ovh.net", 21, "bizarre", null);
    }
}
