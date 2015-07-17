package com.project.medicalreminder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.beans.Medicine;
import com.project.database.Adapter;

public class AddMedicineActivity extends Activity {

	// Activity request codes to open camera
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "medical";

	// header layouts
	LinearLayout headerMedicineName;
	LinearLayout headerDose;
	LinearLayout headerMedicineType;
	LinearLayout headerReminderSpinner;
	LinearLayout headerReminderTime;
	LinearLayout headerInterval;
	LinearLayout headerStartDate;
	LinearLayout headerEndDate;

	// header text views
	TextView medicineNameHeaderTextView;
	TextView doseHeaderTextView;
	TextView medicineTypeHeaderTextView;
	TextView reminderHeaderTextView;
	TextView intervalHeaderTextView;
	TextView startDateHeaderTextView;
	TextView endDateHeaderTextView;

	// content layouts
	LinearLayout contentMedicineName;
	LinearLayout contentDose;
	LinearLayout contentMedicineType;
	LinearLayout contentReminder;
	LinearLayout contentInterval;
	LinearLayout contentStartDate;
	LinearLayout contentEndDate;

	// buttons
	ImageButton btnAddPhoto;

	// image capture
	ImageView imageView;
	Uri fileUri;
	Bitmap bitmap;

	// saving to variables to a medicine object
	Medicine medicine;

	// action text views
	TextView timePickerTextView;
	TextView startDatePickerTextView;
	TextView endDatePickerTextView;

	// inserted values in edit text
	EditText medicineNameEditText;
	EditText doseEditText;
	Spinner medicineTypeSpinner;
	Spinner frequencySpinner;
	Spinner intervalSpinner;

	// returned values as strings
	String timePickerString;
	String startDatePickerString;
	String endDatePickerString;
	String medicineNameEditString;
	String doseEditString;
	String medicineTypeSpinnerString;
	String frequencySpinnerString;
	String intervalSpinnerString;

	// date and time as milliseconds
	int hourOfDay = 8;
	int minuteOfHour = 0;
	long startTime = Calendar.getInstance().getTimeInMillis();
	long startDate = Calendar.getInstance().getTimeInMillis();
	long endDate = Calendar.getInstance().getTimeInMillis() * 2;
	String imagePath = "default";

	// error layout
	LinearLayout errorMsgLayout;

