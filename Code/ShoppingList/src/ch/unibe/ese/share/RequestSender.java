package ch.unibe.ese.share;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import ch.unibe.ese.core.BaseActivity;
import ch.unibe.ese.share.requests.Request;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Is responsible to establish a connection socket to the server (hardcoded IP at the moment)
 * Every request is followed by an answer from the server IN THE SAME ORDER. 
 * The server passes the same object back with flags 'isHandled' and 'wasSuccessful' set
 *
 */
public class RequestSender extends AsyncTask<Request, Object, Boolean>{
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private RequestListener listener;
	
	public RequestSender(RequestListener listener) {
		this.listener = listener;
	}
	
	/**
	 * This method gets started as asynchronous task when you call .run()
	 */
	@Override
	protected Boolean doInBackground(Request... requests) {
		this.initSocket();
		this.send(requests);
		this.waitForAnswer();
		return true;
	}
	
	/**
	 * Waits for the answer from the server and reports the result in the listener
	 */
	private void waitForAnswer() {
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			Request[] answers = (Request[]) in.readObject();
			AnswerHandler aHandler = new AnswerHandler(listener);
			aHandler.handle(answers);
			socket.close();
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
	private void initSocket() {
		try {
			this.socket = new Socket("matter2.nine.ch", 1337);
//			this.socket = new Socket("10.0.0.2", 1337); // Enter your local ip here
		} catch (UnknownHostException e) {
			System.err.println("Unknown Host in initSocket()");
		} catch (IOException e) {
			System.err.println("ERROR in initSocket");
		}
	}
	
	/**
	 * Tries to send a request to the server
	 * @param request
	 */
	
	public void send(Request... request) {
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(request);
			out.flush();
		} catch (IOException e) {
			// TODO
		}
	}

}
