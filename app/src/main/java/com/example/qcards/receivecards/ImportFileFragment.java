package com.example.qcards.receivecards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.qcards.R;
import com.example.qcards.UtilsJson;
import com.example.qcards.UtilsPics;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.ShowDialogs;
import com.example.qcards.groups.UtilsGroups;

public class ImportFileFragment extends Fragment{
	Context mContext;
	FragmentActivity fa; 
	LinearLayout ll;
	Uri data;
	
	//private File mParentPath;
	//private String mParentPath;
	
	public DatabaseHandler mydb;
	
	private UtilsPics im = new UtilsPics();
	//private Contact contact;
	
	//public static Uri data;


public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        fa = super.getActivity();
     	ll = (LinearLayout) inflater.inflate(R.layout.tab1_list, container, false);
     	mContext = getActivity().getApplicationContext();
     	
     	String mParentPath = null;
     	
     	
        // Database
        mydb = new DatabaseHandler(fa);
 
        
     // Get the intent that started this activity
	    Intent intent = fa.getIntent();
	    
        data = intent.getData();
        //String p = data.getPath();
        //String scheme = data.getScheme();

        // Get the Intent action
        String action = intent.getAction();
        UtilsPics im = new UtilsPics();
        
        /*
         * For ACTION_VIEW, the Activity is being asked to display data.
         * Get the URI.
         */
        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            // Get the URI from the Intent
            //Uri beamUri = intent.getData();
            /*
             * Test for the type of URI, by getting its scheme value
             */
            if (TextUtils.equals(data.getScheme(), "file")) {
                mParentPath = im.handleFileUri(data);
            } else if (TextUtils.equals(
            		data.getScheme(), "content")) {
                mParentPath = im.handleContentUri(data, mContext);
            }
        }
        
        
    	ImportCards(mParentPath);
            
     	return ll;
	}

public void ImportCards(String filePath)
{
	int group_id;	
   	int contactId;
   	int[] contacts_ids;
   	Contact contact;
   	InputStream inputStream = null;
	//UtilsPics im = new UtilsPics();
	// Read JSON file from internal storage, it includes cards content.
	
	//Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    
	//i.setType("*/*");
	//startActivityForResult(i, 2);
	//UtilsGroups i2db = new UtilsGroups(fa);
   	if (filePath == null){
   		try 
        {
   			ContentResolver cr = mContext.getContentResolver();
   			inputStream = cr.openInputStream(data);
        }
        catch (FileNotFoundException e) 
        {
                Log.e("TAG", "File not found: " + e.toString());
        }
    }
	String str = im.readFileFromInternalStorage(filePath,mContext,inputStream);
	boolean shareGroup = UtilsJson.checkShareGroupsOrCards(str);
	
	LinkedHashMap<String, List<Contact>> listGroupsImport = new LinkedHashMap<String, List<Contact>>();
	List<Contact> listCardsImport = new ArrayList<Contact>();
	ShowDialogs mshowDialogs = new ShowDialogs(fa);
	
	if (shareGroup){
		
		listGroupsImport = UtilsJson.JSonToGroups(str);
		UtilsGroups.insertGroupsToDB(listGroupsImport, mydb, fa);
		
		/*for (Entry<String, List<Contact>> entry : listGroupsImport.entrySet()) {
	   		
	        String group_name = entry.getKey();
	     // If the group name is empty create a name -> New_grupo	
    		//if (group_name.isEmpty())
    			//group_name = getString(R.string.text_new_group);
		        Groups ngroup = new Groups();
		    	ngroup.setGroupName(group_name);
		    	
		    	group_id = (int)mydb.insertGroup(ngroup);
		    	
		    	listCardsImport = entry.getValue();
		    	
		    	contacts_ids = new int [listCardsImport.size()];
		    	for (int nc = 0; nc < listCardsImport.size(); nc++)
		    	{
		    		
		    		contact = listCardsImport.get(nc);
		    		contactId = (int)mydb.insertContact(contact);
		    		contacts_ids[nc] = contactId; 
	    		
		    	}
		    	
		    	//insert contacts_ids (need to be selected and store in contacts_ids) *****
		    	mydb.insertCardsToGroup(group_id, contacts_ids);
		}*/
	    	
	     	// Re-write all the groups and contacts *************
	     	//updateList = true;
	     	//UtilsLiVim.prepareListCardsGroups(mydb,updateList);
	   	
	}
	else
	{
		
		listCardsImport = UtilsJson.JSonToContacts(str);
		
    	/*for (int nc = 0; nc < listCardsImport.size(); nc++)
    	{
    		contact = listCardsImport.get(nc);
    		mydb.insertContact(contact);
    	}*/
		boolean startAct = true;
		mshowDialogs.ShowDialogGroupImportCards(listCardsImport,startAct);	
		
		

    	// Re-write all the groups and contacts *************
     	//updateList = true;
     	//UtilsLiVim.prepareListCards(mydb,updateList,SortByDialog.orderCardsByDate);
	}
	
   
}
public ParcelFileDescriptor openFile(Uri uri, String mode)
        throws FileNotFoundException {
    File f = new File(mContext.getFilesDir(), uri.getPath());
    
    //InputStream inputStream = mContext.openFileInput(f.getAbsolutePath());
    if (f.exists()) {
        return (ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY));
    }

    throw new FileNotFoundException(uri.getPath());
}

}
