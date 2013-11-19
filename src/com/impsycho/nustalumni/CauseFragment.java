package com.impsycho.nustalumni;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;


public class CauseFragment extends Fragment {
	Boolean loadedall = false;
	ProgressBar emptyloader;
	LinearLayout layout;
	TextView title;
	TextView content;
	TextView readmore;
	Button apply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View causeView = inflater.inflate(R.layout.fragment_cause, container, false);

    	apply    = (Button)  causeView.findViewById(R.id.cause_apply);
    	title    = (TextView)causeView.findViewById(R.id.cause_title);
    	content  = (TextView)causeView.findViewById(R.id.cause_content);
    	readmore = (TextView)causeView.findViewById(R.id.cause_readmore);
    	
    	apply.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { SendMentorshipEmail(); }
    	});
    	
    	readmore.setOnClickListener(new OnClickListener(){
    		public void onClick(View arg0) {
    			if (loadedall)
    				InflateCauseData(false);
    			else
    				InflateCauseData(true);
    		}
    	});
    	
    	emptyloader = (ProgressBar)causeView.findViewById(R.id.cause_loader);
    	layout = (LinearLayout)causeView.findViewById(R.id.cause_layout);
    	
    	InflateCauseData(false); 
    	ParseCauseData();
    	
    	return causeView;
    }
    
    public void SendMentorshipEmail() {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"connect.alumni@nust.edu.pk"});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Applying for NAA Mentorship");
		emailIntent.setType("plain/text");
		startActivity(emailIntent);
    }
    
    public void InflateCauseData(Boolean loadfull) {
    	if (ParseValues.cause_title == null) {
    		layout.setVisibility(LinearLayout.GONE);
    		emptyloader.setVisibility(ProgressBar.VISIBLE);
    	} else {
    		layout.setVisibility(LinearLayout.VISIBLE);
    		emptyloader.setVisibility(ProgressBar.GONE);
    		title.setText(ParseValues.cause_title);
    		
    		if (loadfull) {
    			readmore.setText("Show Less...");
        		content.setText(ParseValues.cause_content);
    			loadedall = true;
    		} else {
    			readmore.setText("Show More...");
        		content.setText(ParseValues.cause_content.substring(0, ParseValues.cause_content.indexOf("transition is made")));
        		loadedall = false;
    		}
        }
    } 
    
    public void ParseCauseData() {
    	if (ParseValues.cause_title == null)
    		ForceParseCauseData();
    }
    
    public void ForceParseCauseData() {
    	APIclient.get("/data/volunteers/", null, getActivity(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
	        	try {
	    			ParseValues.cause_title   = response.getString("title");
	    			ParseValues.cause_content = response.getString("content");
	    			InflateCauseData(false);
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
	    .setNegativeButton("OK", null)
	    .show();
		
	}
}
