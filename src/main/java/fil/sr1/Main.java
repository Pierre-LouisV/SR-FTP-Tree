package fil.sr1;

import java.net.*;
import java.io.*;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class Main {

	public static void main(String[] args) {
		//System.out.println(getServerAdress("ftp.ubuntu.com").getHostAddress());
		//connect("ftp.ubuntu.com");
		FTPClient ftp = new FTPClient("ftp.ubuntu.com",21);
		//ftp.test_co();
		ftp.tree("/",1);
	}
}
