package ch.unibe.ese.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import ch.unibe.ese.Listeners.DragOnTouchListener;
import ch.unibe.ese.shoppinglist.R;

public class SlideMenu extends RelativeLayout   {

	private View menu;
	private View content;
	private int contentId;

	//Layoutinformations
	private static final int restContent = 100;
	private static final int slide_Menu_Id = R.layout.slide_menu;
	private int currentOffSet = 0;

	private enum State {
		CLOSED, OPEN
	}

	private State menuState = State.CLOSED;


	public SlideMenu(Context context) {
		this(context, -1);
	}
	
	public SlideMenu(Context context, int layout){
		super(context);
		
		//Construct both views
		menu = getMenu();
		if(layout >= 0){
			content = LayoutInflater.from(getContext()).inflate(layout, null);
			contentId = layout;
		}
		else 
			content = new View(getContext());

		//listeners for both views
		content.setOnTouchListener(new DragOnTouchListener(this));
		menu.setOnTouchListener(new DragOnTouchListener(this));
		
		addView(content);
	}	
	
	/**
	 * Creates the view of the Windows and is called when a View is attached to
	 * a Window
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	
//		this.content = LayoutInflater.from(getContext()).inflate(contentId, null);
	}
	
	private View getMenu(){
		return LayoutInflater.from(getContext()).inflate(slide_Menu_Id, null);
	}

	/**
	 * Switches between the open menu and the closed menu
	 */
	public void toggleMenu() {
		switch (menuState) {
		case CLOSED:
			currentOffSet = getMenuWidth();
			content.offsetLeftAndRight(2000);
	        addView(menu);
	        
	 
	     
//			this.currentOffSet = this.getMenuWidth();
//			this.content.offsetLeftAndRight(currentOffSet);
			this.menuState = State.OPEN;
			break;
			
			
	           
	      
	           

		case OPEN:
			this.removeView(menu);
//			this.content.offsetLeftAndRight(-currentOffSet);
			this.currentOffSet = 0;
			this.menuState = State.CLOSED;
			break;
		}
		this.invalidate();
	}
	
	public int getMenuWidth()
	{
		return 1000;
	}
	
	

	/**
	 * Is called when this view needs to assign a size and position to all of
	 * its childs
	 */
	public void onLayout(Boolean changed, int left, int top, int right,
			int bottom) {
		if (changed)
			this.calculateChildDimensions();

		this.menu.layout(left, top, right - restContent, bottom);
		this.content.layout(left + currentOffSet, top, right + currentOffSet,
				bottom);
	}

	private void calculateChildDimensions() {
		this.content.getLayoutParams().height = this.getHeight();
		this.content.getLayoutParams().width = this.getWidth();

		this.menu.getLayoutParams().width = this.getWidth() - restContent;
		this.menu.getLayoutParams().height = this.getHeight();

	}


	public View getContent() {
		return content;
	}

	public void setContent(int content) {
		contentId = content;
		this.content = LayoutInflater.from(getContext()).inflate(content, null);
	}
	
	public boolean menuOpen(){
		if(menuState == State.OPEN) return true;
		return false;
	}
}