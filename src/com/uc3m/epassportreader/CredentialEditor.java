package com.uc3m.epassportreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.uc3m.epassportreader.Data.Credentials;
import com.uc3m.epassportreader.GUI.CredentialChooser;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CredentialEditor extends Activity implements OnClickListener{

	
	static EditText bDate;
	static EditText eDate;
	EditText docNumber;
	Button save;
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
	
		//Initialize activity views
		
		bDate=(EditText)findViewById(R.id.editor_bDateInput);
		eDate=(EditText)findViewById(R.id.editor_eDateInput);
		docNumber=(EditText)findViewById(R.id.editor_docNumberInput);
		save=(Button)findViewById(R.id.editor_saveButton);
		
		bDate.setOnClickListener(this);
		eDate.setOnClickListener(this);
		docNumber.setOnClickListener(this);
		save.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.editor_bDateInput:
			 //bDate
			Log.d("DEBUG_Editor","on click eDateInput");
			showDatePicker(v);
			break;
		
		case R.id.editor_eDateInput:
			//eDate
			Log.d("DEBUG_Editor","on click bDateInput");
			showDatePicker(v);
			break;
			
		
		case R.id.editor_saveButton:
			
			Credentials cred=new Credentials();
			SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date=null,date2=null;
			try {
				 date= sourceFormat.parse(bDate.getText().toString());
				 date2=sourceFormat.parse(eDate.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cred.setBirthDate(date);
			cred.setBirthDate(date2);
			cred.setePassportID(docNumber.getText().toString());
			CredentialChooser.adapter.addItem(cred);
			
			Intent i=new Intent(getApplicationContext(),CredentialChooser.class);
			startActivity(i);

			break;
			
		default:
			//Do nothing
			break;
		}
		
	}

	public static void updateView(String date, int viewID){

			switch(viewID){
			
			case R.id.editor_bDateInput:
				Log.d("DEBUG", "bDate");
				bDate.setText(date);
				break;
				
			case R.id.editor_eDateInput:
				Log.d("DEBUG", "eDate");
				eDate.setText(date);
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
