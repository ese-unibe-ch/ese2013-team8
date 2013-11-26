package ch.unibe.ese.shopnote.share.smsverify;

import ch.unibe.ese.shopnote.core.BaseActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver{

	private Context context;
	
	public SmsReceiver(Context context) {
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
						verifyToken(msgBody);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void verifyToken(String msgBody) {
		TokenGenerator tokenGen = new TokenGenerator(context);
		tokenGen.checkToken(msgBody);
		((BaseActivity) context).finish();
	}
}
