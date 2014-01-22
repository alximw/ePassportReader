package com.uc3m.epassportreader.Utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jnbis.WSQDecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.uc3m.epassportreader.GUI.CredentialChooser;

public class Utils {

public static final int INFO=0xA;
public static final int DEBUG=0xB;
public static final int ERROR=0xC;
	
public static WSQDecoder decoder=new WSQDecoder();


public static String
JPEG_MIME_TYPE = "image/jpeg",
JPEG2000_MIME_TYPE = "image/jp2",
JPEG2000_ALT_MIME_TYPE = "image/jpeg2000",
WSQ_MIME_TYPE = "image/x-wsq";
	
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
	
	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	

	public static byte[] getMD5Hash(byte[] bytes) throws NoSuchAlgorithmException{
		
		MessageDigest md=MessageDigest.getInstance("MD5");
		return md.digest(bytes);
		
	}
	
public static byte[] getSHA1Hash(byte[] bytes) throws NoSuchAlgorithmException{
		
		MessageDigest md=MessageDigest.getInstance("SHA-1");
		return md.digest(bytes);
		
	}
	




/* IMAGE DECODIFICATION METHODS */


public  static Bitmap read(InputStream inputStream, int imageLength, String mimeType) throws IOException {
	/* DEBUG */
	synchronized(inputStream) {
		DataInputStream dataIn = new DataInputStream(inputStream);
		byte[] bytes = new byte[(int)imageLength];
		dataIn.readFully(bytes);
		inputStream = new ByteArrayInputStream(bytes);
	}
	/* END DEBUG */
	
	if (JPEG2000_MIME_TYPE.equalsIgnoreCase(mimeType) || JPEG2000_ALT_MIME_TYPE.equalsIgnoreCase(mimeType)) {
		org.jmrtd.jj2000.Bitmap bitmap = org.jmrtd.jj2000.JJ2000Decoder.decode(inputStream);
		return toAndroidBitmap(bitmap);
	} else if (WSQ_MIME_TYPE.equalsIgnoreCase(mimeType)) {
		org.jnbis.Bitmap bitmap = WSQDecoder.decode(inputStream);
		return toAndroidBitmap(bitmap);
	} else {
		return BitmapFactory.decodeStream(inputStream);
	}
}

/* ONLY PRIVATE METHODS BELOW */

private static Bitmap toAndroidBitmap(org.jmrtd.jj2000.Bitmap bitmap) {
	int[] intData = bitmap.getPixels();
	return Bitmap.createBitmap(intData, 0, bitmap.getWidth(), bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

}

private static Bitmap toAndroidBitmap(org.jnbis.Bitmap bitmap) {
	byte[] byteData = bitmap.getPixels();
	int[] intData = new int[byteData.length];
	for (int j = 0; j < byteData.length; j++) {
		intData[j] = 0xFF000000 | ((byteData[j] & 0xFF) << 16) | ((byteData[j] & 0xFF) << 8) | (byteData[j] & 0xFF);
	}
	return Bitmap.createBitmap(intData, 0, bitmap.getWidth(), bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
}

static class BoundedInputStream extends FilterInputStream {

	private long bound;
	private long position;

	protected BoundedInputStream(InputStream inputStream, long bound) {
		super(inputStream);
		this.position = 0;
		this.bound = bound;
	}

	public int read() throws IOException {
		if (position >= bound) { return -1; }
		try {
			return super.read();
		} finally {
			position++;
		}
	}
}






	
	
}
