package ch.unibe.ese.core;

import java.util.ArrayList;
import java.util.List;

public class NoopPersistenceManager implements PersistenceManager {

	@Override
	public List<ShoppingList> readLists() {
		return new ArrayList<ShoppingList>();

	}

	@Override
	public void save(ShoppingList list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(ShoppingList list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(Item item, ShoppingList list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Item item, ShoppingList list) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Item> getItems(ShoppingList list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Friend> readFriends() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Friend friend) {
		// TODO Auto-generated method stub

	}

}
