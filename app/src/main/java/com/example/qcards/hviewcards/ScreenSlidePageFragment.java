/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.qcards.hviewcards;

//import android.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qcards.R;
import com.example.qcards.UtilsPics;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    Context mContext;
    
    ImageView card_image;
	TextView person_name, company, jobposition, citycompany;
	WebView description;

   
    /*private static String[] dataObjects = new String[]{ "Text #1",
		"Text #2",
		"Text #3" };*/
    

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	mContext = getActivity().getApplicationContext();
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
        
    	// Inflate the layout containing a title and body text.
    	
        /*ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
                */
    	View rootView = (View) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
    	 	
    	/*View rootView = (View) inflater
                .inflate(R.layout.fragment_screen_slide_page, null);
    	*/
    	
        // Set the title view to show the page number.
        //((TextView) rootView.findViewById(android.R.id.text1)).setText(
        		//getString(R.string.title_template_step, mPageNumber + 1));
    	//UtilsPics im = null;
    	UtilsPics im = new UtilsPics();
    	 
    	
        Bitmap bm = null;
        bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[mPageNumber],  220, 220);
 	   
 	   //View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_content_activity, null);
        
        card_image = (ImageView)rootView.findViewById(R.id.card_image);
        description = (WebView)rootView.findViewById(R.id.description);
		person_name = (TextView)rootView.findViewById(R.id.person_name);
		company = (TextView)rootView.findViewById(R.id.company);
		jobposition = (TextView)rootView.findViewById(R.id.jobposition);
		citycompany = (TextView)rootView.findViewById(R.id.citycompany);

	    card_image.setImageBitmap(bm);
		card_image.setAdjustViewBounds(true); 

	       
	       person_name.setText("Mauricio Venail");
	  	      
	  	      String text = "<html><head>"
	  	          + "<style  type=\"text/css\">body{color: #000; background-color: #fff; text-align:justify}"
	  	          + "</style></head>"
	  	          + "<body>"                          
	  	          + "We are a company specialized in building a connection between the "
	  	          	+ "research developed at universities"
	  		  		+ " and the problems that currently affect the society."
	  	          + "</body></html>";

	  	      
	  	      description.loadData(text, "text/html", "utf-8");
	       
	  	      company.setText("Appliscience");
	  	      citycompany.setText("Barcelona, Spain");
	  	      jobposition.setText("CEO at Appliscience");
	  	      
	  	    card_image.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View arg0) {
	                //Toast.makeText(mContext, "Cilcksdfsdfadsfadsfadsed..",Toast.LENGTH_SHORT).show();
	                
	                // Sending image id to EditCardActivity
	                Intent i = new Intent(mContext, com.example.qcards.contactsqlite.EditCardActivity.class);
	                
	                // Passing image array index
	                i.putExtra("position", mPageNumber);
	                
	                // Passing id for new card
	                i.putExtra("flag_new", true);

	                startActivity(i);

	            }
	        });


        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
