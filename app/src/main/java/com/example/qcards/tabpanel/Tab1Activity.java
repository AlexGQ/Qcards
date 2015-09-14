package com.example.qcards.tabpanel;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.qcards.R;
import com.example.qcards.UtilsListView;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.DeleteCardGroupDialog;
import com.example.qcards.dialogs.GroupCardDialog;
import com.example.qcards.dialogs.ShowDialogs;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.displayingbitmapscards.ImageDetailActivity;
import com.example.qcards.hviewcards.DisplayCardsLayouts;
import com.example.qcards.listview.SectionedAdapter;
import com.example.qcards.quickactionbar.ActionItem;
import com.example.qcards.quickactionbar.QActionOnCardClick;
import com.example.qcards.quickactionbar.QuickAction;
import com.example.qcards.sharecards.ShareCard;

public class Tab1Activity extends Fragment{
	
	private static final int RESULT_OK = -1;
	private static final int RESULT_CANCELED = 0;
	private static boolean tab1Visible = false;
	
	private static final int FILE_EXPLORER_RC = 2;
	
	//public static int selection;
	//public static boolean doTheSame = false;

	
	private OnHeadCardSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadCardSelectedListener {
        /** Called by MainActivity when a list item is selected */
        public void onCardSelected(int CId, int posiCard);
    }
	
	public DatabaseHandler mydb;
    
    private PinnedHeaderListView list;
    
    public static ArrayList<Integer> NLetters;
	public static ArrayList<Integer> AccNLetters;
	public static SectionedAdapter listviewadapter;
	
	private static final int CURRENT_TAB = 0;
    
    //private Contact contact;
    
    public static List<Contact> contactList;
    public static int posi;
	//private List<Contact> contactListToShare;
    
    private LinearLayout ll;
    private FragmentActivity fa;
    
    private Context mContext;
    
	//private UtilsListView UtilsLiVim = new UtilsListView();
	//private UtilsPics im = new UtilsPics();
	
	private boolean updateList;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        fa = super.getActivity();
     	ll = (LinearLayout) inflater.inflate(R.layout.tab1_list, container, false);
     	mContext = getActivity().getApplicationContext();
     	UtilsListView UtilsLiVim = new UtilsListView();
     	
     	setHasOptionsMenu(true);
     	
        // Database
        mydb = new DatabaseHandler(fa);
        
        // Prepare list cards 
        updateList = false;
        UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
        
        /**
         *  Quick action bar - share, edit, add to group, remove. 
        **/
       
    	// Add action item
        ActionItem shareAction = new ActionItem();
		
        shareAction.setTitle(getString(R.string.text_share));
        shareAction.setIcon(getResources().getDrawable(R.drawable.ic_action_share));
		
		// Edit action item
		ActionItem editAction = new ActionItem();
		
		editAction.setTitle(getString(R.string.text_edit));
		editAction.setIcon(getResources().getDrawable(R.drawable.ic_action_edit));
		
		// Group action item
		ActionItem groupAction = new ActionItem();
				
		groupAction.setTitle(getString(R.string.text_group));
		groupAction.setIcon(getResources().getDrawable(R.drawable.ic_action_group));

		// Delete action item
		ActionItem deleteAction = new ActionItem();
		
		deleteAction.setTitle(getString(R.string.text_delete));
		deleteAction.setIcon(getResources().getDrawable(R.drawable.ic_action_discard));
		
		final QuickAction mQuickAction 	= new QuickAction(fa);
		
