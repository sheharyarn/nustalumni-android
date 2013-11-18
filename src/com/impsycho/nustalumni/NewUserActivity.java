package com.impsycho.nustalumni;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class NewUserActivity extends FragmentActivity {
	public static String NEW_USER = "new_user";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		
        Bundle arguments = new Bundle();
        arguments.putBoolean(NewUserActivity.NEW_USER, true);
        
		MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.profile_container, fragment)
                .commit();
	}

}
