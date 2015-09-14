package com.example.qcards;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.tabpanel.Tab1Activity;
import com.example.qcards.tabpanel.Tab2Activity;

public class UtilsListView {
	
	 // constructor
    public UtilsListView() {
 
    }
    
    
    /*
    * Preparing the list with cards and groups
    */
    public void prepareListCardsGroups (DatabaseHandler mydb, boolean updateList)
    {
    	prepareListGroups(mydb,updateList,SortByDialog.orderGroupsByDate);
        prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
    }
    
    /*
     * Preparing list with groups
     */
    public void prepareListGroups (DatabaseHandler mydb, boolean updateList, boolean orderByDate)
    {
    	List<String> listDataHeader = new ArrayList<String>();
    	LinkedHashMap<String, List<Contact>> listDataChild = new LinkedHashMap<String, List<Contact>>();
        
        listDataChild = mydb.getAllGroupsWithContacts(orderByDate);
        
        // iterate and display values
        for (Entry<String, List<Contact>> entry : listDataChild.entrySet()) {
                String group_name = entry.getKey();
                listDataHeader.add(group_name);
        }
        
        Tab2Activity.listDataHeader = new ArrayList<String>(listDataHeader);
        Tab2Activity.listDataChild = new LinkedHashMap<String, List<Contact>>(listDataChild);
        
        // Update Listview
        if (updateList)
        	Tab2Activity.listAdapter.UpdateGroupsList(listDataHeader, listDataChild, orderByDate);
    }

    /*
     * Preparing list with cards
     */
    
    public void prepareListCards (DatabaseHandler mydb, boolean updateList, boolean orderByDate)
    {
        List<Contact> contactList, mycontactList, nomycontactList;
        
        ArrayList<Integer> NLetters = new ArrayList<Integer>();
    	ArrayList<Integer> AccNLetters = new ArrayList<Integer>();
        
        nomycontactList = new ArrayList<Contact>();
        mycontactList = new ArrayList<Contact>();

	    mycontactList = mydb.getMyCards(1,orderByDate);
		nomycontactList = mydb.sortbyAlphaWithoutMyCards(1,orderByDate);
	
		AccNLetters = mydb.AccNLet;
		NLetters = mydb.NLet;
	 
		contactList = new ArrayList<Contact>(mycontactList);
		contactList.addAll(nomycontactList);
		
		Tab1Activity.AccNLetters = new ArrayList<Integer>(AccNLetters);
		Tab1Activity.NLetters = new ArrayList<Integer>(NLetters);
		Tab1Activity.contactList = new ArrayList<Contact>(contactList);
		
		if (updateList)
			Tab1Activity.listviewadapter.UpdateCardsList(contactList, NLetters, AccNLetters,orderByDate);
	}
    
    //Set group indicator expand and collapse in Expandable listview
    public void setGroupIndicatorToRight(ExpandableListView expListView, Activity fa) {
    	
    	Context Contx = fa.getApplicationContext();
    	
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        fa.getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(35,Contx), width
                - getDipsFromPixel(5,Contx));
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels, Context Contx) {
        // Get the screen's density scale
        final float scale = Contx.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}
