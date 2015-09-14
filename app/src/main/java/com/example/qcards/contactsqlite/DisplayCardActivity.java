package com.example.qcards.contactsqlite;

import java.io.File;
import java.util.ArrayList;
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
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qcards.R;
import com.example.qcards.UtilsJson;
import com.example.qcards.UtilsPics;
import com.example.qcards.dialogs.DeleteCardGroupDialog;
import com.example.qcards.sharecards.ShareCard;
import com.example.qcards.tabpanel.Tab1Activity.OnHeadCardSelectedListener;

//public class DisplayCardActivity extends FragmentActivity{
public class DisplayCardActivity extends Fragment{
	
	private ShareActionProvider mShareActionProvider;
	
	final static String ARG_POSITION = "ContactId";
	//int mCurrentPosition = -1;
	int ContactId = -1;
	
	public DatabaseHandler mydb;
	private Contact contact;
	private List<Contact> contactListToShare;
	
	private ImageView card_image;
	private TextView person_name, company, jobposition, citycompany;
	private WebView description;
	
	//int ContactId;
	private Bitmap bm = null;
	
	private LinearLayout ll;
	//private ScrollView ll;
	
    private FragmentActivity fa;
	 
	private Context mContext;
	
	private UtilsPics im = new UtilsPics();
	private int imageID;
	NfcAdapter mNfcAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		
        if (savedInstanceState != null) {
        	ContactId = savedInstanceState.getInt(ARG_POSITION);
        }

		fa = super.getActivity();
     	ll = (LinearLayout) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
	    
	    mContext = fa.getApplicationContext();
	    
	    // Database
	    mydb = new DatabaseHandler(fa);
	    
	    ContactId = getArguments().getInt("ContactId");
	
	    return ll;

     }
	
	public void updateCardView(int CId)
	{
		
        card_image = (ImageView)getActivity().findViewById(R.id.card_image);
        description = (WebView)getActivity().findViewById(R.id.description);
        person_name = (TextView)getActivity().findViewById(R.id.person_name);
        company = (TextView)getActivity().findViewById(R.id.company);
	   	jobposition = (TextView)getActivity().findViewById(R.id.jobposition);
	   	citycompany = (TextView)getActivity().findViewById(R.id.citycompany);
	   	
		//contact = mydb.getData(CId);
	   	int[] Id = {0}; 
		List<Contact> contactList = new ArrayList<Contact>();
		//contactList = mydb.getAllContacts();
		Id[0] = CId;

		contactList = mydb.getContactsById(Id);
		contact = contactList.get(0);

		
		contactListToShare = new ArrayList<Contact>();
		contactListToShare.add(contact);
		 

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
	
    
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.update_card_actions, menu);*/
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
        	updateCardView(args.getInt("ContactId"));
        } else if (ContactId != -1) {
            // Set article based on saved instance state defined during onCreateView
        	updateCardView(ContactId);
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