package com.project.database;

import com.project.beans.Medicine;
import com.project.configuration.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Adapter {

	Helper helper;

	public Adapter(Context context) {
		helper = new Helper(context);
	}

	public long insertRow(Medicine medicine) {

		SQLiteDatabase db = helper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		// values.put(Constants.MEDICINE_ID, null);
		values.put(Constants.MEDICINE_NAME, medicine.getName());
		values.put(Constants.MEDICINE_DOSE, medicine.getDose());
		values.put(Constants.MEDICINE_TYPE, medicine.getType());
		values.put(Constants.MEDICINE_FREQUENCY, medicine.getFrecuency());
		values.put(Constants.MEDICINE_INTERVAL, medicine.getInterval());
		values.put(Constants.MEDICINE_START_TIME, medicine.getStartTime());
		values.put(Constants.MEDICINE_START_DATE, medicine.getStartDate());
		values.put(Constants.MEDICINE_END_DATE, medicine.getEndDate());
		values.put(Constants.MEDICINE_IMAGE, medicine.getImage());

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(Constants.TABLE_MEDICINE, null, values);
		return newRowId;
	}

	public Cursor selectRowById(long medicineId) {
		SQLiteDatabase db = helper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { Constants.MEDICINE_ID, Constants.MEDICINE_NAME,
				Constants.MEDICINE_DOSE, Constants.MEDICINE_TYPE,
				Constants.MEDICINE_FREQUENCY, Constants.MEDICINE_INTERVAL,
				Constants.MEDICINE_START_TIME, Constants.MEDICINE_START_DATE,
				Constants.MEDICINE_END_DATE, Constants.MEDICINE_IMAGE };

		// How you want the results sorted in the resulting Cursor
		// String sortOrder =
		// FeedEntry.COLUMN_NAME_UPDATED + " DESC";
		String selection = Constants.MEDICINE_ID + "=?";

		String[] selectionArgs = { "" + medicineId };

		Cursor c = db.query(Constants.TABLE_MEDICINE, // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
	}

	public Cursor selectRowByTime(long currentTime) {
		SQLiteDatabase db = helper.getReadableDatabase();

		long min = currentTime - 30 * 1000;
		long max = currentTime + 30 * 1000;

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { Constants.MEDICINE_ID, Constants.MEDICINE_NAME,
				Constants.MEDICINE_DOSE, Constants.MEDICINE_TYPE,
				Constants.MEDICINE_FREQUENCY, Constants.MEDICINE_INTERVAL,
				Constants.MEDICINE_START_TIME, Constants.MEDICINE_START_DATE,
				Constants.MEDICINE_END_DATE, Constants.MEDICINE_IMAGE };

		// calculate start time needed currentTime=start+24/frequency
		// int startTime = currentTime - (24/);

		// How you want the results sorted in the resulting Cursor
		// String sortOrder =
		// FeedEntry.COLUMN_NAME_UPDATED + " DESC";
		String selection = Constants.MEDICINE_START_TIME + " BETWEEN ? AND ? ";

		// String[] selectionArgs = { currentTime + "- 24/"
		// + Constants.MEDICINE_FREQUENCY };
		String[] selectionArgs = { min + "", max + "" };

		Cursor c = db.query(Constants.TABLE_MEDICINE, // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);
		Log.i("time  ", "bnvdvxcgfc");
		while (c.moveToNext()) {
			Log.i("time  ",
					c.getLong(c.getColumnIndex(Constants.MEDICINE_START_TIME))
							+ "");
		}
		return c;
	}

	public Cursor selectAllRows() {
		SQLiteDatabase db = helper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { Constants.MEDICINE_ID, Constants.MEDICINE_NAME,
				Constants.MEDICINE_DOSE, Constants.MEDICINE_TYPE,
				Constants.MEDICINE_FREQUENCY, Constants.MEDICINE_INTERVAL,
				Constants.MEDICINE_START_TIME, Constants.MEDICINE_START_DATE,
				Constants.MEDICINE_END_DATE, Constants.MEDICINE_IMAGE };

		Cursor c = db.query(Constants.TABLE_MEDICINE, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				null // The sort order
				);

		return c;
	}

	public long updateRow(Medicine medicine) {

		SQLiteDatabase db = helper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		// values.put(Constants.MEDICINE_ID, medicine.getId());
		values.put(Constants.MEDICINE_NAME, medicine.getName());
		values.put(Constants.MEDICINE_DOSE, medicine.getDose());
		values.put(Constants.MEDICINE_TYPE, medicine.getType());
		values.put(Constants.MEDICINE_FREQUENCY, medicine.getFrecuency());
		values.put(Constants.MEDICINE_INTERVAL, medicine.getInterval());
		values.put(Constants.MEDICINE_START_TIME, medicine.getStartTime());
		values.put(Constants.MEDICINE_START_DATE, medicine.getStartDate());
		values.put(Constants.MEDICINE_END_DATE, medicine.getEndDate());
		values.put(Constants.MEDICINE_IMAGE, medicine.getImage());

		String selection = Constants.MEDICINE_ID + "= ? ";

		String[] selectionArgs = { "" + medicine.getId() };

		// update the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.update(Constants.TABLE_MEDICINE, values, selection,
				selectionArgs);

		return newRowId;
	}

	public long updateStartTime(long medicineId, long startTime) {

		SQLiteDatabase db = helper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		// values.put(Constants.MEDICINE_ID, medicine.getId());
		values.put(Constants.MEDICINE_START_TIME, startTime);
		values.put(Constants.MEDICINE_START_DATE, startTime);

		String selection = Constants.MEDICINE_ID + "= ? ";

		String[] selectionArgs = { "" + medicineId };

		// update the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.update(Constants.TABLE_MEDICINE, values, selection,
				selectionArgs);

		return newRowId;
	}

	public long deleteRow(long selected) {

		SQLiteDatabase db = helper.getWritableDatabase();

		// String query = "DELETE FROM " + Constants.TABLE_MEDICINE + " = ?";

		String selection = Constants.MEDICINE_ID + "= ? ";

		String[] selectionArgs = { "" + selected };

		// delete the row
		long newRowId;
		newRowId = db
				.delete(Constants.TABLE_MEDICINE, selection, selectionArgs);

		return newRowId;
	}

	public long deleteAllRows() {
		SQLiteDatabase db = helper.getWritableDatabase();

		// delete the row
		long newRowId;
		newRowId = db.delete(Constants.TABLE_MEDICINE, null, null);

		return newRowId;
	}

}