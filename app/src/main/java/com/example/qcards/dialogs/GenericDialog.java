
package com.example.qcards.dialogs;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.qcards.R;


//public class GenericDialog extends DialogFragment implements OnEditorActionListener {
public class GenericDialog extends DialogFragment{
	
	private static final String ARG_LISTENER_TYPE = "listenerType";
	private GenericDialogListener mListener;
	
	private FragmentActivity fa;
	private Context mContext;
	
	private ArrayAdapter<String> listAdapter;
	private ListView lv;

	static enum ListenerType {
        ACTIVITY, FRAGMENT
    }

	
 public interface GenericDialogListener {
     //void onFinishEditDialog(String inputText);
     public void onDialogPositiveClick(DialogFragment dialog);
     public void onDialogNegativeClick(DialogFragment dialog);
 }
 
 public GenericDialog() {
     // Empty constructor required for DialogFragment
 }
  public static GenericDialog newInstance(GenericDialogListener listener, String []textDialog) {
	    //final EditNameDialog instance;
	    GenericDialog instance = new GenericDialog();
	    instance = createInstance(ListenerType.FRAGMENT,textDialog);
	    instance.setTargetFragment((Fragment) listener, 0);
	    
	    return instance;
	}
 
	 private static GenericDialog createInstance(ListenerType type, String []textDialog) {
		 GenericDialog fragment = new GenericDialog();
	
	     Bundle args = new Bundle();
	     args.putSerializable(ARG_LISTENER_TYPE, type);
	     args.putStringArray("textDialog", textDialog);
	     fragment.setArguments(args);
	
	     return fragment;
	 }
	 
 	@Override
 	public Dialog onCreateDialog(Bundle savedInstanceState) {
 		
 		final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
 		fa = getActivity();
    	mContext = fa.getApplicationContext();
 		
 		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
 		
 		String []mdtitle = new String[2];
	    
	    mdtitle = getArguments().getStringArray("textDialog");
	    
	    builder.setTitle(mdtitle[0]);
	    //builder.setMessage(mdtitle[1]);
	    
	    /*LayoutInflater inflater = (LayoutInflater) fa.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    	
    	View view = inflater.inflate(R.layout.generic_list, null);
    	
    	listAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_list_item_multiple_choice,group_names);
       	
       	lv = (ListView) view.findViewById(R.id.gen_list);
    	
    	lv.setAdapter(listAdapter);
    	
    	lv.setItemsCanFocus(false);
    	lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);*/
    	
	 // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	    builder.setMultiChoiceItems(R.array.sort_by_opt, null,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
	                   if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedItems.add(which);
	                   } else if (mSelectedItems.contains(which)) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	    
    	.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity

            	mListener.onDialogPositiveClick(GenericDialog.this);
            }
            
        })
        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                mListener.onDialogNegativeClick(GenericDialog.this);
            }
        });
	    return builder.create();
 	}
	 
	 
 
 
	 @Override
	 public void onAttach(Activity activity) {
	     super.onAttach(activity);
	
	     // Find out how to get the DialogListener instance to send the callback
	     // events to
	     Bundle args = getArguments();
	     ListenerType listenerType = (ListenerType) args.getSerializable(ARG_LISTENER_TYPE);
	
	     switch (listenerType) {
	     case ACTIVITY: {
	         // Send callback events to the hosting activity
	         mListener = (GenericDialogListener) activity;
	         break;
	     }
	     case FRAGMENT: {
	         // Send callback events to the "target" fragment
	         mListener = (GenericDialogListener) getTargetFragment();
	         break;
	     }
	     }
	 }
	
/*	 @Override
	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	     if (EditorInfo.IME_ACTION_DONE == actionId) {
	
	    	 // Find out how to get the DialogListener instance to send the callback
	         // events to
	         Bundle args = getArguments();
	         ListenerType listenerType = (ListenerType) args.getSerializable(ARG_LISTENER_TYPE);
	
	         switch (listenerType) {
	         case ACTIVITY: {
	             // Send callback events to the hosting activity
	             mListener = (GenericDialogListener) getActivity();
	             break;
	         }
	         case FRAGMENT: {
	             // Send callback events to the "target" fragment
	             mListener = (GenericDialogListener) getTargetFragment();
	             GenericDialogListener fragment = (GenericDialogListener) getTargetFragment();
	        	 //fragment.onFinishEditDialog(mEditText.getText().toString());
	             this.dismiss();
	             
	             break;
	         }
	         }
	         
	    	 
	         return true;
	     }
	     return false;
	 }*/
	 @Override
	 public void onDetach()
	 {
	      mListener = null;
	      super.onDetach();
	 }
 
}


