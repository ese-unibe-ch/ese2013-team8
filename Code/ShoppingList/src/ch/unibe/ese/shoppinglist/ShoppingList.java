package ch.unibe.ese.shoppinglist;

import java.util.Date;

public class ShoppingList {
	private String name;
	private Date dueDate;
	private String shop;
	
	public ShoppingList(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getShop() {
		return shop;
	}
	public void setShop(String shop) {
		this.shop = shop;
	}
	
	
}
