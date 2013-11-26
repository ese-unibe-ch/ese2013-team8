package ch.unibe.ese.shopnote.activities;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import ch.unibe.ese.shopnote.R;
import ch.unibe.ese.shopnote.core.BaseActivity;
import ch.unibe.ese.shopnote.share.smsverify.SmsReceiver;
import ch.unibe.ese.shopnote.share.smsverify.SmsSender;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyNumberActivity extends BaseActivity {

	private static final String ACTION="android.provider.Telephony.SMS_RECEIVED";
	private static boolean finished;
	private SmsReceiver receiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_number);
		
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        filter.setPriority(10);
        this.receiver = new SmsReceiver(this);
        registerReceiver(this.receiver, filter);
		
        finished = false;
		
		setButtonListener();
		
		setTitle(this.getString(R.string.title_activity_verify_number));
	}

	private void setButtonListener() {
		final Button button = (Button) findViewById(R.id.verify_number_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getTextViewText(R.id.verify_number_text).length() > 0) {
					VerifyNumberActivity.this.sendToken();
					button.setEnabled(false);
				} else {
					showToast(getString(R.string.verify_number_empty));
				}
			}
		});
		
	}
	
	protected void sendToken() {
		String phoneNumberString = getTextViewText(R.id.verify_number_text);
		
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumber phoneNumber = new PhoneNumber();
		try {
			phoneNumber = phoneUtil.parse(phoneNumberString, "CH");
		} catch (NumberParseException e) {
			Toast.makeText(this, getString(R.string.verify_number_invalid), Toast.LENGTH_SHORT).show();
		}
		if(phoneUtil.isValidNumber(phoneNumber)) {
			phoneNumberString = phoneUtil.format(phoneNumber, PhoneNumberFormat.E164);
			savePhoneNumberInPreferences(phoneNumberString);
			new SmsSender(phoneNumberString, this).execute();
		} else {
			Toast.makeText(this, getString(R.string.verify_number_invalid), Toast.LENGTH_SHORT).show();
		}
	}

	private void savePhoneNumberInPreferences(String phoneNumber) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = settings.edit();
		edit.putString("phonenumber", phoneNumber);
		edit.apply();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verify_numer, menu);
		return true;
	}
	
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.receiver);
    }
    
    public void setSuccessful(boolean wasSuccessful) {
    	if(!finished) {
        	if(wasSuccessful) {
        		Toast.makeText(this, getString(R.string.verify_number_successful), Toast.LENGTH_SHORT).show();
        	} else {
        		Toast.makeText(this, getString(R.string.verify_number_failed), Toast.LENGTH_SHORT).show();
        	}
        	finished = true;
        	this.finish();
    	}
    }

}
