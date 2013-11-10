package com.impsycho.nustalumni;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	private Boolean modeIsLogin = true;
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
				new AlertDialog.Builder(this)
				    .setTitle("Error")
				    .setMessage("Your Passwords do not match")
				    .setNegativeButton("Try Again", null)
				    .show();
			} else if (user_pass.getText().toString().length() < 8) {
				new AlertDialog.Builder(this)
				    .setTitle("Error")
				    .setMessage("Password Length is too short (Minimum 8 Characters)")
				    .setNegativeButton("Try Again", null)
				    .show();
			} else {
				NewUser();
			}
		}
	}
	
	public void LoginUser() {
		
	}
	
	public void NewUser() {
		RequestParams params = new RequestParams();
		params.put("email", user_email.getText().toString());
		params.put("name",  user_name.getText().toString());
		params.put("pass",  user_pass.getText().toString());
		
		APIclient.post("/user/new/", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject Response) {
				
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject Response) {
				
			}
			
			@Override
			public void onFinish() {
				progressbar.setVisibility(ProgressBar.INVISIBLE);
			}
		});
	}

}
