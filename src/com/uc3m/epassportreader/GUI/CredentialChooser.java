package com.uc3m.epassportreader.GUI;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.CredentialListAdapter;
import com.uc3m.epassportreader.DataBase.DataBaseHandler;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class CredentialChooser extends  Activity{

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
	
	
	
	
	
	



	

	

}
