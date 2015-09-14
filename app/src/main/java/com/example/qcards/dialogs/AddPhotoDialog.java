
package com.example.qcards.dialogs;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qcards.MainActivity;
import com.example.qcards.R;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.groups.Groups;
import com.example.qcards.preferences.SettingsFragment;


public class AddPhotoDialog extends DialogFragment {
	//private FragmentActivity fa;
	//private Activity fa;
	public DatabaseHandler mydb;
	private ArrayAdapter<String> listAdapter;
	private FragmentActivity fa;
	
	private static final int CAMERA_REQUEST1 = 1888;
	//private static final int FILE_EXPLORER_RC = 2;
	private static final int FILE_GALLERY = 3;
	
	private static final String PREFS_NAME = "MyPrefsFile";
	private static final int MODE_PRIVATE = 0;
	private static SharedPreferences settings;

	AlertDialog.Builder builder;
	
	private Context mContext;
	private ListView lv;
	
    List<Groups> mygroups = new ArrayList<Groups>();
    
    //private ArrayList<String> mOfficeListItems = new ArrayList<String>();
	
    public AddPhotoDialog() {
     }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	
    	//fa = getActivity();
    	mContext = getActivity().getApplicationContext();
    	
    	settings = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    	
        builder = new AlertDialog.Builder(getActivity());
        
        String titleD = getArguments().getString("textDialog");

        builder.setTitle(titleD);
    	
    	//Collections.addAll(mOfficeListItems, getResources().getStringArray(R.array.load_photo_opt)); 
        View v = getActivity().getLayoutInflater().inflate(R.layout.simple_list, null);

        lv = (ListView)v.findViewById(R.id.slist);

        String []loadPhotoArray;
	    // Load sortByList from res
	    
        loadPhotoArray = getResources().getStringArray(R.array.load_photo_opt);
        //TextView textView=(TextView) v.findViewById(android.R.id.text1);

        /*YOUR CHOICE OF COLOR*/
        //textView.setTextColor(Color.BLUE);
        
        
        //LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //listAdapter = new ArrayAdapter<String>(this,
                //android.R.layout.simple_list_item_1, android.R.id.text1, loadPhotoArray);
        listAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
        		android.R.id.text1,loadPhotoArray){
        			public View getView(int position, View convertView, ViewGroup parent) {
        				View view = super.getView(position, convertView, parent);
        				TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        				text1.setTextColor(Color.BLACK);
        				if (!isEnabled(position) & (position == 2))
        				//if (position == 1)
        				{
        					//view.setBackgroundColor(getResources().getColor(R.color.gray_dialog));
        					text1.setTextColor(Color.WHITE);
        				}
        					//view.setEnabled(isEnabled(position));
        				//view.setSelected(!isEnabled(position));
        				
        				
                     return view;

            };
            public boolean isEnabled(int position) 
            { 
            	boolean ck;
            	if((position == 2) & (!MainActivity.mPPhoto))
            		ck = false;
            	else
            		ck = true;
            		
                return ck; 
            } 
        };;
        
        
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          //      android.R.layout.simple_list_item_1, android.R.id.text1, loadPhotoArray);
      
        
        lv.setAdapter(listAdapter);
        builder.setView(v);
        
        

        // ListView Item Click Listener
        lv.setOnItemClickListener(new OnItemClickListener() {
        	

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
                
               // ListView Clicked item index
               int itemPosition = position;
               
               dismiss();
               
               if (itemPosition == 0)
               {
            	   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
               	   getActivity().startActivityForResult(cameraIntent, CAMERA_REQUEST1);
               }   
               else if (itemPosition == 1)
               {
            	   /*Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            	   i.setType("image/*");
       		   	   getActivity().startActivityForResult(i, FILE_EXPLORER_RC);*/
            	   Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                   photoPickerIntent.setType("image/*");
                   getActivity().startActivityForResult(photoPickerIntent, FILE_GALLERY);
               }
               else
               {
                   SharedPreferences.Editor editor = settings.edit();
                   editor.putBoolean("mProfilePhoto", false);
                   editor.commit();
                   MainActivity.mPPhoto = false;
                   SettingsFragment.custom.updatePPhoto();
               }
               
               // ListView Clicked item value
               /*String  itemValue    = (String) lv.getItemAtPosition(position);
                  
                // Show Alert 
                Toast.makeText(mContext,
                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();*/
             
              }
              
           

         }); 
    
    	
        return builder.create();
    }
    

    public static AddPhotoDialog newInstance(String []textDialog) {
    	AddPhotoDialog f = new AddPhotoDialog();

	    Bundle args = new Bundle();
	    args.putString("textDialog", textDialog[0]);
	    f.setArguments(args);
    	
        return f;
        }
    
    
/*    
    @Override
	  public void onResume() {
	    super.onResume();
	    	if (MainActivity.mPPhoto)
	    		lv.setItemChecked(2, true);
	    	else
	    		lv.setItemChecked(2, false);
	  }
  */  
    
    
}