	long medicineId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_medicine);

		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTimeInMillis(System.currentTimeMillis());
		startDate = calendarNow.getTimeInMillis();
		startTime = calendarNow.getTimeInMillis();
		endDate = calendarNow.getTimeInMillis() * 2;

		// error message
		errorMsgLayout = (LinearLayout) findViewById(R.id.contentMedicineNameError);

		// image
		imageView = (ImageView) findViewById(R.id.imagePreview);

		// header layouts
		headerMedicineName = (LinearLayout) findViewById(R.id.headerMedicineName);
		headerDose = (LinearLayout) findViewById(R.id.headerDose);
		headerMedicineType = (LinearLayout) findViewById(R.id.headerMedicineType);
		headerReminderSpinner = (LinearLayout) findViewById(R.id.headerReminderSpinner);
		headerReminderTime = (LinearLayout) findViewById(R.id.headerReminderTime);
		headerInterval = (LinearLayout) findViewById(R.id.headerInterval);
		headerStartDate = (LinearLayout) findViewById(R.id.headerStartDate);
		headerEndDate = (LinearLayout) findViewById(R.id.headerEndDate);

		// header text views
		medicineNameHeaderTextView = (TextView) findViewById(R.id.medicineNameHeaderTextView);
		doseHeaderTextView = (TextView) findViewById(R.id.doseHeaderTextView);
		medicineTypeHeaderTextView = (TextView) findViewById(R.id.medicineTypeHeaderTextView);
		reminderHeaderTextView = (TextView) findViewById(R.id.reminderHeaderText);
		intervalHeaderTextView = (TextView) findViewById(R.id.intervalHeaderTextView);
		startDateHeaderTextView = (TextView) findViewById(R.id.startDateHeaderTextView);
		endDateHeaderTextView = (TextView) findViewById(R.id.endDateHeaderTextView);

		// content layouts
		contentMedicineName = (LinearLayout) findViewById(R.id.contentMedicineName);
		contentDose = (LinearLayout) findViewById(R.id.contentDose);
		contentMedicineType = (LinearLayout) findViewById(R.id.contentMedicineType);
		contentReminder = (LinearLayout) findViewById(R.id.contentReminder);
		contentInterval = (LinearLayout) findViewById(R.id.contentInterval);
		contentStartDate = (LinearLayout) findViewById(R.id.contentStartDate);
		contentEndDate = (LinearLayout) findViewById(R.id.contentEndDate);

		// edit values
		timePickerTextView = (TextView) findViewById(R.id.RemindeTimePicker);
		startDatePickerTextView = (TextView) findViewById(R.id.startDatePickerTextView);
		endDatePickerTextView = (TextView) findViewById(R.id.endDatePickerTextView);
		medicineNameEditText = (EditText) findViewById(R.id.medicineNameEditText);
		doseEditText = (EditText) findViewById(R.id.doseEditText);
		medicineTypeSpinner = (Spinner) findViewById(R.id.medicineTypeSpinner);
		frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
		intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);

		// expandable layouts settings
		initializeTextAndSetListener(headerMedicineName,
				medicineNameHeaderTextView, R.string.medicineName,
				contentMedicineName);
		initializeTextAndSetListener(headerDose, doseHeaderTextView,
				R.string.dose, contentDose);
		initializeTextAndSetListener(headerMedicineType,
				medicineTypeHeaderTextView, R.string.medicineType,
				contentMedicineType);
		initializeTextAndSetListener(headerReminderSpinner,
				reminderHeaderTextView, R.string.reminder, contentReminder);
		initializeTextAndSetListener(headerReminderTime,
				reminderHeaderTextView, R.string.reminderTime, contentReminder);
		initializeTextAndSetListener(headerInterval, intervalHeaderTextView,
				R.string.interval, contentInterval);
		initializeTextAndSetListener(headerStartDate, startDateHeaderTextView,
				R.string.startDate, contentStartDate);
		initializeTextAndSetListener(headerEndDate, endDateHeaderTextView,
				R.string.endDate, contentEndDate);

		// frequency spinner adapter
		Spinner spinner = (Spinner) findViewById(R.id.frequencySpinner);
		setSpinnerAdaptr(spinner, R.array.frequencyArray);
		// medicine type spinner adapter
		Spinner typeSpinner = (Spinner) findViewById(R.id.medicineTypeSpinner);
		setSpinnerAdaptr(typeSpinner, R.array.medicineTypeArray);
		// interval spinner adapter
		Spinner intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);
		setSpinnerAdaptr(intervalSpinner, R.array.intervalArray);

		medicineNameEditText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				errorMsgLayout.setVisibility(LinearLayout.GONE);
				return false;
			}
		});
		medicineNameEditText.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				errorMsgLayout.setVisibility(LinearLayout.GONE);
				return false;
			}
		});

		// reminder time dialog
		timePickerTextView = (TextView) findViewById(R.id.RemindeTimePicker);
		timePickerTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR);
				int minute = mcurrentTime.get(Calendar.MINUTE);

				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(AddMedicineActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker,
									int selectedHour, int selectedMinute) {
								hourOfDay = selectedHour;
								minuteOfHour = selectedMinute;
								Calendar temp = Calendar.getInstance();
								temp.set(Calendar.YEAR, Calendar.MONTH,
										Calendar.DAY_OF_MONTH, hourOfDay,
										minuteOfHour, 0);
								temp.set(Calendar.SECOND, 0);
								temp.set(Calendar.MILLISECOND, 0);
								startTime = temp.getTimeInMillis();
								startDate = temp.getTimeInMillis();

								timePickerTextView.setText("" + selectedHour
										+ ":" + selectedMinute);
							}
						}, hour, minute, false);

				mTimePicker.setTitle("Select first dose time");

				mTimePicker.show();
			}
		});

		// date picker of start date
		startDatePickerTextView = (TextView) findViewById(R.id.startDatePickerTextView);
		startDatePickerTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// To show current date in the datepicker
				Calendar mcurrentDate = Calendar.getInstance();
				int mYear = mcurrentDate.get(Calendar.YEAR);
				int mMonth = mcurrentDate.get(Calendar.MONTH);
				int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog mDatePicker;
				mDatePicker = new DatePickerDialog(AddMedicineActivity.this,
						new OnDateSetListener() {
							public void onDateSet(DatePicker datepicker,
									int selectedyear, int selectedmonth,
									int selectedday) {
								/* Your code to get date and time */
								Calendar selectedStartDate = Calendar
										.getInstance();
								selectedStartDate.set(selectedyear,
										selectedmonth, selectedday, hourOfDay,
										minuteOfHour, 0);
								selectedStartDate.set(Calendar.SECOND, 0);
								selectedStartDate.set(Calendar.MILLISECOND, 0);

								startDate = selectedStartDate.getTimeInMillis();
								startTime = selectedStartDate.getTimeInMillis();

								selectedmonth = selectedmonth + 1;
								startDatePickerTextView.setText(""
										+ selectedday + "-" + selectedmonth
										+ "-" + selectedyear);
							}
						}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Select start Date");

				mDatePicker.show();
			}
		});

		// date picker of end date
		endDatePickerTextView = (TextView) findViewById(R.id.endDatePickerTextView);
		endDatePickerTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// To show current date in the date picker
				Calendar mcurrentDate = Calendar.getInstance();
				int mYear = mcurrentDate.get(Calendar.YEAR);
				int mMonth = mcurrentDate.get(Calendar.MONTH);
				int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog mDatePicker;
				mDatePicker = new DatePickerDialog(AddMedicineActivity.this,
						new OnDateSetListener() {
							public void onDateSet(DatePicker datepicker,
									int selectedyear, int selectedmonth,
									int selectedday) {
								/* Your code to get date and time */
								Calendar selectedEndDate = Calendar
										.getInstance();
								selectedEndDate.set(selectedyear,
										selectedmonth, selectedday, 24, 60, 0);
								selectedEndDate.set(Calendar.SECOND, 0);
								selectedEndDate.set(Calendar.MILLISECOND, 0);
								endDate = selectedEndDate.getTimeInMillis();

								selectedmonth = selectedmonth + 1;
								endDatePickerTextView.setText("" + selectedday
										+ "-" + selectedmonth + "-"
										+ selectedyear);
							}
						}, mYear, mMonth, mDay);
				mDatePicker.setTitle("Select end date");

				mDatePicker.show();
			}
		});

		// opening camera
		btnAddPhoto = (ImageButton) findViewById(R.id.btnAddPhoto);
		btnAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("aya cam", "onClick");
				captureImage();
			}
		});
	}

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e("aya ", "Failed to create storage directory.");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
			return mediaFile;
		} else {
			return null;
		}

	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri photoUri = null;
				// successfully captured the image
				// display it in image view
				if (data == null) {
					photoUri = fileUri;
				} else {
					photoUri = data.getData();
				}
				previewCapturedImage();
				imagePath = photoUri.getPath();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/*
	 * Display image from a path to ImageView
	 */
	private void previewCapturedImage() {
		try {
			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);

			imageView.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Here we store the file url as it will be null pointer exception after
	 * returning from camera app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	/*
	 * Here we restore the fileUri again
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	// get the values that the user inserted as strings
	private void getInsertedValuesAsStrings() {
		// returned values as strings
		timePickerString = timePickerTextView.getText().toString();
		startDatePickerString = startDatePickerTextView.getText().toString();
		endDatePickerString = endDatePickerTextView.getText().toString();
		medicineNameEditString = medicineNameEditText.getText().toString();
		doseEditString = doseEditText.getText().toString();
		medicineTypeSpinnerString = medicineTypeSpinner.getSelectedItem()
				.toString();
		frequencySpinnerString = frequencySpinner.getSelectedItem().toString();
		intervalSpinnerString = intervalSpinner.getSelectedItem().toString();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_menu_add_medicine, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.saveMenuItem:
			getInsertedValuesAsStrings();
			if (medicineNameEditString.length() <= 0) {
				contentMedicineName.setVisibility(LinearLayout.VISIBLE);
				errorMsgLayout.setVisibility(LinearLayout.VISIBLE);
			} else {
				if (doseEditString.length() <= 0) {
					doseEditString = "1";
					saveMedicineToDB();
					startActivity(new Intent(AddMedicineActivity.this, HomeActivity.class));
					finish();
				}
			}
			break;
		case R.id.syncMenuItem:
			Toast.makeText(getApplicationContext(), "sync button",
					Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// set spinner array of data
	private void setSpinnerAdaptr(Spinner spinner, int resourceArray) {

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, resourceArray, R.layout.spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.drop_down_spinner);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}

	// headers listeners
	private void initializeTextAndSetListener(LinearLayout headerLayout,
			final TextView headerTextView, final int headerTextId,
			final LinearLayout contentLayout) {

		headerTextView.setText(getResources().getString(R.string.minusSign)
				+ " " + getResources().getString(headerTextId));

		headerLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// expandOrShrinkLayout(textView, stringId, contentLayout);
				if (contentLayout.getVisibility() == LinearLayout.GONE) {
					contentLayout.setVisibility(LinearLayout.VISIBLE);
					headerTextView.setText(getResources().getString(
							R.string.minusSign)
							+ " " + getResources().getString(headerTextId));
				} else {
					contentLayout.setVisibility(LinearLayout.GONE);
					headerTextView.setText(getResources().getString(
							R.string.plusSign)
							+ " " + getResources().getString(headerTextId));
				}
			}
		});
	}

	// save button action in sqlite
	public void saveMedicineToDB() {
		Adapter adapter = new Adapter(getApplicationContext());
		double dose = Double.parseDouble(doseEditString);
		int frequency = frequencySpinner.getSelectedItemPosition() + 1;
		Calendar defaultDate = Calendar.getInstance();
		defaultDate.setTimeInMillis(System.currentTimeMillis());
		defaultDate.set(Calendar.SECOND, 0);
		defaultDate.set(Calendar.MILLISECOND, 0);
		if (timePickerString.endsWith("AM")
				&& startDatePickerString.equalsIgnoreCase("Today")) {
			defaultDate.set(defaultDate.get(Calendar.YEAR),
					defaultDate.get(Calendar.MONTH),
					defaultDate.get(Calendar.DAY_OF_MONTH), 8, 0, 0);
			startTime = defaultDate.getTimeInMillis();
			startDate = defaultDate.getTimeInMillis();
		} else if (timePickerString.endsWith("AM")) {
			// Date startDateValue = new Date(startDate);
			defaultDate.set(defaultDate.get(Calendar.YEAR),
					defaultDate.get(Calendar.MONTH),
					defaultDate.get(Calendar.DAY_OF_MONTH), hourOfDay,
					minuteOfHour, 0);
			startTime = defaultDate.getTimeInMillis();
			startDate = defaultDate.getTimeInMillis();
		} else if (startDatePickerString.equalsIgnoreCase("Today")) {
			defaultDate.set(defaultDate.get(Calendar.YEAR),
					defaultDate.get(Calendar.MONTH),
					defaultDate.get(Calendar.DAY_OF_MONTH), hourOfDay,
					minuteOfHour, 0);
			startTime = defaultDate.getTimeInMillis();
			startDate = defaultDate.getTimeInMillis();
		}

		Medicine med = new Medicine(medicineNameEditString, dose,
				medicineTypeSpinnerString, frequency, intervalSpinnerString,
				startTime, startDate, endDate, imagePath);

		long inserted = adapter.insertRow(med);
		if (inserted != -1) {
			medicineId = inserted;
			// ///create pending intent for the alarm activity*
			Calendar cal = Calendar.getInstance();
			Intent intent = new Intent(this, Alert.class);
			intent.putExtra("medicineId", inserted);
			intent.putExtra("notification", true);
			PendingIntent pintent = PendingIntent.getActivity(this, 0, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			cal.setTimeInMillis(System.currentTimeMillis());
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm")
					.format(new Date(startTime));

			// get interval in hours
			long intervalTime = 24; // daily
			if (intervalSpinnerString.equalsIgnoreCase("Daily")) {
				intervalTime = 24;
			} else if (intervalSpinnerString.equalsIgnoreCase("Weekly")) {
				intervalTime = 168;
			} else if (intervalSpinnerString.equalsIgnoreCase("Monthly")) {
				intervalTime = 720;
			}

			intervalTime = intervalTime * 60 * 60 * 1000;

			alarm.setRepeating(AlarmManager.RTC_WAKEUP, startTime,
					(intervalTime / frequency), pintent);

		} else {
			String toastString = "Sorry, An error occured while signing up";
			Toast signUpToast = Toast.makeText(getApplicationContext(),
					toastString, Toast.LENGTH_SHORT);
			signUpToast.show();
		}

	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
}
