


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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qcards.R;
import com.example.qcards.UtilsPics;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.tabpanel.Tab1Activity;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class ScreenSlicePageFragCards extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    static Context mContext;
    //public static int[] CCId = {0};
    
    private FragmentActivity fa;
    public DatabaseHandler mydb;
    
    private Bitmap bm = null;
    
	private Contact contact;
	private List<Contact> contactListToShare;
	
	private ImageView card_image;
	private TextView person_name, company, jobposition, citycompany;
	private WebView description;
	
	private UtilsPics im = new UtilsPics();
	private int imageID;
	private LinearLayout ll;
	private int posiCard;
	//private int ContactId;

	
    /*private static String[] dataObjects = new String[]{ "Text #1",
		"Text #2",
		"Text #3" };*/
    

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private static int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    //public static ScreenSlicePageFragCards create(int pageNumber, int mContactId, int posiCard) {
    public static ScreenSlicePageFragCards create(int pageNumber, int mContactId) {
    	ScreenSlicePageFragCards fragment = new ScreenSlicePageFragCards();
        Bundle args = new Bundle();
        args.putInt("ContactId", mContactId);
        //args.putInt("posiCard", posiCard);
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlicePageFragCards() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	mContext = getActivity().getApplicationContext();
        mPageNumber = getArguments().getInt(ARG_PAGE);
        posiCard = getArguments().getInt("posiCard");
        //ContactId = getArguments().getInt("ContactId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	setHasOptionsMenu(true);
    	fa = super.getActivity();
    	
	    // Database
	    mydb = new DatabaseHandler(fa);
	    
	    
	    //ContactId = getArguments().getInt("ContactId");

        
    	// Inflate the layout containing a title and body text.
    	
        /*ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
                */
    	//View rootView = (View) inflater
                //.inflate(R.layout.fragment_screen_slide_page, container, false);
    	ll = (LinearLayout) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
    	 	
	  	      /*	  	    card_image.setOnClickListener(new OnClickListener() {

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
	        });*/
    	updateCardView();

        //return rootView;
    	return ll;
    }
    
    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    
	//public static void updateCardView(int CId)
    //public static void updateCardView()
    public void updateCardView()
	{
    	//int pos;
    	//mydb.numberOfRows();
    	/*
    	int max_indx = mydb.numberOfRows() - posiCard;
    	if (mPageNumber >= max_indx){
    		pos = posiCard + mPageNumber;
    	}else{
    		pos = posiCard + mPageNumber;
    	}*/
    		
    	
	   	int CId[] = {0}; 
	   	CId[0] = Tab1Activity.contactList.get(mPageNumber).getID();
	   	
	   	contact = mydb.getContactsById(CId).get(0);

	   	contactListToShare = new ArrayList<Contact>();
		contactListToShare.add(contact);

    	
        card_image = (ImageView)ll.findViewById(R.id.card_image);
        description = (WebView)ll.findViewById(R.id.description);
        person_name = (TextView)ll.findViewById(R.id.person_name);
        company = (TextView)ll.findViewById(R.id.company);
	   	jobposition = (TextView)ll.findViewById(R.id.jobposition);
	   	citycompany = (TextView)ll.findViewById(R.id.citycompany);
	   	
		//contact = mydb.getData(CId);
	   	//int[] CCId = {0}; 
		//List<Contact> contactList = new ArrayList<Contact>();
		//contactList = mydb.getAllContacts();
		//Id[0] = CId;
		 

		String text = "<html><head>"
  	          + "<style  type=\"text/css\">body{color: #000; background-color: #fff; text-align:justify}"
  	          + "</style></head>"
  	          + "<body>"                          
  	          + "We are a company specialized in building a connection between the "
  	          	+ "research developed at universities"
  		  		+ " and the problems that currently affect the society."
  	          + "</body></html>";

 	    // Display contact details
  	    description.loadData(text, "text/html", "utf-8");
       
  	    company.setText("Appliscience");
  	    jobposition.setText("CEO at Appliscience");
  	    
		// Get image id of the card selected        		
		imageID = contact.getImageID();
  
		person_name.setText((CharSequence)contact.getName());
	    citycompany.setText((CharSequence)contact.getPlace());
	    
	    // Set image from resources -> mThumbIds
	    bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[(int)imageID-1],  220, 220);
	    card_image.setImageBitmap(bm);

	}
	


}
