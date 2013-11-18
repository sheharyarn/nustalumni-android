package com.impsycho.nustalumni;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	public static String USER_SAVED = "user_saved";
	public static String USER_EMAIL = "user_email";
	public static String USER_PASS  = "user_password";
	
	private Boolean modeIsLogin = true;
	private SharedPreferences prefs;
	private ProgressBar progressbar;
	private TextView changemode;
	private Button loginbutton;
	private EditText user_email;
	private EditText user_name;
	private EditText user_pass;
	private EditText user_pass2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		user_email  = (EditText)findViewById(R.id.login_edit_email);
		user_name   = (EditText)findViewById(R.id.login_edit_name);
		user_pass   = (EditText)findViewById(R.id.login_edit_password);
		user_pass2  = (EditText)findViewById(R.id.login_edit_password2);
		
		changemode  = (TextView)findViewById(R.id.login_text_create);
		progressbar = (ProgressBar)findViewById(R.id.login_progress);
		loginbutton = (Button)findViewById(R.id.login_button);
		
		changemode.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) { ReverseLoginMode(); }
		});

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean(USER_SAVED, false)) {
			user_email.setText(prefs.getString(USER_EMAIL, ""));
			user_pass.setText(prefs.getString(USER_PASS, ""));
			LoginUser();
		}
	}

	public void ReverseLoginMode() {
		if (modeIsLogin) {
			changemode.setText("Already a User?");
			loginbutton.setText("Sign Up");
			user_name.setVisibility(EditText.VISIBLE);
			user_pass2.setVisibility(EditText.VISIBLE);

		} else {
			changemode.setText("Create Account?");
			loginbutton.setText("Login");

			user_name.setVisibility(EditText.GONE);
			user_pass2.setVisibility(EditText.GONE);
		}
		
		modeIsLogin = !modeIsLogin;
	}
	
	public void PerformLoginOperation(View view) {
		progressbar.setVisibility(ProgressBar.VISIBLE);
		
		if (modeIsLogin) {
			LoginUser();
		} else {
			if (!user_pass.getText().toString().equals(user_pass2.getText().toString())) {
				ErrorAlert("Your Passwords do not Match");
			} else if (user_pass.getText().toString().length() < 8) {
				ErrorAlert("Password Length is too short (Minimum 8 Characters)");
			} else {
				NewUser();
			}
		}
	}
	
	public void LoginUser() {
		LoginUser(user_email.getText().toString(), user_pass.getText().toString());
	}
	
	public void LoginUser(final String email, final String password) {
		StartLoginProcedure();
		
		RequestParams params = new RequestParams();
		params.put("email",    email);
		params.put("password", password);
		
		APIclient.post("/user/login/", params, LoginActivity.this, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
	        	try {
	    			APIclient.api_auth_token = response.getString("auth_token");
	    			SaveUserInSettings(email, password);
	    			StartSession(false);
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
				DeleteUserFromSettings();
	        	try {
	    			ErrorAlert(Capitalize(response.getString("error")));
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFinish() { FinishLoginProcedure(); }
		});
	}

	public void NewUser() {
		NewUser(user_email.getText().toString(), user_pass.getText().toString(), user_name.getText().toString());
	}
	
	public void NewUser(final String email, final String password, String name) {
		StartLoginProcedure();
		RequestParams params = new RequestParams();
		params.put("email",    email);
		params.put("name",     name);
		params.put("password", password);
		
		APIclient.post("/user/new/", params, LoginActivity.this,  new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
	        	try {
	    			APIclient.api_auth_token = response.getString("auth_token");
	    			SaveUserInSettings(email, password);
	    			StartSession(true);
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject response) {
				DeleteUserFromSettings();
	        	try {
	    			ErrorAlert(Capitalize(response.getString("error")));
	        	} catch (JSONException e1) { e1.printStackTrace(); }
			}
			
			@Override
			public void onFinish() { FinishLoginProcedure(); }
		});
	}
	
	public void ErrorAlert(String message) {
		new AlertDialog.Builder(this)
	    .setTitle("Error")
	    .setMessage(message)
	    .setNegativeButton("OK", null)
	    .show();
		
		FinishLoginProcedure();
	}
	
	public void FinishLoginProcedure() {
		progressbar.setVisibility(ProgressBar.INVISIBLE);
		user_email.setEnabled(true);
		user_name.setEnabled(true);
		user_pass.setEnabled(true);
		user_pass2.setEnabled(true);
		loginbutton.setEnabled(true);

		changemode.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) { ReverseLoginMode(); }
		});
	}
	
	public void StartLoginProcedure() {
		progressbar.setVisibility(ProgressBar.VISIBLE);
		user_email.setEnabled(false);
		user_name.setEnabled(false);
		user_pass.setEnabled(false);
		user_pass2.setEnabled(false);
		loginbutton.setEnabled(false);

		changemode.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) { }
		});
	}
	
	public void SaveUserInSettings(String email, String password) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(USER_SAVED, true);
		editor.putString(USER_EMAIL, email);
		editor.putString(USER_PASS, password);
		editor.commit();		
	}
	
	public void DeleteUserFromSettings() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(USER_SAVED, false);
		editor.putString(USER_EMAIL, "");
		editor.putString(USER_PASS, "");
		editor.commit();
		
	}
	
	public static String Capitalize(String input) {
		return (input.substring(0, 1).toUpperCase() + input.substring(1));
	}
	
	public void StartSession(Boolean newuser) {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.putExtra(NewUserActivity.NEW_USER, newuser);
        
		getApplicationContext().startActivity(intent);
        finish();
	}
	
}
