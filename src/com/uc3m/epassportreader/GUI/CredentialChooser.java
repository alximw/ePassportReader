package com.uc3m.epassportreader.GUI;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Data.CredentialListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.ListView;

public class CredentialChooser extends  Activity {

Button add;				
ListView credentialList;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		//Initialize the view elements
		credentialList=(ListView)findViewById(R.id.main_credentialsList);
		
		//create new listAdapter (custom) and set it as credential list adapter
		CredentialListAdapter adapter=new CredentialListAdapter(getApplicationContext(),R.layout.credentiallayout);
		credentialList.setAdapter(adapter);
	}



	

	

}
