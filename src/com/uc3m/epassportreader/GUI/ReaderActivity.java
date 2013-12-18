package com.uc3m.epassportreader.GUI;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.widget.Toast;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.Credentials;
import com.uc3m.epassportreader.Utils.Utils;
import com.uc3m.epassportreader.com.AsyncCommunication;
import com.uc3m.epassportreader.com.ePassportCommunication;


public class ReaderActivity extends Activity implements OnCancelListener {

private Credentials BAC;	
private String[][] techList;	
private NfcAdapter adapter;

PendingIntent pendingIntent;
IntentFilter techDetected;

ProgressDialog readInProgress;

private long startTime=0;
private long endTime=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		//create a new Pending Intent to be filled when a tag is detected
		 pendingIntent= PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		techDetected= new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		techList= new String[][] { new String[] { IsoDep.class.getName()} };
		adapter=NfcAdapter.getDefaultAdapter(this);
		handleNewIntent(getIntent());
		
		
		
	}
	
	
	
	private void handleNewIntent(Intent intent){
		String intentAction=intent.getAction();
		
		
		if(intentAction.equals((NfcAdapter.ACTION_TECH_DISCOVERED)) ){
			
			Utils.debug(Utils.INFO,"ISODEP tag detected");
			Tag foundTag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			beginIsoDepCommunication(IsoDep.get(foundTag));
			
			
		}else if (intentAction.equals(CredentialChooser.READING_ACTION)){
			
			Utils.debug(Utils.INFO,"BAC received from CredentialChooser");
			BAC=intent.getExtras().getParcelable("BAC_EXTRA");
			
		}
			
		
		
		
	}
	
	
	
	private void beginIsoDepCommunication(IsoDep isodep){
		//progresDialog
		//crear conexion
		//tarea asincrona que la maneje y devuelva un resultado
		Utils.debug(Utils.INFO,"Beggining communication with ISODEP tag");
		
		readInProgress=new ProgressDialog(this);
		readInProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		readInProgress.setMessage("");
		readInProgress.setCancelable(true);
		readInProgress.setOnCancelListener(this);
		
		//show the progress dialog
		readInProgress.show();
		//we wait no more than 10s for a transceive() operation
		isodep.setTimeout(10000);
		//create new ePassportCommunicationObject
		//ePassportCommunication com=new ePassportCommunication(isodep);
		//save current time
		startTime=java.lang.System.currentTimeMillis();
		//launch a read asyncTask
		//new AsyncCommunication().execute(com);
		
	}


	@Override
	protected void onNewIntent(Intent intent) {
		handleNewIntent(intent);
	}


	@Override
	protected void onPause() {
		super.onPause();
		adapter.disableForegroundDispatch(this);
	}


	@Override
	protected void onResume() {
		super.onResume();
		adapter.enableForegroundDispatch(this,pendingIntent,new IntentFilter[]{techDetected}, techList);
	}



	@Override
	public void onCancel(DialogInterface arg0) {

		Utils.debug(Utils.INFO, "reading cancelled");
		Toast.makeText(this, "ePassport reading cancelled by the user", Toast.LENGTH_SHORT).show();
		
		
	}

	

}