		mQuickAction.addActionItem(shareAction);
		mQuickAction.addActionItem(editAction);
		mQuickAction.addActionItem(groupAction);
		mQuickAction.addActionItem(deleteAction);
		
		
		//setup the action item click listener
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(int pos) {
				Contact contact;
				QActionOnCardClick mQCardClick = new QActionOnCardClick();
				// Get card position in the listview
				int position = mQuickAction.poslistview[0];
		
				contact = contactList.get(position);
				mQCardClick.OnCardClick (pos, CURRENT_TAB, contact, mydb, fa);
				
			}
		});
		
		/**
         * Set adapter with contactList (cards)  
         * */

		list = (PinnedHeaderListView) ll.findViewById(R.id.pinnedListView);
        listviewadapter = new SectionedAdapter(fa,contactList, mQuickAction,NLetters,AccNLetters,SortByDialog.orderCardsByDate);
        list.setAdapter(listviewadapter);
 
        
         // On click event for single listview item
        
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	int posl;
            	int section;

            	boolean isec = listviewadapter.isSectionHeader(position);
                section = listviewadapter.getSectionForPosition(position);

                // Index of the card in contactList array
                posl = position - section - 1;
                
                // If the selected item is not a section
                if (!isec)
                {
	                // The user selected a card
	                // capture the Id from the MainActivity
                	//posi = posl;
                	mCallback.onCardSelected(contactList.get(posl).getID(),posl);
                }
            }
         });
        
        /**
         * Configure Contextual Action Bar (CAB) to select multiple items
         * */

        	list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        	
        	// 	Capture ListView item click
        	list.setMultiChoiceModeListener(new MultiChoiceModeListener() {
 
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                    int position, long id, boolean checked) {
            	
            	int posl;
            	int section;

                section = listviewadapter.getSectionForPosition(position);
                posl = position - section - 1;
                
                boolean isec = listviewadapter.isSectionHeader(position);
                
                // Calls toggleSelection method from ListViewAdapter Class                
                if (!isec) listviewadapter.toggleSelection(posl);
                
                SparseBooleanArray selected = listviewadapter.getSelectedIds();

                // Set the CAB title according to total checked items
                mode.setTitle(selected.size() +" "+ getString(R.string.cards_selected));
            }
            
        	// 	Capture menu item click
 
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            	
            	int k = 0;
            	String[] textDialog = {"",""};
            	boolean deleteGroup;
            	List<Contact> contactListToShare;
            	
            	SparseBooleanArray selected = listviewadapter.getSelectedIds();
            	
            	int []SelectedId = new int [selected.size()];
            	
                // Captures all selected ids with a loop
            	
                for (int i = (selected.size() - 1); i >= 0; i--) 
                {
                    if (selected.valueAt(i)) {
                    	Contact contactup = new Contact();
                    	contactup = contactList.get(selected.keyAt(i));
                    	SelectedId[k++] = contactup.getID();
                    }
                }
                
                // Copy cards Ids selected in CId 
                int []CId = new int [k];
                System.arraycopy( SelectedId, 0, CId, 0, k);

            	// Action from the menu which will be executed with the selected cards
                
                switch (item.getItemId()) {
                
                case R.id.action_delete:
                	// Delete selected cards
                	
                	deleteGroup = false;
                	textDialog[0] = getString(R.string.delete_cards);
                	textDialog[1] = getString(R.string.are_you_sure_delete_card);
                	
					// Call the dialog to delete the card selected
					DeleteCardGroupDialog dialogd = DeleteCardGroupDialog.newInstance(textDialog, CId, deleteGroup);
		            dialogd.show(fa.getSupportFragmentManager(), "fragmentDialogDelete");
		            
                    mode.finish();
                    return true;
                    
                case R.id.action_group:
                	// Group selected cards
                	
                	textDialog[0] = getString(R.string.add_to_group);
                	textDialog[1] = getString(R.string.create_new_group);
            		
					GroupCardDialog dialogAddCards = GroupCardDialog.newInstance(textDialog, CId);
					dialogAddCards.show(getActivity().getSupportFragmentManager(), "fragmentDialogGroup");
                	
                    // Close CAB
                    mode.finish();
                 return true;
                 
                case R.id.action_share:
                	
                	LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
                	int indexGroup = 0;
					contactListToShare = mydb.getContactsById(CId);
					ShareCard ShCard = new ShareCard(mContext);
					cardsByGroups = ShCard.ShareCards(contactListToShare,indexGroup);
					Intent shareIntent = ShCard.SetupShare(cardsByGroups);

					startActivity(Intent.createChooser(shareIntent, getString(R.string.text_share_card_to)));

                    mode.finish();
                    return true;


                    
                default:
                    return false;
                }
            }
 
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.cab_menu, menu);
                mode.setTitle("Select card(s)");
                return true;
            }
 
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                listviewadapter.removeSelection();
            }
 
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        return ll;
        	
    }

	
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
	
	// Action after pressing create a new card in button of the menu (action bar).
	public void BtnNewOnClick()
	{
		startActivity(new Intent(fa, DisplayCardsLayouts.class));
	}
	
	// Action after pressing location found in the menu (action bar).
    private void LocationFound() {
        Intent i = new Intent(fa, ImageDetailActivity.class);
        startActivity(i);

        //Intent i = new Intent(fa, LocationFound.class);
        //startActivity(i);
    }
    
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_delete_groups).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	// Take appropriate action for each action item click
    	ShowDialogs mshowDialogs = new ShowDialogs(fa);
    	boolean sortByGroup = false;
		
    	switch (item.getItemId()) {
        case R.id.action_search:
            return true;
        case R.id.action_new:
        	BtnNewOnClick();
           return true;
        /*case R.id.action_sign_in:
        	BtnSignInOnClick();
           return true;*/   
        case R.id.action_location_found:
        	LocationFound();
            return true;
        case R.id.action_import_cards:
        	ImportCards();
            return true;    
        case R.id.action_help:
        	ShowExample();
            return true;
/*        case R.id.action_settings:
        	ShowSettings();
            return true;*/
        case R.id.action_sortby:
        	mshowDialogs.ShowDialogSortBy(sortByGroup);
            return true;    
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    
    public void ShowExample(){
    	
    	//Intent intent= new Intent(mContext, com.example.qcards.cardlayouts.MyAvatarActivity.class);
    	
    	//Intent intent= new Intent(mContext, com.example.qcards.contactsqlite.EditCardAvatar.class);
  	    Intent intent= new Intent(mContext, com.flavienlaurent.notboringactionbar.ShowCardActivity.class);
		startActivity(intent);

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
                    + " must implement OnCardHeadSelectedListener");
        }
    }
    
    
    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        // mDrawerToggle.syncState();
        
        if (getChildFragmentManager().findFragmentById(R.id.cardsdetails) != null) {
        
        	//list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    
/*    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
        	//Toast.makeText(getActivity(), "Time becoming visible", Toast.LENGTH_LONG).show();
        	tab1Visible = true;
            // If we are becoming invisible, then...
            if (!isVisibleToUser){
            	tab1Visible = false;
            }  
            	
            	//Toast.makeText(getActivity(), "Time becoming invisible", Toast.LENGTH_LONG).show();
            //else
            	//Toast.makeText(getActivity(), "Time becoming visible", Toast.LENGTH_LONG).show();
                //Log.d("MyFragment", "2 Not visible anymore.  Stopping audio.");
                // TODO stop audio playback
            
        }
    }*/

    
	/*public void onDRGPositiveClick(DialogFragment dialog, int sel, boolean checkFirstItem)
	{
		selection = sel;
		doTheSame = checkFirstItem;
	}
	
	
	public void onDRGNegativeClick(DialogFragment dialog)
	{
		selection = -1;
		doTheSame = false;
	}*/

    
}
    






