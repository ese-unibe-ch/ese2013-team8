package ch.unibe.ese.shopnote.core;

/**
 * Class Friend with two values: phoneNr and name
 * 
 * @author ESE Team 8
 * 
 */
public class Friend extends Entity {
	private int phoneNr;
	private String name;

	// TODO: add Image of Friend
	public Friend(int phoneNr, String name) {
		this.phoneNr = phoneNr;
		this.name = name;
		invariant();
	}

	public String toString() {
		return name + ", Phone nr: " + phoneNr;
	}

	public int getPhoneNr() {
		return phoneNr;
	}

	public void setPhoneNr(int phoneNr) {
		this.phoneNr = phoneNr;
		invariant();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		invariant();
	}

	private void invariant() {
		if (this.name == null || this.name.trim().isEmpty())
			throw new IllegalStateException("Empty name is not allowed");
		if (this.phoneNr < 0)
			throw new IllegalStateException("Phone number must be > 0. Nr: "
					+ phoneNr);
	}
}
