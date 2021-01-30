/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1;

import fil.sr1.exception.WrongArgumentsException;

/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
public class TreeCommandParser {
	private String args[];
	
	/**
	 * Default contrsuctor
	 * @param args, your raw command line args.
	 */
	public TreeCommandParser(String[] args) {
		this.args = args;
	}
	
	/**
	 * Return true if the argument contains a password.
	 */
	protected boolean checkArgPassword(String[] args) {
		return args[1].equals("-u") && args[3].equals("-p");
	}
	
	/**
	 * Choose the right way to tree depending on the options.
	 * @throws WrongArgumentsException 
	 */
	public void parseLaunchTree() throws WrongArgumentsException {
		if(args.length == 0) {
			throw new WrongArgumentsException();
		} else if (args.length == 1) {
			FTPClient ftp = new FTPClient(args[0], 21);
			ftp.tree("/");
		} else if (args.length > 4) {
			if (checkArgPassword(args)) { // Connexion with user and password.
				String user = args[2];
				String passwd = args[4];

				FTPClient ftp = new FTPClient(args[0], 21, user, passwd);

				if (args.length == 7) {
					if (args[5].equals("-d")) {
						ftp.tree(args[6]);
					} else { // On as un Level
						int level = Integer.parseInt(args[6]);
						ftp.tree(level);
					}
				} else if (args.length == 9) {
					int level = Integer.parseInt(args[8]);
					ftp.tree(args[6], level);
				} else {
					ftp.tree();
				}
			} else if (args.length == 5) {
				int level = Integer.parseInt(args[4]);
				FTPClient ftp = new FTPClient(args[0], 21);
				ftp.tree(args[2], level);
			} else {
				throw new WrongArgumentsException();
			}
		} else if (args.length == 3) {
			FTPClient ftp = new FTPClient(args[0], 21);
			if (args[1].equals("-d")) {
				ftp.tree(args[2]);
			} else if (args[1].equals("-L")) { // On as un Level
				int level = Integer.parseInt(args[2]);
				ftp.tree(level);
			} else {
				throw new WrongArgumentsException();
			}
		} else {
			throw new WrongArgumentsException();
		}
	}
}
