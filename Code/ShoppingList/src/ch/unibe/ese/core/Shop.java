package ch.unibe.ese.core;

public class Shop {
	private String name;

	public Shop(String name) {
		this.name = name;
		invariant();
	}

	public String getName() {
		return name;
	}

	private void invariant() {
		if (this.name == null || this.name.trim().length() == 0)
			throw new IllegalArgumentException("name musn't be empty!");
	}

}
