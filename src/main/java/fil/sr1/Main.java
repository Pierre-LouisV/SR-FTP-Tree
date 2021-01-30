package fil.sr1;

import fil.sr1.exception.WrongArgumentsException;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class Main {
	
	/**
	 * Main class that uses the tree command parser to launch the tree command. 
	 */
	public static void main(String[] args) {
		/*
		FTPClient ftp = new FTPClient("ftp.ubuntu.com",21);
		//ftp.test_co();
		ftp.tree("/",1);
		*/
		
		/*
		FTPClient ftp = new FTPClient("vps-e0d81721.vps.ovh.net",21,"plv","jaimelesgrosftp");
		ftp.tree(2);
		*/
		
		//String[] fakeArg = {"vps-e0d81721.vps.ovh.net","-u","plv","-p","jaimelesgrosftp","-d","dir2","-L","1"};
		//String[] fakeArg = {"vps-e0d81721.vps.ovh.net","-u","plv","-p","jaimelesgrosftp","-L","10"};
		//String[] fakeArg = {"ftp.ubuntu.com","-L","1"};
		//String[] fakeArg = {"ftp.ubuntu.com","-d","a"};
		
		TreeCommandParser tcp = new TreeCommandParser(args);
		try {
			tcp.parseLaunchTree();
		} catch (WrongArgumentsException e) {
			throw new RuntimeException(e);
		}
	}
}
