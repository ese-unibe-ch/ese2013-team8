package ch.unibe.ese.core;

/**
 * Class Friend with two values: phoneNr and name
 * 
 * @author ESE Team 8
 * 
 */
public class Friend {
	private Long id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Friend other = (Friend) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		if (this.id != null)
			throw new IllegalArgumentException(
					"it's not allowed to set the ID of an item twice");
		this.id = id;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().isEmpty())
			throw new IllegalStateException("Empty name is not allowed");
		if (this.phoneNr < 0)
			throw new IllegalStateException("Phone number must be > 0. Nr: "
					+ phoneNr);
	}
}
