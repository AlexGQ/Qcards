
package com.example.qcards.dialogs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qcards.MainActivity;
import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.groups.Groups;
import com.example.qcards.sharecards.ShareCard;


public class ShareGroupsDialog extends DialogFragment{
	private FragmentActivity fa;
	
	private Context mContext;
	
	public DatabaseHandler mydb;
	private List<Groups> mygroups = new ArrayList<Groups>();
	
	private ListView lv;

	 public ShareGroupsDialog() {
	     // Empty constructor required for DialogFragment
	 }
	 
	 public static ShareGroupsDialog newInstance(String []textDialog) {
		 ShareGroupsDialog fragment = new ShareGroupsDialog();

	     Bundle args = new Bundle();
	     args.putStringArray("textDialog", textDialog);
	     //args.putBoolean("sortByGroup", sortByGroup);
	     fragment.setArguments(args);

	     return fragment;
	 }

	 

	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 	fa = getActivity();
	    	mContext = fa.getApplicationContext();
	    	
	    	ArrayAdapter<String> groupsAdapter;
	    
		 	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 	
		 	mydb = new DatabaseHandler(fa);
		    
	    	String []mdtitle = new String[2];
	    	mdtitle = getArguments().getStringArray("textDialog");
	    	
	    	mydb = new DatabaseHandler(fa);
	    	
	    	Groups g = new Groups();
	    	
	    	mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
	    	
	    	ArrayList<String> group_names = new ArrayList<String>();
	    	
	    	for (int i = 0; i < mygroups.size() ; i++) {
	    		
	    		g = mygroups.get(i);
	    		group_names.add((String)g.getGroupName());
	    		
	        }
	    	
		    //sortByGroup = getArguments().getBoolean("sortByGroup");
		    
		    builder.setTitle(mdtitle[0]);
		    
		    LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    	
		    View view = inflater.inflate(R.layout.generic_list, null);
		    	    
		    //String []sortByList;
		    // Load sortByList from res
		    
		    groupsAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,group_names);
       	
		    lv = (ListView) view.findViewById(R.id.gen_list);
    	
		    lv.setAdapter(groupsAdapter);
    	
		    lv.setItemsCanFocus(false);
		    lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	    	lv.setOnItemClickListener(new OnItemClickListener() {

	            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
	                    long arg3) {

	            }
	        });
	    	
		    builder.setView(view)
		    
		    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
      		 {
      			 public void onClick(DialogInterface dialog, int id) 
      			 {
      				UtilsListView UtilsLiVim = new UtilsListView();
      				LinkedHashMap<String, List<Contact>> groupListToShare = new LinkedHashMap<String, List<Contact>>();
      				LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
      				
      	    		Groups gs = new Groups();
      	    		boolean updateList = true;
      	    		//int ng = 0; 
      	    		
      	    		// Get all groups from DB again, since a new group might be created.
      	    		mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
      	    		SparseBooleanArray choice_option = lv.getCheckedItemPositions();
      	    		
      	    		int[] groupIdsSelected = new int[mygroups.size()];
      	    		int k = 0;

      	    		// Save the groups ids that were checked in the Dialog
      	            for (int j = 0; j < mygroups.size(); j++) {
      	                if (choice_option.get(j)) {
      	                    gs = mygroups.get(j);
      	                    groupIdsSelected[k++] = gs.getId();
      	                }
      	            }
      	            
      	           	if (k != 0)
      	           	{
      		            // Copy cards Ids selected in CId 
      		            int []groupIdsToShare = new int [k];
      		            System.arraycopy( groupIdsSelected, 0, groupIdsToShare, 0, k);
      		
						groupListToShare = mydb.getContactsWithGroupIds(groupIdsToShare);
						ShareCard ShCard = new ShareCard(mContext);
						cardsByGroups = ShCard.ShareGroups(groupListToShare);
						Intent shareIntent = ShCard.SetupShare(cardsByGroups);

						startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share_card_to)));
      		            
      		            
      		    	      /*Toast.makeText(mContext,
      		    	    		groupIdsToShare.length + " " + getString(R.string.groups_deleted), Toast.LENGTH_LONG)     
      		    	      .show();*/
      		              
      		     	      //updateList = true;
      		    	      //UtilsLiVim.prepareListGroups(mydb,updateList,SortByDialog.orderGroupsByDate);

      	           	}

      				 
      				 
      			 }
      		})
      	    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
      	    {
      	    	public void onClick(DialogInterface dialog, int id) {
      	    		// User cancelled the dialog
      	    	}
      	    });
		    
		    return builder.create();
	 }
	 
	  @Override
	  public void onResume() {
	    super.onResume();
	  }
}

