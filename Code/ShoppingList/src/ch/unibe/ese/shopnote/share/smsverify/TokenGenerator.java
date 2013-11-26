package ch.unibe.ese.shopnote.share.smsverify;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

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
		int token = 0;
		int deviceId = 0;
		int number = 0;
		if(deviceIdText.length() > 0) {
			try {
				deviceId = Integer.parseInt(deviceIdText);
				number = Integer.parseInt(phoneNumber);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			token = (deviceId%10000)+(number%10000);
		}
		return ""+token;
	}
	
	private String getPhoneNumberFromPreferences() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String phoneNumber = settings.getString("phonenumber", "0");
		return phoneNumber;
	}

	public void checkToken(String msgBody) {
		String phoneNumber = getPhoneNumberFromPreferences();
		String token = generateToken(phoneNumber);
		if(msgBody.equals(token)) {
			approvePhoneNumber();
			Toast.makeText(context, "Phone-number verified successfully", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Phone-number verification failed", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void approvePhoneNumber() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = settings.edit();
		edit.putBoolean("phonenumberapproved", true);
		edit.apply();
	}
	
}
