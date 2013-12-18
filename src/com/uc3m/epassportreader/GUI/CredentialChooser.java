package com.uc3m.epassportreader.GUI;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.CredentialListAdapter;
import com.uc3m.epassportreader.Data.Credentials;
import com.uc3m.epassportreader.DataBase.DataBaseHandler;
import com.uc3m.epassportreader.Utils.Utils;

public class CredentialChooser extends  Activity implements OnItemClickListener{

public static final String READING_ACTION="PASSING_BAC";
	
public final static boolean DEBUG=true;	
private ListView credentialList;
public static CredentialListAdapter adapter;

	
	DataBaseHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set the propper GUI layout
		setContentView(R.layout.main_layout);
		
		//New handler in order to access the database methods
		handler=new DataBaseHandler(getApplicationContext());
		
		//Initialize the view elements
		credentialList=(ListView)findViewById(R.id.main_credentialsList);
		
		//create new listAdapter (custom) and set it as credential list adapter
		adapter=new CredentialListAdapter(getApplicationContext(),R.layout.credential_layout,handler.getEPassports());
		credentialList.setAdapter(adapter);
		registerForContextMenu(credentialList);
		credentialList.setOnItemClickListener(this);
		
	}

	
	public void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent); 
		Log.i("NFC", "adsdsd");
		if (intent.getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)||
				intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)){
			Tag T=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			for(String a:T.getTechList()){
				Log.i("NFC", a);
			}
		}
		
	}
	


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater mInflater=getMenuInflater();
		mInflater.inflate(R.menu.context_menu, menu);
		
		switch (v.getId()){
		
		case R.id.main_credentialsList:
			menu.setHeaderTitle("Credential Options");
			break;
			
		default:
			//Do Nothing
			break;
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
	protected void onResume() {
		//refresh the list
		adapter.populateList(handler.getEPassports());
		adapter.notifyDataSetChanged();
		super.onResume();
		
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




	@Override
	public void onItemClick(AdapterView parent, View listItem, int position, long ID) {

		
		//retrieve the data from the view
		CredentialListAdapter adapter= (CredentialListAdapter) parent.getAdapter();
		Date birthDate=adapter.getItem(position).getBirthDate();
		Date ExpityDate=adapter.getItem(position).getExpiryDate();
		String docID=adapter.getItem(position).getePassportID();
		Credentials BAC=new Credentials(birthDate, ExpityDate, docID);
		
		Utils.debug(Utils.DEBUG, "item selected: "+docID);

		Intent i=new Intent(getApplicationContext(),ReaderActivity.class);
		i.putExtra("BAC_EXTRA", BAC)
		.setAction(this.READING_ACTION);
		
		startActivity(i);
		
	}








	
	
	
	
	
	



	

	

}
