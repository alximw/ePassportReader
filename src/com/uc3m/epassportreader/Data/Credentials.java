package com.uc3m.epassportreader.Data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jmrtd.BACKey;





public class Credentials extends BACKey implements Serializable{

	SimpleDateFormat formater_dmy=new SimpleDateFormat("dd/MM/yy");
	SimpleDateFormat formater_ymd = new SimpleDateFormat("yyMMdd"); 



	public Credentials(String documentID, Date birthDate, Date expiryDate) {
		super(documentID, birthDate, expiryDate);
		// TODO Auto-generated constructor stub
	}

	public Credentials(String documentID, String birthDate, String expiryDate) {
		super(documentID, birthDate, expiryDate);
		// TODO Auto-generated constructor stub
	}

	public Credentials() {
	}


	public String getExpiryDateAsFormatedString() {
		String parsedDate="";
		try{ 
		String date=getDateOfExpiry();
		Date eDate=formater_ymd.parse(date);
		parsedDate=formater_dmy.format(eDate);
		}catch(Exception e){
			
		}
		
		return parsedDate;


	}


	public Date getBirthDateAsDateObject(){
		
		try {
			return formater_ymd.parse(getDateOfBirth());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}

	public Date getExpiryDateAsDateObject(){
		try {
			return formater_ymd.parse(getDateOfExpiry());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;


	}


	public String getBirthDateAsFormatedString(){
		String parsedDate="";
		try{ 
		String date=getDateOfBirth();
		Date eDate=formater_ymd.parse(date);
		parsedDate=formater_dmy.format(eDate);
		}catch(Exception e){
			
		}
		
		return parsedDate;


	}

}
