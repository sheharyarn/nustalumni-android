package com.impsycho.nustalumni;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;


public class HomeFragment extends Fragment {
	ProgressBar emptyloader;
	LinearLayout layout;
	TextView title;
	TextView content;
	TextView readmore;
	Boolean loadedall = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View homeView = inflater.inflate(R.layout.fragment_home, container, false);

    	title    = (TextView)homeView.findViewById(R.id.home_title);
    	content  = (TextView)homeView.findViewById(R.id.home_content);
    	readmore = (TextView)homeView.findViewById(R.id.home_readmore);
    	
    	readmore.setOnClickListener(new OnClickListener(){
    		public void onClick(View arg0) {
    			if (loadedall)
    				InflateHomeData(false);
    			else
    				InflateHomeData(true);
    		}
    	});
    	
    	emptyloader = (ProgressBar)homeView.findViewById(R.id.home_loader);
    	layout = (LinearLayout)homeView.findViewById(R.id.home_layout);
    	
    	InflateHomeData(false); 
    	ParseHomeData();
    	
    	return homeView;
    }
    
    public void InflateHomeData(Boolean loadfull) {
    	if (ParseValues.home_title == null) {
    		layout.setVisibility(LinearLayout.GONE);
    		emptyloader.setVisibility(ProgressBar.VISIBLE);
    	} else {
    		layout.setVisibility(LinearLayout.VISIBLE);
    		emptyloader.setVisibility(ProgressBar.GONE);
    		title.setText(ParseValues.home_title);
    		
    		if (loadfull) {
    			readmore.setText("Show Less...");
        		content.setText(ParseValues.home_content);
    			loadedall = true;
    		} else {
    			readmore.setText("Show More...");
        		content.setText(ParseValues.home_content.substring(0, ParseValues.home_content.indexOf("\n\n")));
        		loadedall = false;
    		}
        }
    } 
    
    public void ParseHomeData() {
    	if (ParseValues.home_title == null)
    		ForceParseHomeData();
    }
    
    public void ForceParseHomeData() {
    	APIclient.get("/data/home/", null, getActivity(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
	        	try {
	    			ParseValues.home_title = response.getString("title");
	    			ParseValues.home_content = response.getString("content");
	    			InflateHomeData(false);
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
