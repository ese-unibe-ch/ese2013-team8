package ch.unibe.ese.core;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The JsonPersistenceManager persists a List of ShoppingList in a JSON-format
 * on the internal storage of the device.
 */
public class JsonPersistenceManager implements PersistenceManager {

	protected static final String FILE_NAME = "shopping_list.json";

	private final Context context;

	/**
	 * @param applicationContext
	 *            not null
	 */
	public JsonPersistenceManager(Context applicationContext) {
		if (applicationContext == null)
			throw new IllegalArgumentException("null is not allowed");
		this.context = applicationContext;
	}

	@Override
	public List<ShoppingList> readLists() {
		FileInputStream in = null;
		InputStreamReader reader = null;
		try {
			in = context.openFileInput(FILE_NAME);
			reader = new InputStreamReader(in);
			Gson gson = new Gson();
			return gson.fromJson(reader, getType());
		} catch (FileNotFoundException e) {
			return new ArrayList<ShoppingList>();
		} finally {
			closeIgnoringException(in);
			closeIgnoringException(reader);
		}
	}

	public void save(List<ShoppingList> lists) throws IOException {
		FileOutputStream out = null;
		OutputStreamWriter writer = null;
		try {
			out = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			Gson gson = new Gson();
			gson.toJson(lists, getType(), writer);
		} catch (FileNotFoundException e) {
			// can this even happen? Javadoc of context.openFileOutput states
			// that it will create this file if it does not exist.
			throw new IllegalStateException(e);
		} finally {
			if (writer != null)
				writer.close();
			if (out != null)
				out.close();
		}
	}

	private Type getType() {
		return new TypeToken<List<ShoppingList>>() {
		}.getType();
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

	private void closeIgnoringException(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException e) {
				// nothing we can do.
				e.printStackTrace();
			}
		}
	}

}
