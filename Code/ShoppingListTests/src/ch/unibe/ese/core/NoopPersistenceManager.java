package ch.unibe.ese.core;

import java.util.ArrayList;
import java.util.List;

public class NoopPersistenceManager implements PersistenceManager {

	@Override
	public List<ShoppingList> read() {
		return new ArrayList<ShoppingList>();
	}

	@Override
	public void save(List<ShoppingList> lists) {
		// noop
	}
}
