package com.uc3m.epassportreader.Data;

import java.util.ArrayList;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.Utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CredentialListAdapter extends ArrayAdapter<Credentials> {

	//list of credentials shown on the list
	ArrayList<Credentials> list;
	
	//context in which the adapter is created
	Context adapterContext;
	
	//element layout reference
	int layout;
    
	//element holder (reuse the views in the list entry)
	static class ListElementHolder{
		TextView eDate; //Expire date
		TextView bDate; //birth date
		TextView ID;  //Document number
		
	}
	
	
	
	
	
	public CredentialListAdapter(Context context, int resource, ArrayList<Credentials> objects) {
		super(context, resource, objects);
		
		this.list=objects;
		this.layout=resource;
		this.adapterContext=context;
		// TODO Auto-generated constructor stub
	}

	
	


	/**
	 * Return a reference to this adapter list of elements
	 * @return The list of elements shown on the listView
	 */
	public ArrayList<Credentials> getList(){
		return this.list;
	}
	
	/**
	 * Add new Credentials object into the list
	 * @param newCreds: new Credentials object to be inserted in the list
	 */
	public void  addItem(Credentials newCreds){
		this.list.add(newCreds);
	}
	/**
	 * Fill the adapter list with some Credentials instances
	 * @param credentialList: list with some Credentials to be displayed in the ListView
	 */
	public void populateList(ArrayList<Credentials> credentialList){
		Utils.debug(Utils.DEBUG, "Items in DataBase: "+String.valueOf(credentialList.size()));
		this.list=credentialList;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Utils.debug(Utils.DEBUG,"getView Called");
		View listElement=convertView;
		ListElementHolder elementHolder;		
		
		
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
		
		elementHolder.bDate.setText("Birth Date: "+list.get(position).getBirthDateAsString() );
		elementHolder.eDate.setText("Expiry Date: "+list.get(position).getExpiryDateAsString());
		elementHolder.ID.setText("ID: "+list.get(position).getePassportID());
		if(position%2==0){
			listElement.setBackgroundColor(0xA1FDFF);
		}
		
		return listElement;	
	}

}
