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
			System.out.println(reponse);
			
			//ER <SP> <nom d'utilisateur> <CRLF>
			write("USER anonymous");
			reponse = in.readLine();
			System.out.println(reponse);
			
			//PASSS <SP> <mot de passe> <CRLF>
			write("PASS guest");
			reponse = in.readLine();
			System.out.println(reponse);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tree() {
		String reponse = "";
		
		try {
			
			//PWD <CRLF>
			write("PWD");
			reponse = in.readLine();
			System.out.println(reponse);
			
			//Connection passive
			write("PASV");
			reponse = in.readLine();
			System.out.println(reponse);
				
			//On parse la réponse. On as comme format (IP1,IP2,IP3,IP4,PORT1,PORT2).
			String[] rep = reponse.split("\\(");
			
			String[] repSplited = rep[1].split(",");
			
			String ipAdress = repSplited[0] +"."+ repSplited[1] +"."+ repSplited[2] +"."+ repSplited[3]; 
			
			System.out.println(ipAdress);
			
			int port = Integer.parseInt(repSplited[4]) * 256 + Integer.parseInt(repSplited[5].replace(")",""));
			
			System.out.println(port);
			
			//LIST [<SP> <chemin d'accès>] <CRLF>
			write("LIST /");
			
			Socket sktData = new Socket(ipAdress, port);
			
			reponse = in.readLine();
			System.out.println(reponse);
			
			BufferedReader inData = new BufferedReader(new InputStreamReader(sktData.getInputStream()));
			
			String reponse2 = inData.readLine();
			StringBuilder sb = new StringBuilder();
			
			while(reponse2!=null) {
				sb.append(reponse2).append("\n");
				reponse2 = inData.readLine();
			}
			
			String lsReponse = sb.toString();
			
			//Ne change pas grand chose.
			//inData.close();
			//sktData.close();
			
			System.out.println(lsReponse);
			
			write("PWD");
			reponse = in.readLine();
			System.out.println(reponse);
			
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
	
	
	
}
