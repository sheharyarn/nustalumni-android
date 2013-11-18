package com.impsycho.nustalumni;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends Activity {
	public static String USER_EMAIL = "user_email";
	private String userEmail;
	
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
	Button    contact;
	Button    update;

	TextView  email;
	TextView  change;
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_myprofile);
		
        getActionBar().setDisplayHomeAsUpEnabled(true);

    	emptyloader = (ProgressBar)  findViewById (R.id.myprofile_loader);
    	layout      = (LinearLayout) findViewById (R.id.myprofile_layout);
    	
    	layout.setVisibility(LinearLayout.GONE);
            	
    	namefirst   = (EditText) findViewById (R.id.myprofile_firstname);
    	namelast    = (EditText) findViewById (R.id.myprofile_lastname);
    	discipline  = (EditText) findViewById (R.id.myprofile_discipline);
    	course      = (EditText) findViewById (R.id.myprofile_course);
    	courseyear  = (EditText) findViewById (R.id.myprofile_courseyear);
    	campus      = (EditText) findViewById (R.id.myprofile_campus);
    	jobcompany  = (EditText) findViewById (R.id.myprofile_jobcompany);
    	jobposition = (EditText) findViewById (R.id.myprofile_jobposition);
    	
    	status      = (Spinner)  findViewById (R.id.myprofile_profstatus);
    	graduate    = (Spinner)  findViewById (R.id.myprofile_graduate);

    	contact     = (Button)   findViewById (R.id.myprofile_changepass);
    	update      = (Button)   findViewById (R.id.myprofile_update);
    	
    	email       = (TextView) findViewById (R.id.myprofile_email);
    	change      = (TextView) findViewById (R.id.myprofile_change);
    	image       = (ImageView)findViewById (R.id.myprofile_image);
    	
    	DisableEverything();
    	
        userEmail = getIntent().getStringExtra(ProfileActivity.USER_EMAIL);
        email.setText(userEmail);
        GetProfileData(userEmail);
        
    	contact.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {userEmail});
				emailIntent.setType("plain/text");
				startActivity(emailIntent);
			}
    	});
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    
    public void GetProfileData(String userEmail) {
		RequestParams params = new RequestParams();
		params.put("email", userEmail);
		
    	APIclient.get("/user/", params, ProfileActivity.this, new JsonHttpResponseHandler() {
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

	        		emptyloader.setVisibility(ProgressBar.GONE);
	        		layout.setVisibility(LinearLayout.VISIBLE);	
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
    
    public void DisableEverything() {
        namefirst.setEnabled(false);   
    	namelast.setEnabled(false);    
    	discipline.setEnabled(false);  
    	course.setEnabled(false);      
    	courseyear.setEnabled(false);  
    	campus.setEnabled(false);      
    	jobcompany.setEnabled(false);  
    	jobposition.setEnabled(false); 
    	status.setEnabled(false);      
    	graduate.setEnabled(false);    
    	update.setEnabled(false);     

    	change.setVisibility(TextView.GONE);
    	update.setVisibility(Button.GONE);  
    	contact.setText("Contact User");   
    }   

	public void ErrorAlert(String message) {
		new AlertDialog.Builder(ProfileActivity.this)
		    .setTitle("Error")
		    .setMessage(message)
		    .setNegativeButton("OK", null)
		    .show();
	}
    
}
