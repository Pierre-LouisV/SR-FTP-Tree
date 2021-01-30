/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1;

import org.junit.Test;
import fil.sr1.TreeCommandParser;
import fil.sr1.exception.WrongArgumentsException;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
public class TreeCommandParserTest {
	public static final String FAKE_FTP_NAME = "some.server.sr"; 
	
	@Test(expected=WrongArgumentsException.class)
    public void testParseLaunchTreeWithWrongSyntax() throws WrongArgumentsException {
    	String[] args = {FAKE_FTP_NAME,"-d"};
		
    	TreeCommandParser tcp = new TreeCommandParser(args);
    	tcp.parseLaunchTree();
    }
}
