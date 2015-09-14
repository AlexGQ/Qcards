
package com.example.qcards.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.DatabaseHandler;


public class DeleteCardGroupDialog extends DialogFragment{
	private FragmentActivity fa;
	
	private Context mContext;
	
	public DatabaseHandler mydb;
	private static int[] contactIdToDelete;
	private static int[] groupIdToDelete;
	 //private EditText mEditText;
	 //private static String []mdtitle;
	 //private GroupNameDialogListener mListener;

	 //private String [] mdtitle = {"",""};

	 public DeleteCardGroupDialog() {
	     // Empty constructor required for DialogFragment
	 }
	 
	 public static DeleteCardGroupDialog newInstance(String []textDialog, int []ContactsId, boolean deleteGroup) {
		 DeleteCardGroupDialog fragment = new DeleteCardGroupDialog();

	     Bundle args = new Bundle();
	     contactIdToDelete = ContactsId;
	     //args.putSerializable(ARG_LISTENER_TYPE, type);
	     //args.putString("textDialog", textDialog);
	     //args.putString("message", dialogText[1] );
	     args.putStringArray("textDialog", textDialog);
	     //args.putStringArray("textDialog", textDialog);
	     args.putBoolean("deleteGroup", deleteGroup);
	     //args.putIntArray("contactIdToDelete",ContactsId);
	     fragment.setArguments(args);

	     return fragment;
	 }

	 

	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 	fa = getActivity();
	    	mContext = fa.getApplicationContext();
	    	
		    String []mdtitle = new String[2];
		    final boolean deleteGroup;
		    
		    //final int []contactIdToDelete = getArguments().getIntArray("contactIdToDelete");
		    
		 	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 	
		 	mydb = new DatabaseHandler(fa);
		    
		    mdtitle = getArguments().getStringArray("textDialog");
		    deleteGroup = getArguments().getBoolean("deleteGroup");
		    //mdtitle[1] = getArguments().getString("message");
		    builder.setTitle(mdtitle[0]);
		    
		    builder.setMessage(mdtitle[1])
      		 .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
      		 {
      			 public void onClick(DialogInterface dialog, int id) 
      			 {
      				boolean updateList = true; 
      				UtilsListView UtilsLiVim = new UtilsListView();
      				if (!deleteGroup)
      				{
      					for (int i = 0; i< contactIdToDelete.length; i++)
      						//for (int i : contactIdToDelete)
      					{
      						mydb.deleteContact(contactIdToDelete[i]);
      					}
      				
      					// UtilsLiVim.prepareListCards(mydb);
      					
      					UtilsLiVim.prepareListCardsGroups(mydb,updateList);
      				 
      				 Toast.makeText(mContext, R.string.delete_success, Toast.LENGTH_SHORT).show();
      				} 
      				else {
      					 groupIdToDelete = contactIdToDelete;
					     //mydb.deleteGroupWithContacts(groupIdToDelete);
      					 mydb.deleteGroups(groupIdToDelete);
      					//mydb.deleteGroup(contactIdToDelete[0]);
					     
						 UtilsLiVim.prepareListGroups(mydb,updateList,SortByDialog.orderGroupsByDate);
						 
	       				 Toast.makeText(mContext, R.string.delete_success, Toast.LENGTH_SHORT).show();
      					
      				}
      			 }
      		})
      	    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
      	    {
      	    	public void onClick(DialogInterface dialog, int id) {
      	    		// User cancelled the dialog
      	    	}
      	    });
		    
		    return builder.create();
	 }

}

