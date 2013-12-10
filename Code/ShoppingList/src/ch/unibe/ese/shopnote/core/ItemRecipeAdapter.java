package ch.unibe.ese.shopnote.core;

public class ItemRecipeAdapter {
			private String name;
			private Long id;
			private Type entryTyp;
			
			private enum Type {RECIPE, ITEM};
			
			public ItemRecipeAdapter(Item item) {
				name = item.getName();
				id = item.getId();
				entryTyp = Type.ITEM;
			}
			
			public ItemRecipeAdapter(Recipe recipe, String text) {
				if (text.equals(""))
					name = recipe.getName();
				else
					name = recipe.toString() + " (" + text + ")";

				id = recipe.getId();
				entryTyp = Type.RECIPE;		
			}
			
			public String toString() {
				return name;
			}
			
			public boolean isItem() {
				return entryTyp == Type.ITEM;
			}
			
			public Long getId() {
				return id;
			}
}