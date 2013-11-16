package com.impsycho.nustalumni;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyProfileFragment extends Fragment {
	ProgressBar emptyloader;
	LinearLayout layout;

	EditText  namefirst;
	EditText  namelast;
	EditText  discipline;
	EditText  course;
	EditText  courseyear;
	EditText  campus;
	EditText  jobcompany;
	EditText  jobposition;

	Spinner   status;
	Spinner   graduate;
	Button    password;
	Button    update;
	
	TextView  email;
	ImageView image;
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View profileView = inflater.inflate(R.layout.fragment_myprofile, container, false);
    	
    	emptyloader = (ProgressBar)profileView.findViewById(R.id.myprofile_loader);
    	layout = (LinearLayout)profileView.findViewById(R.id.myprofile_layout);
    	layout.setVisibility(LinearLayout.GONE);
    	
    	FrameLayout imagebox = (FrameLayout)profileView.findViewById(R.id.myprofile_imagebox);
    	imagebox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(getActivity())
			    .setTitle("Gravatar")
			    .setMessage("This app uses 'Gravatar' to show Profile Pictures. To add or change your Profile Picture, visit the Gravatar Website.\n\nGo to the Website now?")
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
    	
    	namefirst   = (EditText) profileView.findViewById (R.id.myprofile_firstname);
    	namelast    = (EditText) profileView.findViewById (R.id.myprofile_lastname);
    	discipline  = (EditText) profileView.findViewById (R.id.myprofile_discipline);
    	course      = (EditText) profileView.findViewById (R.id.myprofile_course);
    	courseyear  = (EditText) profileView.findViewById (R.id.myprofile_courseyear);
    	campus      = (EditText) profileView.findViewById (R.id.myprofile_campus);
    	jobcompany  = (EditText) profileView.findViewById (R.id.myprofile_jobcompany);
    	jobposition = (EditText) profileView.findViewById (R.id.myprofile_jobposition);
    	
    	status      = (Spinner)  profileView.findViewById (R.id.myprofile_profstatus);
    	graduate    = (Spinner)  profileView.findViewById (R.id.myprofile_graduate);

    	password    = (Button)   profileView.findViewById (R.id.myprofile_changepass);
    	update      = (Button)   profileView.findViewById (R.id.myprofile_update);
    	
    	email       = (TextView) profileView.findViewById (R.id.myprofile_email);
    	image       = (ImageView)profileView.findViewById (R.id.myprofile_image);
    	
    	password.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { ChangePassword(); }
    	});
    	update.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) { UpdateUserData(); }
    	});
    	
    	GetProfileData();
    	
    	return profileView;
    }
    
    public void GetProfileData() {
    	APIclient.get("/user/", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
	        	try {
	        		namefirst.setText(response.getString("name_first"));
	        		namelast.setText(response.getString("name_last"));
	        		email.setText(response.getString("email"));
	        		discipline.setText(response.getString("discipline"));
	        		course.setText(response.getString("course"));
	        		campus.setText(response.getString("campus"));
	        		jobcompany.setText(response.getString("job_company"));
	        		jobposition.setText(response.getString("job_position"));

	        		courseyear.setText(String.valueOf(response.getInt("course_year")));
	        		status.setSelection(response.getInt("professional_status"));
	        		graduate.setSelection((response.getBoolean("graduate"))? 1 : 0);
	        		
	        		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        	        .cacheInMemory(true)
	        	        .cacheOnDisc(true)
	        	        .build();
	        		ImageLoader.getInstance().displayImage(response.getString("image"), image, options);
	        		
	        		FinishUpdateProcedure();
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
    
    public void ChangePassword() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(LoginActivity.USER_SAVED, false);
		editor.putString(LoginActivity.USER_EMAIL, "");
		editor.putString(LoginActivity.USER_PASS, "");
		editor.commit();    	
    }
    
    public void UpdateUserData() {
    	StartUpdateProcedure();
		RequestParams params = new RequestParams();
		params.put("name_first",          namefirst.getText().toString());
		params.put("name_last",           namelast.getText().toString());
		params.put("campus",              campus.getText().toString());
		params.put("discipline",          discipline.getText().toString());
		params.put("job_company",         jobcompany.getText().toString());
		params.put("job_position",        jobposition.getText().toString());
		params.put("course",              course.getText().toString());
		params.put("course_year",         courseyear.getText().toString());

		params.put("graduate",            GraduateStatus(graduate.getSelectedItem().toString()));
		params.put("professional_status", GetStatusFrom(status.getSelectedItem().toString()));
		
		APIclient.post("/user/", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				new AlertDialog.Builder(getActivity())
				    .setTitle("Success!")
				    .setMessage("Your Profile Details have been Successfully Saved")
				    .setPositiveButton("OK", null)
				    .show();
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
	        	try {
	    			ErrorAlert(LoginActivity.Capitalize(response.getString("error")));
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFinish() { FinishUpdateProcedure(); }
		});
    	
    }
    
    public void FinishUpdateProcedure() {
		emptyloader.setVisibility(ProgressBar.GONE);
		layout.setVisibility(LinearLayout.VISIBLE);	
    }
    
    public void StartUpdateProcedure() {
		emptyloader.setVisibility(ProgressBar.VISIBLE);
		layout.setVisibility(LinearLayout.GONE);
    }
    
    public String GetStatusFrom(String item) {
    	String[] states = getResources().getStringArray(R.array.professional_status);

    	if (item.equals(states[0]))
    		return "0";
    	else if (item.equals(states[1]))
    		return "1";
    	else if (item.equals(states[2]))
    		return "2";
    	
    	return "0";	
    }
    
    public String GraduateStatus(String item) {
    	String[] states = getResources().getStringArray(R.array.bool);

    	if (item.equals(states[0]))
    		return "0";
    	else if (item.equals(states[1]))
    		return "1";
    	
    	return "0";	
    }
	
	public void ErrorAlert(String message) {
		new AlertDialog.Builder(getActivity())
	    .setTitle("Error")
	    .setMessage(message)
	    .setNegativeButton("Cancel", null)
	    .show();
	}
}
