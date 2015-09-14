package com.example.qcards.preferences;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;

import com.example.qcards.MainActivity;
import com.example.qcards.UtilsPics;


public class SettingsActivity extends Activity {
	//private static final int CAMERA_REQUEST1 = 1888;
	Context mContext;
	private static final int CAMERA_REQUEST1 = 1888;
	private static final int RESULT_OK = -1;
	//private static final int FILE_EXPLORER_RC = 2;
	private static final int FILE_GALLERY = 3;
	
	
	private static final String PREFS_NAME = "MyPrefsFile";
	
	private static SharedPreferences settings;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = getApplicationContext();
        
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        
        //android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment(),"SETTINGS")
        .commit(); 
        //transaction.replace(android.R.id.content, new SettingsFragment(),"SETTINGS")
          //      .commit();
        
        //getFragmentManager().beginTransaction().addToBackStack(null);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(resultCode, resultCode, data);
    	String filePath;
    	UtilsPics im = new UtilsPics();
    	
    	// Preferences
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        
        if (requestCode == CAMERA_REQUEST1 && resultCode == RESULT_OK) {  
            //Bitmap photo = (Bitmap) data.getExtras().get("data"); 
        	Bitmap photo_avatar = (Bitmap) data.getExtras().get("data");
        	
        	
        	// Set order profile photo as true 
            editor.putBoolean("mProfilePhoto", true);
            editor.commit();
            MainActivity.mPPhoto = true;
            
            File FolderDir = im.FindDir(mContext);
            
            String filename = UtilsPics.MPPHOTO;
            im.saveImageToInternalStorage(mContext,photo_avatar,FolderDir, filename);
            
            
    		
    		//   Intent intent = new Intent(getApplicationContext(),com.example.qcards.MainActivity.class);
            //startActivity(intent);
        }
        //if (requestCode == FILE_EXPLORER_RC && resultCode == RESULT_OK) {
        if (requestCode == FILE_GALLERY && resultCode == RESULT_OK) {
        	
        	 Uri photoUri = data.getData();
             if (photoUri != null)
             {
                 String[] filePathColumn = {MediaStore.Images.Media.DATA};
                 Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                 cursor.moveToFirst();
                 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                 filePath = cursor.getString(columnIndex);
                 cursor.close();
                 
                 File requestFile = new File(filePath);
        		 requestFile.setReadable(true, false);
        		 Uri orgUri = Uri.fromFile(requestFile);
        		 
        		 try {
         			Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(orgUri));
    			     
         			File FolderDir = im.FindDir(mContext);
         			
         			editor.putBoolean("mProfilePhoto", true);
         			editor.commit();
         			MainActivity.mPPhoto = true;
         			
         			String filename = UtilsPics.MPPHOTO;
         			im.saveImageToInternalStorage(mContext,bm,FolderDir, filename);
         			
    			    } catch (FileNotFoundException e) {
    			    	e.printStackTrace(); 
    			    }
             }
        	
             //String transferFile = "/storage/emulated/legacy/OGQ/BackgroundsHD/Images/04261_BG.jpg";
   		  //orgUri = Uri.fromFile(new File(transferFile));
   		  
   		  		
            // Uri u = data.getData();	
        		//String path1 = u.getPath();
        		//String path = getPath(u);
        		//String path = getRealPathFromURI(u); 
        		
        		//String path = u.getPath();
        		//if (TextUtils.equals(data.getScheme(), "file")) {
                //path = im.handleFileUri(u);
        		/*if (TextUtils.equals(data.getScheme(), "file")) {
                	path = im.handleFileUri(u);
                } else if (TextUtils.equals(
                		data.getScheme(), "content")) {
                	path = im.handleContentUri(u,mContext);
                }*/
        		//}
        		
        		
        		
        		
        		//Bitmap photo_avatar = (Bitmap) data.getExtras().get("data");
        		//Bitmap photo_avatar = (Bitmap) data.getExtras().get("data");
	        	
	            //UtilsPics im = new UtilsPics();
	            
	            //File FolderDir = im.FindDir(mContext);
	            
	            //String filename = UtilsPics.IMFILENAME;
	            //im.saveImageToInternalStorage(mContext,photo_avatar,FolderDir, filename);
        }
    }
    
    public String getRealPathFromURI(Uri contentUri) {
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

    
  //UPDATED!
    /*public String getPath(Uri uri) {
    String[] projection = { MediaStore.Images.Media.DATA };
    Cursor cursor = managedQuery(uri, projection, null, null, null);
	if(cursor!=null)
	{
	    //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	    //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	    }
	    	else return null;
	}*/ 
    // See more at: http://blog.kerul.net/2011/12/pick-file-using-intentactiongetcontent.html#sthash.gSkkHHBb.dpuf
    
}