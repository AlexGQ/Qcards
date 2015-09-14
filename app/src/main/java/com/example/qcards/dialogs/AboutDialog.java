package com.example.qcards.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.qcards.R;
	 
public class AboutDialog extends DialogFragment {
	
		public AboutDialog()
		{
		}
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	Context mContext = getActivity().getApplicationContext();
	    	final TextView message = new TextView(mContext);
	    	// Linkify the message
	        //final SpannableString s = new SpannableString(msg);
	        //Linkify.addLinks(s, Linkify.ALL);
	        
	        //message.setText(s);
	        //message.setMovementMethod(LinkMovementMethod.getInstance());

		    // Load sortByList from res
		    
	    	//String descriptionString = getResources().getString(R.string.description_about);
	    	//String aboutApp =  getResources().getString(R.string.about_app);
	    	
	    	String version = null;
	    	int versionCode = 0;
			try {
				PackageInfo pInfo;
				pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
				version = pInfo.versionName;
				versionCode = pInfo.versionCode;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			String tTle = getString(R.string.about_app) + " v " + version + "." + Integer.toString(versionCode); 
					  
			
	        AlertDialog d = new AlertDialog.Builder(getActivity())
	                // Set Dialog Icon
	                .setIcon(R.drawable.ic_launcher)
	                // Set Dialog Title
	                .setTitle(tTle)
	                // Set Dialog Message
	                .setMessage(R.string.description_about)
	                
	                
	 
	                // Positive button
	                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        // Do something else
	                    }
	                }).create();
	        
	        d.show();
	        
	     // Make the textview clickable. Must be called after show()
	        ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
	        
	        return d;
	    }
	    
/*	    public static AboutDialog newInstance(String []textDialog) {
	    	AboutDialog f = new AboutDialog();

		    Bundle args = new Bundle();
		    args.putString("textDialog", textDialog[0]);
		    f.setArguments(args);
	    	
	        return f;
	        }*/
		

	}
	


