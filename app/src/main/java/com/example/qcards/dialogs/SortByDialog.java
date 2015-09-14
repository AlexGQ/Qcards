
package com.example.qcards.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
//import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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
import com.example.qcards.contactsqlite.DatabaseHandler;


public class SortByDialog extends DialogFragment{
	private FragmentActivity fa;
	
	private Context mContext;
	
	public DatabaseHandler mydb;
	
	public static boolean orderCardsByDate;
	public static boolean orderGroupsByDate;
	
	private boolean orderByDate;
	private boolean sortByGroup;
	
	private ListView lv;

	 public SortByDialog() {
	     // Empty constructor required for DialogFragment
	 }
	 
	 public static SortByDialog newInstance(String []textDialog, boolean sortByGroup) {
		 SortByDialog fragment = new SortByDialog();

	     Bundle args = new Bundle();
	     args.putStringArray("textDialog", textDialog);
	     args.putBoolean("sortByGroup", sortByGroup);
	     fragment.setArguments(args);

	     return fragment;
	 }

	 

	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 	fa = getActivity();
	    	mContext = fa.getApplicationContext();
	    	
	    	ArrayAdapter<String> sortByAdapter;
	    
		 	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 	
		 	mydb = new DatabaseHandler(fa);
		    
	    	String []mdtitle = new String[2];
	    	mdtitle = getArguments().getStringArray("textDialog");
	    	
		    sortByGroup = getArguments().getBoolean("sortByGroup");
		    
		    builder.setTitle(mdtitle[0]);
		    
		    LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    	
		    View view = inflater.inflate(R.layout.generic_list, null);
		    	    
		    String []sortByList;
		    // Load sortByList from res
		    
		    sortByList = getResources().getStringArray(R.array.sort_by_opt);
		    sortByAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,sortByList);
       	
		    lv = (ListView) view.findViewById(R.id.gen_list);
    	
		    lv.setAdapter(sortByAdapter);
    	
		    lv.setItemsCanFocus(false);
		    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		    
		    lv.setOnItemClickListener(new OnItemClickListener() {

	            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
	                    long arg3) {
	            	
	            	if (pos == 0){
	            		orderByDate = false;
	            		Toast.makeText(mContext, "Name", Toast.LENGTH_SHORT).show();
	    			}
	    			else
	    			{
	    				orderByDate = true;
	    				Toast.makeText(mContext, "Date", Toast.LENGTH_SHORT).show();
	    			}

	            }
	        });

		    builder.setView(view)
		    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
      		 {
      			 public void onClick(DialogInterface dialog, int id) 
      			 {
      				boolean updateList = true; 
      				UtilsListView UtilsLiVim = new UtilsListView();
      				
      				if (!sortByGroup)
      				{
      					orderCardsByDate = orderByDate;
      					UtilsLiVim.prepareListCards(mydb,updateList,orderCardsByDate);
      				} 
      				else {
      					
      					 orderGroupsByDate = orderByDate; 
      					 UtilsLiVim.prepareListGroups(mydb,updateList,orderGroupsByDate);
      				}
      				prefeChanges();
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
	 
	  @Override
	  public void onResume() {
	    super.onResume();
	    	if (!sortByGroup) 
	    		orderByDate = orderCardsByDate;
	    	else
	    		orderByDate = orderGroupsByDate;
	    		
	    	if (orderByDate)
	    		lv.setItemChecked(1, true);
	    	else
	    		lv.setItemChecked(0, true);
	  }
	  
	  public void prefeChanges(){
		  
			 SharedPreferences settings = fa.getSharedPreferences(MainActivity.PREFS_NAME, 0);
			 SharedPreferences.Editor editor = settings.edit();
			 
			 if (!sortByGroup)
				 editor.putBoolean("orderCardsByDate", orderCardsByDate);
			 else
				 editor.putBoolean("orderGroupsByDate", orderGroupsByDate);

			 editor.commit();

		  
	  }

}

