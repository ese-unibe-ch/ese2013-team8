package ch.unibe.ese.core;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.support.v4.content.FileProvider;
import android.test.ProviderTestCase2;
import android.text.format.DateFormat;

public class JsonPersistenceManagerTest extends ProviderTestCase2<FileProvider> {

	private PersistenceManager manager;

	public JsonPersistenceManagerTest() {
		super(FileProvider.class, "android.support.v4.content");
	}

	public void setUp() throws Exception {
		super.setUp();
		this.manager = new JsonPersistenceManager(getContext());
	}

	public void tearDown() throws Exception {
		super.tearDown();
		getContext().deleteFile(JsonPersistenceManager.FILE_NAME);
	}

	public void testRead() throws IOException {
		List<ShoppingList> list = manager.read();
		assertNotNull(list);
		assertTrue(list.isEmpty());
	}

	public void testSave() throws IOException {
		List<ShoppingList> list = new ArrayList<ShoppingList>();
		list.add(new ShoppingList("list1"));
		manager.save(list);

		String[] files = getContext().fileList();
		assertEquals(1, files.length);
		assertEquals(JsonPersistenceManager.FILE_NAME, files[0]);
	}

	public void testSaveAndRead() throws IOException {
		List<ShoppingList> list = new ArrayList<ShoppingList>();
		list.add(new ShoppingList("list1"));
		manager.save(list);

		List<ShoppingList> readList = manager.read();

		assertEquals(list, readList);
	}

	public void testSaveAndReadMultipleLists() throws IOException,
			ParseException {
		List<ShoppingList> lists = new ArrayList<ShoppingList>();
		for (int i = 1; i <= 9; i++) {
			ShoppingList list = new ShoppingList("list" + i);
			Date date = DateFormat.getDateFormat(getContext()).parse(
					String.format("%02d/10/2013", i));
			list.setDueDate(date);
			list.setShop("Shop " + i);
			lists.add(list);
		}
		manager.save(lists);

		List<ShoppingList> readList = manager.read();
		assertEquals(lists, readList);
	}
}
