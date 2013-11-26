package ch.unibe.ese.shopnote.share;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Is responsible to establish a connection socket to the server (hardcoded IP at the moment)
 * Every request is followed by an answer from the server IN THE SAME ORDER. 
 * The server passes the same object back with flags 'isHandled' and 'wasSuccessful' set
 *
 */
public class RequestSender extends AsyncTask<Request, Void, Boolean>{
	
	// Server data
	private String host = "matter.nine.ch";
	private int port = 1337;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private AnswerHandler handler;
	private Request[] answers;
	
	public RequestSender(AnswerHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * This method gets started as asynchronous task when you call .run()
	 * @return 
	 */
	@Override
	protected Boolean doInBackground(Request... requests) {
		boolean isConnected = this.initSocket();
		if(isConnected) {
			this.send(requests);
			this.waitForAnswer();
		} else {
			handler.setRequests(requests);
		}
		return isConnected;
	}
	
	@Override
	protected void onPostExecute(Boolean a) {
		handler.updateUI();
    }
	
	/**
	 * Waits for the answer from the server and reports the result in the listener
	 */
	private void waitForAnswer() {
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			answers = (Request[]) in.readObject();
			socket.close();
			handler.setRequests(answers);
		} catch (StreamCorruptedException e) {
			System.err.println("Failed to open stream from server");
		} catch (IOException e) {
			System.err.println("Failed to read answers from server");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to read class from server");
		}
	}

	/**
	 * Tries to open a socket on the android device to a specified Host
	 */
	private boolean initSocket() {
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			socket = new Socket();
			socket.connect(sockaddr, 5000);
			return true;
		} catch (UnknownHostException e) {
			System.err.println("Unknown Host in initSocket()");
		} catch (IOException e) {
			System.err.println("Connection timed out");
		}
		return false;
	}
	
	/**
	 * Tries to send a request to the server
	 * @param request
	 */
	
	public void send(Request... request) {
		if(socket != null) {
			try {
				this.out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(request);
				out.flush();
			} catch (IOException e) {
				// TODO
			}
		}
	}

}
