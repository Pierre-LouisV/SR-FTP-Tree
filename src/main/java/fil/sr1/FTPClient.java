package fil.sr1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import fil.sr1.exception.*;

/**
 * @author Pierre-Louis Virey 19 janv. 2021
 */
public class FTPClient {

	public static final boolean TALK = false;			//Display the server messages if true
	public static final int MAX_SUB_DIRECTORY = 6400;	//The max number of sub-directory to list
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
		try {
			connectAnon(adress, port);
		} catch (ConnexionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Constructor for logged connection
	 */
	public FTPClient(String adress, int port, String user, String passwd) {
		try {
			connectLogin(adress, port, user, passwd);
		} catch (ConnexionException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Connects anon to the ftp server with the adress
	 * @throws ConnexionException 
	 * @throws ConnectException 
	 */
	public void connectAnon(String adress, int port) throws ConnexionException {
		try {
			skt = new Socket(adress, port);

			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

			String reponse = in.readLine();
			if (TALK) {
				System.out.println(reponse);
			}

			write("USER anonymous");
			read();

			write("PASS guest");
			read();
			//TODO AJOUTER gestion de mauvaise connection avec mauvais login /mdp.
		} catch (Exception e) {
			throw new ConnexionException(e.getMessage());
		}
	}

	/**
	 * Connects anon to the ftp server with the adress
	 * @throws ConnexionException 
	 */
	public void connectLogin(String adress, int port, String user, String passwd) throws ConnexionException {
		try {
			skt = new Socket(adress, port);

			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));

			String reponse = in.readLine();
			if (TALK) {
				System.out.println(reponse);
			}

			write("USER " + user);
			read();

			write("PASS " + passwd);
			read();

		} catch (Exception e) {
			throw new ConnexionException(e.getMessage());
		}
	}

	/**
	 * Execute ls on the current folder of the FTP server.
	 * 
	 * @return the raw result of the command.
	 * @throws IOException
	 */
	public ArrayList<String> ls(String directory) throws IOException {
		// Connection passive
		write("PASV");
		String reponse = read();

		// On parse la réponse. On as comme format (IP1,IP2,IP3,IP4,PORT1,PORT2).
		String[] rep = reponse.split("\\(");
		String[] repSplited = rep[1].split(",");
		String ipAdress = repSplited[0] + "." + repSplited[1] + "." + repSplited[2] + "." + repSplited[3];
		if (TALK) {
			System.out.println(ipAdress);
		}

		int port = Integer.parseInt(repSplited[4]) * 256 + Integer.parseInt(repSplited[5].replace(")", ""));
		if (TALK) {
			System.out.println(port);
		}

		// LIST [<SP> <chemin d'accès>] <CRLF> Ici on liste le répertoire courant.
		write("LIST " + directory);

		Socket sktData = new Socket(ipAdress, port);

		read();

		BufferedReader inData = new BufferedReader(new InputStreamReader(sktData.getInputStream()));

		String reponse2 = inData.readLine();
		ArrayList<String> lsResult = new ArrayList<String>();

		while (reponse2 != null) {
			lsResult.add(reponse2);
			reponse2 = inData.readLine();
		}

		read();

		// Ne change pas grand chose car normalement c'est fermé.
		// inData.close();
		// sktData.close();

		return lsResult;
	}

	/**
	 * Parse the ls command
	 * 
	 * @return the array with the name inside.
	 */
	public String[] parseLs(String lsRes) {
		return null;
	}

	public void test_co() {
		String reponse = "";

		try {

			// PWD <CRLF>
			write("PWD");
			read();

			ArrayList<String> lsReponse = ls("/");
			for (String file : lsReponse) {
				System.out.println(file);
			}

			// On lis le resultat de la commande ls
			// read();

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
			// read();

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
		return splited[splited.length - 1];
	}
	
	/**
	 * Return the good number of tab.
	 */
	public String getTab(int level, int baseLevel) {
		StringBuilder sb = new StringBuilder();
		int nbTab = baseLevel - level;

		for (int i = 0; i < nbTab; i++) {
			sb.append("\t");
		}

		return sb.toString();
	}
	
	/**
	 * Execute the tree function on the FTP server on the root folder.
	 */
	public void tree() {
		System.out.println(".");
		tree("/", MAX_SUB_DIRECTORY, MAX_SUB_DIRECTORY);
	}
	
	/**
	 * Execute the tree function on the FTP server.
	 * @param directory the directory to list.
	 */
	public void tree(String directory) {
		System.out.println(".");
		tree(directory, MAX_SUB_DIRECTORY, MAX_SUB_DIRECTORY);
	}
	
	/**
	 * Execute the tree function on the FTP server on the root folder.
	 * @param the max level of subdirectory to display.
	 */
	public void tree(int level) {
		System.out.println(".");
		tree("/", level, level);
	}
	
	/**
	 * Execute the tree function on the FTP server.
	 * @param directory the directory to list.
	 * @param the max level of subdirectory to display.
	 */
	public void tree(String directory, int level) {
		System.out.println(".");
		tree(directory, level, level);
	}

	/**
	 * Real implementation, execute the tree function on the FTP server.
	 * @param directory the directory to list.
	 * @param the max level of subdirectory to display.
	 * @param baseLevel the firstLevel used to print with good tabulation
	 */
	protected void tree(String directory, int level, int baseLevel) {

		// Non execution de la fonction si le niveau n'est est de 0.
		if (level <= 0) {
			return;
		}

		try {
			ArrayList<String> lsReponse = ls(directory);
			for (int i = 0; i < lsReponse.size(); i++) {
				String file = lsReponse.get(i);
				String fileName = getFileName(file);

				String tab = getTab(level, baseLevel);

				if (isDirectory(file)) {
					if (i != lsReponse.size() - 1) {
						System.out.println(tab + "├──" + fileName);
					} else {
						System.out.println(tab + "└──" + fileName);
					}
					tree(directory + "/" + fileName, level - 1, baseLevel);
				} else {
					if (i != lsReponse.size() - 1) {
						System.out.println(tab + "├──" + fileName);
					} else {
						System.out.println(tab + "└──" + fileName);
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
			out.write(str + "\r\n");
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
			if (TALK)
				System.out.println(reponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reponse;
	}

}
