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
	private String host = "10.0.0.2";
	private int port = 1337;
	
	private Socket socket;
	private AnswerHandler handler;
	
	public RequestSender(AnswerHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * This method gets started as asynchronous task when you call .run()
	 * @return 
	 */
	@Override
	protected Boolean doInBackground(Request... requests) {
		return sendAndReceive(requests);
	}

	private synchronized boolean sendAndReceive(Request... requests) {
		boolean isConnected = this.initSocket();
		boolean hasSent = false;
		boolean hasReceived = false;
		if(isConnected) {
			hasSent = this.send(requests);
			hasReceived = this.waitForAnswer();
		}
		if (!isConnected || !hasReceived || !hasSent) {
			handler.setRequests(requests);
		}
		return isConnected;
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
	
	public boolean send(Request... request) {
		if(socket != null) {
			try {
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(request);
				out.flush();
				return true;
			} catch (IOException e) {
				System.err.println("Couldn't write to socket in RequestSender");
			}
		}
		return false;
	}
	
	/**
	 * Waits for the answer from the server and reports the result in the listener
	 */
	private boolean waitForAnswer() {
		try {
			socket.setSoTimeout(5000);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Request [] answers = (Request[]) in.readObject();
			socket.close();
			handler.setRequests(answers);
			return true;
		} catch (StreamCorruptedException e) {
			System.err.println("Failed to open stream from server");
		} catch (IOException e) {
			System.err.println("Failed to read answers from server");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to read class from server");
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean successful) {
		handler.updateUI(successful);
    }

}
