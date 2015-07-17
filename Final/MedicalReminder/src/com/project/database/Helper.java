package com.project.database;

import com.project.configuration.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper{
	
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + Constants.TABLE_MEDICINE;
	
	private static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + Constants.TABLE_MEDICINE + " (" +
		    Constants.MEDICINE_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT," +
		    Constants.MEDICINE_NAME + " TEXT" + "," +
		    Constants.MEDICINE_DOSE + " REAL" + "," +
		    Constants.MEDICINE_TYPE+ " TEXT" + "," +
		    Constants.MEDICINE_FREQUENCY + " INTEGER" + "," +
		    Constants.MEDICINE_INTERVAL + " TEXT" + "," +
		    Constants.MEDICINE_START_TIME + " INTEGER" + "," +
		    Constants.MEDICINE_START_DATE + " INTEGER" + "," +
		    Constants.MEDICINE_END_DATE + " INTEGER" + "," +
		    Constants.MEDICINE_IMAGE + " TEXT" +
		    " )";
	
	
	public Helper(Context context) {
		super(context, Constants.DATABASE_NAME, null, 4);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_ENTRIES);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_ENTRIES);	
		db.execSQL(SQL_CREATE_ENTRIES);	
	}
	

}
