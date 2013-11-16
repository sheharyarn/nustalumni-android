package com.impsycho.nustalumni;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MyProfileFragment extends Fragment {
	ProgressBar emptyloader;
	LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View profileView = inflater.inflate(R.layout.fragment_myprofile, container, false);
    	
    	emptyloader = (ProgressBar)profileView.findViewById(R.id.myprofile_loader);
    	layout = (LinearLayout)profileView.findViewById(R.id.myprofile_layout);
    	
    	FrameLayout imagebox = (FrameLayout)profileView.findViewById(R.id.myprofile_imagebox);
    	imagebox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(getActivity())
			    .setTitle("Gravatar")
			    .setMessage("This app uses 'Gravatar' to show Profile Pictures. To add or change your Profile Picture, visit the Gravatar Website.\n\n Go to the Website now?")
			    .setNegativeButton("Not Now", null)
			    .setPositiveButton("Open Gravatar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
		            	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.gravatar.com/"));
		        		startActivity(browserIntent);
					}
				})
			    .show();
			}
    		
    	});
    	
/*    	InflateFAQData(); 
    	ParseFAQData();*/
    	
    	return profileView;
    }
    
    public void InflateProfileData() {
    	if (ParseValues.faq_questions == null) {
    		layout.setVisibility(LinearLayout.GONE);
    		emptyloader.setVisibility(ProgressBar.VISIBLE);
    	} else {
    		layout.setVisibility(LinearLayout.VISIBLE);
    		emptyloader.setVisibility(ProgressBar.GONE);

        }
    } 
    
    public void GetProfileData() {
    	APIclient.get("/data/faq/", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray response) {
	        	try {
        			ParseValues.faq_questions = new ArrayList<String>();
        			ParseValues.faq_answers   = new ArrayList<String>();
        			
	        		for (int i=0; i<response.length(); i++) {
	        			JSONObject item = response.getJSONObject(i);
	        			ParseValues.faq_questions.add(item.getString("question"));
	        			ParseValues.faq_answers.add(item.getString("answer"));
	        		}
	        		
	    			InflateProfileData();
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
	        	try {
	    			ErrorAlert(LoginActivity.Capitalize(response.getString("error")));
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
		});
    }
	
	public void ErrorAlert(String message) {
		new AlertDialog.Builder(getActivity())
	    .setTitle("Error")
	    .setMessage(message)
	    .setNegativeButton("Cancel", null)
	    .show();
		
	}
}
