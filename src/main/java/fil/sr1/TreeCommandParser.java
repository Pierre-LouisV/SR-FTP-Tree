/**
 * @author Pierre-Louis Virey
 * 30 janv. 2021
 */
package fil.sr1;

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
	 * Display the error message about the arguments.
	 */
	private void incorrectUsage() {
		System.out.println("Incorrect options please refer to the following pattern :\n"
							+"java -jar TreeFtp.jar server_adress [[-u user] [-p password]] [-d directory] [-L level]");
		System.exit(1);
	}
	
	/**
	 * Return true if the argument contains a password.
	 */
	private boolean checkArgPassword(String[] args) {
		return args[1].equals("-u") && args[3].equals("-p");
	}
	
	/**
	 * Choose the right way to tree depending on the options.
	 */
	public void parseLaunchTree() {
		if(args.length == 0) {
			incorrectUsage();
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
				incorrectUsage();
			}
		} else if (args.length == 3) {
			FTPClient ftp = new FTPClient(args[0], 21);
			if (args[1].equals("-d")) {
				ftp.tree(args[2]);
			} else if (args[1].equals("-L")) { // On as un Level
				int level = Integer.parseInt(args[2]);
				ftp.tree(level);
			} else {
				incorrectUsage();
			}
		} else {
			incorrectUsage();
		}
	}
}
