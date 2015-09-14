package com.example.qcards.quickactionbar;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.qcards.R;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.DeleteCardGroupDialog;
import com.example.qcards.dialogs.ShowDialogs;
import com.example.qcards.groups.ActivityGroupCards;
import com.example.qcards.groups.Groups;
import com.example.qcards.sharecards.ShareCard;

public class QActionOnGroupClick {
	public QActionOnGroupClick (){
	}
	
	public void OnGroupClick (int pos, Groups group, DatabaseHandler mydb,FragmentActivity fa){
	
	int [] GId = {0};
	String [] Dgname = {"",""};
	
	LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
	
	String[] textDialog = {"",""};
	
	boolean deleteGroup;
	

	Context mContext = fa.getApplicationContext();
	//group = mydb.getGrupoByName(listDataHeader.get(groupPosition));
	
	if (pos == 0) { //Share group 
		
		//group = mydb.getGrupoByName(listDataHeader.get(groupPosition));
		GId[0] = group.getId();
		LinkedHashMap<String, List<Contact>> groupListToShare;
		
		groupListToShare = mydb.getContactsWithGroupIds(GId);
		ShareCard ShCard = new ShareCard(mContext);
		ShCard.ShareGroups(groupListToShare);
		Intent shareIntent = ShCard.SetupShare(cardsByGroups);

		fa.startActivity(Intent.createChooser(shareIntent, fa.getString(R.string.text_share_card_to)));


		
	} else if (pos == 1) { //Rename group
			ShowDialogs shD = new ShowDialogs(fa);    
			
			//Dgname[1] = listDataHeader.get(groupPosition);
			Dgname[1] = group.getGroupName();
			//group = mydb.getGrupoByName(Dgname[1]);
			//boolean new_group = false;
			shD.showEditDialog(Dgname);
			
	}else if (pos == 2)
	{
		ActivityGroupCards newFragment = new ActivityGroupCards();
		
        Bundle args = new Bundle();
        args.putString("ngroupText", group.getGroupName());
        newFragment.setArguments(args);

        //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        FragmentTransaction transaction = fa.getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        //transaction.add(R.id.fragment_container, newFragment);
        transaction.replace(R.id.fragment_container, newFragment);
        //transaction.addToBackStack();
        transaction.addToBackStack(null);
        
        // Commit the transaction
        transaction.commit();

			
	} else if (pos == 3) { //Delete group (cards in the group are not deleted)
		
		//group = mydb.getGrupoByName(listDataHeader.get(groupPosition));
		GId[0] = group.getId();
		
		deleteGroup = true;   //False to delete cards
		
		// Text to be included in the dialog					
		textDialog[0] = fa.getString(R.string.delete_groups);
		textDialog[1] = fa.getString(R.string.are_you_sure_delete_group);
		
		// Call the dialog to delete the card selected
		DeleteCardGroupDialog dialog = DeleteCardGroupDialog.newInstance(textDialog, GId, deleteGroup);
        dialog.show(fa.getSupportFragmentManager(), "fragmentDialogDelete");
	}

	}
}

