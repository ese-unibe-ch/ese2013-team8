package ch.unibe.ese.share;

import ch.unibe.ese.core.BaseActivity;

/**
 * Gives the possibility to print a Toast from an asynchronous task
 * Invoke like this:
 * 		BaseActivity activity = (BaseActivity) context;
		activity.runOnUiThread(new ToastMaker(request.toString(), activity));
		
 * @author Stephan
 *
 */
public class ToastMaker implements Runnable {

	private String text;
	private BaseActivity activity;
	
	public ToastMaker(String text, BaseActivity activity) {
		this.text = text;
		this.activity = activity;
	}

	@Override
	public void run() {
		activity.showToast(text);
	}

}
