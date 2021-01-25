/**
 * @author Pierre-Louis Virey
 * 25 janv. 2021
 */
package fil.sr1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Pierre-Louis Virey
 * 25 janv. 2021
 */
public class FTPClientTest {
	
    @Test
    public void testGetFileNameReturnTheGoodResult() {
        
    	String line = "drwxr-xr-x   31 997      997          4096 Jan 25 18:47 cdimage"; //example of line we can expect.
    	
    	FTPClient ftp = new FTPClient();
    	String name = ftp.getFileName(line);
    	assertEquals(name,"cdimage");
    }
}
