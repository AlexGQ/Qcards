package com.example.qcards.dialogs;

import java.util.LinkedHashMap;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.example.qcards.R;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.dialogs.EditNameDialog.EditNameDialogListener;
import com.example.qcards.tabpanel.TabsPagerAdapter;


public class ShowDialogs {
	
	FragmentActivity fa;
	
	public ShowDialogs(FragmentActivity mfa) {
		fa = mfa; 
	}
	
	public void showEditDialog(String[] Dgname) {
    	EditNameDialog dialogFragment;
    	
		//Fragment fragmentTab2 = ((TabsPagerAdapter) TabsFragment.mViewPager.getAdapter()).getFragment(TabsPagerAdapter.INDEX_TAB_2);
    	Fragment fragmentTab2 = TabsPagerAdapter.getFragment(TabsPagerAdapter.INDEX_TAB_2);
		dialogFragment = EditNameDialog.newInstance((EditNameDialogListener) fragmentTab2,Dgname);

    	dialogFragment.show(fa.getSupportFragmentManager(), "EditNameGroupDialog");
    }
	
	public void showRepeatedGroupDialog(String[] Dgname, LinkedHashMap<String, List<Contact>> listGroupsImport, boolean startAct, int groups_repeated) {
		RepeatedGroupDialog dialogFragment;
		
		//Fragment fragmentTabs = fa.getSupportFragmentManager().findFragmentByTag("TABS_FRAGMENTS");
		
		dialogFragment = RepeatedGroupDialog.newInstance(Dgname, listGroupsImport, startAct, groups_repeated);

    	dialogFragment.show(fa.getSupportFragmentManager(), "RepeatedGroupDialog");
    }

    
        
    public void ShowDialogSortBy(boolean sortByGroup)
	{
    	String [] Dgname = {"",""};

	   SortByDialog dialogFragment;
   	
   	   Dgname[0] = fa.getString(R.string.sort_by);
   	   Dgname[1] = fa.getString(R.string.dont_group_cards);
   	
   	   dialogFragment = SortByDialog.newInstance(Dgname,sortByGroup);
   	   dialogFragment.show(fa.getSupportFragmentManager(), "SortByDialog");

   }
    
    public void ShowDialogGroupImportCards(List<Contact> listCardsImport,boolean startAct)
	{
	   
		// Text to be included in the dialog
		String[] textDialog = {fa.getString(R.string.add_to_group), fa.getString(R.string.create_new_group)};
		
		// Call the dialog to group the card selected
		GroupImportCardDialog dialog = GroupImportCardDialog.newInstance(textDialog, listCardsImport, startAct);
	    dialog.show(fa.getSupportFragmentManager(), "fragmentDialogImportCards");
	    
   }

    public void ShowDialogShareGroups()
	{
	   
       String [] Dgname = {"",""};
	   ShareGroupsDialog dialogFragment;
   	
   	   Dgname[0] = fa.getString(R.string.text_share_group);
   	
   	   dialogFragment = ShareGroupsDialog.newInstance(Dgname);
   	   dialogFragment.show(fa.getSupportFragmentManager(), "ShareGroupsDialog");

   }


}


