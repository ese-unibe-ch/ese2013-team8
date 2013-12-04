package ch.unibe.ese.shopnote.share.requests.listchange;

public class RecipeDescriptionRequest extends ListChangeRequest {

	private static final long serialVersionUID = -3618259206963096050L;

	private String description;

	public RecipeDescriptionRequest(String phoneNumber, long localListId, String description) {
		super(phoneNumber, localListId);
		this.description = description;
		this.isRecipe(true);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int getSubType() {
		return ListChangeRequest.RECIPE_DESCRIPTION_REQUEST;
	}

	@Override
	public ListChangeRequest getCopy() {
		return new RecipeDescriptionRequest(getPhoneNumber(), getLocalListId(), getDescription());
	}

}
