package ch.unibe.ese.shopnote.core;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.unibe.ese.shopnote.core.sqlite.SQLitePersistenceManager;

import android.support.v4.content.FileProvider;
import android.test.ProviderTestCase2;

public class SQLitePersistenceManagerTest extends
		ProviderTestCase2<FileProvider> {
	private PersistenceManager manager;

	public SQLitePersistenceManagerTest() {
		super(FileProvider.class, "android.support.v4.content");
	}

	@Override
	protected void setUp() throws Exception {
		getContext().deleteDatabase("shoppinglist.db");
		this.manager = new SQLitePersistenceManager(getContext());
		// Make sure there are no shopping lists in the DB.
		assertTrue(manager.getLists().isEmpty());
	}

	@Override
	protected void tearDown() throws Exception {
		getContext().deleteDatabase("shoppinglist.db");
	}

	public void testGetLists() {
		ShoppingList list = new ShoppingList("List 1");
		manager.save(list);

		List<ShoppingList> lists = manager.getLists();
		assertEquals(1, lists.size());
		assertEquals(list, lists.get(0));
	}

	public void testSaveList() {
		final String name = "List 1";
		final Date dueDate = Calendar.getInstance().getTime();
		final String shop = "Shop 1";
		ShoppingList list = new ShoppingList(name);
		list.setDueDate(dueDate);
		list.setShop(shop);

		manager.save(list);
		ShoppingList checkList = manager.getLists().get(0);
		assertEquals(list, checkList);
		assertEquals(name, checkList.getName());
		assertEquals(dueDate, checkList.getDueDate());
		assertEquals(shop, checkList.getShop());
	}

	public void testRemoveList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.save(list1);
		ShoppingList checkList = manager.getLists().get(0);
		assertEquals(list1, checkList);

		manager.remove(list1);
		assertTrue(manager.getLists().isEmpty());
	}

	public void testAddItemToList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.save(list1);

		assertTrue(manager.getItems(list1).isEmpty());
		Item item1 = new Item("item1");
		manager.save(item1, list1);
		assertEquals(1, manager.getItems(list1).size());
		assertEquals(item1, manager.getItems(list1).get(0));
	}
	
	public void testAddItemWithPrice(){
		Item item = new Item("item1");
		item.setPrice(new BigDecimal("1.00"));
		ShoppingList list = new ShoppingList("list");
		manager.save(list);
		manager.save(item, list);
		
		Item checkItem = manager.getItems(list).get(0);
		assertEquals(item,checkItem);
		assertEquals(new BigDecimal("1.00"), checkItem.getPrice());
	}

	public void testRemoveItemFromList() {
		ShoppingList list1 = new ShoppingList("list1");
		manager.save(list1);

		assertTrue(manager.getItems(list1).isEmpty());
		Item item1 = new Item("item1");
		manager.save(item1, list1);

		manager.remove(item1, list1);
		assertTrue(manager.getItems(list1).isEmpty());
	}

	public void testGetAllItems() {
		assertTrue(manager.getAllItems().isEmpty());
		Item item1 = new Item("item1");
		Item item2 = new Item("item2");
		manager.save(item1);
		manager.save(item2);
		assertEquals(2, manager.getAllItems().size());
		assertEquals(item1, manager.getAllItems().get(0));
		assertEquals(item2, manager.getAllItems().get(1));
	}

	public void testAddItem() {
		assertTrue(manager.getAllItems().isEmpty());
		Item item1 = new Item("item1");
		manager.save(item1);
		assertEquals(1, manager.getAllItems().size());
		assertEquals(item1, manager.getAllItems().get(0));
	}

	public void testRemoveItem() {
		assertTrue(manager.getAllItems().isEmpty());
		Item item1 = new Item("item1");
		manager.save(item1);
		assertEquals(1, manager.getAllItems().size());

		manager.remove(item1);
		assertTrue(manager.getAllItems().isEmpty());
	}

	public void testReadFriends() {
		assertTrue(manager.getFriends().isEmpty());
		final String phoneNr1 = "12345678";
		final String phoneNr2 = "23456789";
		final String name1 = "friend1";
		final String name2 = "friend2";
		Friend friend1 = new Friend(phoneNr1, name1);
		Friend friend2 = new Friend(phoneNr2, name2);
		manager.save(friend1);
		manager.save(friend2);

		assertEquals(2, manager.getFriends().size());
		Friend testFriend1 = manager.getFriends().get(0);
		Friend testFriend2 = manager.getFriends().get(1);
		assertEquals(friend1, testFriend1);
		assertTrue(testFriend1.getId() > 0);
		assertEquals(phoneNr1, testFriend1.getPhoneNr());
		assertEquals(name1, testFriend1.getName());
		assertEquals(friend2, testFriend2);
		assertTrue(testFriend2.getId() > 0);
		assertEquals(phoneNr2, testFriend2.getPhoneNr());
		assertEquals(name2, testFriend2.getName());
	}

	public void testSaveFriend() {
		assertTrue(manager.getFriends().isEmpty());
		Friend friend1 = new Friend("12345678", "friend1");
		manager.save(friend1);
		assertEquals(1, manager.getFriends().size());
		assertEquals(friend1, manager.getFriends().get(0));
	}

	public void testRemoveFriend() {
		assertTrue(manager.getFriends().isEmpty());
		Friend friend1 = new Friend("12345678", "friend1");
		manager.save(friend1);
		assertEquals(1, manager.getFriends().size());

		manager.removeFriend(friend1);
		assertTrue(manager.getFriends().isEmpty());
	}
}
