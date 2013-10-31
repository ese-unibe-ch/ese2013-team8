package ch.unibe.ese.server.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import ch.unibe.ese.share.requests.Request;

/**
 * This is the thread which gets created for each new user that connects to the server
 * @author Stephan
 *
 */
public class ClientThread extends Thread {

	private Socket socket;
	private RequestHandler requestHandler;
	
	
	public ClientThread(Socket socket, RequestHandler requestHandler) {
		this.socket = socket;
		this.requestHandler = requestHandler;
	}
	
	/**
	 * This method does the magic when a user is connected to the socket
	 */
	public void run() {
		System.out.println("\n" + new Date() +"\n\t"+ "Client Connected from " + socket.getInetAddress() + ":" + socket.getPort());
		ObjectInputStream in;
		Request[] request = null;
		// Open inputstream from socket
		try {
			in = new ObjectInputStream(socket.getInputStream());
			// Get the request
			request = (Request[]) in.readObject();
			// Handle the request and set flags 'isHandled' and 'wasSuccessfull'
			requestHandler.handle(request);
			// Open outputstream from socket
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			// Send answer to client
			out.writeObject(request);
			out.flush();
			System.out.println("\tClosed Socket from " + socket.getInetAddress() + ":" + socket.getPort());
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
	}
}
