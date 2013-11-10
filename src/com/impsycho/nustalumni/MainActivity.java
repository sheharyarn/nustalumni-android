package com.impsycho.nustalumni;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] MenuItemList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
	            public void onDrawerClosed(View view) {
	                getActionBar().setTitle("Niggerless");
	                invalidateOptionsMenu();
	            }
	
	            public void onDrawerOpened(View drawerView) {
	                getActionBar().setTitle("Bitchless");
	                invalidateOptionsMenu();
	            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	String option = MenuItemList[position];
            if (option.equals("Home"))
            	DoNothing();
            else if (option.equals("Sign Out"))
            	DoNothing();
            else if (option.equals("New Situation"))
            	DoNothing();
            
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
    
    public static void DoNothing() {
    	
    }

}
