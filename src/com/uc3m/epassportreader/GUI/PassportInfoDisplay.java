package com.uc3m.epassportreader.GUI;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import net.sourceforge.scuba.smartcards.CardService;
import net.sourceforge.scuba.smartcards.CardServiceException;

import org.jmrtd.BACDeniedException;
import org.jmrtd.MRTDTrustStore;
import org.jmrtd.Passport;
import org.jmrtd.PassportService;
import org.jmrtd.lds.ChipAuthenticationInfo;
import org.jmrtd.lds.DG14File;
import org.jmrtd.lds.DG1File;
import org.jmrtd.lds.DG2File;
import org.jmrtd.lds.DG3File;
import org.jmrtd.lds.DataGroup;
import org.jmrtd.lds.FaceImageInfo;
import org.jmrtd.lds.FaceInfo;
import org.jmrtd.lds.LDS;
import org.jmrtd.lds.LDSFile;
import org.jmrtd.lds.SODFile;
import org.jmrtd.lds.SecurityInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.epassportreader.R;
import com.uc3m.epassportreader.DataBase.DataBaseHandler;
import com.uc3m.epassportreader.Utils.Utils;

public class PassportInfoDisplay extends Activity {

	long t1=0;
	long t2=0;
	DataBaseHandler handler;
	TextView name,surnames,Bdate,gender, nationality,passportNumber,expDate,expiryDate,documentID,certificateInfo, certificatesSignAlgorithm,certificateFingerprint;
	ImageView photo;
	TextView dg1Stored,dg1Computed,dg1MD5;
	TextView dg2Stored,dg2Computed,dg2MD5;
	TextView dg3Stored,dg3Computed,dg3MD5;
	TextView dg14Stored,dg14Computed,dg14MD5;
	Passport readedPassport;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passport_info_display_layout);
		views();

		handler=new DataBaseHandler(getApplicationContext());
		handleIntent(getIntent());

	}

	
	
	
	private void views(){
		name=(TextView)findViewById(R.id.resourceName);
		surnames=(TextView)findViewById(R.id.surnamesText);
		Bdate=(TextView)findViewById(R.id.dateofbirthText);
		gender=(TextView)findViewById(R.id.genderText);
		nationality=(TextView)findViewById(R.id.nationalityText);
		
		documentID=(TextView)findViewById(R.id.documentIDText);
		passportNumber=(TextView)findViewById(R.id.documentNumberText);

		expiryDate=(TextView)findViewById(R.id.expirationDateText);

		//expDate=(TextView)findViewById(R.id.expeditionDateText);

		certificateInfo=(TextView) findViewById(R.id.certificateText);

		certificatesSignAlgorithm=(TextView)findViewById(R.id.signatureAlgorithmText);
		certificateFingerprint=(TextView)findViewById(R.id.fingerprintText);
		
		
		
		
		dg1Stored=(TextView)findViewById(R.id.datagroup1StoredText);
		dg1Computed=(TextView)findViewById(R.id.datagroup1ComputedText);
		dg1MD5=(TextView)findViewById(R.id.datagroup1ComputedMD5Text);
		
		dg2Stored=(TextView)findViewById(R.id.datagroup2StoredText);
		dg2Computed=(TextView)findViewById(R.id.datagroup2ComputedText);
		dg2MD5=(TextView)findViewById(R.id.datagroup2ComputedMD5Text);
		
		dg3Stored=(TextView)findViewById(R.id.datagroup3StoredText);
		dg3Computed=(TextView)findViewById(R.id.datagroup3ComputedText);
		dg3MD5=(TextView)findViewById(R.id.datagroup3ComputedMD5Text);
		
		dg14Stored=(TextView)findViewById(R.id.datagroup15StoredText);
		dg14Computed=(TextView)findViewById(R.id.datagroup15ComputedText);
		dg14MD5=(TextView)findViewById(R.id.datagroup15ComputedMD5Text);
		photo=(ImageView)findViewById(R.id.passportPhoto);
		
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
		super.onNewIntent(intent);
	}


	private void handleIntent(Intent intent){

		if(intent.getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)){

			if(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)!=null){
				Tag t=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				if (Arrays.asList(t.getTechList()).contains("android.nfc.tech.IsoDep")){

					//Toast.makeText(getApplicationContext(),"IsoDep Tag Found", Toast.LENGTH_LONG).show();
					beginePassportCommunication(IsoDep.get(t));

				}
			}

		}else{
			Toast.makeText(getApplicationContext(),intent.getAction(), Toast.LENGTH_LONG).show();
		}
	}


	private void beginePassportCommunication(IsoDep dep) {


		try{
			dep.setTimeout(1000);
			new AsyncPassportCreate().execute(dep);

		}catch(Exception e){


		}


	}

	class AsyncPassportCreate extends AsyncTask<IsoDep, String, Passport> {

		@Override
		protected Passport doInBackground(IsoDep... params) {
			try {

				IsoDep iso=params[0];
				//cardService instance for nfc communication
				CardService cService=CardService.getInstance(iso);
				cService.open();//begin new session
				//passportService for epassport especial case communication
				PassportService pService =new PassportService(cService);
				//add Debug information
				/*	pService.addPlainTextAPDUListener(new APDUListener() {

					@Override
				public void exchangedAPDU(APDUEvent event) {

						Log.i("NFC CHAT: Command", Hex.bytesToPrettyString(event.getCommandAPDU().getBytes()));
						Log.i("NFC CHAT: Response", Hex.bytesToPrettyString(event.getResponseAPDU().getBytes()));
					}

				});

				pService.addAPDUListener(new APDUListener() {

					@Override
					public void exchangedAPDU(APDUEvent event) {

						Log.i("NFC CHAT: Command", Hex.bytesToPrettyString(event.getCommandAPDU().getBytes()));
						Log.i("NFC CHAT: Response", Hex.bytesToPrettyString(event.getResponseAPDU().getBytes()));
					}

				});
				 */


				try {
					t1=System.currentTimeMillis();
					Passport p=new Passport(pService,new MRTDTrustStore(),handler.getEPassportsAsBACKeys(),1);
					t2=System.currentTimeMillis();
					return p;
				} catch (BACDeniedException cse) {
					t2=System.currentTimeMillis();

					Log.d("ERROR","BACDenied Exception"+cse.getMessage());


				} 
				return null;
			} catch (CardServiceException cse) {
				Log.d("ERROR","CardService Exception "+cse.getMessage());
				cse.printStackTrace();
				return null;
			} catch (Exception e) {
				Log.d("ERROR","another exception happened");
				return null;
			}finally{
				Log.i("TIME","Readding attemp completed in: "+(t2-t1)+" miliseconds");

			}
		}

		@Override
		protected void onPostExecute(Passport passport) {
			// if (passport == null) { throw new IllegalArgumentException("Failed to get a passport"); }
			if (passport == null) {
				Log.w("ERROR","The passport object is null");
				Toast.makeText(getApplicationContext(), "can not read the ePassport data. Wrong BAC data?", Toast.LENGTH_LONG).show();
				Intent i=new Intent(getApplicationContext(),CredentialChooser.class);
				startActivity(i);
			} else {

				//Log.v("SUCCES","Passport readed in: "+((t2-t1)/1000));
				readedPassport=passport;
				startePassportDecofication(passport);
			}
		}
	}

	public void startePassportDecofication(Passport passport){

		//get the pasport's logical data structure containing the available datagroups
		LDS lds=passport.getLDS();
		new AsyncPassportDecodification().execute(passport);

	}
	
	
	class AsyncPassportDecodification extends AsyncTask<Passport,LDSFile, Integer>{

		@Override
		protected Integer doInBackground(Passport... params) {
			try{
				Passport passport=params[0];
				LDS logicDataStructure=passport.getLDS();

				List<Short> DGList= logicDataStructure.getFileList();
				Collections.sort(DGList);

				for(short dg: DGList){
					switch(dg){

					case PassportService.EF_COM:
						break;
					case PassportService.EF_SOD:
						SODFile sod=logicDataStructure.getSODFile();
						publishProgress(sod);
						break;
					case PassportService.EF_DG1:
						DG1File dg1=logicDataStructure.getDG1File();
						publishProgress(dg1);
						break;
					case PassportService.EF_DG2:
						DG2File dg2=logicDataStructure.getDG2File();

						List<FaceImageInfo> allFaceImageInfos = new ArrayList<FaceImageInfo>();
						List<FaceInfo> faceInfos = dg2.getFaceInfos();
						
						for (FaceInfo faceInfo : faceInfos) {
							allFaceImageInfos.addAll(faceInfo.getFaceImageInfos());
						}
						
						if (allFaceImageInfos.size() > 0) {
							Log.i("[INFO]","This passport has images attached");
							new AsyncImageDecodification().execute(allFaceImageInfos.get(0));

						} else {
								//do nothing
						}

						break;
					case PassportService.EF_DG14:
						DG14File dg14=logicDataStructure.getDG14File();
						publishProgress(dg14);
						break;
					
					default:
						Log.i("DG list", "Ignored DataGroup found "+dg);
						
						break;

					}
				}

				return 0;
			}catch(Exception e){
				Log.i("Error", e.getMessage());
				return null;
			}

		}



		@Override
		protected void onProgressUpdate(LDSFile... values) {
			LDSFile file=values[0];
			if(file instanceof SODFile){

				X500Principal principal= ((SODFile) file).getIssuerX500Principal();
				
				String name=principal.getName(X500Principal.RFC1779);
				Log.i("DATA","issuerName: "+name);
				try {
					X509Certificate cert=((SODFile) file).getDocSigningCertificate();
					DG14File f;
					
					PublicKey pkey=cert.getPublicKey();
					BigInteger big=new BigInteger(pkey.getEncoded());
					Log.i("[CERT PK]",Utils.bytesToHex(pkey.getEncoded()));
					//Log.i("DATA","cert pk: "+big.toString(16)+":"+cert.getSigAlgName());
					Log.i("crypto", "DATAGROUP DIGEST ALGORITHM: "+((SODFile)file).getDigestAlgorithm());
					for (int i:((SODFile)file).getDataGroupHashes().keySet()){
						
						if(((SODFile)file).getDataGroupHashes().get(i)!=null){
							byte[] hash=((SODFile)file).getDataGroupHashes().get(i);
							
							if(i==1){
								Log.i("CRYPTO: Datagroup: ",i+" "+Utils.bytesToHex(hash));
								dg1Stored.setText(Utils.bytesToHex(hash));
								dg1Computed.setText(Utils.bytesToHex(Utils.getSHA1Hash(readedPassport.getLDS().getDG1File().getEncoded())));
								dg1MD5.setText(Utils.bytesToHex(Utils.getMD5Hash(readedPassport.getLDS().getDG1File().getEncoded())));

							}else if(i==2){
								Log.i("CRYPTO: Datagroup: ",i+" "+Utils.bytesToHex(hash));
								dg2Stored.setText(Utils.bytesToHex(hash));
								dg2Computed.setText(Utils.bytesToHex(Utils.getSHA1Hash(readedPassport.getLDS().getDG2File().getEncoded())));
								dg2MD5.setText(Utils.bytesToHex(Utils.getMD5Hash(readedPassport.getLDS().getDG2File().getEncoded())));

							}else if(i==3){
								//Log.i("CRYPTO: Datagroup: ",i+" "+Utils.bytesToHex(hash));
								dg3Stored.setText("NOT AVAILABLE");
								dg3Computed.setText("NOT COMPUTED");
								dg3MD5.setText("NOT COMPUTED");
								
								


							}else if(i==14){
								dg14Stored.setText(Utils.bytesToHex(hash));
								Log.i("CRYPTO: Datagroup: ",i+" "+Utils.bytesToHex(hash));
								//dg14Computed.setText(Utils.bytesToHex(Utils.getSHA1Hash(readedPassport.getLDS().getDG14File().getEncoded())));
								dg14MD5.setText(Utils.bytesToHex(Utils.getMD5Hash(readedPassport.getLDS().getDG14File().getEncoded())));

							}
						}
						
						
						
					}
					
					//expDate.setText(cert.getNotBefore().toString());
					certificateInfo.setText(name);
					certificatesSignAlgorithm.setText(cert.getSigAlgName());

					certificateFingerprint.setText(Utils.bytesToHex(Utils.getSHA1Hash(cert.getEncoded())));

					
				} catch (CertificateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}else if (file instanceof DataGroup){
				
				if(file instanceof DG1File){
					DG1File dg1_file=(DG1File)file;
					Log.i("DATA",dg1_file.getMRZInfo().getSecondaryIdentifier().replace("<", "")+" "+dg1_file.getMRZInfo().getPrimaryIdentifier());
					
					name.setText(dg1_file.getMRZInfo().getPrimaryIdentifier());
					surnames.setText(dg1_file.getMRZInfo().getSecondaryIdentifier().replace("<", ""));
					Bdate.setText(dg1_file.getMRZInfo().getDateOfBirth());
					gender.setText(dg1_file.getMRZInfo().getGender().toString());
					nationality.setText(dg1_file.getMRZInfo().getNationality());
					
					passportNumber.setText(dg1_file.getMRZInfo().getDocumentNumber());
					documentID.setText(dg1_file.getMRZInfo().getPersonalNumber());
					expiryDate.setText(dg1_file.getMRZInfo().getDateOfExpiry());
					
					//Log.i("[info]", dg1_file.getMRZInfo().getOptionalData1()+" "+dg1_file.getMRZInfo().getOptionalData2());
					
					
				}
				if(file instanceof DG14File){
					DG14File f=(DG14File)file;

				/*	for(SecurityInfo info:f.getSecurityInfos()){
						Log.i("[SEC. of Chip]",Utils.bytesToHex(info.getEncoded()));
					}
					
					for(int i=0;i<f.getChipAuthenticationInfos().keySet().size();i++){
						BigInteger bigI=(BigInteger) f.getChipAuthenticationInfos().keySet().toArray()[i];
						String data=f.getChipAuthenticationInfos().get(bigI);
						Log.i("[Chip Auth. Info]",data);
					}
					
					for(int i=0;i<f.getChipAuthenticationPublicKeyInfos().keySet().size();i++){
						BigInteger bigI=(BigInteger) f.getChipAuthenticationPublicKeyInfos().keySet().toArray()[i];
						PublicKey data=f.getChipAuthenticationPublicKeyInfos().get(bigI);
						
						try {
							Log.i("[Chip Auth.PK Info (hashed)]",data.getAlgorithm()+ " KEY: "+Utils.bytesToHex( Utils.getSHA1Hash(data.getEncoded())  ));
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.i("[DG14 RAW DUMP]",Utils.bytesToHex(f.getEncoded()));*/
					
					
					try {
						dg14Computed.setText(Utils.bytesToHex(Utils.getSHA1Hash(f.getEncoded())));
						
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}


		}



		protected void onPostExecute(Integer i){
			if(i!=null){
				Log.i("SUCCES","datagroups decodified succesfully");
			}else{
				Log.e("ERROR", "Error while decodifying datagroups");
			}
		}

	}
	
	
	public class AsyncImageDecodification extends AsyncTask<FaceImageInfo, Object, Bitmap>{



		@Override
		protected Bitmap doInBackground(FaceImageInfo... params) {
			try{

				FaceImageInfo faceImage=params[0];
				Log.i("Bprogess: ", "...on doInBackground");
				
				
				int imageLength = faceImage.getImageLength();
				String mimeType = faceImage.getMimeType();
				InputStream imageInputStream = faceImage.getImageInputStream(); /* These are buffered by now */
				DataInputStream dataInputStream = new DataInputStream(imageInputStream);
				byte[] imageBytes = new byte[imageLength];
				dataInputStream.readFully(imageBytes);
				Bitmap bitmap;
				bitmap = Utils.read(new ByteArrayInputStream(imageBytes), imageLength, mimeType);
				
				return bitmap;


			} catch (Exception e) {
				System.err.println("DEBUG: EXCEPTION: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			Log.i("Progress"," on PostExecute");

			photo.setImageBitmap(result);

		}
		
		
		
		
	}






}


