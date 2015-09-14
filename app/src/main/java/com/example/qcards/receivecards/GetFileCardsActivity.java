package com.example.qcards.receivecards;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.qcards.R;
import com.example.qcards.dialogs.SortByDialog;



//public class GetFileCardsActivity  extends FragmentActivity implements RepeateGroupDialogListener{
public class GetFileCardsActivity  extends FragmentActivity{
	
	private SharedPreferences settings;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_file);
        
        String PREFS_NAME = "MyPrefsFile";
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
 
        ImportFileFragment firstFragment = new ImportFileFragment();
        
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_container_import, firstFragment, "IMPORT_FILE_FRAGMENT").commit();

        
    }
    /*public void onDRGPositiveClick(DialogFragment dialog, int sel)
	{
    	UtilsGroups.selection = sel;
    	UtilsGroups.getGroup = true;
	}
	
	
	public void onDRGNegativeClick(DialogFragment dialog)
	{
		
		UtilsGroups.getGroup = false;
	}
	*/
	@Override
	  protected void onResume() {
	      super.onResume();

	      // If the app is run for first time
	      
	      if (settings.getBoolean("firstRun", true)) {
	          // Do first run stuff here then set 'firstRun' as false.
	    	  
	    	  SharedPreferences.Editor editor = settings.edit();

	    	  // Set order cards by date as false 
	          editor.putBoolean("orderCardsByDate", false);
	    	  // Set order groups by date as false
	          editor.putBoolean("orderGroupsByDate", false);
	          
	          editor.putBoolean("firstRun", false);

	          // Commit the edits
	          editor.commit();
	      }
	      else
	      {   
	    	  // Set attributes from preferences 
	    	  SortByDialog.orderCardsByDate = settings.getBoolean("orderCardsByDate", false);
	    	  SortByDialog.orderGroupsByDate = settings.getBoolean("orderGroupsByDate", false);
	      }
	  	}
}