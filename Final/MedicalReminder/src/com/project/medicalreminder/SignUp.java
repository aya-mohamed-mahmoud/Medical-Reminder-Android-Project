package com.project.medicalreminder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.project.classes.IsNetworkAvailable;
import com.project.configuration.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity {
	EditText name;
	EditText password;
	EditText confirmPassword;
	Button signup;
	SharedPreferences sh;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		name = (EditText) findViewById(R.id.username_edit);
		password = (EditText) findViewById(R.id.password_edit);
		confirmPassword = (EditText) findViewById(R.id.confirm_password_edit);
		signup = (Button) findViewById(R.id.signup_btn);

		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IsNetworkAvailable.isNetworkAvailable(SignUp.this)) {
					if (name.getText().length() <= 0
							|| password.getText().length() <= 0
							|| confirmPassword.getText().length() <= 0) {
						Toast.makeText(getApplicationContext(),
								"Please, type a username and password",
								Toast.LENGTH_SHORT).show();
					} else if (!password.getText().toString()
							.equals(confirmPassword.getText().toString())) {
						Toast.makeText(getApplicationContext(),
								"Passwords don't match", Toast.LENGTH_SHORT)
								.show();
					} else {
						// Toast.makeText(getApplicationContext(),
						// "please wait Connecting to server..",
						// Toast.LENGTH_SHORT).show();
						//
						final Toast toast = Toast.makeText(
								getApplicationContext(),
								"please wait Connecting to server..",
								Toast.LENGTH_SHORT);
						toast.show();

						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								toast.cancel();
							}
						}, 1000);
						//
						new SignupTask().execute();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Network error. Enable WIFI", Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));// implict
				}
			}
		});

		sh = getSharedPreferences(Constants.SHARED_PREF_FILE, 0);// private file
		editor = sh.edit();
	}

	class SignupTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpConnectionParams.setConnectionTimeout(new BasicHttpParams(),
					3000);

			String result = null;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_URL
					+ Constants.SIGNUP_SERVLET);
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("username", name.getText()
					.toString()));
			nameValuePair.add(new BasicNameValuePair("password", password
					.getText().toString()));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}

			try {
				HttpResponse response = httpClient.execute(httpPost);

				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity resEntity = response.getEntity();
					result = EntityUtils.toString(resEntity);
				} else {
					return null;
				}
				Log.d("aya ", "response " + result);

			} catch (ClientProtocolException e) {
				// writing exception to log
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();
				return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				if (result.equalsIgnoreCase("ok")) {
					//
					editor.putBoolean("flag", true);
					editor.putString(Constants.USER_NAME,
							name.getText().toString()).commit();
					editor.putString(Constants.PASSWORD,
							password.getText().toString()).commit();
					startActivity(new Intent(SignUp.this, Login.class));
					finish();
					Toast.makeText(getApplicationContext(),
							"Welcome to Medical Reminder", Toast.LENGTH_SHORT)
							.show();
					//

				} else if (result.equalsIgnoreCase("exists")) {
					Toast.makeText(getApplicationContext(),
							"Sorry, this username already exists",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Sorry, invalid data", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Sorry, connection failed try again",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

}
