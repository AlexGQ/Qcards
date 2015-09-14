package com.example.qcards.sharecards;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.example.qcards.R;
import com.example.qcards.UtilsJson;
import com.example.qcards.UtilsPics;
import com.example.qcards.contactsqlite.Contact;

public class ShareCard {
	
	Context mContext;
	
    // constructor
    public ShareCard(Context context) {
		mContext = context;
    }
        
    public Intent SetupShare(LinkedHashMap<Integer, Integer> cardsByGroups){
   	
    	Uri orgUri;
    	UtilsPics im = new UtilsPics();
    	ArrayList<Uri> fileUris = new ArrayList<Uri>();
    	
    	Intent shareIntent = new Intent();
    	shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
    	
    	shareIntent.setType("*/*");
    	//shareIntent.setType("application/json");
    	
    	int nc;
    	int ng = 0;
    	String s1;
    	String s2;
    	//list(Integer)
    	for (Entry<Integer, Integer> entry : cardsByGroups.entrySet()) 
        {
            // Get the group index
    		ng = entry.getKey();
			nc = cardsByGroups.get(ng);
            s1 = new Integer(ng).toString(); 
                
	    	for (int i = 0; i < (nc + 1); i++)
	    	{
	    		
	    		s2 = new Integer(i).toString();
	    		String transferFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UtilsPics.APP_FOLDER + "/" + UtilsPics.IMFILENAME + s1 +s2 + ".jpg";
	    	
	    		File requestFile = new File(transferFile);
	    		requestFile.setReadable(true, false);
	    		orgUri = Uri.fromFile(requestFile);
	    		fileUris.add(orgUri);
	    	}
	    	
        }
    	
		String transferFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UtilsPics.APP_FOLDER + "/" + UtilsPics.JSONFILENAME + ".json";
	
		File requestFile = new File(transferFile);
		requestFile.setReadable(true, false);
		orgUri = Uri.fromFile(requestFile);
		fileUris.add(orgUri);
    	
    	shareIntent.putExtra(Intent.EXTRA_SUBJECT,"My business card");
    	shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
    	shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
    	
    	return shareIntent;
	}
	
    /**
	 * 
	 * */
	public LinkedHashMap<Integer, Integer> ShareGroups(LinkedHashMap<String, List<Contact>> groupListToShare) {
		
		
    	
    	int indexGroup = 0;
    	UtilsPics im = new UtilsPics();
    	List<Contact> contactListToShare = new ArrayList<Contact>();
    	
		File FolderDir = im.FindDir(mContext);
		LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
		LinkedHashMap<Integer, Integer> cbgTemp = new LinkedHashMap<Integer, Integer>();
		
		for (Entry<String, List<Contact>> entry : groupListToShare.entrySet()) 
        {
                String group_name = entry.getKey();
                // Add the group name
                contactListToShare = groupListToShare.get(group_name);
                cbgTemp = ShareCards(contactListToShare, indexGroup);
                cardsByGroups.put(indexGroup, cbgTemp.get(indexGroup));
                indexGroup++;
        } 
		
		String groupsData = UtilsJson.GroupsToJSon(groupListToShare);
		im.saveFileToInternalStorage(mContext,groupsData,FolderDir ,UtilsPics.JSONFILENAME);
		
		return cardsByGroups;
	}
	
    /**
	 * 
	 * */
	public LinkedHashMap<Integer, Integer> ShareCards(List<Contact> contactListToShare, int indexGroup) {
		
		String subix1;
    	String subix2;
    	LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
    	
    	int indexCards;
    	int imageID;
    	Bitmap bm;
    	UtilsPics im = new UtilsPics();
    	Contact contact = new Contact();
    	
    	
		File FolderDir = im.FindDir(mContext);
		subix1 = new Integer(indexGroup).toString();    	
            	
        for (indexCards = 0; indexCards < contactListToShare.size(); indexCards++)
        {
        	contact = contactListToShare.get(indexCards);
        	imageID = contact.getImageID();
        	
        	subix2 = new Integer(indexCards).toString();
        	
        	        			
			bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[imageID-1],  220, 220);
			// Copy file to memory 
			
			String filename = UtilsPics.IMFILENAME + subix1 + subix2;
			im.saveImageToInternalStorage(mContext,bm,FolderDir, filename);
        }
        cardsByGroups.put(indexGroup,indexCards-1);

		
		String cardData = UtilsJson.ContactsToJSon(contactListToShare);
		// Create JSON file and Copy it to memory
		im.saveFileToInternalStorage(mContext,cardData,FolderDir ,UtilsPics.JSONFILENAME);

		return cardsByGroups;
	}
	
    public Intent InviteFriends(){
       	
    	Intent shareIntent = new Intent();
    	shareIntent.setAction(Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(Intent.EXTRA_SUBJECT,"My business card");
    	shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
    	
    	return shareIntent;
	}

	
}
