package ch.unibe.ese.shopnote.share.smsverify;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class TokenGenerator {

	private Context context;


	public TokenGenerator(Context context) {
		this.context = context;
	}
	
	/**
	 * Simple token generation. Can be improved / made more secure
	 */
	public String generateToken(String phoneNumber) {
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceIdText = telephonyManager.getDeviceId();
		
		String token = ""+(deviceIdText+phoneNumber).hashCode();
		
		return ""+token;
	}
	
	private String getPhoneNumberFromPreferences() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String phoneNumber = settings.getString("phonenumber", "0");
		return phoneNumber;
	}

	public boolean checkToken(String msgBody) {
		String phoneNumber = getPhoneNumberFromPreferences();
		String token = generateToken(phoneNumber);
		if(msgBody.equals(token)) {
			approvePhoneNumber();
		}
		return msgBody.equals(token);
	}
	
	private void approvePhoneNumber() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = settings.edit();
		edit.putBoolean("phonenumberapproved", true);
		edit.putLong("timeOfApprovement", System.currentTimeMillis());
		edit.apply();
	}
	
}
