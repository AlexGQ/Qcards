
package com.example.qcards.dialogs;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.EditNameDialog.EditNameDialogListener;
import com.example.qcards.groups.Groups;


public class GroupImportCardDialog extends DialogFragment implements EditNameDialogListener{
	private FragmentActivity fa;
	public DatabaseHandler mydb;
	private static List<Contact> mlistCardsImport = new ArrayList<Contact>();
	
	private ArrayAdapter<String> listAdapter;
	private boolean mstartAct;

	AlertDialog.Builder builder;
	
	private Context mContext;
	private ListView lv;
	
	private boolean updateList;
	private UtilsListView UtilsLiVim = new UtilsListView();
	
    List<Groups> mygroups = new ArrayList<Groups>();
	
    public GroupImportCardDialog() {
     }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	fa = getActivity();
    	mContext = fa.getApplicationContext();
    	
        builder = new AlertDialog.Builder(fa);
        
        String titleD = getArguments().getString("textDialog");
        mstartAct = getArguments().getBoolean("startAct");
        

        builder.setTitle(titleD);
    	
    	mydb = new DatabaseHandler(fa);
    	
    	Groups g = new Groups();
    	
    	mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
    	
    	ArrayList<String> group_names = new ArrayList<String>();
    	
    	for (int i = 0; i < mygroups.size() ; i++) {
    		
    		g = mygroups.get(i);
    		group_names.add((String)g.getGroupName());
    		
        }
    	
    	LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    	
    	View BtnNewGroupView = inflater.inflate(R.layout.dialog_groups_list, null);
    	
    	listAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,group_names);
       	
       	lv = (ListView) BtnNewGroupView.findViewById(R.id.add_cgroup);
    	
    	lv.setAdapter(listAdapter);
    	
    	lv.setItemsCanFocus(false);
    	lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	/*lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long arg3) {

            }
        });*/
	    
	    Button ngb = (Button) BtnNewGroupView.findViewById(R.id.btn_newg);
	    
	    builder.setView(BtnNewGroupView);

	    setTargetFragment(this, 0);
	    
	    ngb.setOnClickListener(new View.OnClickListener()
	    {
	    @Override
	    public void onClick(View view)
	        {
	    	
	    		String [] Dgname = {"",""};
	    		EditNameDialog dialogFragment;
	    		
	    		EditNameDialogListener parentFragment = (EditNameDialogListener) getTargetFragment();
	    		
	    		Dgname[0] = getString(R.string.create_new_group);
	    		Dgname[1] = "";
	    		
	    		dialogFragment = EditNameDialog.newInstance(parentFragment,Dgname);
	    		dialogFragment.show(getFragmentManager(), getTag());
	    		
	    		
	        }
	    });
    	
    	// Set the action buttons
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	@Override
    	public void onClick(DialogInterface dialog, int id) {
    		Contact contact = new Contact(); 
    		Groups gs = new Groups();
    		
    		
    		
    		// Get all groups from DB
    		mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
    		SparseBooleanArray choice_option = lv.getCheckedItemPositions();

    		// Save the Card in the groups that were checked in the Dialog
            for (int j = 0; j < mygroups.size(); j++) {
                if (choice_option.get(j)) {
                    gs = mygroups.get(j);
                    int[] cId = new int[mlistCardsImport.size()];
                	for (int nc = 0; nc < mlistCardsImport.size(); nc++)
                	{
                		contact = mlistCardsImport.get(nc);
                		cId[nc] = (int) mydb.insertContact(contact);
                	}
                	mydb.insertCardsToGroup(gs.getId(), cId);
                }
            }
            
            
    	   Toast.makeText(mContext,
   	       "Total "+ choice_option.size() +" Items Selected.\n", Toast.LENGTH_LONG)     
    	      .show();
    	   dismiss();
    	   if (mstartAct){
    		   Intent intentMainAct = new Intent(mContext, com.example.qcards.MainActivity.class);
    		   startActivity(intentMainAct);
    	   }
    		updateList = true;
	     	//UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
	     	UtilsLiVim.prepareListCardsGroups(mydb,updateList);
    	   	

    	}})
    	.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int id) {
    			Contact contact = new Contact();
    			for (int nc = 0; nc < mlistCardsImport.size(); nc++)
    	    	{
    	    		contact = mlistCardsImport.get(nc);
    	    		mydb.insertContact(contact);
    	    	}
    			dismiss();
    			
    			if (mstartAct){
    				Intent intentMainAct = new Intent(mContext, com.example.qcards.MainActivity.class);
    				startActivity(intentMainAct);
    			}
    			updateList = true;
		     	UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
    			
    			
    		}
    	});
    	
        return builder.create();
    }

    public static GroupImportCardDialog newInstance(String []textDialog, List<Contact> listCardsImport, boolean startAct) {
    	GroupImportCardDialog f = new GroupImportCardDialog();

    	mlistCardsImport = listCardsImport;
    	
	     Bundle args = new Bundle();
	     args.putString("textDialog", textDialog[0]);
	     args.putString("textDialog", textDialog[0]);
	     args.putBoolean("startAct", startAct);
	     f.setArguments(args);
    	
        return f;
    }
    
    // On positive click - create a new group dialog 
    public void onDialogPositiveClick(DialogFragment dialog,String inputText, boolean new_group) {
    	
    		Groups ngroup = new Groups();
            
    		// Check if the group name is not empty 
        	if (inputText.isEmpty())
        		inputText = getString(R.string.text_new_group);
        	
    		// Check if the group name already exit, if so a new name is created
    		
    		int indx = 2;
    		String group_name = inputText; 
    		while(mydb.ifGroupNameExist(group_name))
    		{
    			group_name = inputText + " (" + indx + ")";
    			indx++;
    		}
    		
            //Create a new group 
        	ngroup.setGroupName(group_name);
        	mydb.insertGroup(ngroup);
        	
        	mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
        	
        	ArrayList<String> group_names = new ArrayList<String>();
        	Groups g = new Groups();
        	
        	for (int i = 0; i < mygroups.size() ; i++) {
        		
        		g = mygroups.get(i);
        		group_names.add((String)g.getGroupName());
        		
            }
        	listAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,group_names);
        	
        	lv.setAdapter(listAdapter);
        	listAdapter.notifyDataSetChanged();

   	}
    
    public void onDialogNegativeClick(DialogFragment dialog) {
    	dialog.dismiss();
        // User touched the dialog's negative button
    }
    
    
}

