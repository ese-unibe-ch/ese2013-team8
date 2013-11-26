package ch.unibe.ese.shopnote.share.smsverify;

import ch.unibe.ese.shopnote.activities.VerifyNumberActivity;
import ch.unibe.ese.shopnote.core.BaseActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver{

	private VerifyNumberActivity context;
	
	public SmsReceiver(VerifyNumberActivity context) {
		this.context = context;
	}

	@Override
	public void onReceive(Context notUsed, Intent intent) {
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			if (bundle != null) {
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						String msgBody = msgs[i].getMessageBody();
						if(verifyToken(msgBody)) {
							abortBroadcast();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private boolean verifyToken(String msgBody) {
		TokenGenerator tokenGen = new TokenGenerator(context);
		boolean wasSuccessful = tokenGen.checkToken(msgBody);
		if(wasSuccessful) {
			context.setSuccessful(true);
			context.finish();
		}
		return wasSuccessful;
	}
}
