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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.beans.Medicine;
import com.project.classes.IsNetworkAvailable;
import com.project.configuration.Constants;
import com.project.database.Adapter;

public class Login extends Activity {
	EditText name;
	EditText password;
	Button login;

	SharedPreferences sh;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		name = (EditText) findViewById(R.id.username_edit);
		password = (EditText) findViewById(R.id.password_edit);
		login = (Button) findViewById(R.id.login_btn);

		sh = getSharedPreferences(Constants.SHARED_PREF_FILE, 0);// private file
		editor = sh.edit();

		// ////////////////////////
		// null is default value if no value
		String retrievedName = sh.getString(Constants.USER_NAME, null);
		String retrievedPassword = sh.getString(Constants.PASSWORD, null);
		if (retrievedName != null && retrievedPassword != null) {
			name.setText(retrievedName);
			password.setText(retrievedPassword);
		}

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (IsNetworkAvailable.isNetworkAvailable(Login.this)) {
					if (name.getText().length() <= 0
							|| password.getText().length() <= 0) {
						Toast.makeText(getApplicationContext(),
								"Please, type your username and password",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Connecting to server..", Toast.LENGTH_SHORT)
								.show();
						new LoginTask().execute();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Network error. Enable WIFI", Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				}
			}
		});
	}

	class LoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			HttpConnectionParams.setConnectionTimeout(new BasicHttpParams(),
					3000);
			String result = null;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_URL
					+ Constants.LOGIN_SERVLET);
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
			if (!result.equalsIgnoreCase("no") && result != null) {
				//
				editor.putBoolean("flag", true);
				editor.putString(Constants.USER_NAME, name.getText().toString())
						.commit();
				editor.putString(Constants.PASSWORD,
						password.getText().toString()).commit();
				startActivity(new Intent(Login.this, Login.class));
				finish();
				Toast.makeText(getApplicationContext(),
						"Welcome to Medical Reminder", Toast.LENGTH_SHORT)
						.show();
				//
				Gson gson = new Gson();
				Medicine[] obj = gson.fromJson(result, Medicine[].class);
				Adapter DBObj = new Adapter(getApplicationContext());
				for (int counter = 0; counter < obj.length; counter++) {
					String name = obj[counter].getName();
					double dose = obj[counter].getDose();
					String type = obj[counter].getType();
					Long isInserted = DBObj.insertRow(obj[counter]);
					System.out.println("inserted" + isInserted);
					DBObj.selectAllRows();
					System.out.println("selected from DB");

				}// for
				startActivity(new Intent(Login.this, HomeActivity.class));
				finish();
			} else {
				Toast.makeText(Login.this,
						"Sorry, wrong username and/or password",
						Toast.LENGTH_SHORT).show();
			}// else
		}
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
}
