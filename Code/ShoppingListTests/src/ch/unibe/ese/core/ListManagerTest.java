package ch.unibe.ese.core;

import android.test.AndroidTestCase;

public class ListManagerTest extends AndroidTestCase {

	private ListManager manager;

	public void setUp() throws Exception {
		this.manager = new ListManager(new NoopPersistenceManager());
	}

	public void testAaddShoppingList() {
		assertTrue(manager.getShoppingLists().isEmpty());
		ShoppingList list = new ShoppingList("list1");
		manager.addShoppingList(list);

		assertEquals(1, manager.getShoppingLists().size());
		assertEquals(list, manager.getShoppingLists().get(0));
	}

	public void testAddShoppingLists() {
		assertTrue(manager.getShoppingLists().isEmpty());
		ShoppingList list1 = new ShoppingList("list1");
		ShoppingList list2 = new ShoppingList("list2");
		manager.addShoppingList(list1);
		manager.addShoppingList(list2);

		assertEquals(2, manager.getShoppingLists().size());
		assertEquals(list1, manager.getShoppingLists().get(0));
		assertEquals(list2, manager.getShoppingLists().get(1));
	}

	public void testRemoveShoppingList() {
		assertTrue(manager.getShoppingLists().isEmpty());
		ShoppingList list = new ShoppingList("list1");
		manager.addShoppingList(list);
		assertEquals(list, manager.getShoppingLists().get(0));

		manager.removeShoppingList(list);
		assertTrue(manager.getShoppingLists().isEmpty());

		// Multiple invocations dont raise an exception
		manager.removeShoppingList(list);
		assertTrue(manager.getShoppingLists().isEmpty());
	}
}
