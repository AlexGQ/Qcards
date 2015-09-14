
package com.example.qcards.dialogs;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.groups.Groups;



public class DeleteGroupsDialog extends DialogFragment{
	private FragmentActivity fa;
	public DatabaseHandler mydb;
	//private int[] groupIdToDelete;
	//private static String titleA;
	//private static int []contactIdToGroup;
	//private static List<Integer>ContactIdToGroup = new List<Integer>();
	String GroupText;
	
	private ArrayAdapter<String> groupsAdapter;
	private ArrayAdapter<String> selectAllGroupsAdapter;
	
	private boolean checkFirstItem = false;
	private AlertDialog.Builder builder;
	
	
	private Context mContext;
	private ListView lv;
	
    private List<Groups> mygroups = new ArrayList<Groups>();
	
	
    public DeleteGroupsDialog() {
     }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	fa = getActivity();
    	mContext = fa.getApplicationContext();
    	
        builder = new AlertDialog.Builder(fa);
        
        String []titleD = new String[2];
        String []checkAllTitle = new String[1];
         
        titleD = getArguments().getStringArray("textDialog");
        checkAllTitle[0] = titleD[1];
        //final int []contactIdToGroup = getArguments().getIntArray("ContactIdToGroup");
	    		
    	builder.setTitle(titleD[0]);
    	//builder.setMessage("Cards will not be deleted");
    	
    	mydb = new DatabaseHandler(fa);
    	
    	Groups g = new Groups();
    	
    	mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
    	
    	ArrayList<String> group_names = new ArrayList<String>();
    	
    	for (int i = 0; i < mygroups.size() ; i++) {
    		
    		g = mygroups.get(i);
    		group_names.add((String)g.getGroupName());
    		
        }
    	
    	LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    	
    	View BtnNewGroupView = inflater.inflate(R.layout.dialog_delete_group_list, null);
    	
    	groupsAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,group_names);
       	
       	lv = (ListView) BtnNewGroupView.findViewById(R.id.delete_cgroup);
    	
    	lv.setAdapter(groupsAdapter);
    	
    	lv.setItemsCanFocus(false);
    	lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long arg3) {

            }
        });
	  
	    
	    
	    //Button ngb = (Button) BtnNewGroupView.findViewById(R.id.btn_newg);
	    
	    //CheckBox chkDelAll = (CheckBox) BtnNewGroupView.findViewById(R.id.checkbox_delete_all);
	    //chkDelAll.setText(titleD[1]);
    	final ListView chkDelAll = (ListView) BtnNewGroupView.findViewById(R.id.checkbox_delete_all);
    	
    	selectAllGroupsAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,checkAllTitle);
    	
    	chkDelAll.setAdapter(selectAllGroupsAdapter);
    	
    	chkDelAll.setItemsCanFocus(false);
    	chkDelAll.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	
    	/*AlertDialog alert = builder.create();
		final Button buttonOk = alert.getButton(AlertDialog.BUTTON_POSITIVE);
		buttonOk.setEnabled(false);
    	*/
    	
    	chkDelAll.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                    long arg3) {
            	
            	
            	SparseBooleanArray iselectedAll = chkDelAll.getCheckedItemPositions();
            	
            	//if (!checkFirstItem) {
            	if(iselectedAll.valueAt(0)){
            		//checkFirstItem = true;
    				for(int i = 0; i < mygroups.size(); i++) 
    					lv.setItemChecked(i, true); 
    				//Toast.makeText(MyAndroidAppActivity.this,
    			 	  // "Bro, try Android :)", Toast.LENGTH_LONG).show();
    				
    				  //buttonOk.setEnabled(true);
    			}
    			else
    			{
    				//checkFirstItem = false;
    				for(int i = 0; i < mygroups.size(); i++) 
    					lv.setItemChecked(i, false);
    				
    				//buttonOk.setEnabled(false);
    			}
            	

            }
        });

	    
	    /*chkDelAll.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) {
				
				for(int i = 0; i < mygroups.size(); i++) 
					lv.setItemChecked(i, true); 
				//Toast.makeText(MyAndroidAppActivity.this,
			 	  // "Bro, try Android :)", Toast.LENGTH_LONG).show();
			}
			else
			{
				for(int i = 0; i < mygroups.size(); i++) 
					lv.setItemChecked(i, false);
				
			}
				
	 
		  }
		});
		*/
	    
	    builder.setView(BtnNewGroupView);
	    
	    //float size = ngb.getTextSize();
	    
    	// Set the action buttons
    	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	@Override
    	public void onClick(DialogInterface dialog, int id) {
    		
    		UtilsListView UtilsLiVim = new UtilsListView();
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
	            int []groupIdToDelete = new int [k];
	            System.arraycopy( groupIdsSelected, 0, groupIdToDelete, 0, k);
	
	            mydb.deleteGroups(groupIdToDelete);
	            
	            
	    	      Toast.makeText(mContext,
	    	    		  groupIdToDelete.length + " " + getString(R.string.groups_deleted), Toast.LENGTH_LONG)     
	    	      .show();
	              
	     	      //updateList = true;
	    	      UtilsLiVim.prepareListGroups(mydb,updateList,SortByDialog.orderGroupsByDate);

           	}
    	}})
    	.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    		@Override
    		public void onClick(DialogInterface dialog, int id) {

    		}
    	});
    	
        return builder.create();
    }

    public static DeleteGroupsDialog newInstance(String []textDialog) {
    	DeleteGroupsDialog f = new DeleteGroupsDialog();
    	
	     Bundle args = new Bundle();
	     args.putStringArray("textDialog", textDialog);
	     //args.putIntArray("contactIdToGroup",ContactsId);
	     f.setArguments(args);
    	
	     return f;
     }
}
