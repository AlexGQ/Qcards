package com.example.qcards.tabpanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.DeleteCardGroupDialog;
import com.example.qcards.dialogs.DeleteGroupsDialog;
import com.example.qcards.dialogs.EditNameDialog;
import com.example.qcards.dialogs.EditNameDialog.EditNameDialogListener;
import com.example.qcards.dialogs.ShowDialogs;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.groups.ActivityGroupCards;
import com.example.qcards.groups.Groups;
import com.example.qcards.listview.ExpandableListAdapter;
import com.example.qcards.quickactionbar.ActionItem;
import com.example.qcards.quickactionbar.QActionOnCardClick;
import com.example.qcards.quickactionbar.QActionOnGroupClick;
import com.example.qcards.quickactionbar.QuickAction;
 
public class Tab2Activity extends Fragment implements EditNameDialogListener{
	
	private static final int RESULT_OK = -1;
	private static final int RESULT_CANCELED = 0;
	private static final int FILE_EXPLORER_RC = 2;
	
	public static boolean tab2Visible = false;
	  
	public static SparseBooleanArray toggleCollExp = new SparseBooleanArray();
	
	private static final int CURRENT_TAB = 1;
	
	private OnHeadCardSelectedListener mCallback;
	
   // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadCardSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        public void onCardSelected(int CId, int posiCard);
    }


    //private boolean new_group = false;
    private Groups group = new Groups();
    
    //private List<Groups> mygroups = new ArrayList<Groups>();
    
    private boolean actionModeEnabled = false;
    
    //private boolean updateList;
    //private UtilsListView UtilsLiVim = new UtilsListView();
    
    //private Contact contact;
    
	private LinearLayout ll;
    private FragmentActivity fa;
    private Context mContext;
    
    private ExpandableListView expListView;
    
    public static ExpandableListAdapter listAdapter;
    public static List<String> listDataHeader;
    public static LinkedHashMap<String, List<Contact>> listDataChild;
    
    private static boolean firstItemChecked = false;
    
    //ShowDialogs mshowDialogs;

	
    public DatabaseHandler mydb;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {

    	boolean updateList;
    	UtilsListView UtilsLiVim = new UtilsListView();
    	
    	setHasOptionsMenu(true);
    	
        fa = super.getActivity();
     	ll = (LinearLayout) inflater.inflate(R.layout.tab2_list, container, false);
     	
     	//mshowDialogs = new ShowDialogs(fa); 

     	mContext = getActivity().getApplicationContext();
     	
        // Database
        mydb = new DatabaseHandler(fa);
        
        /**
         *  Quick action bar - share, edit, remove. 
         **/
        
        ActionItem shareGroupsAction = new ActionItem();
		
        shareGroupsAction.setTitle(getString(R.string.text_share));
        shareGroupsAction.setIcon(getResources().getDrawable(R.drawable.ic_action_share));
		
		//Edit action item
		ActionItem editGroupNameAction = new ActionItem();
		
		editGroupNameAction.setTitle(getString(R.string.text_edit));
		editGroupNameAction.setIcon(getResources().getDrawable(R.drawable.ic_action_edit));
		
		// Group action item
		ActionItem addCardsAction = new ActionItem();
				
		addCardsAction.setTitle(getString(R.string.text_add));
		addCardsAction.setIcon(getResources().getDrawable(R.drawable.ic_action_group));


		//Delete action item
		ActionItem deleteGroupAction = new ActionItem();
		
		deleteGroupAction.setTitle(getString(R.string.text_delete));
		deleteGroupAction.setIcon(getResources().getDrawable(R.drawable.ic_action_discard));
		
		
		final QuickAction mQuickAction 	= new QuickAction(fa);
		
		mQuickAction.addActionItem(shareGroupsAction);
		mQuickAction.addActionItem(editGroupNameAction);
		mQuickAction.addActionItem(addCardsAction);
		mQuickAction.addActionItem(deleteGroupAction);
		
		//setup the action item click listener
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			
			@Override
			public void onItemClick(int pos) {
				
				
				int groupPosition = mQuickAction.poslistview[0];
				Contact contact;
				
				int childPosition = mQuickAction.poslistview[1];
				
				// If a group was selected
				if (childPosition == -1)
				{
					group = mydb.getGrupoByName(listDataHeader.get(groupPosition));
					QActionOnGroupClick mQGroupClick = new QActionOnGroupClick();
					mQGroupClick.OnGroupClick (pos,group, mydb, fa);
				}
				else
				{
					contact = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
					QActionOnCardClick mQCardClick = new QActionOnCardClick();
					mQCardClick.OnCardClick (pos, CURRENT_TAB, contact, mydb, fa);
				}
			}
		});
		
		
		expListView = (ExpandableListView) ll.findViewById(R.id.lvExp);
		
		// Prepare list with groups 
        updateList = false;
        UtilsLiVim.prepareListGroups(mydb,updateList,SortByDialog.orderGroupsByDate);
        
        listAdapter = new ExpandableListAdapter(fa, listDataHeader, listDataChild, mQuickAction);
 
        // Setting list adapter
        expListView.setAdapter(listAdapter);
        
        UtilsLiVim.setGroupIndicatorToRight(expListView,fa);
        
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
        @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
        			
        			if (actionModeEnabled) 
        				expListView.setItemChecked(groupPosition, true);
        			
        	 return actionModeEnabled;
            }
        });
 
        // Listview Group expand listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            	toggleCollExp.put(groupPosition, true);
            	
                /*Toast.makeText(mContext,
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            	//mCurrentViewIsExpandableListView = true;
            }
        });
 
        // Listview Group collapse listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
            	toggleCollExp.delete(groupPosition);
                /*Toast.makeText(mContext,
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/
            	//mCurrentViewIsExpandableListView = false;
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                		mContext,
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                
                Contact Cr = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                
                mCallback.onCardSelected(Cr.getID(),0);

                return false;
            }
        });
        
       
       /**
        * Configure Contextual Action Bar (CAB) to select multiple items
        * */

        expListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
       	
       	// 	Capture ListView item click
        expListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

           @Override
           public void onItemCheckedStateChanged(ActionMode mode,
                   int position, long id, boolean checked) {
        	   
        	   long pos;
        	   int groupPosition;
        	   int tpos;
    		   pos = expListView.getExpandableListPosition(position);
    		   groupPosition = ExpandableListView.getPackedPositionGroup(pos);
    		   tpos = position;
    		   if (((groupPosition != position) || ((groupPosition == 0) && (position == 0))) && (firstItemChecked))
    		   {
    			   firstItemChecked = false;
    			   tpos = groupPosition;
    		   }
    		   if (!(ExpandableListView.getPackedPositionType(pos) ==
    		   		ExpandableListView.PACKED_POSITION_TYPE_CHILD) || ((groupPosition != position) && (checked))) {
    			   listAdapter.toggleSelection(tpos);
    		   }
        	   
           	   SparseBooleanArray selected = listAdapter.getSelectedIds();
           	   mode.setTitle(selected.size() +" "+ getString(R.string.groups_selected));
           }
           
       	// 	Capture menu item click

           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
           	
           	int k = 0;
           	String[] textDialog = {"",""};
           	boolean deleteGroup;
           	List<Groups> mygroups = new ArrayList<Groups>();
           	
           	SparseBooleanArray selected = listAdapter.getSelectedIds();
           	
           	int []SelectedId = new int [selected.size()];
           	
               // Captures all selected ids with a loop
        	mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);

               for (int i = (selected.size() - 1); i >= 0; i--) 
               {
                   if (selected.valueAt(i)) {
                   	Groups groupup = new Groups();
                   	groupup = mygroups.get(selected.keyAt(i));
                   	SelectedId[k++] = groupup.getId();
                   }
               }
               
               // Copy cards Ids selected in CId 
               int []GId = new int [k];
               System.arraycopy( SelectedId, 0, GId, 0, k);

           	// Action from the menu which will be executed with the selected cards
               
               switch (item.getItemId()) {
               
               case R.id.action_delete:
               	// Delete selected groups 
               	
	               	deleteGroup = true;
	               	textDialog[0] = getString(R.string.delete_groups);
	               	textDialog[1] = getString(R.string.are_you_sure_delete_group);
               	
					// Call the dialog to delete the card selected
					DeleteCardGroupDialog dialogd = DeleteCardGroupDialog.newInstance(textDialog, GId, deleteGroup);
		            dialogd.show(fa.getSupportFragmentManager(), "dialogDeleteGroups");
                   
                   mode.finish();
                   return true;
                   
               case R.id.action_group:
               	// Group cards selected
               	
                   // Close CAB
                   mode.finish();
                return true;

                   
               default:
                   return false;
               }
           }

           @Override
           public boolean onCreateActionMode(ActionMode mode, Menu menu) {
               mode.getMenuInflater().inflate(R.menu.cab_menu, menu);
               actionModeEnabled = true;
               firstItemChecked = true;
               mode.setTitle("Select group(s)");
               return true;
           }

           @Override
           public void onDestroyActionMode(ActionMode mode) {
               // TODO Auto-generated method stub
               listAdapter.removeSelection();
        	   actionModeEnabled = false;
        	   firstItemChecked = false;
           }

           @Override
           public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               // TODO Auto-generated method stub
               return false;
           }
       });

        return ll;
    }
 
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_location_found).setVisible(false);
        menu.findItem(R.id.action_delete_groups).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_delete_groups).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
   	   // Take appropriate action for each action item click
    	boolean sortByGroup = true;
    	//boolean new_group = true;
    	String [] Dgname = {"",""};
		//EditNameDialog dialogFragment;
	
		Dgname[0] = getString(R.string.create_new_group);

    	ShowDialogs mshowDialogs;
     	mshowDialogs = new ShowDialogs(fa);
		
    	switch (item.getItemId()) {
        case R.id.action_search:
            // search action
            return true;
        case R.id.action_new:
        	mshowDialogs.showEditDialog(Dgname);
            return true;
        case R.id.action_share:
        	mshowDialogs.ShowDialogShareGroups();
        	return true;
        case R.id.action_import_cards:
        	ImportCards();
            return true;    
        case R.id.action_delete_groups:
			// Text to be included in the dialog
			String[] textDialog = {getString(R.string.delete_groups), getString(R.string.select_all_groups)};
			
			// Call the dialog to group the card selected
			DeleteGroupsDialog dialog = DeleteGroupsDialog.newInstance(textDialog);
            dialog.show(getActivity().getSupportFragmentManager(), "fragmentDialogDeleteGroups");
        	
            return true;
        case R.id.action_sortby:
        	mshowDialogs.ShowDialogSortBy(sortByGroup);
            return true;
    
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    

 /*   @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(mContext, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }*/
    /**
     * Action after pressing import cards.
     * It launches the file explorer where the user will get a file.
    * */

	public void ImportCards()
	{
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("*/*");
		getParentFragment().startActivityForResult(i, FILE_EXPLORER_RC);
	}	

    
    @Override
    public void onDialogPositiveClick(DialogFragment dialog,String inputText, boolean new_group) {
    	String ngroupText = inputText;
    	boolean updateList;
    	UtilsListView UtilsLiVim = new UtilsListView();
    	// If create a new group  
    	if (new_group)
    	{
    		//new_group = false;
    		//Toast.makeText(mContext, "Group " + inputText + " created", Toast.LENGTH_SHORT).show();
    		
    		ActivityGroupCards newFragment = new ActivityGroupCards();
    		
	        Bundle args = new Bundle();
	        args.putString("ngroupText", ngroupText);
	        newFragment.setArguments(args);

	        //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
	        FragmentTransaction transaction = fa.getSupportFragmentManager().beginTransaction();
	
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack so the user can navigate back
	        transaction.add(R.id.fragment_container, newFragment);
	        //transaction.replace(R.id.fragment_container, newFragment);
	        //transaction.addToBackStack();
	        transaction.addToBackStack(null);
	        
	        // Commit the transaction
	        transaction.commit();

    	}
    	else
    	{
        	// If the group name is empty create a name -> New_grupo	
    		if (inputText.isEmpty())
    			inputText = getString(R.string.text_new_group);
    		
        	// If the group name exists create a new name -> grupo_name(x)
    		/*int indx = 2;
    		String group_name = inputText;
    		while(mydb.ifGroupNameExist(group_name))
    		{
    			group_name = inputText + " (" + indx + ")";
    			indx++;
    		}*/

    		Groups editGroup = new Groups();
    		int[] GId = {0};
   		
    		String new_group_name = inputText; 
    		String prev_group_name = group.getGroupName();
        	// If the group name exists then combine groups
    		if(mydb.ifGroupNameExist(new_group_name))
    		{
    			// Get the contact list of the group to be renamed
        		List<Contact>contactListToMove = listDataChild.get(group.getGroupName());
        		int[]contactIdToMove = new int[contactListToMove.size()];
        		
        		// Get the Ids of the group to be combined
        		for (int i = 0; i < contactListToMove.size();i++)
        		{
        			Contact c = contactListToMove.get(i);
        			contactIdToMove[i] = c.getID();
        		}
        		// Get the group that has the same name
    			editGroup = mydb.getGrupoByName(new_group_name);
    			// Insert the contacts to combined both groups
    			mydb.insertCardsToGroup(editGroup.getId(), contactIdToMove);
    			
    			Toast.makeText(mContext, "Group " + prev_group_name + " combined with " + new_group_name , Toast.LENGTH_SHORT).show();
    			// Remove the group that was "renamed"
    			GId[0] = group.getId();
    			
    			mydb.deleteGroups(GId);
    			
    		}else{
    			group.setGroupName(new_group_name);
    			mydb.updateGroup(group);
        		// Renamed group
        		Toast.makeText(mContext, "Group " + prev_group_name +" renamed as " +  new_group_name, Toast.LENGTH_SHORT).show();

    		}
    		
    		
    		// Re-write all the groups and contacts *************
    		updateList = true;
    		UtilsLiVim.prepareListGroups(mydb,updateList,SortByDialog.orderGroupsByDate);
    	}
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    	//dialog.dismiss();
        // User touched the dialog's negative button
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadCardSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCardlineSelectedListener");
        }
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
        	//Toast.makeText(getActivity(), "Time becoming visible", Toast.LENGTH_LONG).show();
        	tab2Visible = true;
            // If we are becoming invisible, then...
            if (!isVisibleToUser){
            	tab2Visible = false;
            }  
            	
            	//Toast.makeText(getActivity(), "Time becoming invisible", Toast.LENGTH_LONG).show();
            //else
            	//Toast.makeText(getActivity(), "Time becoming visible", Toast.LENGTH_LONG).show();
                //Log.d("MyFragment", "2 Not visible anymore.  Stopping audio.");
                // TODO stop audio playback
            
        }
    }

}