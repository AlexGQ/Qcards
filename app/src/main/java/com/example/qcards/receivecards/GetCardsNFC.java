package com.example.qcards.receivecards;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsJson;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.ShowDialogs;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.groups.UtilsGroups;


public class GetCardsNFC extends FragmentActivity{
	
    public DatabaseHandler mydb;
    public static SharedPreferences settings;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_file);
        
        String PREFS_NAME = "MyPrefsFile";
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mydb = new DatabaseHandler(this);
 
        /*ImportFileFragment firstFragment = new ImportFileFragment();
        
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_container_import, firstFragment, "RECEIVED_NFC_FRAGMENT").commit();
         */
        
    }




@Override
public void onNewIntent(Intent intent) {
	// onResume gets called after this to handle the intent
	setIntent(intent);
}


/**
* Parses the NDEF Message from the intent and prints to the TextView
*/
	void processIntent(Intent intent) {
	
		//boolean updateList;
		//UtilsListView UtilsLiVim = new UtilsListView();
		
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
		        NfcAdapter.EXTRA_NDEF_MESSAGES);
		
		
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		if (msg != null)
		{
			// record 0 contains the MIME type, record 1 is the AAR, if present
		String str = new String(msg.getRecords()[0].getPayload());
		
		boolean shareGroup = UtilsJson.checkShareGroupsOrCards(str);
		
		LinkedHashMap<String, List<Contact>> listGroupsImport = new LinkedHashMap<String, List<Contact>>();
		List<Contact> listCardsImport = new ArrayList<Contact>();
		ShowDialogs mshowDialogs = new ShowDialogs(this);
		
			if (shareGroup){
				
				listGroupsImport = UtilsJson.JSonToGroups(str);
				
				UtilsGroups.insertGroupsToDB(listGroupsImport, mydb, this);
				
		       	/*for (Entry<String, List<Contact>> entry : listGroupsImport.entrySet()) {
		       		
		            String group_name = entry.getKey();
		            Groups ngroup = new Groups();
		        	ngroup.setGroupName(group_name);
		        	
		        	group_id = (int)mydb.insertGroup(ngroup);
		        	
		        	listCardsImport = entry.getValue();
		        	
		        	contacts_ids = new int [listCardsImport.size()];
		        	for (int nc = 0; nc < listCardsImport.size(); nc++)
		        	{
		        		
		        		contact = listCardsImport.get(nc);
		        		contactId = (int)mydb.insertContact(contact);
		        		contacts_ids[nc] = contactId; 
		        		
		        	}
		        	
		        	//insert contacts_ids (need to be selected and store in contacts_ids) *****
		         	mydb.insertCardsToGroup(group_id, contacts_ids);	        	
		        	
		         	// Re-write all the groups and contacts *************
		         	//updateList = true;
		         	//UtilsLiVim.prepareListCardsGroups(mydb,updateList);
		       	}*/
			}
			else
			{
				
				listCardsImport = UtilsJson.JSonToContacts(str);
				boolean startAct = true;
				mshowDialogs.ShowDialogGroupImportCards(listCardsImport,startAct);
				
			    //updateList = true;
		     	//UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
		
		    	/*for (int nc = 0; nc < listCardsImport.size(); nc++)
		    	{
		    		contact = listCardsImport.get(nc);
		    		contactId = (int)mydb.insertContact(contact);
		    	}*/
		    	// Re-write all the groups and contacts *************
		     	//updateList = true;
		     	//UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
			}
		}else
		{
			Toast.makeText(getApplicationContext(), "0 card(s) received", Toast.LENGTH_LONG).show();
		}
	}
    
    @Override
    protected void onResume() {
        super.onResume();

        // If the app is run for first time
        
        if (settings.getBoolean("firstRun", true)) {
            // Do first run stuff here then set 'firstRun' as false.
      	  
      	  SharedPreferences.Editor editor = settings.edit();

      	  // Set order cards by date as false 
            editor.putBoolean("orderCardsByDate", false);
      	  // Set order groups by date as false
            editor.putBoolean("orderGroupsByDate", false);
            
            editor.putBoolean("firstRun", false);

            // Commit the edits
            editor.commit();
        }
        else
        {   
      	  // Set attributes from preferences 
      	  SortByDialog.orderCardsByDate = settings.getBoolean("orderCardsByDate", false);
      	  SortByDialog.orderGroupsByDate = settings.getBoolean("orderGroupsByDate", false);
        }
      	// Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
    	    processIntent(getIntent());
    	}

    }


}