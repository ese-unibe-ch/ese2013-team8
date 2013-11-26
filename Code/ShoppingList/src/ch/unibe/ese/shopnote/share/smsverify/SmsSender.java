package ch.unibe.ese.shopnote.share.smsverify;

import ch.unibe.ese.shopnote.activities.VerifyNumberActivity;
import android.os.AsyncTask;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;

public class SmsSender extends AsyncTask<Void, Void, Void> {

	private String phoneNumber;
	private VerifyNumberActivity context;
	
	public SmsSender(String phoneNumber, VerifyNumberActivity context) {
		this.phoneNumber = formatPhoneNumber(phoneNumber);
		this.context = context;
	}

	/**
	 * Generates and sends a token to your own phoneNumber for later verification
	 */
	private void sendToken() {
		TokenGenerator tokenGen = new TokenGenerator(context);
		String token = tokenGen.generateToken(phoneNumber);
		
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(this.phoneNumber, null, token, null, null);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Formats a String phonenumber
	 * @param phoneNumber
	 * @return
	 */
	private String formatPhoneNumber(String phoneNumber) {
		String formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber);
		return formattedNumber;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		sendToken();
		return null;
	}
	
	@Override
	protected void onPostExecute(Void a) {
		context.setSuccessful(false);
	}
	
	
}
