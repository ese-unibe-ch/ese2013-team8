package ch.unibe.ese.shopnote.share;

import android.content.Context;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.share.requests.Request;

/**
 * Handles Request-answers from the server (failure, processing, etc.)
 *
 */
public class AnswerHandler {
	
	private SyncManager syncmanager;
	private Request[] answers;
	private RequestListener listener;
	private Context context;
	
	public AnswerHandler(RequestListener listener) {
		this.syncmanager = SyncManager.getInstance();
		this.listener = listener;
	}
	
	public void handle(Request... answers) {
		this.answers = answers;
		for(int c = 0; c<answers.length; c++) {
			if(answers[c].isHandled()) {
				listener.setHandled(c);
			} else {
				syncmanager.addRequest(answers[c]);
			}
			if(answers[c].wasSuccessful()) {
				listener.setSuccessful(c);
			}
		}
		listener.updateUI();
	}
	
}
