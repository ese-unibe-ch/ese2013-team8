package ch.unibe.ese.shopnote.core;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;


/**
 * Class Friend with two values: phoneNr and name
 * 
 * @author ESE Team 8
 * 
 */
public class Friend extends Entity {
	private String phoneNr;
	private String name;
	private boolean hasTheApp;

	// TODO: add Image of Friend
	public Friend(String phoneNr, String name) {
		this.phoneNr = formatPhoneNumber(phoneNr);
		this.name = name;
		this.hasTheApp = false;
		invariant();
	}

	public String toString() {
		return name + ", " + phoneNr;
	}

	public String getPhoneNr() {
		return phoneNr;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		invariant();
	}
	
	public void setHasApp() {
		this.hasTheApp = true;
	}
	
	public void setHasNotApp()  {
		this.hasTheApp = false;
	}
	
	public boolean hasTheApp() {
		return hasTheApp;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().isEmpty())
			throw new IllegalStateException("Empty name is not allowed");
		if (this.name == null || this.name.trim().isEmpty())
			throw new IllegalStateException("Empty Phone number is not allowed ");
	}
	
	private String formatPhoneNumber(String phoneNr) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		PhoneNumber phoneNumber = new PhoneNumber();
		try {
			phoneNumber = phoneUtil.parse(phoneNr, "CH");
		} catch (NumberParseException e) {
			e.printStackTrace();
		}
		if(phoneUtil.isValidNumber(phoneNumber)) {
			phoneNr = phoneUtil.format(phoneNumber, PhoneNumberFormat.E164);
		}
		return phoneNr;
	}

}
