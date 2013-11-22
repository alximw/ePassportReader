package com.uc3m.epassportreader.DataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.util.Log;

import com.uc3m.epassportreader.Data.Credentials;

public class DataBaseHandler {

	private Context context;

	public DataBaseHandler(Context context){
		this.context = context;
	}

	/**
	 * Insert new record in the epassport table of the database	
	 * @param ePassport Credentials of the ePassport
	 */
	public void insertEPassport(Credentials ePassport){
		try{
			//Open the database
			DataBaseHelper databaseHelper = new DataBaseHelper(context);
			SQLiteDatabase db = databaseHelper.getWritableDatabase();

			try{
				//Create the object to insert in the database
				ContentValues cv = new ContentValues();
				cv.put(DataBaseHelper.DOCUMENT_NUMBER, ePassport.getePassportID());
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
				String birthDate = dateFormat.format(ePassport.getBirthDate());
				cv.put(DataBaseHelper.DATE_OF_BIRTH, birthDate);
				String expiryDate = dateFormat.format(ePassport.getExpiryDate());
				cv.put(DataBaseHelper.EXPIRATION_DATE, expiryDate);

				//Insert the values of the ContentValues object in the table of the database
				Log.d("DEBUG", "Inserting in the ePassport table");	
				db.insert("ePassport", DataBaseHelper.DOCUMENT_NUMBER, cv);

			} catch (ParseException e) {
				e.printStackTrace();  
				Log.e("ERROR", "ERROR: Failed to format the date: " + e.getStackTrace());
			}

			//Close the database
			db.close();
		}
		catch(Exception e){
			e.printStackTrace(); 
			Log.e("ERROR", "ERROR: Failed to insert in the ePassport table: " + e.getStackTrace());
		}
	}

	/**
	 * Update a record in the epassport table of the database	
	 * @param ePassport Credentials of the ePassport
	 */
	public void updateEPassport(Credentials ePassport){
		try{
			//Open the database
			DataBaseHelper databaseHelper = new DataBaseHelper(context);
			SQLiteDatabase db = databaseHelper.getWritableDatabase();

			try{
				//Create the object to update in the database
				ContentValues cv = new ContentValues();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
				String birthDate = dateFormat.format(ePassport.getBirthDate());
				cv.put(DataBaseHelper.DATE_OF_BIRTH, birthDate);
				String expiryDate = dateFormat.format(ePassport.getExpiryDate());
				cv.put(DataBaseHelper.EXPIRATION_DATE, expiryDate);
				String whereConstrait = DataBaseHelper.DOCUMENT_NUMBER + "=" + ePassport.getePassportID();

				//Insert the values of the ContentValues object in the table of the database
				Log.d("DEBUG", "Update the ePassport table");	
				db.update("ePassport", cv, whereConstrait, null);

			} catch (ParseException e) {
				e.printStackTrace();  
				Log.e("ERROR", "ERROR: Failed to format the date: " + e.getStackTrace());
			}

			//Close the database
			db.close();
		}
		catch(Exception e){
			e.printStackTrace(); 
			Log.e("ERROR", "ERROR: Failed to update the ePassport table: " + e.getStackTrace());
		}
	}


	/**
	 * Delete a record in the epassport table of the database	
	 * @param ePassport Credentials of the ePassport
	 */
	public void deleteEPassport(Credentials ePassport){
		try{
			//Open the database
			DataBaseHelper databaseHelper = new DataBaseHelper(context);
			SQLiteDatabase db = databaseHelper.getWritableDatabase();

			try{
				
				String whereConstrait = DataBaseHelper.DOCUMENT_NUMBER + "=" + ePassport.getePassportID();

				//Insert the values of the ContentValues object in the table of the database
				Log.d("DEBUG", "Delete a record of the ePassport table");	
				db.delete("ePassport", whereConstrait, null);

			} catch (ParseException e) {
				e.printStackTrace();  
				Log.e("ERROR", "ERROR: Failed to format the date: " + e.getStackTrace());
			}

			//Close the database
			db.close();
		}
		catch(Exception e){
			e.printStackTrace(); 
			Log.e("ERROR", "ERROR: Failed to delete a record of the ePassport table: " + e.getStackTrace());
		}
	}
	/**
	 * 
	 * @return List with all the ePassports in the database
	 */
	public ArrayList <Credentials> getEPassports(){
		ArrayList <Credentials> result = new ArrayList<Credentials>();
		try{
			//Open the database
			DataBaseHelper databaseHelper = new DataBaseHelper(context);
			SQLiteDatabase db = databaseHelper.getWritableDatabase();

			//Perform the query and store it in an Cursor object
			String select = "SELECT * from ePassport";
			Cursor cursor = db.rawQuery(select,null);

			Log.d("DEBUG", "Searching in the database...");	
			if(cursor.moveToFirst()){
				do{
					try{
						Credentials epassport = new Credentials();
						epassport.setePassportID(cursor.getString(0)); //Position 0: Document number
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
						Date birthDate = dateFormat.parse(cursor.getString(1)); //Position 1: Date of birth
						epassport.setBirthDate(birthDate); 
						Date expiryDate = dateFormat.parse(cursor.getString(2)); //Position 2: Date of expiration
						epassport.setExpiryDate(expiryDate);
						result.add(epassport);
					} catch (ParseException e) {
						e.printStackTrace();  
						Log.e("ERROR", "ERROR: Failed to parse the date: " + e.getStackTrace());
					}
				} while(cursor.moveToNext());
			}

			//Close Cursor object
			cursor.close();

			//Close database
			db.close();
		}
		catch(Exception e){
			e.printStackTrace(); 
			Log.e("ERROR", "ERROR: Failed to select the ePassport table: " + e.getStackTrace());
		}		
		return result;
	}


}
