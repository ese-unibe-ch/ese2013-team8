package ch.unibe.ese.Listeners;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import ch.unibe.ese.core.SlideMenu;

public class DragOnTouchListener implements OnTouchListener{
	private SlideMenu menu;
	
	public DragOnTouchListener(SlideMenu menu)
	{
		this.menu = menu;
	}
	
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		
			this.menu.toggleMenu();
			return true;
		
	}
}
