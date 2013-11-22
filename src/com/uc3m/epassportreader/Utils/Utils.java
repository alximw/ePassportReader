package com.uc3m.epassportreader.Utils;

import android.util.Log;

import com.uc3m.epassportreader.GUI.CredentialChooser;

public class Utils {

public static final int INFO=0xA;
public static final int DEBUG=0xB;
public static final int ERROR=0xC;
	
	
	public static void debug(int TAG,String trace){
		
		if( CredentialChooser.DEBUG){
			
			switch(TAG){
			case INFO:
				Log.i("ePassportReader[INFO]",trace);
				break;
			case DEBUG:
				Log.d("ePassportReader[DEBUG]",trace);
				break;
				
			case ERROR:
				Log.e("ePassportReader[ERROR]",trace);
				break;
				
			default:
				Log.v("[ePassportReader]",trace);
				break;
				
			}
			
		}
		
		
	}
	
	
	
	
}
