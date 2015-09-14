package com.example.qcards.tabpanel;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.qcards.R;
import com.example.qcards.UtilsJson;
import com.example.qcards.UtilsListView;
import com.example.qcards.UtilsPics;
import com.example.qcards.common.view.SlidingTabLayout;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.GroupImportCardDialog;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.groups.UtilsGroups;
import com.example.qcards.signin.SignInActivity;
import com.example.qcards.signin.UserDB;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TabsFragment extends Fragment{
	
	private static final int RESULT_OK = -1;
	private static final int RESULT_CANCELED = 0;
	
	private static final int FILE_EXPLORER_RC = 2;
	public DatabaseHandler mydb;

	private LinearLayout ll;
	
    private FragmentActivity fa;

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;
    
    

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    
	public static ViewPager mViewPager;
	
	static final String LOG_TAG = "SlidingTabsBasicFragment";
	
	private Context mContext;
	
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
          Bundle savedInstanceState) {
	  
	    super.onCreate(savedInstanceState);
	    fa = super.getActivity();
	    
     	mContext = getActivity().getApplicationContext();

    	  // DB connection (write mode)
     	  UserDB DBconnection = new UserDB(fa, "PB_COM_USERS", null, 1);

	      // Is new user (activity -> create new account)
	      
	      SQLiteDatabase db = DBconnection.getReadableDatabase();
	      mydb = new DatabaseHandler(fa);
	      if(db != null)
	      {
		        //Cursor c = db.rawQuery(" SELECT EMAIL FROM PB_COM_USERS WHERE ID_USER = 0", null);
			    //Cursor c = db.rawQuery(" SELECT EMAIL FROM PB_COM_USERS WHERE ID_USER = 0", null);
			    int nContacts = mydb.numberOfRows();
		        //if(c.getCount() > 0)
			     if (nContacts > 0)
		        {
			   /*     c.moveToFirst();
			        String email = c.getString(0);*/
		        	//if (!email.isEmpty()) {
		        		
		        	ll = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
			        /*} else {
			            startActivity(new Intent(fa, SignInActivity.class));
			        }*/
		        } else {
		        	startActivity(new Intent(fa, SignInActivity.class));
		        }
		        db.close();
		       
	      } else {
	      	startActivity(new Intent(fa, SignInActivity.class));
	      }
	     // ll = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
	
	  	   return ll;
  }
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      
      mViewPager = (ViewPager) view.findViewById(R.id.pager);
      mViewPager.setAdapter(new TabsPagerAdapter(getChildFragmentManager(),mContext));
      
      mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
      mSlidingTabLayout.setViewPager(mViewPager);
  }
  
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
     	boolean updateList;
     	
       	UtilsListView UtilsLiVim = new UtilsListView();
       	UtilsPics im = new UtilsPics();
       	InputStream inputStream = null;
       	
       	// Database
        mydb = new DatabaseHandler(fa);

		if (requestCode == FILE_EXPLORER_RC) {
            if(resultCode == RESULT_OK){
            	
            	Uri u = data.getData();	
            	String path = u.getPath();
                    
                // Test for the type of URI, by getting its scheme value
                 
                if (TextUtils.equals(data.getScheme(), "file")) {
                	path = im.handleFileUri(u);
                } else if (TextUtils.equals(
                		data.getScheme(), "content")) {
                	path = im.handleContentUri(u,mContext);
                }
               	if (path == null){
               		try 
                    {
               			ContentResolver cr = mContext.getContentResolver();
               			inputStream = cr.openInputStream(u);
                    }
                    catch (FileNotFoundException e) 
                    {
                            Log.e("TAG", "File not found: " + e.toString());
                        }
                    }
                	String str = im.readFileFromInternalStorage(path,mContext,inputStream);
 
				boolean shareGroup = UtilsJson.checkShareGroupsOrCards(str);
				
				LinkedHashMap<String, List<Contact>> listGroupsImport = new LinkedHashMap<String, List<Contact>>();
				List<Contact> listCardsImport = new ArrayList<Contact>();
				
				if (shareGroup){
					
					listGroupsImport = UtilsJson.JSonToGroups(str);
					//i2db.insertGroupsToDB (listGroupsImport, mydb);
					UtilsGroups.insertGroupsToDB(listGroupsImport, mydb, fa);
			        	
			         // Re-write all the groups and contacts *************
			         updateList = true;
			         UtilsLiVim.prepareListCardsGroups(mydb,updateList);
				}
				else
				{
					listCardsImport = UtilsJson.JSonToContacts(str);
					
					// Text to be included in the dialog
					String[] textDialog = {mContext.getString(R.string.add_to_group), mContext.getString(R.string.create_new_group)};
					
					// Call the dialog to group the card selected
					boolean startAct = false;
					GroupImportCardDialog dialog = GroupImportCardDialog.newInstance(textDialog, listCardsImport,startAct);
				    dialog.show(fa.getSupportFragmentManager(), "fragmentDialogGroup");
				    
				    updateList = true;
		         	UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);

				}
	
	            }
	            if (resultCode == RESULT_CANCELED) {
	                //Write your code if there's no result
	            }
		  }   
	}

  	public void onDRGPositiveClick(DialogFragment dialog, int sel)
	{
		UtilsGroups.selection = sel;
		UtilsGroups.getGroup = true;
	}
	
	public void onDRGNegativeClick(DialogFragment dialog)
	{
		
		UtilsGroups.getGroup = false;
	}
  
}
