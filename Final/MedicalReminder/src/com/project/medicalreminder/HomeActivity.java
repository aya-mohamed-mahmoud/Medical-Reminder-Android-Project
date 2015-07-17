package com.project.medicalreminder;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.beans.Medicine;
import com.project.classes.IsNetworkAvailable;
import com.project.classes.MyArrayAdapter;
import com.project.configuration.Constants;
import com.project.database.Adapter;

public class HomeActivity extends Activity implements OnItemClickListener {
	Button add;
	TextView date;
	ListView list;
	LinearLayout listLayout;
	RelativeLayout emptyHomeLayout;
	Medicine[] listSource;
	int position;

	public SharedPreferences sh;
	public String userName = "";

	// @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		list = (ListView) findViewById(R.id.list);

		add = (Button) findViewById(R.id.additem);

		listLayout = (LinearLayout) findViewById(R.id.listLayout);
		emptyHomeLayout = (RelativeLayout) findViewById(R.id.emptyHomeLayout);

		// //////////////
		sh = getSharedPreferences("LoggedIn", 0);
		userName = sh.getString(Constants.USER_NAME, "no shared prefrence");
		// ////////////////

		setListAdapter();
		list.setOnItemClickListener(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setListAdapter();
	}

	public void setListAdapter() {
		// select all rows
		Adapter adapter = new Adapter(getApplicationContext());
		// list = new ListView(this);
		Cursor cursor = adapter.selectAllRows();
		if (cursor.getCount() <= 0) {
			listLayout.setVisibility(LinearLayout.GONE);
			emptyHomeLayout.setVisibility(LinearLayout.VISIBLE);
		} else {
			emptyHomeLayout.setVisibility(LinearLayout.GONE);
			listLayout.setVisibility(LinearLayout.VISIBLE);
			if (list == null) {
				list = new ListView(this);
				listLayout.addView(list);
			}
			listSource = new Medicine[cursor.getCount()];
			int i = 0;
			while (i < cursor.getCount() && cursor.moveToNext()) {
				int id = cursor.getInt(cursor
						.getColumnIndex(Constants.MEDICINE_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_NAME));
				double dose = cursor.getInt(cursor
						.getColumnIndex(Constants.MEDICINE_DOSE));
				String type = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_TYPE));
				cursor.getString(cursor.getColumnIndex(Constants.MEDICINE_NAME));
				int freq = cursor.getInt(cursor
						.getColumnIndex(Constants.MEDICINE_FREQUENCY));
				String interval = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_INTERVAL));
				long startTime = cursor.getLong(cursor
						.getColumnIndex(Constants.MEDICINE_START_TIME));
				long startDate = cursor.getLong(cursor
						.getColumnIndex(Constants.MEDICINE_START_DATE));
				long endDate = cursor.getLong(cursor
						.getColumnIndex(Constants.MEDICINE_END_DATE));
				String image = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_IMAGE));

				listSource[i] = new Medicine(id, name, dose, type, freq,
						interval, startTime, startDate, endDate, image);
				i++;
			}

			list.setAdapter(new MyArrayAdapter(getApplicationContext(),
					listSource));
			registerForContextMenu(list);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_action_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.addMenuItem:
			startActivity(new Intent(HomeActivity.this,
					AddMedicineActivity.class));
			finish();
			break;
		case R.id.syncMenuItem:
			if (IsNetworkAvailable.isNetworkAvailable(HomeActivity.this)) {

				Toast.makeText(getApplicationContext(),
						"please wait Connecting to server...",
						Toast.LENGTH_SHORT).show();
				// sync async task
				new AsyncNewData().execute();
			} else {
				Toast.makeText(getApplicationContext(),
						"Network error. Enable WIFI", Toast.LENGTH_SHORT)
						.show();
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.list) {
			ListView lv = (ListView) v;
			registerForContextMenu(lv);
			AdapterView.AdapterContextMenuInfo acmi = (android.widget.AdapterView.AdapterContextMenuInfo) menuInfo;
			this.position = acmi.position;
		}

		menu.setHeaderTitle("Options");
		menu.add("Edit");
		menu.add("Delete");

		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.menu_list, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Medicine medicine = listSource[position];

		if (item.getTitle() == "Edit") {
			Intent selectedItem = new Intent(getApplicationContext(),
					EditMedicine.class);
			selectedItem.putExtra("update", medicine);
			startActivity(selectedItem);
		} else if (item.getTitle() == "Delete") {
			Adapter adapter = new Adapter(getApplicationContext());
			adapter.deleteRow(medicine.getId()); // remove row from db
			setListAdapter();

		} else {
			return false;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> item, View Selectedview,
			int position, long arg3) {

		this.position = position;
	}

	// /////////////aya////////////////////////////
	class AsyncNewData extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String medecineName;
			double dose;
			String type;
			String image;
			int frequency;
			String interval;
			long startDate;
			long endDate;
			long startTime;

			HttpConnectionParams.setConnectionTimeout(new BasicHttpParams(),
					3000);
			String result = null;
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Constants.SERVER_URL
					+ Constants.ASYNC_SERVLET);
			ArrayList<Medicine> listofMedicine = new ArrayList<Medicine>();
			Gson gson = new Gson();
			Adapter db = new Adapter(getApplicationContext());
			Cursor cursor = db.selectAllRows();// select from sqlite
			while (cursor.moveToNext()) {
				medecineName = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_NAME));
				dose = cursor.getDouble(cursor
						.getColumnIndex(Constants.MEDICINE_DOSE));
				type = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_TYPE));
				image = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_IMAGE));
				frequency = cursor.getInt(cursor
						.getColumnIndex(Constants.MEDICINE_FREQUENCY));
				interval = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_INTERVAL));
				startDate = cursor.getLong(cursor
						.getColumnIndex(Constants.MEDICINE_START_DATE));
				endDate = cursor.getLong(cursor
						.getColumnIndex(Constants.MEDICINE_END_DATE));
				startTime = cursor.getLong(cursor
						.getColumnIndex(Constants.MEDICINE_START_TIME));
				Medicine medicine = new Medicine();
				medicine.setUserName(userName);// get from shared prefrence
				medicine.setName(medecineName);
				medicine.setInterval(interval);
				medicine.setDose(dose);
				medicine.setFrecuency(frequency);
				medicine.setStartDate(startDate);
				medicine.setEndDate(endDate);
				medicine.setImage(image);
				medicine.setType(type);
				medicine.setStartTime(startTime);
				listofMedicine.add(medicine);
			}
			String jasonObject = gson.toJson(listofMedicine);// convert list of
																// object to
																// json
			System.out.println("result json object " + jasonObject);//

			try {
				httpPost.setEntity(new StringEntity(jasonObject));
				HttpResponse response = httpClient.execute(httpPost);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity resEntity = response.getEntity();
					result = EntityUtils.toString(resEntity);
				}
			} catch (ClientProtocolException e) {
				// writing exception to log
				e.printStackTrace();
				// return null;
			} catch (IOException e) {
				// writing exception to log
				e.printStackTrace();
				// return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(),
					"Successfully Sync with Server", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

}
