package com.example.qcards.cardlayouts;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qcards.R;
import com.example.qcards.UtilsPics;


public class MyAvatarActivity extends Activity{
	
	private ImageView card_image;
	private ImageView card_avatar;
	private TextView person_name, company, jobposition, citycompany;
	private WebView description;
	
	private UtilsPics im = new UtilsPics();
	private int imageID;
	private Bitmap bm = null;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.avatar_overlay);
	        
	        card_image = (ImageView)findViewById(R.id.header_imageview);
	        card_avatar = (ImageView)findViewById(R.id.avatar);
	        company = (TextView)findViewById(R.id.text_company);
	        person_name = (TextView)findViewById(R.id.text_name);
	        citycompany = (TextView)findViewById(R.id.text_city);
		
	        company.setText("Appliscience");
	  	    
			person_name.setText("Mauri");
		    citycompany.setText("Barcelona");
		    
	        		   	
		    // Set image from resources -> mThumbIds
		    bm = im.decodeSampledBitmapFromRes(getApplicationContext(),im.mThumbIds[1],  200, 200);
		    card_image.setImageBitmap(bm);
	        
		    
		    bm = im.decodeSampledBitmapFromRes(getApplicationContext(),im.mThumbIds[4],  100, 100);
		    
	        RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
	        //card_avatar.setImageBitmap(rad.getBitmap());
	        
	        card_avatar.setBackground(rad);
	        
	        // get action bar   
	        //ActionBar actionBar = getActionBar();
	 
	        // Enabling Up / Back navigation
	        //actionBar.setDisplayHomeAsUpEnabled(true);
	    }
	 
	 /*@Override
	 public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
	     int totalItemCount) {
	   if(visibleItemCount == 0) return;
	   if(firstVisibleItem != 0) return;

	   //card_image.setTranslationY(-mListView.getChildAt(0).getTop() / 2);
	   card_image.setCurrentTranslation(mListView.getChildAt(0).getTop());

	 }
	 
	 public void setCurrentTranslation(int currentTranslation) {
		  mCurrentTranslation = currentTranslation;
		  invalidate();
		}

		@Override
		public void draw(Canvas canvas) {
		  canvas.save();
		  canvas.translate(0, -mCurrentTranslation / 2)  ;
		  super.draw(canvas);
		  canvas.restore();
		}
	
		public void setCurrentTranslation(int currentTranslation) {
		    mCurrentTranslation = currentTranslation;
		    float ratio =  -mCurrentTranslation / (float)getHeight();
		    int color = Color.argb((int) (fMAX_COLORFILTER_ALPHA * ratio), 0, 0, 0);
		    setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		}
	*/

}
