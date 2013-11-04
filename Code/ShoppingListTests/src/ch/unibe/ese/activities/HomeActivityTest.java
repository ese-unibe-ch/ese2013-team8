package ch.unibe.ese.activities;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;

public class HomeActivityTest extends ActivityInstrumentationTestCase2<HomeActivity>{

	private HomeActivity activity;
	
	public HomeActivityTest() {
		super(HomeActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testStartCreateListActivity() {
	    // add monitor to check for the second activity
	    ActivityMonitor monitor =
	        getInstrumentation().
	          addMonitor(CreateListActivity.class.getName(), null, false);
	}
	
}
