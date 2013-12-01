package ch.unibe.ese.shopnote.core;

import java.math.BigDecimal;
import java.util.List;

import android.test.AndroidTestCase;
import ch.unibe.ese.shopnote.core.Item;
import ch.unibe.ese.shopnote.core.ListManager;
import ch.unibe.ese.shopnote.core.ShoppingList;
import ch.unibe.ese.shopnote.core.sqlite.SQLitePersistenceManager;

public class ListManagerTest extends AndroidTestCase {

	private ListManager manager;

	public void setUp() throws Exception {
		getContext().deleteDatabase("shoppinglist.db");
		manager = new ListManager(new SQLitePersistenceManager(getContext()));
		assertTrue(manager.getShoppingLists().isEmpty());
	}

	@Override
	protected void tearDown() throws Exception {
		getContext().deleteDatabase("shoppinglist.db");
	}

	public void testAddShoppingList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.saveShoppingList(list1);

		assertEquals(1, manager.getShoppingLists().size());
		assertEquals(list1, manager.getShoppingLists().get(0));
	}

	public void testAddShoppingLists() {
		ShoppingList list1 = new ShoppingList("list1");
		ShoppingList list2 = new ShoppingList("list2");
		manager.saveShoppingList(list1);
		manager.saveShoppingList(list2);

		assertEquals(2, manager.getShoppingLists().size());
		assertEquals(list1, manager.getShoppingLists().get(0));
		assertEquals(list2, manager.getShoppingLists().get(1));
	}

	public void testRemoveShoppingList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.saveShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));

		manager.removeShoppingList(list1);
		assertTrue(manager.getShoppingLists().isEmpty());

		// Multiple invocations don't raise an exception
		manager.removeShoppingList(list1);
		assertTrue(manager.getShoppingLists().isEmpty());
	}

	public void testRenameShoppingList() {
		ShoppingList list = new ShoppingList("List 1");
		manager.saveShoppingList(list);

		List<ShoppingList> lists = manager.getShoppingLists();
		assertEquals(list, lists.get(0));

		final String rename = "Renamed 1";
		list.setName(rename);
		manager.saveShoppingList(list);
		lists = manager.getShoppingLists();
		assertEquals(1, lists.size());
		assertEquals(list, lists.get(0));
		assertEquals(rename, lists.get(0).getName());
	}

	public void testAddItemToList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.saveShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));

		assertTrue(manager.getItemsFor(list1).isEmpty());
		final String name = "item1";
		final BigDecimal price = new BigDecimal("100.00");
		final BigDecimal quantity = new BigDecimal("500");
		final ItemUnit unit = ItemUnit.GRAM;
		Item item1 = new Item(name);
		item1.setPrice(price);
		item1.setQuantity(quantity, unit);
		manager.save(item1);
		manager.addItemToList(item1, list1);

		assertEquals(1, manager.getItemsFor(list1).size());
		Item testItem = manager.getItemsFor(list1).get(0);
		assertEquals(item1, testItem);
		assertEquals(name, testItem.getName());
		assertEquals(price, testItem.getPrice());
		assertEquals(quantity, testItem.getQuantity());
		assertEquals(unit, testItem.getUnit());
		assertNull(testItem.getShop());
	}

	public void testAddItemsToList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.saveShoppingList(list1);
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

	public void testAddItemTwice() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.saveShoppingList(list1);
		assertEquals(list1, manager.getShoppingLists().get(0));

		assertTrue(manager.getItemsFor(list1).isEmpty());
		Item item1 = new Item("item1");
		assertTrue(manager.addItemToList(item1, list1));
		assertFalse(manager.addItemToList(item1, list1));

		assertEquals(1, manager.getItemsFor(list1).size());
		assertEquals(item1, manager.getItemsFor(list1).get(0));
	}

	public void testAddItemsWithSameUnit() {
		ShoppingList list = new ShoppingList("list1");
		manager.saveShoppingList(list);
		assertEquals(list, manager.getShoppingLists().get(0));

		assertTrue(manager.getItemsFor(list).isEmpty());
		Item item1 = new Item("item");
		final BigDecimal quantity1 = new BigDecimal("1.23");
		final BigDecimal quantity2 = new BigDecimal("3.21");
		final ItemUnit unit = ItemUnit.KILO_GRAM;
		item1.setQuantity(quantity1, unit);
		assertTrue(manager.addItemToList(item1, list));

		Item item2 = new Item("item");
		item2.setQuantity(quantity2, unit);
		manager.addItemToList(item2, list);

		List<Item> items = manager.getItemsFor(list);
		assertEquals(1, items.size());
		Item testItem = items.get(0);
		assertEquals(new BigDecimal("4.44"), testItem.getQuantity());
		assertEquals(unit, testItem.getUnit());
	}

	public void testAddItemsWithGrams() {
		ShoppingList list = new ShoppingList("list1");
		manager.saveShoppingList(list);
		assertEquals(list, manager.getShoppingLists().get(0));

		assertTrue(manager.getItemsFor(list).isEmpty());
		Item item1 = new Item("item");
		final BigDecimal quantity1 = new BigDecimal("1.23");
		final BigDecimal quantity2 = new BigDecimal("321");
		final ItemUnit unit1 = ItemUnit.KILO_GRAM;
		final ItemUnit unit2 = ItemUnit.GRAM;
		item1.setQuantity(quantity1, unit1);
		assertTrue(manager.addItemToList(item1, list));

		Item item2 = new Item("item");
		item2.setQuantity(quantity2, unit2);
		manager.addItemToList(item2, list);

		List<Item> items = manager.getItemsFor(list);
		assertEquals(1, items.size());
		Item testItem = items.get(0);
		assertEquals(new BigDecimal("1.551"), testItem.getQuantity());
		assertEquals(unit1, testItem.getUnit());
	}

	// TODO currently, an item with the same name is only added once to a list.
	// this should be changed so if an item exists in a list and an item with
	// the same name but different unit is added, it should be added again, so
	// the list has 2 items.
	// public void testAddItemsWithDifferrentUnits(){
	// ShoppingList list = new ShoppingList("list1");
	// manager.saveShoppingList(list);
	// assertEquals(list, manager.getShoppingLists().get(0));
	//
	// assertTrue(manager.getItemsFor(list).isEmpty());
	// Item item1 = new Item("item");
	// final BigDecimal quantity1 = new BigDecimal("1.23");
	// final BigDecimal quantity2 = new BigDecimal("321");
	// final ItemUnit unit1 = ItemUnit.KILO_GRAM;
	// final ItemUnit unit2 = ItemUnit.PIECE;
	// item1.setQuantity(quantity1, unit1);
	// assertTrue(manager.addItemToList(item1, list));
	//
	// Item item2 = new Item("item");
	// item2.setQuantity(quantity2, unit2);
	// manager.addItemToList(item2, list);
	//
	// List<Item> items = manager.getItemsFor(list);
	// assertEquals(2, items.size());
	// Item testItem1 = items.get(0);
	// Item testItem2 = items.get(0);
	// assertEquals(quantity1, testItem1.getQuantity());
	// assertEquals(unit1, testItem1.getUnit());
	// assertEquals(quantity2, testItem2.getQuantity());
	// assertEquals(unit2, testItem2.getUnit());
	// }

	public void testRemoveItemFromList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.saveShoppingList(list1);
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
		Item testItem = manager.getAllItems().get(0);
		assertEquals(item1, testItem);
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
