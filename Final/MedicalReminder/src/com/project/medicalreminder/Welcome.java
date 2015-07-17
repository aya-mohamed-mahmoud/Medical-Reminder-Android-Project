package com.project.medicalreminder;

import com.project.configuration.Constants;
import com.project.medicalreminder.R.id;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Welcome extends Activity implements OnClickListener {
	Button signup, login;
	SharedPreferences sh;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		// getActionBar().hide();
		sh = getSharedPreferences(Constants.SHARED_PREF_FILE, 0);
		boolean prefValue = sh.getBoolean("flag", false);
		if (prefValue) {
			startActivity(new Intent(Welcome.this, HomeActivity.class));
		}

		signup = (Button) findViewById(R.id.signup_btn);
		login = (Button) findViewById(R.id.login_btn);
		signup.setOnClickListener(this);
		login.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signup_btn:
			Intent signup = new Intent(getApplicationContext(), SignUp.class);
			startActivity(signup);
			break;

		case R.id.login_btn:
			Intent login = new Intent(getApplicationContext(), Login.class);
			startActivity(login);
			finish();
			break;
		}
	}

}
