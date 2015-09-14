
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
//import android.app.FragmentManager;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.qcards.R;
import com.example.qcards.UtilsJson;
import com.example.qcards.UtilsPics;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.DeleteCardGroupDialog;
import com.example.qcards.sharecards.ShareCard;
/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class DisplayCardsFragment extends Fragment {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
	
	
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    //private ViewPager mPager;
    public static ViewPager mPager;
	
    private UtilsPics im = new UtilsPics();
    
	private ShareActionProvider mShareActionProvider;
	
	final static String ARG_POSITION = "ContactId";
	//int mCurrentPosition = -1;
	int ContactId = -1;
	
	public DatabaseHandler mydb;
	private List<Contact> contactListToShare;
	
	
	//int ContactId;
	private Bitmap bm = null;
	
	private LinearLayout ll;
	//private ScrollView ll;
	
    private FragmentActivity fa;
	 
	private Context mContext;
	private boolean isLayout = false;
	
	NfcAdapter mNfcAdapter;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		
        if (savedInstanceState != null) {
        	ContactId = savedInstanceState.getInt(ARG_POSITION);
        }

		fa = super.getActivity();
     	ll = (LinearLayout) inflater.inflate(R.layout.activity_screen_slide, container, false);
	    
	    mContext = fa.getApplicationContext();
	    
	    // Database
	    mydb = new DatabaseHandler(fa);
	    
	    ContactId = getArguments().getInt("ContactId");
	    int posiCard = getArguments().getInt("posiCard");
	    
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) ll.findViewById(R.id.pager);
        //mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        isLayout = false;
        //mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(),isLayout, ContactId,mydb.numberOfRows(),posiCard);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(),isLayout, ContactId,mydb.numberOfRows());
        
        mPager.setAdapter(mPagerAdapter);
        
        
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                fa.invalidateOptionsMenu();

            }
        });
        
        mPager.setCurrentItem(posiCard);

        return ll;
     }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    	// Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.update_card_actions, menu);
       
       MenuItem item = menu.findItem(R.id.action_share);
       mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
       
       super.onCreateOptionsMenu(menu,inflater);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) 
    { 
       super.onOptionsItemSelected(item);
       
       switch(item.getItemId()) 
       { 
       	 case R.id.action_edit:
           	 // Edit card details
       		 
             // Sending image id to EditCardActivity
             Intent intent = new Intent(mContext, com.example.qcards.contactsqlite.EditCardActivity.class);
             
             // Passing id of the card
             intent.putExtra("ContactId", ContactId);
             
             intent.putExtra("flag_new", false);       // flag_new = true ONLY TO CREATE A NEW CARD!
             
             startActivity(intent);

 		      return true; 
       	 case R.id.action_delete:
       		  deleteCard();
       		  return true;
       	 case R.id.action_share:
       	  	   // Share card
        	  ShareCards();
 		      return true;
       	 case R.id.action_shake:
 		      return true;
 		      
       	 case R.id.action_beam:
       		   NfcCard();
 		      return true; 
       default: 
       return super.onOptionsItemSelected(item); 

       } 
    } 
    
    //Call to update the share intent
    private void setShareIntent(Intent shareIntent) 
    {
      if (mShareActionProvider != null) {
          mShareActionProvider.setShareIntent(shareIntent);
      }
    }
    
    private void deleteCard()
    {
        int []CId = {0};
        boolean deleteGroup;
        String []textDialog = {"",""};

   		CId[0] = ContactId;
		deleteGroup = false;   //False to delete cards
		
		// Text to be included in the dialog					
		textDialog[0] = getString(R.string.delete_cards);
		textDialog[1] = getString(R.string.are_you_sure_delete_card);
		
		// Call the dialog to delete the card selected
		DeleteCardGroupDialog dialog = DeleteCardGroupDialog.newInstance(textDialog, CId, deleteGroup);
        dialog.show(fa.getSupportFragmentManager(), "fragmentDialogDelete");
		//dialog.show(DisplayCardActivity.this, "fragmentDialogDelete");
        
    }
    

	/**
	 * Launching activity to share a card by NFC
	 * */
	private void NfcCard() {
		
		File FolderDir;
		FolderDir = im.FindDir(mContext);
		String filename = UtilsPics.IMFILENAME + "00";
		im.saveImageToInternalStorage(mContext,bm,FolderDir,filename);
		
	    //Intent i = new Intent(DisplayCardActivity.this, com.example.qcards.sharecards.NfcActivity.class);
		Intent i = new Intent(fa, com.example.qcards.sharecards.NfcActivity.class);
	    startActivity(i);
	}
	
	/**
	 * Launching activity to share card
	 * */
	private Intent ShareCards() {
		
		String cardData;
		// Copy file to memory 
		File FolderDir = im.FindDir(mContext);
		String filename = UtilsPics.IMFILENAME + "00";
		
		im.saveImageToInternalStorage(mContext,bm,FolderDir,filename);
		
		cardData = UtilsJson.ContactsToJSon(contactListToShare);
		
		im.saveFileToInternalStorage(mContext,cardData,FolderDir ,UtilsPics.JSONFILENAME);
		
		ShareCard ShCard = new ShareCard(mContext);
		LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
		cardsByGroups.put(0,0);
		
		Intent shareIntent = ShCard.SetupShare(cardsByGroups);
	
		startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share_card_to)));
		return shareIntent;
	}
	
    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
        	//int u = args.getInt("ContactId");
        	//ScreenSlicePageFragCards.updateCardView(args.getInt("ContactId"));
        } else if (ContactId != -1) {
            // Set article based on saved instance state defined during onCreateView
        	//ScreenSlicePageFragCards.updateCardView(ContactId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current card selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, ContactId);
    }
    
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        menu.findItem(R.id.action_location_found).setVisible(false);
        menu.findItem(R.id.action_new).setVisible(false);
        menu.findItem(R.id.action_location_found).setVisible(false);
        menu.findItem(R.id.action_help).setVisible(false);
        menu.findItem(R.id.action_sortby).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_delete_groups).setVisible(false);
        
        //return super.onPrepareOptionsMenu(menu);
    }

 }


