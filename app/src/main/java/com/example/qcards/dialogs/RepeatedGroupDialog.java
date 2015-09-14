


package com.example.qcards.dialogs;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.groups.Groups;


public class RepeatedGroupDialog extends DialogFragment{
	private FragmentActivity fa;
	public DatabaseHandler mydb;
	private static LinkedHashMap<String, List<Contact>> listGroupsImport;
	//private int[] groupIdToDelete;
	//private static String titleA;
	//private static int []contactIdToGroup;
	//private static List<Integer>ContactIdToGroup = new List<Integer>();
	String GroupText;
	
	private ArrayAdapter<String> groupsAdapter;
	
	private int sel;
	private AlertDialog.Builder builder;
	
	private Context mContext;
	private ListView lv;
	private boolean startAct;
	
	private boolean updateList;
	private UtilsListView UtilsLiVim = new UtilsListView();
	
    public RepeatedGroupDialog() {
     }
   
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	fa = getActivity();
    	mContext = fa.getApplicationContext();
    	
        builder = new AlertDialog.Builder(fa);
        
        String []titleD = new String[2];
        String []checkAllTitle = new String[1];
         
        titleD = getArguments().getStringArray("textDialog");
        startAct = getArguments().getBoolean("startAct");
        int groups_repeated = getArguments().getInt("groups_repeated");
        checkAllTitle[0] = titleD[1];
        //final int []contactIdToGroup = getArguments().getIntArray("ContactIdToGroup");
        
        mydb = new DatabaseHandler(fa);
	    		
    	builder.setTitle(titleD[0] + " " + groups_repeated);
    	//builder.setMessage("Cards will not be deleted");
    	
    	String []mList;
	    // Load sortByList from res
	    
    	mList = getResources().getStringArray(R.array.repeat_group_opt);
    	
    	LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    	
    	View BtnNewGroupView = inflater.inflate(R.layout.dialog_delete_group_list, null);
    	
    	groupsAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,mList);
       	
       	lv = (ListView) BtnNewGroupView.findViewById(R.id.delete_cgroup);
    	
    	lv.setAdapter(groupsAdapter);
    	
    	lv.setItemsCanFocus(false);
	    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    
    	lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long arg3) {
            	
            	//SparseBooleanArray isel = lv.getCheckedItemPositions();
            	sel = pos;
            	 

            }
        });
	  
	    builder.setView(BtnNewGroupView);
	  
	    
    	// Set the action buttons
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	@Override
    	public void onClick(DialogInterface dialog, int id) {
    		int group_id;
    	   	int contactId;
    	   	int[] contacts_ids;
    	   	Contact contact;
    	   	List<Contact> listCardsImport = new ArrayList<Contact>();
    	   	
    	   	String group_name;
    	   	//int groups_repeated = 0;
    	   	Groups ngroup = new Groups();
    	   	Groups editGroup = new Groups();
    		
           	//mListener.onDRGPositiveClick(RepeatGroupDialog.this, sel);
       		for (Entry<String, List<Contact>> entry : listGroupsImport.entrySet()) {
    	        group_name = entry.getKey();
    	        
       	        if(mydb.ifGroupNameExist(group_name))
       			{
       	        	
    	        	 switch (sel) {
    	       	        case 0: // Combine groups
    		            	 
    		         		// Get the group that has the same name
    		     			editGroup = mydb.getGrupoByName(group_name);
    		     			
    				    	listCardsImport = entry.getValue();
    				    	
    				    	contacts_ids = new int [listCardsImport.size()];
    				    	for (int nc = 0; nc < listCardsImport.size(); nc++)
    				    	{
    				    		
    				    		contact = listCardsImport.get(nc);
    				    		contactId = (int)mydb.insertContact(contact);
    				    		contacts_ids[nc] = contactId; 
    			    		
    				    	}
    		     			// Insert the contacts in the existent group, this combined both groups.
    		     			mydb.insertCardsToGroup(editGroup.getId(), contacts_ids);
                            break;
    		             case 1: // Rename group  
    		            	 
    		             	// If the group name exists create a new name -> grupo_name(x)
    		         		int indx = 2;
    		         		String all_group_name = group_name;
    		         		while(mydb.ifGroupNameExist(group_name))
    		         		{
    		         			group_name = all_group_name + " (" + indx + ")";
    		         			indx++;
    		         		}
    				        
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
    				    	mydb.insertCardsToGroup(group_id, contacts_ids);
    				    	
    		                break;
    		             default: 
    		                break;
    	        	 }
       			}else
       			{

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
    	        	
        		}
	     	   dismiss();
	     	   if (startAct){
	     		   Intent intentMainAct = new Intent(mContext, com.example.qcards.MainActivity.class);
	     		   startActivity(intentMainAct);
	     	   }
	     	   
	     	   //updateList = true;
	     	   //UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
	     	   //UtilsLiVim.prepareListCardsGroups(mydb,updateList);


    		
    	}})
    	.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int id) {
    	       	//mListener.onDRGNegativeClick(RepeatGroupDialog.this);
    		   	   if (startAct){
    	     		   Intent intentMainAct = new Intent(mContext, com.example.qcards.MainActivity.class);
    	     		   startActivity(intentMainAct);
    	     	   }

    		}
    	});
    	
        return builder.create();
    }
    
    @Override
	  public void onResume() {
	    super.onResume();
	    		lv.setItemChecked(0, true);
	  }

	 public static RepeatedGroupDialog newInstance(String []textDialog, LinkedHashMap<String, List<Contact>> mlistGroupsImport, boolean mstartAct, int mgroups_repeated) {
		 RepeatedGroupDialog fragment = new RepeatedGroupDialog();
	
	     Bundle args = new Bundle();
	     //args.putSerializable(ARG_LISTENER_TYPE, type);
	     listGroupsImport = mlistGroupsImport;
	     args.putStringArray("textDialog", textDialog);
	     args.putBoolean("startAct", mstartAct);
	     args.putInt("groups_repeated", mgroups_repeated);
	     fragment.setArguments(args);
	
	     return fragment;
	 }
}
