package ch.unibe.ese.shoppinglist;

import java.util.ArrayList;
import java.util.List;

public class ListManager {

	private List<ShoppingList> shoppingLists;
	
	public ListManager(){
		shoppingLists = new ArrayList<ShoppingList>();
	}

	public List<ShoppingList> getShoppingLists() {
		return shoppingLists;
	}
	
	public void addShoppingList(ShoppingList list){
		shoppingLists.add(list);
	}
}
