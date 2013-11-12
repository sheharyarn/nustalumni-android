package com.impsycho.nustalumni;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View aboutView = inflater.inflate(R.layout.fragment_about, container, false);
    	
    	TextView nustlink = (TextView) aboutView.findViewById(R.id.about_linknust);
    	nustlink.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://nust.edu.pk/Alumni"));
        		startActivity(browserIntent);
        	}
    	});
    	
    	return aboutView;
    }
    
    public void OpenDeveloperWebsite(View view) {
    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sheharyar.me/"));
		startActivity(browserIntent);
    }
    
}
 