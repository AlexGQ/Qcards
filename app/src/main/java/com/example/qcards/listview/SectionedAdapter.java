/*Copyright (c) 2012, James Smith
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of the nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL James Smith BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.example.qcards.listview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.qcards.R;
import com.example.qcards.UtilsPics;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.customlistviewvolley.AppController;
//import com.example.qcards.quickactionbar.QuickAction;
import com.example.qcards.quickactionbar.QuickAction;
import com.example.qcards.sharecards.ShareCard;

public class SectionedAdapter extends SectionedBaseAdapter {
	
	private boolean isActionMode;
	private Contact contact;	
	private Context mContext;
	private SparseBooleanArray mSelectedItemsIds;
	public ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	public View mconvertView;
	public QuickAction mQuickAction;
	private static ArrayList<Integer> mNLetters;
	private static ArrayList<Integer> mAccNLetters;
    
    public List<Contact> mcontactList, mycontactList;
    private static boolean morderByDate; 
    
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    
    public SectionedAdapter(Context c, List<Contact> contactList, QuickAction QAction, ArrayList<Integer> NLetters, ArrayList<Integer> AccNLetters,boolean orderByDate) {
    	mContext = c;
    	mcontactList = contactList;
    	mNLetters = NLetters;
    	mAccNLetters = AccNLetters;
    	morderByDate = orderByDate;
    	mQuickAction = QAction;
    	mSelectedItemsIds = new SparseBooleanArray();
    	this.isActionMode = false;

    }

    @Override
    public Object getItem(int section, int position) {
    	return mcontactList.get(position);
        // TODO Auto-generated method stub
        //return null;
    }

    @Override
    public long getItemId(int section, int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSectionCount() {
    	return mNLetters.size();
    }

    @Override
    public int getCountForSection(int section) {
    	return mNLetters.get(section);
    }
    
    
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
         
    	public ListView lv;
        public TextView card_type;
        public TextView connect_p;
        public TextView duration;
        public ImageView im_expand;
        public ImageView im_share;
        public ImageView list_image;
  
    }
    
    public void setActionMode(boolean isActionMode)
    {
        this.isActionMode = isActionMode;
    }


    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
    	long imageID = 1;
        //ViewHolder holder;
        final int posl;
        contact = new Contact();
        UtilsPics im = new UtilsPics();
    	
    	
        RelativeLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) inflator.inflate(R.layout.tab1_list_row, null);
            
            /****** View Holder Object to contain tabitem.xml file elements ******/
            
            //holder = new ViewHolder(); 
            //holder.card_type = (TextView)layout.findViewById(R.id.textItem);
            /*holder.card_type = (TextView)layout.findViewById(R.id.card_type);
            holder.connect_p = (TextView)layout.findViewById(R.id.connect_p);
            holder.duration = (TextView)layout.findViewById(R.id.duration);
            holder.im_expand = (ImageView)layout.findViewById(R.id.im_expand);
            holder.list_image = (ImageView)layout.findViewById(R.id.list_image);*/
            
            /************  Set holder with LayoutInflater ************/
            //convertView.setTag( holder );
            
        } else {
            layout = (RelativeLayout) convertView;
        	//layout = (LinearLayout) convertView;
            //holder=(ViewHolder)convertView.getTag();
        }
        
        posl = position + mAccNLetters.get(section);
        contact = mcontactList.get(position + mAccNLetters.get(section));
 	    imageID = contact.getImageID();
	    
	    Bitmap bm = null;
    	bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[(int)imageID-1],  70, 70);
    	String tc = "Business card";
    	String conn = "Connections: ";
    	String dat = contact.getDateTimeC();
    	String date = dat.substring(8, 10) + "/" +	dat.substring(5, 7) + "/" + dat.substring(0, 4); 
    	
	    
	    //((TextView) layout.findViewById(R.id.card_type)).setText("Section " + section + " Item " + (CharSequence)contact.getName());
	    //((TextView) layout.findViewById(R.id.connect_p)).setText("Section " + section + " Item " + (CharSequence)contact.getDateTimeC());
	    ((TextView) layout.findViewById(R.id.person_name)).setText((CharSequence)contact.getName());
	    ((TextView) layout.findViewById(R.id.connect_p)).setText((CharSequence)conn);
	    ((TextView) layout.findViewById(R.id.duration)).setText((CharSequence)date);
	    
	    ((TextView) layout.findViewById(R.id.card_type)).setText((CharSequence)tc);
	    
	    
	    //((ImageView) layout.findViewById(R.id.list_image)).setImageBitmap(bm);
	    ((ImageView) layout.findViewById(R.id.thumbnail)).setImageBitmap(bm);
	    
	    //NetworkImageView thumbNail = (NetworkImageView) layout.findViewById(R.id.thumbnail);
	    //((NetworkImageView) layout.findViewById(R.id.thumbnail)).setImageBitmap(bm);
	    
	    //thumbNail.setImageBitmap(bm);
	    
	    ImageView ie = ((ImageView)layout.findViewById(R.id.im_expand));
	    ie.setOnClickListener(new View.OnClickListener() {
   		 @Override
			public void onClick(View v) {
   			    int [] poslist = {0,-1};
   			    poslist[0] = posl;
				mQuickAction.show(v, poslist);
				mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
			}
         });
	    
	    ImageView ish = ((ImageView)layout.findViewById(R.id.im_share));
	    ish.setOnClickListener(new View.OnClickListener() {
   		 @Override
			public void onClick(View v) {
   			List<Contact> contactListToShare = new ArrayList<Contact>(); 
   			LinkedHashMap<Integer, Integer> cardsByGroups = new LinkedHashMap<Integer, Integer>();
        	int indexGroup = 0;
			contactListToShare.add(contact);
			ShareCard ShCard = new ShareCard(mContext);
			cardsByGroups = ShCard.ShareCards(contactListToShare,indexGroup);
			Intent shareIntent = ShCard.SetupShare(cardsByGroups);

			mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.text_share_card_to)));

   			
			}
         });
	    
		   
   	    // Set the color of the item 
	    layout.setBackgroundResource(R.drawable.gradient_bg);
    	  
    	  // Changed color of selected items (multiple selection)
    	  if (mSelectedItemsIds.size() > 0){
    		// Find the ids of all selected items with a loop and change their color 
	     	  for (int i = (mSelectedItemsIds.size() - 1); i >= 0; i--) {
	     		if (mSelectedItemsIds.keyAt(i) == posl)
					layout.setBackgroundColor(Color.parseColor("#FF9912"));
	     	  }
   	   }
       
        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
    	
    	//String DateC = "XXXX-XX-XX";
    	Contact contact = new Contact();
    	int index = mAccNLetters.get(section);
    	contact = mcontactList.get(index);
    	
    	String NameC = contact.getName();
    	//System.arraycopy(contact.getDateTimeC(), 0, DateC, 0, 10);
    	//String DateC = contact.getDateTimeC().substring(0,10);
    	
    	String dat = contact.getDateTimeC();
    	String DateC = dat.substring(8, 10) + "/" +	dat.substring(5, 7) + "/" + dat.substring(0, 4);
	    
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        
        if (section == 0)
        	//((TextView) layout.findViewById(R.id.textItem)).setText("My cards " + section + 
        		//Integer.toString(mNLetters.get(section)) + Character.toUpperCase(NameC.charAt(0)) + Integer.toString(mAccNLetters.get(section)));
        	((TextView) layout.findViewById(R.id.textItem)).setText("My cards");
        else
        	//((TextView) layout.findViewById(R.id.textItem)).setText("Header for section " + section + 
            	//	Integer.toString(mNLetters.get(section)) + Character.toUpperCase(NameC.charAt(0)) + Integer.toString(mAccNLetters.get(section)));
        	if(!morderByDate)
        		((TextView) layout.findViewById(R.id.textItem)).setText(""+Character.toUpperCase(NameC.charAt(0)));
        	else
        		((TextView) layout.findViewById(R.id.textItem)).setText(DateC);
        

        return layout;
    }
    
    //@Override
    public void remove(Contact object) {
    		mcontactList.remove(object);
    		notifyDataSetChanged();
    }
    
    //public void removesort(List<Contact> contactList, ArrayList<Integer> NLetters, ArrayList<Integer> AccNLetters) {
    public void UpdateCardsList(List<Contact> contactList, ArrayList<Integer> NLetters, ArrayList<Integer> AccNLetters, boolean orderByDate) {
    	mcontactList = contactList;
		mNLetters = NLetters;
    	mAccNLetters = AccNLetters;
    	morderByDate = orderByDate;
		notifyDataSetChanged();
	}
    	public void toggleSelection(int position) {
    		selectView(position, !mSelectedItemsIds.get(position));
	    }
	 
	    public void removeSelection() {
	        mSelectedItemsIds = new SparseBooleanArray();
	        notifyDataSetChanged();
	    }
	    
	    public void selectView(int position, boolean value) {
	       	if (value)
	            mSelectedItemsIds.put(position, value);
	        else
	            mSelectedItemsIds.delete(position);
	        notifyDataSetChanged();
	    }
	 
	    public int getSelectedCount() {
	        return mSelectedItemsIds.size();
	    }
	 
	    public SparseBooleanArray getSelectedIds() {
	        return mSelectedItemsIds;
	    }

}
