package ch.unibe.ese.shopnote.core.entities;

/**
 * Base class for everything that gets stored in the SQLite database.
 *
 */
public abstract class Entity {
	private Long id;

	public Long getId() {
		return id;
	}

	/**
	 * The ID of each object can only be set once (best time: on creation)
	 * @param id
	 */
	public void setId(long id) {
		if (this.id != null && this.id >= 0)
			throw new IllegalArgumentException(
					"it's not allowed to set the ID of an item twice");
		this.id = id;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
