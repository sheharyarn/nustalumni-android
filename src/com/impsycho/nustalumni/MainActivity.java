package com.impsycho.nustalumni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] MenuItemList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Boolean newuser = getIntent().getBooleanExtra(NewUserActivity.NEW_USER, false);
		getIntent().removeExtra(NewUserActivity.NEW_USER);
		
		if (newuser) {
			Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        
			getApplicationContext().startActivity(intent);
		}
		
		// Lazy Loader
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
		
		MenuItemList = getResources().getStringArray(R.array.menuitems);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, MenuItemList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
        	this,
        	mDrawerLayout,
        	R.drawable.ic_drawer, 
        	R.string.app_name,
        	R.string.app_name
        	) {

            public void onDrawerClosed(View view)       { invalidateOptionsMenu(); }
            public void onDrawerOpened(View drawerView) { invalidateOptionsMenu(); }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        LoadHomeFragment();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
	    super.onPostCreate(savedInstanceState);
	    // Sync the toggle state after onRestoreInstanceState has occurred.
	    mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }

        return super.onOptionsItemSelected(item);
    }

	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	String option = MenuItemList[position];
            if (option.equals("Home"))
            	LoadHomeFragment();
            else if (option.equals("News"))
            	LoadNewsFragment();
            else if (option.equals("People"))
            	LoadPeopleFragment();
            else if (option.equals("Profile"))
            	LoadProfileFragment();
            else if (option.equals("FAQ"))
            	LoadFAQFragment();
            else if (option.equals("About"))
            	LoadAboutFragment();
            else if (option.equals("Settings"))
            	LoadSettings();
            else if (option.equals("Sign Out"))
            	Logout();

        	getActionBar().setTitle(option);
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
    
    
    // ==== Fragment Loaders
    public void LoadHomeFragment()     { LoadMyFragment(new HomeFragment());      }
    public void LoadNewsFragment()     { LoadMyFragment(new NewsFragment());      }
    public void LoadPeopleFragment()   { LoadMyFragment(new PeopleFragment());    }    
    public void LoadProfileFragment()  { LoadMyFragment(new MyProfileFragment()); }    
    public void LoadFAQFragment()      { LoadMyFragment(new FAQFragment());       }    
    public void LoadAboutFragment()    { LoadMyFragment(new AboutFragment());     }
    
    public void LoadMyFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
    }
    
    public void LoadSettings() {
    	LoadMyFragment(new EmptyFragment());
    	getFragmentManager()
	    	.beginTransaction()
	        .replace(R.id.content_frame, new SettingsFragment())
	        .commit();
    }
    
    public void Logout() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(LoginActivity.USER_SAVED, false);
		editor.putString(LoginActivity.USER_EMAIL, "");
		editor.putString(LoginActivity.USER_PASS, "");
		editor.commit();
		
    	APIclient.api_auth_token = null;
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        getApplicationContext().startActivity(intent);
        finish();
    	
    }

}
