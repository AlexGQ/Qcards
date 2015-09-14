
package com.example.qcards.groups;


import java.util.ArrayList;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.dialogs.GenericDialog.GenericDialogListener;
import com.example.qcards.listview.SectionedAdapter;
import com.example.qcards.quickactionbar.QuickAction;
import com.example.qcards.tabpanel.Tab1Activity;
 

//public class ActivityGroupCards extends FragmentActivity implements GenericDialogListener{
public class ActivityGroupCards extends Fragment implements GenericDialogListener {
	
	public DatabaseHandler mydb;
    Contact contact;
    
	private static final int RESULT_OK = -1;
	private static final int RESULT_CANCELED = 0;
	
    private UtilsListView UtilsLiVim = new UtilsListView();

    //List<Contact> contactList, mycontactList, nomycontactList;
    private List<Contact> contactList;
    
    PinnedHeaderListView list;
    SectionedAdapter listviewadapter;
    
    int imageID, ContactId;
    
    ActionMode activeMode;
    
	private ArrayList<Integer> NLetters;
	private ArrayList<Integer> AccNLetters;

    private LinearLayout ll;
    private FragmentActivity fa;
    private String ngroupText;
    private ViewGroup mcontainer;
 
    
    Context mContext;
 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	
    	//setHasOptionsMenu(true);
    	
        fa = super.getActivity();
     	ll = (LinearLayout) inflater.inflate(R.layout.tab1_list, container, false);
     	mcontainer = container;

     	mContext = getActivity().getApplicationContext();

