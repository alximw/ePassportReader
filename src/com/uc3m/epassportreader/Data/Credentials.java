package com.uc3m.epassportreader.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Credentials implements Serializable {

	private Date birthDate;
	private Date expiryDate;
	private String ePassportID;
	
	public Credentials(){
		super();
	}
	
	public Credentials(Date birth,Date expiry,String ID){
		this.birthDate=birth;
		this.expiryDate=expiry;
		this.ePassportID=ID;
	}

	public Date getBirthDate() {
		
		return birthDate;
	}
	
	
	public String getBirthDateAsString(){
		String date="";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		date=dateFormat.format(this.birthDate);
		
		return date;
	}
	
	public String getExpiryDateAsString(){
		String date="";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
		date=dateFormat.format(this.expiryDate);
		
		return date;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getePassportID() {
		return ePassportID;
	}

	public void setePassportID(String ePassportID) {
		this.ePassportID = ePassportID;
	}
	
	
	
	
	
	
}
