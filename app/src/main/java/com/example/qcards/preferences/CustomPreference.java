package com.example.qcards.preferences;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qcards.MainActivity;
import com.example.qcards.R;
import com.example.qcards.UtilsPics;
import com.example.qcards.cardlayouts.RoundedAvatarDrawable;

public class CustomPreference extends Preference {
    
	/*public CustomPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}*/

	private TextView txt;
    private static ImageView user_avatar;
    private static Context mContext;
    
    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context; 
        //this.setWidgetLayoutResource(R.layout.only_avatar);
    }

    /*@Override
    protected void onBindView(View view) {
        super.onBindView(view);
        //txt = (TextView) view.findViewById(R.id.summary);
        user_avatar = (ImageView)view.findViewById(R.id.avatar);
    }*/
    
    @Override
    protected View onCreateView( ViewGroup parent )
    {
      
      Bitmap bm = null;
      LayoutInflater li = (LayoutInflater)getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      View v = li.inflate( R.layout.only_avatar, parent, false);
      user_avatar = (ImageView)v.findViewById(R.id.avatar);
      

      UtilsPics im = new UtilsPics();
      if (MainActivity.mPPhoto)
      {
    	  //String transferFile = "/storage/emulated/legacy/OGQ/BackgroundsHD/Images/04261_BG.jpg";
      	  //orgUri = Uri.fromFile(new File(transferFile));
    	  File FolderDir = im.FindDir(mContext);
          
          String filename = UtilsPics.MPPHOTO + ".jpg";
          //String fn = filename + ".jpg";
          File requestFile = new File(FolderDir,filename);
	  
		  //File requestFile = new File(transferFile);
	      requestFile.setReadable(true, false);
	      Uri orgUri = Uri.fromFile(requestFile);
	      
	      try {
	    	  	bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(orgUri));
			    } catch (FileNotFoundException e) {
			     e.printStackTrace(); 
			    }
      }
      else
    	  bm = im.decodeSampledBitmapFromRes(mContext,im.pphoto[0], 55, 55);
	    
      RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
      //user_avatar.setImageBitmap(bm);
      
      user_avatar.setBackground(rad);
      return v;
    }
    
    /*@Override 
    protected void onBindView(View view) {
        super.onBindView(view);

        user_avatar = (ImageView)view.findViewById(R.id.avatar);
        Bitmap bm = null;

        UtilsPics im = new UtilsPics();
        if (MainActivity.mPPhoto)
        {
      	  //String transferFile = "/storage/emulated/legacy/OGQ/BackgroundsHD/Images/04261_BG.jpg";
        	  //orgUri = Uri.fromFile(new File(transferFile));
      	  File FolderDir = im.FindDir(mContext);
            
            String filename = UtilsPics.MPPHOTO + ".jpg";
            //String fn = filename + ".jpg";
            File requestFile = new File(FolderDir,filename);
  	  
  		  //File requestFile = new File(transferFile);
  	      requestFile.setReadable(true, false);
  	      Uri orgUri = Uri.fromFile(requestFile);
  	      
  	      try {
  	    	  	bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(orgUri));
  			    } catch (FileNotFoundException e) {
  			     e.printStackTrace(); 
  			    }
        }
        else
      	  bm = im.decodeSampledBitmapFromRes(mContext,im.pphoto[0], 55, 55);
  	    
        RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
        //user_avatar.setImageBitmap(bm);
        
        user_avatar.setBackground(rad);
    }*/
    public void updatePPhoto(){
    	   //user_avatar = (ImageView)view.findViewById(R.id.avatar);
      /*     Bitmap bm = null;

           UtilsPics im = new UtilsPics();
           if (MainActivity.mPPhoto)
           {
         	  //String transferFile = "/storage/emulated/legacy/OGQ/BackgroundsHD/Images/04261_BG.jpg";
           	  //orgUri = Uri.fromFile(new File(transferFile));
         	  File FolderDir = im.FindDir(mContext);
               
               String filename = UtilsPics.MPPHOTO + ".jpg";
               //String fn = filename + ".jpg";
               File requestFile = new File(FolderDir,filename);
     	  
     		  //File requestFile = new File(transferFile);
     	      requestFile.setReadable(true, false);
     	      Uri orgUri = Uri.fromFile(requestFile);
     	      
     	      try {
     	    	  	bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(orgUri));
     			    } catch (FileNotFoundException e) {
     			     e.printStackTrace(); 
     			    }
           }
           else
         	  bm = im.decodeSampledBitmapFromRes(mContext,im.pphoto[0], 55, 55);
     	    
           RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
           //user_avatar.setImageBitmap(bm);
           
           user_avatar.setBackground(rad);*/
        notifyChanged();
    }
    /*public void setUserAvatar() {
    	
    	UtilsPics im = new UtilsPics();
        
        Bitmap bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[5],  50, 50);
	    
        RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
        //user_avatar.setImageBitmap(bm);
        
        user_avatar.setBackground(rad);

    }*/
}