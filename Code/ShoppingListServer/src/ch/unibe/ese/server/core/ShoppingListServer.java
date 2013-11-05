package ch.unibe.ese.server.core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Driver for the shopping list server
 * It just waits for connections and refers them to new ClientThreads which can be handled in parallel
 * Note: conflicts can occur if you do it in parallel
 * @author Stephan
 *
 */
public class ShoppingListServer {

	/**
	 * Configuration
	 */
	public static boolean WIPE_DATABSE_ON_STARTUP = true;
	public static boolean REDIRECT_OUTPUT_TO_FILE = false;
	private static int PORT = 1337;
	/**
	 * \Configuration 
	 */
	
	private ServerSocket serverSocket;
	private RequestHandler requestHandler;
	
	public ShoppingListServer() {
		if(REDIRECT_OUTPUT_TO_FILE) {
			try {
				FileOutputStream f = new FileOutputStream("serverlog.txt");
				PrintStream pf = new PrintStream(f);
				System.setOut(pf);
				System.setErr(pf);
			} catch (FileNotFoundException e) {
				System.err.println("Failed to open serverlog.txt");
			}
		}
		this.requestHandler = new RequestHandler();
		this.initServerSocket();
		this.waitForConnection();
	}
	
	private void waitForConnection() {
		do {
			accept();
		} while (true);
	}


	private void accept() {
		try {
			// Get socket from client
			Socket socket = serverSocket.accept();
			// Start new thread to handle client request
			new ClientThread(socket,requestHandler).start();
		} catch (IOException e) {
			System.err.println("IOException in accept()");
			e.printStackTrace(System.err);
		}
	}

	private void initServerSocket() {
		try {
			this.serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.err.println("Failed to init socket on port " + PORT);
		}
	}
	
	public static void main(String[] args) {
		new ShoppingListServer();
	}
	
}
