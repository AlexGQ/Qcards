package com.example.qcards.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qcards.R;
import com.example.qcards.UtilsPics;
import com.example.qcards.contactsqlite.Contact;
//import com.example.qcards.listview.CardsAdapterListview.ViewHolder;
import com.example.qcards.quickactionbar.QuickAction;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context _context;
    private static List<String> mlistDataHeader; // header titles
    // child data in format of header title, child title
    private static LinkedHashMap<String, List<Contact>> mlistDataChild;
    private boolean morderByDate;
    //public List<Contact> mcontactList;
    public QuickAction mQuickAction;
    public View mconvertView;
    
    private SparseBooleanArray mSelectedItemsIds;
	public ArrayList<Integer> selectedIds = new ArrayList<Integer>();
 
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
    		LinkedHashMap<String, List<Contact>> listChildData, QuickAction QAction) {
    	mQuickAction = QAction;
        this._context = context;
        mlistDataHeader = listDataHeader;
        mlistDataChild = listChildData;
        mSelectedItemsIds = new SparseBooleanArray();
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mlistDataChild.get(mlistDataHeader.get(groupPosition)).get(childPosition);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
         
    	public ListView lv;
        public TextView card_type;
        public TextView connect_p;
        public TextView duration;
        public ImageView im_expand;
        public ImageView list_image;
  
    }

 
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	
    	ViewHolder holder;
    	int imageID = 1;
        //final int posl;
        
        Contact contact = new Contact();
        UtilsPics im = new UtilsPics();
        
 
        //final String childText = (String) getChild(groupPosition, childPosition);
        
        contact = (Contact) getChild(groupPosition, childPosition);
        
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.tab2_list_row, null);
            
            holder = new ViewHolder();
            holder.card_type = (TextView) convertView.findViewById(R.id.card_type);
            holder.connect_p = (TextView)convertView.findViewById(R.id.connect_p);
            holder.duration = (TextView)convertView.findViewById(R.id.duration);
            holder.im_expand = (ImageView) convertView.findViewById(R.id.im_expand);
            holder.list_image = (ImageView)convertView.findViewById(R.id.list_image);
            
            /************  Set holder with LayoutInflater ************/
            convertView.setTag( holder );
        }
        else
        {
        	
        	holder=(ViewHolder)convertView.getTag();
        }
 
        //TextView txtListChild = (TextView) convertView
                //.findViewById(R.id.lblListItem);
        
     	//contact = mcontactList.get(position);
	    imageID = contact.getImageID();
	    int cid = contact.getID();
    		
       	holder.card_type.setText((CharSequence)contact.getName());
       	holder.connect_p.setText((CharSequence)contact.getDateTimeC());
       	String logi = String.valueOf(cid);
       	holder.duration.setText(logi);
        
       	Bitmap bm = null;
       	bm = im.decodeSampledBitmapFromRes(this._context,im.mThumbIds[(int)imageID-1],  70, 70);
       	
   	    holder.list_image.setImageBitmap(bm);
   	    holder.list_image.setAdjustViewBounds(true);
   	    
   	    //ImageView ie = ((ImageView)holder.findViewById(R.id.im_expand));
   	    holder.im_expand.setOnClickListener(new View.OnClickListener() {
		 @Override
			public void onClick(View v) {
			 	int [] poslist = {0,0}; 
			 	poslist[0] = groupPosition;
			 	poslist[1] = childPosition;
				mQuickAction.show(v, poslist);
				mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
			}
      });

 
        //txtListChild.setText(childText);
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return mlistDataChild.get(mlistDataHeader.get(groupPosition)).size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return mlistDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return mlistDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	
    	
        String headerTitle = (String) getGroup(groupPosition);
        //final int posl = groupPosition;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        
        	
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ImageView group_options = (ImageView) convertView.findViewById(R.id.group_options);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        
        group_options.setOnClickListener(new View.OnClickListener() {
    		@Override
 			public void onClick(View v) {
    			int [] poslist = {0,-1}; 
    			poslist[0] = groupPosition;
				mQuickAction.show(v, poslist);
 				mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
 			}
         });
        
        // Set the color of the item 
        convertView.setBackgroundResource(R.drawable.gradient_bg);
    	  
    	  // Changed color of selected items (multiple selection)
    	  if (mSelectedItemsIds.size() > 0){
    		// Find the ids of all selected items with a loop and change their color 
	     	  for (int i = (mSelectedItemsIds.size() - 1); i >= 0; i--) {
	     		if (mSelectedItemsIds.keyAt(i) == groupPosition)
	     			convertView.setBackgroundColor(Color.parseColor("#FF9912"));
	     	  }
   	   }
 
        return convertView;
    }
    
   	public void AddGroup(String GroupName, List<Contact> listData2Add) {
   		
   			mlistDataHeader.add(GroupName);
   			//this._listDataChild.put(GroupName,listData2Add.get(GroupName));
   			mlistDataChild.put(GroupName,listData2Add);
    		//public void remove() {
    		//mcontactList.remove(object);
    		notifyDataSetChanged();
   	}
   	
   	public void UpdateGroupsNameList( List<String> listDataHeaderE) {
   		mlistDataHeader = listDataHeaderE;
		notifyDataSetChanged();
	}


   	public void UpdateGroupsList( List<String> listDataHeaderE,
   			LinkedHashMap<String, List<Contact>> listChildDataE,boolean orderByDate) {
   		mlistDataHeader = listDataHeaderE;
        mlistDataChild = listChildDataE;
        morderByDate = orderByDate;
		notifyDataSetChanged();
	}

 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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