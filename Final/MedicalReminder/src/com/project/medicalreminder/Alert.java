package com.project.medicalreminder;

import java.util.Calendar;

import com.project.configuration.Constants;
import com.project.database.Adapter;
import com.project.medicalreminder.R;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Alert extends Activity {

	final Context context = this;
	private Button button;
	Intent intent;

	long[] medicines;
	long medicineId;
	Calendar calendar;
	Cursor cursorNow;
	boolean finish = true;
	Adapter adapter;

	private NotificationManager mManager;

	// AddMedicineActivity medicine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);

		intent = getIntent();
		Calendar cal = Calendar.getInstance();
		medicineId = intent.getLongExtra("medicineId", 0);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		// select all rows
		adapter = new Adapter(getApplicationContext());
		cursorNow = adapter.selectRowByTime(cal.getTimeInMillis());

		Log.i("retrieved ", cursorNow.getCount() + "");

		medicines = new long[cursorNow.getCount()];

		Log.i("medicines ", medicines.length + "");

		int i = 0;
		if (cursorNow.moveToFirst()) {
			do {
				if (cursorNow.getLong(cursorNow
						.getColumnIndex(Constants.MEDICINE_END_DATE)) >= calendar
						.getTimeInMillis()) {

					finish = false;

					Log.i("inside while ", i + "");
					long id = cursorNow.getLong(cursorNow
							.getColumnIndex(Constants.MEDICINE_ID));
					Log.i("id in alert ", id + "");

					medicines[i] = id;

					Log.i("start time before: ",
							cursorNow.getLong(cursorNow
									.getColumnIndex(Constants.MEDICINE_START_TIME))
									+ "");
					long newStartTime = cursorNow.getLong(cursorNow
							.getColumnIndex(Constants.MEDICINE_START_TIME))
							+ 24
							* 60
							* 60
							* 1000
							/ cursorNow
									.getInt(cursorNow
											.getColumnIndex(Constants.MEDICINE_FREQUENCY));

					Log.i("start time after: ", "" + newStartTime + "");
					adapter.updateStartTime(id, newStartTime);
					i++;
				}
				// use value
			} while (cursorNow.moveToNext());
		}

		if (finish) {
			finish();
		} else {

			if (intent.getBooleanExtra("notification", false)) {
				mManager = (NotificationManager) this
						.getApplicationContext()
						.getSystemService(
								this.getApplicationContext().NOTIFICATION_SERVICE);
				Intent intent1 = new Intent(this.getApplicationContext(),
						Alert.class);
				intent1.putExtra("medicineId", medicineId);
				intent1.putExtra("notification", false);
				Notification notification = new Notification(
						R.drawable.medecine, "Medical Reminder",
						System.currentTimeMillis());
				intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);

				PendingIntent pendingNotificationIntent = PendingIntent
						.getActivity(this.getApplicationContext(), 0, intent1,
								PendingIntent.FLAG_UPDATE_CURRENT);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification
						.setLatestEventInfo(this.getApplicationContext(),
								"Time of Your Medicien ", "",
								pendingNotificationIntent);
				notification.defaults = Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE
						| Notification.DEFAULT_LIGHTS;

				mManager.notify(0, notification);

			}

			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.custom_alert);
			dialog.setTitle("Medicine Time");

			// set the custom dialog components - text, image and button
			TextView text = (TextView) dialog.findViewById(R.id.text);
			text.setText("You should take your medicenes now ^_^");

			Button dialogdismiss = (Button) dialog
					.findViewById(R.id.dialogButtonCancel);
			Button dialogtake = (Button) dialog
					.findViewById(R.id.dialogButtonOK);

			dialogtake.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					// Log.i("retrieved ", cursorNow.getCount() + "");
					Bundle b = new Bundle();
					b.putLongArray("medicines", medicines);

					Intent intent = new Intent(getApplicationContext(),
							LastListview.class);
					intent.putExtras(b);
					// intent.putExtras("medicines", medicines);
					startActivity(intent);
					finish();

				}
			});
			// if button is clicked, close the custom dialog
			dialogdismiss.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// ///////////make an alarm for the next
					// time.//////////////////////////
					int i = 0;
					if (cursorNow.moveToFirst()) {
						do {
							if (cursorNow.getLong(cursorNow
									.getColumnIndex(Constants.MEDICINE_END_DATE)) >= calendar
									.getTimeInMillis()) {

								finish = false;

								Log.i("inside while ", i + "");
								long id = cursorNow.getLong(cursorNow
										.getColumnIndex(Constants.MEDICINE_ID));
								Log.i("id in alert ", id + "");

								medicines[i] = id;

								Log.i("start time before: ",
										cursorNow.getLong(cursorNow
												.getColumnIndex(Constants.MEDICINE_START_TIME))
												+ "");
								long newStartTime = cursorNow.getLong(cursorNow
										.getColumnIndex(Constants.MEDICINE_START_TIME)) + 10 * 60 * 1000;

								Log.i("start time after: ", "" + newStartTime
										+ "");
								adapter.updateStartTime(id, newStartTime);
								i++;
							}
							// use value
						} while (cursorNow.moveToNext());
					}

					Intent intent = new Intent(Alert.this, Alert.class);
					// intent.putExtra("medicineId", inserted);
					intent.putExtra("notification", true);
					PendingIntent pintent = PendingIntent.getActivity(
							Alert.this, 0, intent,
							PendingIntent.FLAG_CANCEL_CURRENT);
					AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					// cal.setTimeInMillis(System.currentTimeMillis());

					alarm.setRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + 10 * 60 * 1000,
							10 * 60 * 1000, pintent);

					dialog.dismiss();
					finish();
				}
			});
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}