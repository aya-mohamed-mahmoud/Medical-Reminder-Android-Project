package com.project.configuration;

public class Constants {

	// server connection
	public static final String SERVER_URL = "http://192.168.1.3:8080/";
	public static final String LOGIN_SERVLET = "MedicalServer/LoginServlet";
	public static final String SIGNUP_SERVLET = "MedicalServer/SignupServlet";
	public static final String ASYNC_SERVLET = "MedicalServer/Async";
	public static final String RESPONSE_OK = "ok";
	public static final String RESPONSE_NO = "no";

	// db connection
	public static final String DATABASE_NAME = "medical";
	public static final String DATABASE_USER_NAME = "root";
	public static final String DATABASE_PASSWORD = "root";
	public static final String TABLE_MEDICINE = "medicine";

	// db columns
	public static final String USER_NAME = "userName";
	public static final String PASSWORD = "password";

	public static final String MEDICINE_ID = "id";
	public static final String MEDICINE_NAME = "name";
	public static final String MEDICINE_DOSE = "dose";
	public static final String MEDICINE_TYPE = "type";
	public static final String MEDICINE_FREQUENCY = "frecuency";
	public static final String MEDICINE_INTERVAL = "interval";
	public static final String MEDICINE_START_TIME = "startTime";
	public static final String MEDICINE_START_DATE = "startDate";
	public static final String MEDICINE_END_DATE = "endDate";
	public static final String MEDICINE_IMAGE = "image";
	// public static final String MEDICINE_USER_NAME="userName";

	// frequency int values
	public static final String FREQUENCY_ONE = "Once a day";
	public static final String FREQUENCY_TWO = "Twice a day";
	public static final String FREQUENCY_THREE = "3 times a day";
	public static final String FREQUENCY_FOUR = "4 times a day";
	public static final String FREQUENCY_FIVE = "5 times a day";
	public static final String FREQUENCY_SIX = "6 times a day";
	public static final String FREQUENCY_SEVEN = "7 times a day";
	public static final String FREQUENCY_EIGHT = "8 times a day";
	public static final String FREQUENCY_NINE = "9 times a day";
	public static final String FREQUENCY_TEN = "10 times a day";
	public static final String FREQUENCY_ELEVEN = "11 times a day";
	public static final String FREQUENCY_TWELVE = "12 times a day";
	// public static final String URL =
	// "http://10.145.245.216:8080/MedicalServer/LoginServlet";

	// prefrences
	public static final String SHARED_PREF_FILE = "LoggedIn";

	private Constants() {

	}

}
