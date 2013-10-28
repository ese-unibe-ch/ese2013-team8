package ch.unibe.ese.share;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestSender{

	private Request request;
	private Socket socket;
	private ObjectOutputStream out;
	
	public RequestSender(Request request) {
		this.request = request;
		initSocket();
		initOutputStream();
	}
	
	private void initOutputStream() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Tries to open a socket on the android device
	 */
	private void initSocket() {
		try {
			this.socket = new Socket("localhost", 1234);
		} catch (UnknownHostException e) {
			System.err.println("Host unknown");
		} catch (IOException e) {
			System.err.println("ERROR in initSocket");
		}
	}
	
	/**
	 * Tries to send a request to the server
	 * @param request
	 */
	public void send(Request request) {
		try {
			out.writeObject(request);
		} catch (IOException e) {
			//TODO
		}
		try {
			out.flush();
		} catch (IOException e) {
			//TODO
		}
	}
	
	public void send() {
		try {
			out.writeObject(this.request);
		} catch (IOException e) {
			//TODO
		}
		try {
			out.flush();
		} catch (IOException e) {
			//TODO
		}
	}
	
}
