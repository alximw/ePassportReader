package com.uc3m.epassportreader.Data;

import java.util.ArrayList;

import com.uc3m.epassportreader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CredentialListAdapter extends ArrayAdapter<Credentials> {

	//list of credentials shown on the list
	private ArrayList<Credentials> list;
	private Context adapterContext;
	int layout;
	private static class ListElementHolder{
		TextView eDate;
		TextView bDate;
		TextView ID;
		
	}
	
	
	public CredentialListAdapter(Context context, int layout) {
		super(context, layout);
		list=new ArrayList<Credentials>();
		adapterContext=context;


	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listElement=convertView;
		ListElementHolder elementHolder;
		//TODO: Create Holder class
		
		
		if(listElement==null){
			//ListElement has not been previously created
			LayoutInflater inflater=(LayoutInflater) adapterContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listElement=inflater.inflate(layout,null,false);
			elementHolder=new ListElementHolder();
			
			elementHolder.bDate=(TextView)listElement.findViewById(R.id.credential_bDate);
			elementHolder.eDate=(TextView)listElement.findViewById(R.id.credential_eDate);
			elementHolder.ID=(TextView)listElement.findViewById(R.id.credential_ID);
			
			listElement.setTag(elementHolder);


		}else{
			//ListElement has been previously created
			elementHolder=(ListElementHolder)listElement.getTag();
		}
		
		elementHolder.bDate.setText(list.get(position).getBirthDate().toString() );
		elementHolder.eDate.setText(list.get(position).getExpiryDate().toString() );
		elementHolder.bDate.setText(list.get(position).getePassportID());
		
		
		return listElement;	
	}

}
