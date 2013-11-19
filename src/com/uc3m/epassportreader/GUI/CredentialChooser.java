package com.uc3m.epassportreader.GUI;

import com.uc3m.epassportreader.CredentialEditor;
import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.CredentialListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

public class CredentialChooser extends  Activity{

private ListView credentialList;
public static CredentialListAdapter adapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		//Initialize the view elements
		credentialList=(ListView)findViewById(R.id.main_credentialsList);
		
		//create new listAdapter (custom) and set it as credential list adapter
		adapter=new CredentialListAdapter(getApplicationContext(),R.layout.credential_layout);
		credentialList.setAdapter(adapter);
		
	}

	


	@Override
	protected void onResume() {
		Log.d("DEBUG",String.valueOf(adapter.getCount()));
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
			//TODO: addCredential inte;nt/dialog
			Intent intent=new Intent(getApplicationContext(), CredentialEditor.class);
			this.startActivity(intent);

			break;

		default:
			
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	



	

	

}
