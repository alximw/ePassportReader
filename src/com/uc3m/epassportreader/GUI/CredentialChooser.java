package com.uc3m.epassportreader.GUI;



import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.CredentialListAdapter;
import com.uc3m.epassportreader.Data.Credentials;
import com.uc3m.epassportreader.DataBase.DataBaseHandler;
import com.uc3m.epassportreader.Utils.Utils;

public class CredentialChooser extends  Activity {

	public final static boolean DEBUG=true; //debug enable global variable	

	private NfcAdapter NFCadapter;

	private ListView credentialList; //BAC listview for the adapter
	public static CredentialListAdapter adapter; //BAC listview's custom adapter



	DataBaseHandler handler; //DataBaseHandler instance

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set the GUI layout
		setContentView(R.layout.main_layout);

		//New handler in order to access the database methods
		handler=new DataBaseHandler(getApplicationContext());

		//Initialize the view elements
		credentialList=(ListView)findViewById(R.id.main_credentialsList);

		//create new listAdapter (custom) and set it as credential list adapter
		adapter=new CredentialListAdapter(getApplicationContext(),R.layout.credential_layout,handler.getEPassports());
		credentialList.setAdapter(adapter);
		registerForContextMenu(credentialList);

	}






	@Override
	protected void onPause() {
		disableForegroundDispatch();
		super.onPause();
	}


	@Override
	protected void onResume() {
		enableForegroundDispatch();
		
		//refresh the list
		adapter.populateList(handler.getEPassports());
		adapter.notifyDataSetChanged();

		super.onResume();

	}

	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {

		Log.i("lol","lol");
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater mInflater=getMenuInflater();
		mInflater.inflate(R.menu.context_menu, menu);
		Log.i("lol","lol");

		switch (v.getId()){

		case R.id.main_credentialsList:
			menu.setHeaderTitle("Credential Options");
			Log.i("lol","lol");

			break;

		default:
			//Do Nothing
			break;
		}

	}




	private void enableForegroundDispatch(){
		NFCadapter=NfcAdapter.getDefaultAdapter(this); // get default nfc adapter

		Intent i=new Intent(getApplicationContext(),PassportInfoDisplay.class); //prepare the intent to the reader activity
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pending=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

		String[][] techs = new String[][] { new String[] { "android.nfc.tech.IsoDep" } }; //the technology we are interested in

		//enable the foregroundDispatch, 
		//this will give this PassportIfoDisplay priority over another
		//to manage this intent
		NFCadapter.enableForegroundDispatch(this, pending,null, techs); 
	}
	
	private void disableForegroundDispatch(){
		try{
		NFCadapter.disableForegroundDispatch(this);
		}catch(Exception e){
			if (e instanceof NullPointerException)
				Utils.debug(Utils.ERROR, "NFCAdapter instance is null, this shouldn't happen");
		}
	}




	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo mInfo= (AdapterContextMenuInfo)item.getMenuInfo();
		Credentials cred=adapter.getItem(mInfo.position);
		switch(item.getItemId()){

		case R.id.ctxMenu_edit:


			Intent mIntent=new Intent(getApplicationContext(), CredentialEditor.class);
			mIntent.putExtra("credentials", cred);
			startActivity(mIntent);

			break;
		case R.id.ctxMenu_delete:
			handler.deleteEPassport(cred);
			adapter.remove(cred);
			break;
		default:
			//Do nothing
			break;

		}

		return super.onContextItemSelected(item);
	}






	@Override
	public void onContextMenuClosed(Menu menu) {
		adapter.notifyDataSetChanged();
		super.onContextMenuClosed(menu);
	}









	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_add:
			//menu add button

			//go to CredentialEditor Activity
			//TODO: Change layout, show text on the button
			Intent intent=new Intent(getApplicationContext(), CredentialEditor.class);
			this.startActivity(intent);

			break;

		default:
			//do Nothing
			break;
		}

		return super.onOptionsItemSelected(item);
	}


























}
