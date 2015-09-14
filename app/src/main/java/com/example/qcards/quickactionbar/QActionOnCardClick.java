package com.example.qcards.quickactionbar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.example.qcards.R;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.DeleteCardGroupDialog;
import com.example.qcards.dialogs.GroupCardDialog;
import com.example.qcards.sharecards.ShareCard;

public class QActionOnCardClick {
	 // constructor
    public QActionOnCardClick() {
 
    }

	public void OnCardClick (int pos, int tab,Contact contact, DatabaseHandler mydb,FragmentActivity fa){	

		boolean deleteGroup;
		int ContactId;
		Context mContext = fa.getApplicationContext();
		
		int [] CId = {0};
		LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
		
		ContactId = contact.getID();
		
		if (pos == 0) { //Share selected card
			
			
			int indexGroup = 0;
			List<Contact> contactListToShare = new ArrayList<Contact>();
			
			CId[0] = ContactId;
			contactListToShare = mydb.getContactsById(CId);
			ShareCard ShCard = new ShareCard(mContext);
			cardsByGroups = ShCard.ShareCards(contactListToShare,indexGroup);
			Intent shareIntent = ShCard.SetupShare(cardsByGroups);
		
			fa.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.text_share_card_to)));
			
		} else if (pos == 1) { //Edit selected card
			
		     Intent intent = new Intent(mContext, com.example.qcards.contactsqlite.EditCardActivity.class);
		     // Passing ContactId of the card.
		     intent.putExtra("ContactId", ContactId);
		     intent.putExtra("flag_new", false);       // flag_new = true ONLY TO CREATE A NEW CARD!
		     fa.startActivity(intent);
		     
		}else if (pos == 2) { //Group selected card
			
				CId[0] = ContactId;
				
				// Text to be included in the dialog
				String[] textDialog = {mContext.getString(R.string.add_to_group), mContext.getString(R.string.create_new_group)};
				
				// Call the dialog to group the card selected
				GroupCardDialog dialog = GroupCardDialog.newInstance(textDialog, CId);
			    dialog.show(fa.getSupportFragmentManager(), "fragmentDialogGroup");
		        
		} else if (pos == 3) { //Delete selected card
			
			deleteGroup = false;  // False to delete cards
			CId[0] = ContactId;
			
			// Text to be included in the dialog					
			String[] textDialog = {mContext.getString(R.string.delete_cards),mContext.getString(R.string.are_you_sure_delete_card)};
			
			// Call the dialog to delete the card selected
			DeleteCardGroupDialog dialog = DeleteCardGroupDialog.newInstance(textDialog, CId, deleteGroup);
		    dialog.show(fa.getSupportFragmentManager(), "fragmentDialogDelete");
		}
	}
}

	
    
	







