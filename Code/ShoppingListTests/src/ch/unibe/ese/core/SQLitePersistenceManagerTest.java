package ch.unibe.ese.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.unibe.ese.core.sqlite.SQLitePersistenceManager;
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
		super.setUp();
		getContext().deleteDatabase("shoppinglist.db");
		this.manager = new SQLitePersistenceManager(getContext());
		// Make sure there are no shopping lists in the DB.
		assertTrue(manager.readLists().isEmpty());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testReadLists() {
		ShoppingList list = new ShoppingList("List 1");
		manager.save(list);

		List<ShoppingList> lists = manager.readLists();
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
		ShoppingList checkList = manager.readLists().get(0);
		assertEquals(list, checkList);
		assertEquals(name, checkList.getName());
		assertEquals(dueDate, checkList.getDueDate());
		assertEquals(shop, checkList.getShop());
	}
}
