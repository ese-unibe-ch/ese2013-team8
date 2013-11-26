package ch.unibe.ese.shopnote.share.smsverify;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;

public class SmsSender {

	private String phoneNumber;
	private Context context;
	
	public SmsSender(String phoneNumber, Context context) {
		this.phoneNumber = phoneNumber;
		this.context = context;
	}

	/**
	 * Generates and sends a token to your own phoneNumber for later verification
	 */
	public void sendToken() {
		TokenGenerator tokenGen = new TokenGenerator(context);
		String token = tokenGen.generateToken(phoneNumber);
		
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(this.phoneNumber, null, token, null, null);
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


	
	
}
