package com.uc3m.epassportreader.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "ePassportsDataBase.db";

	public static final String DOCUMENT_NUMBER = "ePassportID";
	public static final String DATE_OF_BIRTH = "birthDate";
	public static final String EXPIRATION_DATE = "expiryDate";

	public DataBaseHelper(Context context){
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db){ 
		try{
			Log.d("DEBUG", "Create tables of the database");	
			db.execSQL("CREATE TABLE IF NOT EXISTS ePassport (" +
					"ePassportID TEXT PRIMARY KEY NOT NULL, " +
					"birthDate TEXT NOT NULL, " +
					"expiryDate TEXT NOT NULL);");
		}
		catch(Exception e){
			e.printStackTrace(); 
			Log.e("ERROR", "Failed to create the tables of the database: " + e.getStackTrace());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		try{
			Log.d("DEBUG", "Drop tables of the database");	
			db.execSQL("DROP TABLE IF EXISTS ePassport");
			onCreate(db);
		}
		catch(Exception e){
			e.printStackTrace(); 
			Log.e("ERROR", "Failed to drop the tables of the database: " + e.getStackTrace());
		}
	}
}