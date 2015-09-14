package com.example.qcards.sharecards;

import java.io.File;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;


public class NfcActivity extends Activity{
	NfcAdapter mNfcAdapter;
	boolean mAndroidBeamAvailable;
	
	 public final static String APP_FOLDER = "Qcards1";
	 public final static String filename = "cardtest.jpg";

	Uri fileUri = null;

	// http://developer.android.com/training/beam-files/send-files.html
	// https://github.com/commonsguy/cw-omnibus/tree/master/NFC/FileBeam/src

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
      //setContentView(R.layout.activity_nfc);
	  mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
	  
	  if (mNfcAdapter == null) {
	      Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
	      finish();
	      return;
	   // NFC isn't available on the device
	   //if (!PackageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
	            /*
	             * Disable NFC features here.
	             * For example, disable menu items or buttons that activate
	             * NFC-related features
	             */
	            
	        // Android Beam file transfer isn't supported 
	  }
	  else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
	      // If Android Beam isn't available, don't continue.
	      mAndroidBeamAvailable = false;
	      /*
	       * Disable Android Beam file transfer features here.
	       */
	  // Android Beam file transfer is available, continue
	  } 
	  else {
		  
		  /*
	       * Create a list of URIs, get a File,
	       * and set its permissions
	       */
		  String transferFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_FOLDER + "/" + filename;

	      File requestFile = new File(transferFile);
	      requestFile.setReadable(true, false);
	      fileUri = Uri.fromFile(requestFile);
	      mNfcAdapter.setBeamPushUris(new Uri[] {fileUri}, this);
	      
	  }
	  
	  //NdefRecord mimeRecord = NdefRecord.createMime("application/vnd.com.example.android.beam",
		//	    "Beam me up, Android".getBytes(Charset.forName("US-ASCII")));
	}

	  

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode==0 && resultCode==RESULT_OK) 
	  {
		mNfcAdapter.setBeamPushUris(new Uri[] {data.getData()}, this);
	    
	    Button btn=new Button(this);
	    
	    btn.setText(R.string.over);
	    btn.setOnClickListener(this);
	    setContentView(btn);
	  }
	}*/


	public String getRealPathFromURI(Uri contentUri) 
	{
		  String[] proj = { MediaStore.Images.Media.DATA };
		  
		  //This method was deprecated in API level 11
		  //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		  
		  CursorLoader cursorLoader = new CursorLoader(
		            this, 
		            contentUri, proj, null, null, null);        
		  Cursor cursor = cursorLoader.loadInBackground();
		  
		  int column_index = 
		    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		  cursor.moveToFirst();
		  return cursor.getString(column_index); 
	}
		 
		

	/*@Override
	public void onClick(View v) {
	  finish();
	}*/
}