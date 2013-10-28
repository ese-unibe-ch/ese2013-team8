package ch.unibe.ese.share;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.widget.Toast;

public class RequestSender extends AsyncTask<Request, Object, Request>{

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public RequestSender() {}
	
	
	@Override
	protected Request doInBackground(Request... request) {
		this.initSocket();
		this.send(request);
		this.waitForAnswer();
		return null;
	}
	
	private void waitForAnswer() {
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			long start = System.nanoTime();  
			while(System.nanoTime() - start < 10000) {
				//wait
			}
			Request[] answer = (Request[]) in.readObject();
			System.err.println("Received Answer and it is " + answer[0].isHandled());
			
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


	/**
	 * Tries to open a socket on the android device
	 */
	private void initSocket() {
		try {
			this.socket = new Socket("10.0.0.2", 1337);
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
