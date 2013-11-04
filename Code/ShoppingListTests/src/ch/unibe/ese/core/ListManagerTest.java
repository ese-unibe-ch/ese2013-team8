package ch.unibe.ese.core;

import ch.unibe.ese.core.sqlite.SQLitePersistenceManager;
import android.test.AndroidTestCase;
import ch.unibe.ese.core.ShoppingList;

public class ListManagerTest extends AndroidTestCase {

	private ListManager manager;

	public void setUp() throws Exception {
		getContext().deleteDatabase("shoppinglist.db");
		manager = new ListManager(new SQLitePersistenceManager(getContext()));
	}
	
	@Override
	protected void tearDown() throws Exception {
		getContext().deleteDatabase("shoppinglist.db");
	}

	public void testAddShoppingList() {
		assertTrue(manager.getShoppingLists().isEmpty());
		ShoppingList list1 = new ShoppingList("list1");
		manager.addShoppingList(list1);

		assertEquals(1, manager.getShoppingLists().size());
		assertEquals(list1, manager.getShoppingLists().get(0));
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
		ShoppingList list1 = new ShoppingList("list1");
		manager.addShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));

		manager.removeShoppingList(list1);
		assertTrue(manager.getShoppingLists().isEmpty());

		// Multiple invocations don't raise an exception
		manager.removeShoppingList(list1);
		assertTrue(manager.getShoppingLists().isEmpty());
	}
	
	public void testAddItemToList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.addShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));
		
		assertTrue(manager.getItemsFor(list1).isEmpty());
		Item item1 = new Item("item1");
		manager.addItemToList(item1, list1);
		assertEquals(1, manager.getItemsFor(list1).size());
		assertEquals(item1, manager.getItemsFor(list1).get(0));
	}
	
	public void testAddItemsToList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.addShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));
		
		assertTrue(manager.getItemsFor(list1).isEmpty());
		Item item1 = new Item("item1");
		Item item2 = new Item("item2");
		manager.addItemToList(item1, list1);
		manager.addItemToList(item2, list1);
		
		assertEquals(2, manager.getItemsFor(list1).size());
		assertEquals(item1, manager.getItemsFor(list1).get(0));
		assertEquals(item2, manager.getItemsFor(list1).get(1));
	}
	
	public void testRemoveItemFromList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.addShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));
		
		assertTrue(manager.getItemsFor(list1).isEmpty());
		Item item1 = new Item("item1");
		manager.addItemToList(item1, list1);	
		assertEquals(1, manager.getItemsFor(list1).size());
		assertEquals(item1, manager.getItemsFor(list1).get(0));
		
		manager.removeItemFromList(item1, list1);
		assertTrue(manager.getItemsFor(list1).isEmpty());
		
		// Multiple invocations don't raise an exception
		manager.removeItemFromList(item1, list1);
		assertTrue(manager.getItemsFor(list1).isEmpty());	
	}
	
	public void testAddItem() {
		assertTrue(manager.getAllItems().isEmpty());
		Item item1 = new Item("item1");
		manager.save(item1);
		assertEquals(1, manager.getAllItems().size());
		assertEquals(item1, manager.getAllItems().get(0));	
	}
	
	public void testAddItems() {
		assertTrue(manager.getAllItems().isEmpty());
		Item item1 = new Item("item1");
		Item item2 = new Item("item2");
		manager.save(item1);
		manager.save(item2);
		assertEquals(2, manager.getAllItems().size());
		assertEquals(item1, manager.getAllItems().get(0));	
		assertEquals(item2, manager.getAllItems().get(1));	
	}
	
	public void testRemoveItem() {
		assertTrue(manager.getAllItems().isEmpty());
		Item item1 = new Item("item1");
		manager.save(item1);
		assertEquals(1, manager.getAllItems().size());
		assertEquals(item1, manager.getAllItems().get(0));
		
		manager.remove(item1);
		assertTrue(manager.getAllItems().isEmpty());
		
		// Multiple invocations don't raise an exception
		manager.remove(item1);
		assertTrue(manager.getAllItems().isEmpty());
	}
}
