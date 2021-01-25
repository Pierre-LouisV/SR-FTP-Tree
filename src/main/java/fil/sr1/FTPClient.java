package fil.sr1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Pierre-Louis Virey
 * 19 janv. 2021
 */
public class FTPClient {
	
	private static final boolean TALK = true;
	private Socket skt;
	private BufferedReader in;
	private BufferedWriter out;
	
	/**
	 * Constructor for test
	 */
	public FTPClient() {

	}
	
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
	public ArrayList<String> ls(String directory) throws IOException {
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
		write("LIST "+directory);
		
		Socket sktData = new Socket(ipAdress, port);
		
		read();
		
		BufferedReader inData = new BufferedReader(new InputStreamReader(sktData.getInputStream()));
		
		String reponse2 = inData.readLine();
		ArrayList<String> lsResult = new ArrayList<String>();
		
		while(reponse2!=null) {
			lsResult.add(reponse2);
			reponse2 = inData.readLine();
		}
		
		read();
		
		//Ne change pas grand chose car normalement c'est fermé.
		//inData.close();
		//sktData.close();
		
		return lsResult;
	}
	
	/**
	 * Parse the ls command
	 * @return the array with the name inside.
	 */
	public String[] parseLs(String lsRes) {
		return null;
	}
	
	public void test_co() {
		String reponse = "";
		
		try {
			
			//PWD <CRLF>
			write("PWD");
			read();
			
			ArrayList<String> lsReponse = ls("/");
			for (String file : lsReponse) {
				System.out.println(file);
			}
			
			//On lis le resultat de la commande ls
			//read();
			
			write("PWD");
			read();
			
			write("CWD cdimage");
			read();
			
			write("PWD");
			read();
			
			lsReponse = ls("/cdimage/");
			for (String file : lsReponse) {
				System.out.println(file);
			}
			//read();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("------");
	}
	
	/**
	 * Return true if the file is a directory.
	 */
	public boolean isDirectory(String file) {
		return file.startsWith("d");
	}
	
	public String getFileName(String lsLine) {
		String[] splited = lsLine.split(" ");
		return splited[splited.length-1];
	}
	
	public void tree(String directory, int level) {
		System.out.println(".");
		tree(directory,level,level);
	}
	
	/**
	 * Return the good number of tab.
	 */
	public String getTab(int level, int baseLevel) {
		StringBuilder sb = new StringBuilder();
		int nbTab = baseLevel-level;
		
		for (int i=0; i<nbTab; i++) {
			sb.append("\t");
		}
		
		return sb.toString();
	}
	
	protected void tree(String directory, int level, int baseLevel) {
	
		//Non execution de la fonction si le niveau n'est est de 0.
		if(level == 0) {return;}
		
		try {
			ArrayList<String> lsReponse = ls(directory);
			for (int i=0; i<lsReponse.size(); i++) {
				String file = lsReponse.get(i);
				String fileName = getFileName(file);
				
				String tab = getTab(level, baseLevel);
				
				if(isDirectory(file)) {
					if(i!=lsReponse.size()-1) {
						System.out.println(tab+"├──"+fileName);
					} else {
						System.out.println(tab+"└──"+fileName);
					}
					tree(directory+fileName,level-1,baseLevel);
				} else {
					if(i!=lsReponse.size()-1) {
						System.out.println(tab+"├──"+fileName);
					} else {
						System.out.println(tab+"└──"+fileName);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
