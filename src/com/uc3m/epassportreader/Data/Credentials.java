package com.uc3m.epassportreader.Data;

import java.util.Date;

public class Credentials {

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
