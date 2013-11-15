package com.uc3m.epassportreader.Data;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CredentialListAdapter extends ArrayAdapter<Credentials> {

	//list of credentials shown on the list
	private ArrayList<Credentials> list;
	
	public CredentialListAdapter(Context context, int layout) {
		super(context, layout);
		list=new ArrayList<Credentials>();
	}

}
