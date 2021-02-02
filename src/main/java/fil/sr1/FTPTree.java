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
public class FTPTree implements FTPClient {

	public static final boolean TALK = false;			//Display the server messages if true
	public static final int MAX_SUB_DIRECTORY = 6400;	//The max number of sub-directory to list
	private Socket skt;
	private BufferedReader in;
	private BufferedWriter out;

	/**
	 * Blank constructor, to call other method by yourself.
	 */
	public FTPTree() {}

	/**
	 * Constructor for anonymous connection.
	 */
	public FTPTree(String adress, int port) {
		this(adress, port, "anonymous", "guest");
	}

	/**
	 * Constructor for logged connection
	 */
	public FTPTree(String adress, int port, String user, String passwd) {
		try {
			connectLogin(adress, port, user, passwd);
		} catch (ConnexionException | LoginException | PasswordException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Connects anonymously to the ftp server with the adress.
	 * @throws ConnexionException 
	 * @throws PasswordException 
	 * @throws LoginException 
	 * @throws ConnectException 
	 */
	public void connectAnon(String adress, int port) throws ConnexionException, LoginException, PasswordException {
		connectLogin(adress, port, "anonymous", "guest");
	}

	/**
	 * Connects anon to the ftp server with the adress
	 * @throws ConnexionException 
	 * @throws LoginException 
	 * @throws PasswordException 
	 */
	public void connectLogin(String adress, int port, String user, String passwd) throws ConnexionException, LoginException, PasswordException {
		try {
			skt = new Socket(adress, port);		//Connection au socket du serveur.

			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));		//Création du flux d'entrée.
			out = new BufferedWriter(new OutputStreamWriter(skt.getOutputStream()));	//Creation du flux de sortie.
		} catch (Exception e) {
			throw new ConnexionException(e.getMessage());
		}

		String reponse = read();

		write("USER " + user);		//Renseignement du nom d'utilisateur : user
		reponse = read();
		if(!reponse.startsWith("331")) {
			throw new LoginException(reponse);
		}
		
		write("PASS " + passwd);	//Renseignement du mot de passe.
		reponse = read();
		if(!reponse.startsWith("230")) {
			throw new PasswordException(reponse);
		}
	}

	/**
	 * Execute ls on the current folder of the FTP server.
	 * 
	 * @return the raw result of the command.
	 * @throws IOException
	 * @throws PASVException 
	 */
	protected ArrayList<String> ls(String directory) throws IOException, PASVException {
		// Connection en mode passif
		write("PASV");
		String reponse = read();

		if(reponse == null || !reponse.startsWith("227")) {		//Si la reponse est null, il y as un problème à cause du dossier précèdent. 
			throw new PASVException(reponse);
		}
		
		// On parse la réponse. On as comme format (IP1,IP2,IP3,IP4,PORT1,PORT2).
		String[] rep = reponse.split("\\(");
		String[] repSplited = rep[1].split(",");
		String ipAdress = repSplited[0] + "." + repSplited[1] + "." + repSplited[2] + "." + repSplited[3];
		if (TALK) {
			System.out.println(ipAdress);
		}
		// Calcul du port à utiliser pour la connection.
		int port = Integer.parseInt(repSplited[4]) * 256 + Integer.parseInt(repSplited[5].replace(")", ""));
		if (TALK) {
			System.out.println(port);
		}

		//Liste du dossier directory.
		write("LIST " + directory);
		// Le nouveau socket pour lire les data.
		Socket sktData = new Socket(ipAdress, port);
		//Lecture de la réponse du serveur.
		read();
		
		//Création du socket permettant la connection.
		BufferedReader inData = new BufferedReader(new InputStreamReader(sktData.getInputStream()));

		String reponse2 = inData.readLine();
		ArrayList<String> lsResult = new ArrayList<String>();
		
		//Lecture de toutes les fichiers du dossier.
		while (reponse2 != null) {
			lsResult.add(reponse2);
			reponse2 = inData.readLine();
		}
		read();

		return lsResult;
	}

	/**
	 * Return true if the file is a directory.
	 */
	protected boolean isDirectory(String file) {
		return file.startsWith("d");
	}
	
	/**
	 * Return the file name from an lsLine.
	 */
	protected String getFileName(String lsLine) {
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
		if (level <= 0)
			return;

		try {
			ArrayList<String> lsReponse = ls(directory);	//Récupération de la commande ls sur le repo.
			String tab = getTab(level, baseLevel);			//Niveau d'indentation dans la console.
			for (int i = 0; i < lsReponse.size(); i++) {
				String file = lsReponse.get(i);
				String fileName = getFileName(file);
				
				if (i != lsReponse.size() - 1) {	//Choix de l'affichage pour l'estétique.
					System.out.println(tab + "├──" + fileName);
				} else {
					System.out.println(tab + "└──" + fileName);
				}
				if (isDirectory(file)) {	//Execution de la commande sur le noeud s'il s'agit d'un dossier.
					tree(directory + "/" + fileName, level - 1, baseLevel);
				}
			}
		} catch (IOException | PASVException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Writes the string and send it to the server
	 */
	protected void write(String str) {
		try {
			out.write(str + "\r\n");
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Read the string and send it to the server
	 */
	protected String read() {
		String reponse = "pb_read";
		try {
			reponse = in.readLine();
			if (TALK)
				System.out.println(reponse);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return reponse;
	}

}