     	ngroupText = getArguments().getString("ngroupText");
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1_list);*/
       
    //public onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	//setHasOptionsMenu(true);
        
        /*  fa = super.getActivity();
       	    ll = (LinearLayout) inflater.inflate(R.layout.tab1_list, container, false);
        */  
     	//mContext = getApplicationContext();
     	//mContext = getActivity().getApplicationContext();
          
        // Database
        mydb = new DatabaseHandler(mContext);
        
        /*contactList = new ArrayList<Contact>();
        nomycontactList = new ArrayList<Contact>();
        mycontactList = new ArrayList<Contact>();
        
       	mycontactList = mydb.getMyCards(1);
       	nomycontactList = mydb.sortbyAlphaWithoutMyCards(1);
        
       	AccNLetters = mydb.AccNLet;
       	NLetters = mydb.NLet;
         
       	contactList = new ArrayList<Contact>(mycontactList);
       	contactList.addAll(nomycontactList);*/
        
        
        //boolean updateList = false;
        //UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
        
        AccNLetters= Tab1Activity.AccNLetters;
        NLetters = Tab1Activity.NLetters;
        contactList = Tab1Activity.contactList;
    
        list = (PinnedHeaderListView) ll.findViewById(R.id.pinnedListView);

        final QuickAction mQuickAction 	= new QuickAction(mContext);
        
        listviewadapter = new SectionedAdapter(mContext,contactList,mQuickAction,NLetters,AccNLetters,SortByDialog.orderCardsByDate);
        list.setAdapter(listviewadapter);
            
            // Configure Contextual Action Bar (CAB) to select multiple items
                
    	list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    		
    	activeMode = fa.startActionMode(mContentSelectionActionModeCallback);
    		
        list.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                	
        	if (activeMode == null) {
        		activeMode = fa.startActionMode(mContentSelectionActionModeCallback);
        	}
                	
                	int posl;
                	int section;
                    // Capture total checked items
                   
                   section = listviewadapter.getSectionForPosition(position);
                   posl = position - section - 1;

                   //activeMode.setTitle("pos" + position + "sec" + section + " Selected");
                    
                   boolean isec = listviewadapter.isSectionHeader(position);
                    
                   // Calls toggleSelection method from ListViewAdapter Class
                   if (!isec)
                     	listviewadapter.toggleSelection(posl);
                   
                   SparseBooleanArray selected = listviewadapter.getSelectedIds();
               	
                   activeMode.setTitle(selected.size() +" "+ getString(R.string.cards_selected));
                	
                }
             });
        	return ll;

    }
    

	private ActionMode.Callback mContentSelectionActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        	//long ContactId;
            int[] contacts_ids;
            boolean updateList;
            
			switch (item.getItemId()) {
            case R.id.action_group:
            	
                SparseBooleanArray selected = listviewadapter.getSelectedIds();
                contacts_ids = new int[selected.size()];
            	
                // Captures all selected ids with a loop
                for (int i = (selected.size() - 1); i >= 0; i--) 
                {
                    if (selected.valueAt(i)) {
                        
                    	contact = contactList.get(selected.keyAt(i));
                    	
                		contacts_ids[i] = contact.getID();
                		
                    }
                }
                /*Intent returnIntent = new Intent();
                returnIntent.putExtra("contacts_ids",contacts_ids);
                fa.setResult(RESULT_OK,returnIntent);
                fa.finish();*/
                
                //-------
                
                      
               	int group_id;
                    	
                Groups newGroup = new Groups();
                
            	//Check that is not empty 
            	if (ngroupText.isEmpty())
            		ngroupText = getString(R.string.text_new_group);
                
            	// If the group name exists create a new name -> grupo_name(x)
        		int indx = 2;
        		String group_name = ngroupText; 
        		while(mydb.ifGroupNameExist(group_name))
        		{
        			group_name = ngroupText + " (" + indx + ")";
        			indx++;
        		}
            	/*String group_name = ngroupText; 
            	// If the group name exists then combine groups
        		if(mydb.ifGroupNameExist(group_name))
        		{
        			newGroup = mydb.getGrupoByName(group_name);
        			group_id = newGroup.getId();
        		}else{*/
        			//Create group 
        		newGroup.setGroupName(group_name);
            	group_id = (int)mydb.insertGroup(newGroup);
        		//}
        			
            	
            	Toast.makeText(mContext, "Group " + group_name + " created", Toast.LENGTH_SHORT).show();
            	
            	//insert contacts_ids (need to be selected and store in contacts_ids) *****
             	mydb.insertCardsToGroup(group_id, contacts_ids);
             	
             	// Re-write all the groups and contacts *************
             	updateList = true;
             	UtilsLiVim.prepareListCardsGroups(mydb,updateList);
             	

                //-------
                
                // Close CAB
                mode.finish();
                
                
                // Create an instance of ExampleFragment
                //TabsFragment firstFragment = new TabsFragment();
                //Fragment fragment = getFragmentManager().findFragmentByTag("TABS_FRAGMENTS");

                // Add the fragment to the 'fragment_container' FrameLayout
                //getChildFragmentManager().beginTransaction()
                  //    .replace(R.id.fragment_container, firstFragment).commit();
                //getFragmentManager().popBackStackImmediate();
                
                //getSupportFragmentManager().beginTransaction()
                //.add(R.id.fragment_container, firstFragment, "TABS_FRAGMENTS").commit();

                
                return true;
            default:
                return false;
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.cab_menu, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        	getFragmentManager().popBackStackImmediate(); //Take a decision on ok button. 
        	//activeMode = null;
        	//showGenericDialog();
        	//mode.finish();
           	
        	
        	
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        	menu.findItem(R.id.action_delete).setVisible(false);
            // TODO Auto-generated method stub
            return false;
        }
        
    };
    
    /*private void showGenericDialog() {
    	
   	 String []Dgname = {"",""};
   	   GenericDialog dialogFragment;
   	
   		//Dgname[0] = "Create a new group";
   		
   		Dgname[0] = getString(R.string.are_you_sure);
   		Dgname[1] = getString(R.string.dont_group_cards);
   		dialogFragment = GenericDialog.newInstance(ActivityGroupCards.this,Dgname);
   	    //FragmentManager fm = getFragmentManager();
   		//dialogFragment.show(getFragmentManager(), "dontGroupDialog");
   		

   		//dialogFragment.show(getFragmentManager(), "dontGroupDialog");
   		//dialogFragment.show(getFragmentManager(), "dontGroupDialog");
   		//dialogFragment.show(this.getSupportFragmentManager(), "dontGroupDialog");
   		dialogFragment.show(getFragmentManager(), "dontGroupDialog");
   		//dialogFragment.show(this, "dontGroupDialog");
   		//dialogFragment.show(getFragmentManager(), "dontGroupDialog");
   		
   		//DialogFragment newFragment = new GenericDialog();
   	    //newFragment.show(getFragmentManager(), "dialog");
   		
   }*/
        	
        	//@Override
            public void onDialogPositiveClick(DialogFragment dialog ) {
            	getFragmentManager().popBackStackImmediate();
            	//getParentFragment();

            }

            //@Override
            public void onDialogNegativeClick(DialogFragment dialog) {
            	//getFragmentManager().popBackStackImmediate();
                // User touched the dialog's negative button
            }
            
   
}
    






