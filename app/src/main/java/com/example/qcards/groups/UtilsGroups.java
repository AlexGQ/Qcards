package com.example.qcards.groups;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.example.qcards.R;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.ShowDialogs;
import com.example.qcards.groups.Groups;


public class UtilsGroups{
	
	static FragmentActivity fa;
	public static int selection;
	public static boolean getGroup = false;
	public static boolean doTheSame = false;
	
	public UtilsGroups() {
	}
	

public static void insertGroupsToDB (LinkedHashMap<String, List<Contact>> listGroupsImport, DatabaseHandler mydb, FragmentActivity fa)
	{
	int group_id;
   	int contactId;
   	int[] contacts_ids;
   	boolean startAct;
   	Contact contact;
   	String [] Dgname = {"",""};
   	List<Contact> listCardsImport = new ArrayList<Contact>();
   	
   	ShowDialogs mshowDialogs = new ShowDialogs(fa);
   	String group_name;
   	int groups_repeated = 0;
   	Groups ngroup = new Groups();
   	
   	Dgname[0] = fa.getString(R.string.text_group_exist);
   	Dgname[1] = fa.getString(R.string.text_do_the_same);
   	
   	getGroup = false;
   	
   	for (Entry<String, List<Contact>> entry : listGroupsImport.entrySet()) {
   		
        group_name = entry.getKey();
        if(mydb.ifGroupNameExist(group_name))
		{
        	groups_repeated++;
		}
   	}
   	if (groups_repeated != 0)
   	{
   		startAct = true;
		mshowDialogs.showRepeatedGroupDialog(Dgname,listGroupsImport,startAct,groups_repeated);
   	}else
   	{
   		startAct = true;
   		for (Entry<String, List<Contact>> entry : listGroupsImport.entrySet()) {
	   		
	        group_name = entry.getKey();

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
   		}
   		
   		if (startAct){
  		   Intent intentMainAct = new Intent(fa, com.example.qcards.MainActivity.class);
  		   fa.startActivity(intentMainAct);
  	   }


   	}
  }
}


