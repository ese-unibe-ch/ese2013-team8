package ch.unibe.ese.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import ch.unibe.ese.share.Request;

public class ShoppingListServer {

	private ServerSocket serverSocket;
	private Socket socket;
	
	public ShoppingListServer() {
		this.initServerSocket();
		this.waitForConnection();
	}
	
	private void waitForConnection() {
		do {
			this.accept();

			ObjectInputStream in;
			Request request;
			try {
				in = new ObjectInputStream(socket.getInputStream());
				request = (Request) in.readObject();
				System.out.print(request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}

	private void accept() {
		try {
			this.socket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initServerSocket() {
		try {
			this.serverSocket = new ServerSocket(1234);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ShoppingListServer server = new ShoppingListServer();
	}
}
