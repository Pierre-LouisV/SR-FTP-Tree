package fil.sr1;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class Main {

	public static void main(String[] args) {
		/*
		FTPClient ftp = new FTPClient("ftp.ubuntu.com",21);
		//ftp.test_co();
		ftp.tree("/",1);
		*/
		
		FTPClient ftp = new FTPClient("vps-e0d81721.vps.ovh.net",21,"plv","jaimelesgrosftp");
		ftp.tree("/",3);
		
	}
}
