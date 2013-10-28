package ch.unibe.ese.server.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import ch.unibe.ese.server.RequestHandler;
import ch.unibe.ese.share.Request;

public class ShoppingListServer {

	private ServerSocket serverSocket;
	private int port = 1337;
	private RequestHandler requestHandler;
	
	public ShoppingListServer() {
		this.requestHandler = new RequestHandler();
		this.initServerSocket();
		this.waitForConnection();
	}
	
	private void waitForConnection() {
		do {
			Request[] request = accept();
			System.out.println(request[0]);
			requestHandler.handle(request);
		} while (true);
	}


	private Request[] accept() {
		ObjectInputStream in;
		Request[] request = null;
		try {
			// Get socket from client
			Socket socket = serverSocket.accept();
			// Open inputstream from socket
			in = new ObjectInputStream(socket.getInputStream());
			// Get the request
			request = (Request[]) in.readObject();
			// Set the request handled
			request[0].setHandled();
			// Open outputstream from socket
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			// Send answer to client
			out.writeObject(request);
			out.flush();
		} catch (IOException e) {
			System.err.println("IOException in accept()");
			e.printStackTrace(System.err);
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException in accept()");
			e.printStackTrace(System.err);
		}
		return request;
	}

	private void initServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.port );
		} catch (IOException e) {
			System.err.println("Failed to init socket on port " + this.port);
		}
	}
	
	public static void main(String[] args) {
		ShoppingListServer server = new ShoppingListServer();
	}
	
}
