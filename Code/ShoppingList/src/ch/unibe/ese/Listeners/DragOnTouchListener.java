package ch.unibe.ese.Listeners;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import ch.unibe.ese.core.SlideMenu;

public class DragOnTouchListener implements OnTouchListener{
	
	private SlideMenu menu;
	private final GestureDetector gestureDetector;
	
	public DragOnTouchListener(SlideMenu menu)
	{
		this.menu = menu;
		gestureDetector = new GestureDetector(new GestureListener(menu));
	}
	
	
	
	@Override
	public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }
	
	
	public boolean onSwipe()
	{
		return false;
		
	}
	

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int min_Swipe_Width = 100;
        private static final int min_Swipe_Velocity = 100;
        private SlideMenu menu;

        public GestureListener(SlideMenu menu){
        	this.menu = menu;
        }
        
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > min_Swipe_Width && Math.abs(velocityX) > min_Swipe_Velocity) {
                        if (diffX > 0) {
                            if(!menu.menuOpen()) 
                            	menu.toggleMenu();
                        } else {
                        	if(menu.menuOpen()) 
                            	menu.toggleMenu();
                        }
                    }
                } 
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
}
