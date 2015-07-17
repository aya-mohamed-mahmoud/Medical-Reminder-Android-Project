package com.project.medicalreminder;

import com.project.beans.Medicine;
import com.project.classes.MyArrayAdapter;
import com.project.classes.ViewCache;
import com.project.configuration.Constants;
import com.project.database.Adapter;
import com.project.medicalreminder.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import dialogueactivity.ITI.JETS.*;

public class LastListview extends Activity implements OnItemClickListener {

	ListView list;
	String[] value;
	final Context context = this;
	private Button button;
	LinearLayout listHolderLayout;

	long[] medicines;
	long medicineId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.last_listview);
		list = (ListView) findViewById(R.id.picturelist);
		setTitle("Medicines to take now");
		listHolderLayout = (LinearLayout) findViewById(R.id.listHolderLayout);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// populate the fields with data

		// medicines = (Medicine[]) intent.getSerializableExtra("medicines");
		Adapter adapterDB = new Adapter(getApplicationContext());
		Bundle b = this.getIntent().getExtras();
		medicines = b.getLongArray("medicines");
		Log.i("retrieved from bundle ", medicines.length + "");
		//
		if (medicines.length > 0) {
			value = new String[medicines.length];
			Log.i("values string count ", value.length + "");
			for (int i = 0; i < medicines.length; i++) {
				Cursor cursor = adapterDB.selectRowById(medicines[i]);
				cursor.moveToFirst();
				Log.i("cursor count ", cursor.getCount() + "");
				value[i] = cursor.getString(cursor
						.getColumnIndex(Constants.MEDICINE_NAME));
				Log.i("name ", value[i] + "");
			}
			list.setOnItemClickListener(this);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_list_item_1, value);
			MyAdapter myAdapter = new MyAdapter(this, value);
			list.setAdapter(myAdapter);
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("XXX", "on restart");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("XXX", "on pause");
	}

	public void onItemClick(AdapterView<?> l, View v, final int position,
			long id) {

		final long selected = medicines[position];
		// Intent intent = new Intent(LastListview.this, AlertDialog.class);
		// intent.putExtra("medicineId", selected);
		// startActivity(intent);
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.alert_dialogue);

		Adapter adapterDB = new Adapter(getApplicationContext());
		Cursor cursor = adapterDB.selectRowById(selected);
		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor
					.getColumnIndex(Constants.MEDICINE_NAME));
			double dose = cursor.getInt(cursor
					.getColumnIndex(Constants.MEDICINE_DOSE));
			String type = cursor.getString(cursor
					.getColumnIndex(Constants.MEDICINE_TYPE));
			String image = cursor.getString(cursor
					.getColumnIndex(Constants.MEDICINE_IMAGE));

			dialog.setTitle(name);
			// set the custom dialog components - text, image and button
			TextView text = (TextView) dialog.findViewById(R.id.text);
			text.setText(" Dose:  " + dose + "  " + type);
			ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
			if (image.equals("default")) {
				imageView.setImageResource(R.drawable.medecine);
			} else {
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeFile(image, options);
				imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 150,
						150, true));
			}

		}

		Button dialogdismiss = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		Button dialogtake = (Button) dialog.findViewById(R.id.dialogButtonOK);

		// if button is clicked, close the custom dialog
		dialogtake.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listHolderLayout.removeAllViews();
				deleteRowFromList(position);
				dialog.dismiss();
				// finish();

			}
		});
		// if button is clicked, close the custom dialog
		dialogdismiss.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// finish();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public void deleteRowFromList(int position) {

		if (value.length - 1 <= 0) {
			finish();
		} else {
			String[] newValues = new String[value.length - 1];
			if (list == null) {
				list = new ListView(this);
			}

			for (int i = 0; i < value.length; i++) {
				if (i < position) {
					newValues[i] = value[i];
				} else if (i == position) {

				} else if (i > position) {
					newValues[i - 1] = value[i];
				}
			}

			for (int i = 0; i < value.length; i++) {
				Log.i("before", value[i]);
			}
			for (int i = 0; i < newValues.length; i++) {
				Log.i("after", newValues[i]);
			}

			value = newValues;
			Log.i("after", "finished");
			listHolderLayout.addView(list);
			list.setAdapter(new MyAdapter(getApplicationContext(), newValues));
		}
	}

	class MyAdapter extends ArrayAdapter<String> {

		private final Context context;
		private final String[] values;

		public MyAdapter(Context context, String[] values) {
			super(context, R.layout.list_row, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			view = convertView;
			ViewCache viewcache;
			final int currentPosition = position;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_row, parent, false);
				viewcache = new ViewCache(view);
				view.setTag(viewcache);
			} else {
				viewcache = (ViewCache) view.getTag();
			}

			TextView txtdata = (TextView) view.findViewById(R.id.txtdata);
			txtdata.setText(values[currentPosition]);
			return view;
		}

	}
}
