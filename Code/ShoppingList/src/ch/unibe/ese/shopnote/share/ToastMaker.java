package ch.unibe.ese.shopnote.share;

import ch.unibe.ese.shopnote.core.BaseActivity;

/**
 * Gives the possibility to print a Toast from an asynchronous task
 * Use it like this:
 * <code>
 * BaseActivity activity = (BaseActivity) context;
 * activity.runOnUiThread(new ToastMaker(request.toString(), activity));
 * </code>	
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
