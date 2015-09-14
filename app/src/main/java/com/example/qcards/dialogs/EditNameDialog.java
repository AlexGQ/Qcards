package com.example.qcards.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.qcards.R;

public class EditNameDialog extends DialogFragment implements OnEditorActionListener {
	
	private static final String ARG_LISTENER_TYPE = "listenerType";
	private EditNameDialogListener mListener;

	static enum ListenerType {
        ACTIVITY, FRAGMENT
    }

	
 public interface EditNameDialogListener {
     //void onFinishEditDialog(String inputText);
     public void onDialogPositiveClick(DialogFragment dialog,String inputText, boolean new_group);
     public void onDialogNegativeClick(DialogFragment dialog);
 }
 
 private EditText mEditText;
 private static String []mdtitle;
 private static boolean new_group;

 public EditNameDialog() {
     // Empty constructor required for DialogFragment
 }
  public static EditNameDialog newInstance(EditNameDialogListener listener, String []dTitle) {
	    //final EditNameDialog instance;
	    mdtitle = dTitle;
	    //new_group = ngroup;
	    EditNameDialog instance = new EditNameDialog();
	    instance = createInstance(ListenerType.FRAGMENT);
	    instance.setTargetFragment((Fragment) listener, 0);

	    return instance;
	}
 
	 private static EditNameDialog createInstance(ListenerType type) {
		 EditNameDialog fragment = new EditNameDialog();
	
	     Bundle args = new Bundle();
	     args.putSerializable(ARG_LISTENER_TYPE, type);
	     fragment.setArguments(args);
	
	     return fragment;
	 }
	 
 	@Override
 	public Dialog onCreateDialog(Bundle savedInstanceState) {
 		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    
	    
	    
	    View textEntryView = inflater.inflate(R.layout.fragment_edit_name, null);
	    mEditText = (EditText) textEntryView.findViewById(R.id.txt_your_name);
	    
	    if (!(mdtitle[1].isEmpty()))
	    {
    		// Rename group
	    	mEditText.setText(mdtitle[1]);
	    	mdtitle[0] = getString(R.string.rename_group);
	    	new_group = false;
	    }
	    else
	    {
    		// Create a new group
	    	mdtitle[0] = getString(R.string.create_new_group);
	    	new_group = true;
	    }
	    builder.setTitle(mdtitle[0]);
	    
	    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	    
	    builder.setView(textEntryView)
	    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                InputMethodManager imm = (InputMethodManager)  getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
                dismiss();
            	
            	
            	mListener.onDialogPositiveClick(EditNameDialog.this,mEditText.getText().toString(),new_group);

            }
            
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                InputMethodManager imm = (InputMethodManager)  getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
                dismiss();

                mListener.onDialogNegativeClick(EditNameDialog.this);
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
	         mListener = (EditNameDialogListener) activity;
	         break;
	     }
	     case FRAGMENT: {
	         // Send callback events to the "target" fragment
	         mListener = (EditNameDialogListener) getTargetFragment();
	         break;
	     }
	     }
	 }
	
	 
	 @Override
	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	     if (EditorInfo.IME_ACTION_DONE == actionId) {
	
	    	 // Find out how to get the DialogListener instance to send the callback
	         // events to
	         Bundle args = getArguments();
	         ListenerType listenerType = (ListenerType) args.getSerializable(ARG_LISTENER_TYPE);
	
	         switch (listenerType) {
	         case ACTIVITY: {
	             // Send callback events to the hosting activity
	             mListener = (EditNameDialogListener) getActivity();
	             break;
	         }
	         case FRAGMENT: {
	             // Send callback events to the "target" fragment
	             mListener = (EditNameDialogListener) getTargetFragment();
	             //EditNameDialogListener fragment = (EditNameDialogListener) getTargetFragment();
	        	 //fragment.onFinishEditDialog(mEditText.getText().toString());
	             this.dismiss();
	             
	             break;
	         }
	         }
	         
	    	 
	         return true;
	     }
	     return false;
	 }
	 @Override
	 public void onDetach()
	 {
	      mListener = null;
	      super.onDetach();
	 }
 
}


