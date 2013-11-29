package ch.unibe.ese.shopnote.activities;

import java.util.Locale;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import ch.unibe.ese.shopnote.R;

/**
 *	Activity to change the settings of the app
 */
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// set Listener
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        
        setTitle(this.getString(R.string.title_activity_settings));
        
    }
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * If user changes a option, this function is called to initialize all need steps which are required by the options
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("language")) {
			updateLanguage(sharedPreferences);
		}
		else if(key.equals("color_setting")) {
			
		}   
		
		//refresh interface
        NavUtils.navigateUpFromSameTask(this);
        finish();
        startActivity(getIntent());
	}


	private void updateLanguage(SharedPreferences sharedPreferences) {
		String newLanguage = sharedPreferences.getString("language", null);
		String languages[] = getResources().getStringArray(R.array.choosable_languages_keys);
		Configuration config = new Configuration();
		
		Locale locale = config.locale;	
		if(newLanguage.equals(languages[0])) 
			locale = Locale.ENGLISH; 
		else if(newLanguage.equals(languages[1])) 
			locale = Locale.GERMANY;
		else if(newLanguage.equals(languages[2])) 
			locale = Locale.FRANCE;
		
		Locale.setDefault(locale);
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	}
	
}

class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
    }
}