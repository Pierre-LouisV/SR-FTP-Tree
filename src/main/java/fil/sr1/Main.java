package fil.sr1;

import java.net.*;
import java.io.*;

public class Main {

	/**
	 * Convert the string adress in an InetAddress.
	 */
	public static InetAddress getServerAdress(String adress) {
		InetAddress adrServeur = null;

		try {
			adrServeur = InetAddress.getByName(adress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return adrServeur;
	}

	/**
	 * Connect to the FTP server
	 */
	public static void connect(String adress) {
		Socket skt;
		int port = 21;	//The listening port.

		try {
			skt = new Socket(adress, port);

			BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

			String reponse = in.readLine();
			
			if(reponse.startsWith("220")) {
				System.out.println("Service disponible pour nouvel utilisateur");
			} else {
				System.out.println(reponse);
			}
			
			//ER <SP> <nom d'utilisateur> <CRLF>
			out.write("USER anonymous\r\n");
			out.flush();
			reponse = in.readLine();
			System.out.println(reponse);
			
			//PASSS <SP> <mot de passe> <CRLF>
			out.write("PASS guest\r\n");
			out.flush();
			reponse = in.readLine();
			System.out.println(reponse);
			
			//PWD <CRLF>
			out.write("PWD\r\n");
			out.flush();
			reponse = in.readLine();
			System.out.println("La réponse :"+reponse);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		//System.out.println(getServerAdress("ftp.ubuntu.com").getHostAddress());
		connect("ftp.ubuntu.com");
	}
}
