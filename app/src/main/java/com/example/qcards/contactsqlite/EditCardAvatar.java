package com.example.qcards.contactsqlite;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qcards.R;
import com.example.qcards.UtilsPics;

public class EditCardAvatar extends Activity {
    private static final int CAMERA_REQUEST1 = 1888; 
    private static final int CAMERA_REQUEST2 = 1999;
    private ImageView imageView, imageViewHeader;
    private TextView person_name, company, jobposition, citycompany;
    private Bitmap photo_header;
    
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_overlay_edit);
        
        person_name = (TextView) findViewById(R.id.text_namee);
        company = (TextView) findViewById(R.id.text_company);
        citycompany = (TextView) findViewById(R.id.text_city);

        mContext = getApplicationContext();
        imageView = (ImageView)findViewById(R.id.avatar);
        
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST1); 
            }
        });

        imageViewHeader = (ImageView) findViewById(R.id.header_imageview);
        
        imageViewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST2); 
            }
        });
        

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST1 && resultCode == RESULT_OK) {  
            //Bitmap photo = (Bitmap) data.getExtras().get("data"); 
        	Bitmap photo_avatar = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo_avatar);
        }
        if (requestCode == CAMERA_REQUEST2 && resultCode == RESULT_OK) {  
            photo_header = (Bitmap) data.getExtras().get("data"); 
            imageViewHeader.setImageBitmap(photo_header);
        }  
    } 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.save_card_actions, menu);

       return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) 
    { 
       super.onOptionsItemSelected(item); 
       switch(item.getItemId()) 
       { 
       	 case R.id.action_save:
       		  BtnSaveOnClick();
       		  return true;
       	 default: 
       		  return super.onOptionsItemSelected(item); 

       } 
    } 
    
    // Save card
    public void BtnSaveOnClick()
    {
    	UtilsPics im = new UtilsPics();
    	
    	File FolderDir = im.FindDir(mContext);
    	person_name.setDrawingCacheEnabled(true);
        person_name = (TextView) findViewById(R.id.text_namee);
        String a = person_name.getText().toString();
        Bitmap bp = person_name.getDrawingCache();
        
        person_name.buildDrawingCache(true);
    	Bitmap bmp = Bitmap.createBitmap(person_name.getDrawingCache());
    	Bitmap combined = im.combineImages(photo_header,bmp, mContext);
    	person_name.setDrawingCacheEnabled(false);
    	String filename = UtilsPics.IMFILENAME;
		im.saveImageToInternalStorage(mContext,combined,FolderDir, filename);
		
		Intent intent = new Intent(getApplicationContext(),com.example.qcards.MainActivity.class);
        startActivity(intent);
    
    }

}