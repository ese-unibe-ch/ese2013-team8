package ch.unibe.ese.share;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import ch.unibe.ese.core.BaseActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

/**
 * Is responsible to establish a connection socket to the server (hardcoded IP at the moment)
 * Every request is followed by an answer from the server. It passes the same object back with flags 'isHandled' and 'wasSuccessful' set
 * @author Stephan
 *
 */
public class RequestSender extends AsyncTask<Request, Object, Boolean>{
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private RequestListener listener;
	private Context context;
	
	public RequestSender(Context context, RequestListener listener) {
		this.listener = listener;
		this.context = context;
	}
	
	/**
	 * This method gets started as asynchronous task when you call .run()
	 */
	@Override
	protected Boolean doInBackground(Request... request) {
		this.initSocket();
		this.send(request);
		this.waitForAnswer();
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		//TODO
	}
	
	/**
	 * Waits for the answer from the server and reports the result in the listener
	 */
	private void waitForAnswer() {
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			Request[] answer = (Request[]) in.readObject();
			if(answer[0].isHandled()) {
				listener.setHandled();
			}
			if(answer[0].wasSuccessful()) {
				listener.setSuccessful();
			}
			socket.close();
			showToastOnActivity(answer[0]);
			
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void showToastOnActivity(Request request) {
		BaseActivity activity = (BaseActivity) context;
		activity.runOnUiThread(new ToastMaker(request.toString(), activity));
	}

	/**
	 * Tries to open a socket on the android device to a specified Host
	 */
	private void initSocket() {
		try {
			this.socket = new Socket("matter2.nine.ch", 1337);
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
