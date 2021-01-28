package fil.sr1;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class Main {
	
	/**
	 * Display the error message about the arguments.
	 */
	public static void incorrectUsage() {
		System.out.println("Incorrect options please refer to the following pattern :\n"
							+"java -jar TreeFtp.jar server_adress [[-u user] [-p password]] [-d directory] [-L level]");
		System.exit(1);
	}
	
	/**
	 * Return true if the argument contains a password.
	 */
	public static boolean checkArgPassword(String[] args) {
		return args[1].equals("-u") && args[3].equals("-p");
	}
	
	public static void print(String[] args) {
		for (String string : args) {
			System.out.print(string+" ");
		}
	}
	
	/**
	 * Choose the right way to tree depending on the options.
	 */
	public static void parseLaunchTree(String[] args) {
		if(args.length == 0) {
			incorrectUsage();
		} else if (args.length == 1) {
			//FTPClient ftp = new FTPClient(args[0],21);
			//ftp.tree("/");
			print(args);
		} else {
			if(args.length > 4) {
				if(checkArgPassword(args)) {	//Connexion with user and password.
					String user = args[2];
					String passwd = args[4];
					
					FTPClient ftp = new FTPClient(args[0],21,user,passwd);
	
					if(args.length == 7) {
						if(args[5].equals("-d")) {
							ftp.tree(args[6]);
						} else {	//On as un Level
							int level = Integer.parseInt(args[6]);
							ftp.tree(level);
						}
					} else if (args.length == 9) {
						int level = Integer.parseInt(args[8]);
						ftp.tree(args[6],level);
					} else {
						ftp.tree();
					}
				} else if(args.length == 5) {
					int level = Integer.parseInt(args[4]);
					FTPClient ftp = new FTPClient(args[0],21);
					ftp.tree(args[2],level);
				} else {
					incorrectUsage();
				}
			} else if(args.length == 3) {
				FTPClient ftp = new FTPClient(args[0],21);
				if(args[1].equals("-d")) {
					ftp.tree(args[2]);
				} else if(args[1].equals("-L")) {	//On as un Level
					int level = Integer.parseInt(args[2]);
					ftp.tree(level);
				} else {
					incorrectUsage();
				}
			}
		}
	}
	
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
		
		String[] fakeArg = {"vps-e0d81721.vps.ovh.net","-u","plv","-p","jaimelesgrosftp","-d","dir2","-L","1"};
		//String[] fakeArg = {"ftp.ubuntu.dzzd","-d","cdimage"};
		
		parseLaunchTree(fakeArg);
	}
}
