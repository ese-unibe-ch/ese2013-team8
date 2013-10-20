package ch.unibe.ese.core;

import ch.unibe.ese.shoppinglist.R;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class SlideMenu extends LinearLayout {

	private View menu;
	private View content;

	private static final int restContent = 100;
	private static final int slide_Menu_Id = R.layout.slide_menu;
	private int currentOffSet = 0;

	private enum State {
		CLOSED, OPEN
	}

	private State menuState = State.CLOSED;


	public SlideMenu(Context context) {
		super(context);
		//listView.setOnTouchListener( new DragOnTouchListener(menu));
	}

	
	/**
	 * Creates the view of the Windows and is called when a View is attached to
	 * a Window
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

		//this.content = this.getChildAt(contentId);
		
		this.menu = this.getChildAt(slide_Menu_Id);
		this.menu.setVisibility(View.GONE);
	}

	/**
	 * Switches between the open menu and the closed menu
	 */
	public void toggleMenu() {
		switch (menuState) {
		case CLOSED:
			currentOffSet = this.getMenuWidth();
			this.content.offsetLeftAndRight(currentOffSet);
			this.menu.setVisibility(View.VISIBLE);
			this.menuState = State.OPEN;
			break;

		case OPEN:
			this.content.offsetLeftAndRight(-currentOffSet);
			this.currentOffSet = 0;
			this.menuState = State.CLOSED;
			this.menu.setVisibility(View.GONE);
			break;
		}
		this.invalidate();
	}
	
	public int getMenuWidth()
	{
		return this.menu.getLayoutParams().width;
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

	public void setContent(View content) {
		this.content = content;
	}

}
