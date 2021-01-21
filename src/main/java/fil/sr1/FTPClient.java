package fil.sr1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class FTPClient {
	
	private static final boolean TALK = false;
	private Socket skt;
	private BufferedReader in;
	private BufferedWriter out;
	
	/**
	 * Constructor for anonymous connection
	 */
	public FTPClient(String adress, int port) {
		connectAnon(adress, port);
	}
	
	/**
	 * Connects anon to the ftp server with the adress
	 */
	public void connectAnon(String adress, int port) {
		try {
			skt = new Socket(adress, port);

			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

			String reponse = in.readLine();
			if(TALK) {System.out.println(reponse);}
			
			//ER <SP> <nom d'utilisateur> <CRLF>
			write("USER anonymous");
			read();
			
			//PASSS <SP> <mot de passe> <CRLF>
			write("PASS guest");
			read();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute ls on the current folder of the FTP server.
	 * @return the raw result of the command.
	 * @throws IOException 
	 */
	public String ls() throws IOException {
		//Connection passive
		write("PASV");
		String reponse = read();
		
		//On parse la réponse. On as comme format (IP1,IP2,IP3,IP4,PORT1,PORT2).
		String[] rep = reponse.split("\\(");
		String[] repSplited = rep[1].split(",");
		String ipAdress = repSplited[0] +"."+ repSplited[1] +"."+ repSplited[2] +"."+ repSplited[3]; 
		if(TALK) {System.out.println(ipAdress);}
		
		int port = Integer.parseInt(repSplited[4]) * 256 + Integer.parseInt(repSplited[5].replace(")",""));
		if(TALK) {System.out.println(port);}
		
		//LIST [<SP> <chemin d'accès>] <CRLF> Ici on liste le répertoire courant.
		write("LIST ");
		
		Socket sktData = new Socket(ipAdress, port);
		
		read();
		
		BufferedReader inData = new BufferedReader(new InputStreamReader(sktData.getInputStream()));
		
		String reponse2 = inData.readLine();
		StringBuilder sb = new StringBuilder();
		
		while(reponse2!=null) {
			sb.append(reponse2).append("\n");
			reponse2 = inData.readLine();
		}
		
		//Ne change pas grand chose car normalement c'est fermé.
		//inData.close();
		//sktData.close();
		
		return sb.toString();
	}
	
	/**
	 * Parse the ls command
	 * @return the array with the name inside.
	 */
	public String[] parseLs(String lsRes) {
		return null;
	}
	
	public void tree() {
		String reponse = "";
		
		try {
			
			//PWD <CRLF>
			write("PWD");
			read();
			
			String lsReponse = ls();
			
			//System.out.println(lsReponse);
			
			//On lis le resultat de la commande ls
			read();
			
			write("PWD");
			read();
			
			write("CWD cdimage");
			read();
			
			write("PWD");
			read();
			
			lsReponse = ls();
			System.out.println(lsReponse);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("------");
	}
	
	/**
	 * Writes the string and send it to the server
	 */
	public void write(String str) {
		try {
			out.write(str+"\r\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the string and send it to the server
	 */
	public String read() {
		String reponse = "pb_read";
		try {
			reponse = in.readLine();
			if(TALK)
				System.out.println(reponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reponse;
	}
	
	
	
}
