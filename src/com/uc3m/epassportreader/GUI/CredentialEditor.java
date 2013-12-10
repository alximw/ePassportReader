package com.uc3m.epassportreader.GUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.Credentials;
import com.uc3m.epassportreader.DataBase.DataBaseHandler;
import com.uc3m.epassportreader.Utils.Utils;
import com.uc3m.epassportreader.R.id;
import com.uc3m.epassportreader.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CredentialEditor extends Activity implements OnClickListener{

	
	DataBaseHandler handler; //Database handler in order tu call the database methods
	
	static EditText bDate; //view elements
	static EditText eDate;	
	static EditText docNumber;
	static Button save;
	
	public static class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

		
		
		public DatePicker(){
			super();
			
		}


		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			final Calendar c=Calendar.getInstance();
			int cYear=c.get(Calendar.YEAR);
			int cMonth=c.get(Calendar.MONTH);
			int cDay=c.get(Calendar.DAY_OF_MONTH);
			
			return new DatePickerDialog(getActivity(), this, cYear, cMonth, cDay);
		}

		@Override
		public void onDateSet(android.widget.DatePicker arg0, int year,int month, int day) {
			StringBuilder sB= new StringBuilder();
			int viewID=getArguments().getInt("viewID");
			

			sB.append(String.valueOf(day)).
			   append("/").
			   append(String.valueOf(month+1)). //+1 because is zero-based
			   append("/").
			   append(String.valueOf(year));

			updateView(sB.toString(), viewID);
		
			
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credentialseditor_layout);
	
		handler=new DataBaseHandler(getApplicationContext());
		
		//Initialize the view elements...
		
		bDate=(EditText)findViewById(R.id.editor_bDateInput);
		eDate=(EditText)findViewById(R.id.editor_eDateInput);
		docNumber=(EditText)findViewById(R.id.editor_docNumberInput);
		save=(Button)findViewById(R.id.editor_saveButton);
		
		//...and set the onClickListener
		bDate.setOnClickListener(this);
		eDate.setOnClickListener(this);
		docNumber.setOnClickListener(this);
		save.setOnClickListener(this);
		
		
		if(getIntent().getExtras()!=null){
		Credentials creds2Edit=(Credentials) getIntent().getExtras().getSerializable("credentials");
		updateView(creds2Edit.getBirthDateAsString(), bDate.getId());
		updateView(creds2Edit.getExpiryDateAsString(), eDate.getId());
		updateView(creds2Edit.getePassportID(), docNumber.getId());
		//disable user input to avoid PK modification
		docNumber.setKeyListener(null);
		
		}
		
	
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.editor_bDateInput:
			 //bDate
			Utils.debug(Utils.DEBUG, "on click eDateInput");
			showDatePicker(v);
			break;
		
		case R.id.editor_eDateInput:
			//eDate
			Utils.debug(Utils.DEBUG, "on click bDateInput");
			showDatePicker(v);
			break;
			
		
		case R.id.editor_saveButton:
			
			//create new empty Credential object
			Credentials newCredential=new Credentials();
			//create date formating 
			SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date=null,date2=null;
			
			try {
				//format both dates
				 date= sourceFormat.parse(bDate.getText().toString());
				 date2=sourceFormat.parse(eDate.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newCredential.setBirthDate(date);
			newCredential.setExpiryDate(date2);
			newCredential.setePassportID(docNumber.getText().toString());
			
			//TODO: check if we are adding or editing
			
			if(getIntent().getExtras()!=null){
				//editing
				handler.updateEPassport(newCredential);
			}else{
				//adding
				handler.insertEPassport(newCredential);
			}
			
			Intent i=new Intent(getApplicationContext(),CredentialChooser.class);
			startActivity(i);
			
			break;
			
		default:
			//Do nothing
			break;
		}
		
	}

	public static void updateView(String text, int viewID){

			switch(viewID){
			
			case R.id.editor_bDateInput:
				bDate.setText(text);
				break;
				
			case R.id.editor_eDateInput:
				eDate.setText(text);
				break;
			
			case R.id.editor_docNumberInput:
				docNumber.setText(text);
				break;
				
			default:
				//Do nothing
				break;
			}
		
		
		
	}
	
	public  void showDatePicker(View v){
		Bundle b=new Bundle();
		b.putInt("viewID",v.getId());
		DialogFragment newFragment = new DatePicker();
		newFragment.setArguments(b);
	    newFragment.show(getFragmentManager(), "datePicker");
	}

}
