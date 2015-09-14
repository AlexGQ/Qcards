package com.example.qcards.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.dialogs.AboutDialog;
import com.example.qcards.dialogs.AddPhotoDialog;


public class SettingsFragment extends PreferenceFragment //implements OnSharedPreferenceChangeListener  
//implements OnSharedPreferenceChangeListener
{
	
	public static final String KEY_PHOTO = "button_profile_photo";
	public static final String KEY_ABOUT = "button_about";
	
	public static final String KEY_EMAIL = "prefs_summ_button_email";
	
	public static final String KEY_LANGUAGE = "list_language";
	
	public static final String KEY_HUPLOAD = "button_how_to_upload";
	
	
	private static final int CAMERA_REQUEST1 = 1888;
	private static final int RESULT_OK = -1;
	
	private FragmentActivity fa;
	private SharedPreferences prefs;
	private Context mContext;
	public static CustomPreference custom;
	
	//private Preference customPref;
	private static ListPreference language_preference;
	private static ListPreference hupload_preference;
	
	private String[] lanAlias, hupAlias; 

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        
        prefs = getPreferenceManager().getSharedPreferences();
        //fa = super.getActivity();

        // Load the preferences from an XML resource
        
        addPreferencesFromResource(R.xml.preferences);
        
        
        //fa = super.getActivity();
        //LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.only_avatar, container);
        
        //((ImageView) findViewById(R.id.avatar)).setImageResource(someResId);
        
    	//SharedPreferences prefs = 
    		//    PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

    	//preferenceListener=new OnSharedPreferenceChangeListener;
        //getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    	//prefs=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    	
    	// Get the custom preference
    	   //customPref = (Preference) findPreference(KEY_PHOTO);
            custom = new CustomPreference(mContext, null);
        	//CustomPreference custom = new CustomPreference(mContext);
        	//CustomPreference custom = (CustomPreference) findPreference(KEY_PHOTO);
            custom = (CustomPreference) findPreference(KEY_PHOTO);
            
            //prefs.registerOnSharedPreferenceChangeListener(this);
        	//custom.setUserAvatar();
    	   
            Preference aboutPreference = (Preference) findPreference(KEY_ABOUT);
            
            
            
            aboutPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
       	    public boolean onPreferenceClick(Preference preference) {
       	    	
       	    		//String [] Dgname = {"",""};
       	    		//Dgname[0] = getString(R.string.set_pphoto);
            	
       	    		//AboutDialog aboutFragment = AboutDialog.newInstance(Dgname);
       	    		AboutDialog aboutFragment = new AboutDialog();

       	    		aboutFragment.show(getFragmentManager(), null);
        	    	//AboutDialog aboutFragment = new AboutDialog();
                    // Show Alert DialogFragment
                
        	    	return true;
        	    }
            }); 
    	      
    	   
    	   //View v = (View) customPref.getView(null, null);
    	   //ImageView v = (ImageView) customPref.getLayoutResource(R.id.avatar);
    	   
    	   
    	   //customPref.getWidgetLayoutResource();
    	   
    	   /*
    	   
    	   UtilsPics im = new UtilsPics();
           
           //LayoutInflater inflater =  (LayoutInflater)mContext.
             //      getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
           //View view = inflater.inflate(R.layout.only_avatar, container,false);
           //setContentView(R.layout.only_avatar);
    	   //int l = customPref.getLayoutResource();

           
           ImageView card_avatar = (ImageView) customPref.getView(null,null).findViewById(R.id.avatar);
           
           Bitmap bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[4],  50, 50);
   	    
           RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
           //card_avatar.setImageBitmap(rad.getBitmap());
           
           card_avatar.setBackground(rad);
           //card_avatar.setImageBitmap(bm);
    	       	   
    	   */
        	custom.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    	 
    	    public boolean onPreferenceClick(Preference preference) {
    	    	
    	    	String [] Dgname = {"",""};
          	    Dgname[0] = getString(R.string.set_pphoto);
            	
            	AddPhotoDialog dialogFrag = AddPhotoDialog.newInstance(Dgname);
            	//dialogFrag.show(getSupportFragmentManager(), null);
            	dialogFrag.show(getFragmentManager(), null);
                //custom = (CustomPreference) findPreference(KEY_PHOTO);
    	     return true;
    	    }
    	 
    	   });
        	
        	/*ListPreference language_preference = (ListPreference) findPreference(KEY_LANGUAGE);

        	language_preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
        	    public boolean onPreferenceChanged(Preference preference, Object newValue) {
        	        shareprefs.putString("language_preference", (String) newValue);
        	        makeVoiceData();
        	    }
        	});*/
    	
/*    	
    	OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
    		  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
    		    // Implementation
		        	if (key.equals(KEY_PHOTO))
		            {
		            	String [] Dgname = {"",""};
		            	
		          	    Dgname[0] = getString(R.string.sort_by);

		            	
		            	AddPhotoDialog dialogFrag = AddPhotoDialog.newInstance(Dgname);
		            	//dialogFrag.show(getSupportFragmentManager(), null);
		            	dialogFrag.show(getFragmentManager(), null);
		            	//dialogFrag.show(getSupportFragmentManager(), null);
		            	
		            	
		                // Set summary to be the user-description for the selected value
		                //Preference photoPref = findPreference(key);
		                //photoPref.setSummary(sharedPreferences.getString(key, ""));
		            }

    		  }
    		};

    	prefs.registerOnSharedPreferenceChangeListener(listener);
    	*/
        	Preference email_preference = (Preference) findPreference(KEY_EMAIL);
        	email_preference.setSummary("mauri@appliscience.com");
        	email_preference.setEnabled(false);
        	
        	
        	lanAlias = mContext.getResources().getStringArray(R.array.settings_push_language_human_value);
        	
        	language_preference = (ListPreference) findPreference(KEY_LANGUAGE);
        	
        	String languageData = prefs.getString(KEY_LANGUAGE, "English");
        	//int languageValue = prefs.getInt(KEY_LANGUAGE, 0);
        	language_preference.setSummary(lanAlias[Integer.valueOf((String)languageData)]);
        	//language_preference = getPreference("language_preference");
        	
        	language_preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int chosen_option = Integer.valueOf((String) newValue);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(KEY_LANGUAGE, (String) newValue);
                    //editor.putInt(KEY_LANGUAGE, chosen_option);
                    
                    // Commit the edits
                    editor.commit();

                    setLanguagePreference(chosen_option);
                    return true;
                }
            });
        	
        	
        	hupAlias = mContext.getResources().getStringArray(R.array.settings_push_hupload_human_value);
        	
        	hupload_preference = (ListPreference) findPreference(KEY_HUPLOAD);
        	
        	String how2UploadData = prefs.getString(KEY_HUPLOAD, "Wi-Fi only");
        	//int languageValue = prefs.getInt(KEY_LANGUAGE, 0);
        	hupload_preference.setSummary(hupAlias[Integer.valueOf((String)how2UploadData)]);
        	//language_preference = getPreference("language_preference");
        	
        	hupload_preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int chosen_option = Integer.valueOf((String) newValue);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(KEY_HUPLOAD, (String) newValue);
                    //editor.putInt(KEY_LANGUAGE, chosen_option);
                    
                    // Commit the edits
                    editor.commit();

                    setHuploadPreference(chosen_option);
                    return true;
                }
            });


    	
    }
	//OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
/*    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) 
    {
            if (key.equals(KEY_PHOTO)) {
            	custom = new CustomPreference(mContext, null);
                custom = (CustomPreference) findPreference(KEY_PHOTO);
    	    	String [] Dgname = {"",""};
          	    Dgname[0] = getString(R.string.set_pphoto);
            	
            	AddPhotoDialog dialogFrag = AddPhotoDialog.newInstance(Dgname);
            	dialogFrag.show(getFragmentManager(), null);

            }
            if (key.equals(KEY_LANGUAGE)) { 
            	language_preference = (ListPreference) findPreference(KEY_LANGUAGE);
            	//language_preference = getPreference("language_preference");
            	
            	language_preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        int chosen_option = Integer.valueOf((String) newValue);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(KEY_LANGUAGE, (String) newValue);
                        // Commit the edits
                        editor.commit();

                        setChosenPreference(chosen_option);
                        return false;
                    }
                });
        	    }

          }*/
	  
	//};
	
	 private void setLanguagePreference(int chosen_value){
	        // First put all Visibilities on GONE
	        // Then turn the chosen on VISIBLE again
		 	
	        switch(chosen_value){
	            case 0: // Multi-Click CheckBox
	            default:
	            	language_preference.setSummary(lanAlias[chosen_value]);
	            	Toast.makeText(mContext, "Selected language: " + lanAlias[chosen_value], Toast.LENGTH_SHORT).show();
	                break;
	            case 1: // Dropdown CheckBox
	            	language_preference.setSummary(lanAlias[chosen_value]);
	            	Toast.makeText(mContext, "Selected language: " + lanAlias[chosen_value], Toast.LENGTH_SHORT).show();
	                break;
	            case 2: // Pop-up CheckBox
	            	language_preference.setSummary(lanAlias[chosen_value]);
	            	Toast.makeText(mContext, "Selected language: " + lanAlias[chosen_value], Toast.LENGTH_SHORT).show();
	                break;          
	            case 3: // Pop-up CheckBox
	            	language_preference.setSummary(lanAlias[chosen_value]);
	            	Toast.makeText(mContext, "Selected language: " + lanAlias[chosen_value], Toast.LENGTH_SHORT).show();
	                break;          	                
	        }
	    }
	 
	 private void setHuploadPreference(int chosen_value){
	        // First put all Visibilities on GONE
	        // Then turn the chosen on VISIBLE again
		 	
	        switch(chosen_value){
	            case 0: // Multi-Click CheckBox
	            default:
	            	hupload_preference.setSummary(hupAlias[chosen_value]);
	            	Toast.makeText(mContext, hupAlias[chosen_value], Toast.LENGTH_SHORT).show();
	                break;
	            case 1: // Dropdown CheckBox
	            	hupload_preference.setSummary(hupAlias[chosen_value]);
	            	Toast.makeText(mContext, hupAlias[chosen_value], Toast.LENGTH_SHORT).show();
	                break; 
	        }
	    }

/*    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
    	

   	   
        //IT NEVER GETS IN HERE!
        if (key.equals(KEY_PHOTO))
        {
        	String [] Dgname = {"",""};
        	
      	    Dgname[0] = getString(R.string.sort_by);

        	
        	AddPhotoDialog dialogFrag = AddPhotoDialog.newInstance(Dgname);
        	//dialogFrag.show(getSupportFragmentManager(), null);
        	dialogFrag.show(getFragmentManager(), null);
        	//dialogFrag.show(getSupportFragmentManager(), null);
        	
        	
            // Set summary to be the user-description for the selected value
            //Preference photoPref = findPreference(key);
            //photoPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }
*/    
    
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    			
    			//LayoutInflater li = (LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    			//return li.inflate( R.layout.seekbar_preference, parent, false);
    	

    			//View v = inflater.inflate(R.layout.only_avatar, null);
    			//View v = inflater.inflate(R.layout.only_avatar, container, false);
    			//RelativeLayout mainlayout = (RelativeLayout)v.findViewById(R.id.rl);
    			//sublayout = (LinearLayout)mainlayout.findViewById(R.id.avatar);     
        		//View v = inflater.inflate(R.layout.only_avatar, null);

    	  		customPref = (Preference) findPreference(KEY_PHOTO);
        		View v = (View) customPref.getView(null, container);
        		//View thumb1 = mainlayout.findViewById(R.id.avatar);
         	    //ViewGroup vg = (ViewGroup) thumb1.getParent();
        		
         	   //View v1 = (View) customPref.getView(thumb1, vg);
    			//customPref = (Preference) findPreference(KEY_PHOTO);
    			//View v1 = (View) customPref.getView(null, null);
    			
         	   
        		UtilsPics im = new UtilsPics();
                
                //LayoutInflater inflater =  (LayoutInflater)mContext.
                  //      getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
                //View view = inflater.inflate(R.layout.only_avatar, container,false);
                //setContentView(R.layout.only_avatar);
                
                ImageView card_avatar = (ImageView)v.findViewById(R.id.avatar);
                //TextView t = (TextView)v.findViewById(R.id.title);
                //t.setText("Hi");
                Bitmap bm = im.decodeSampledBitmapFromRes(mContext,im.mThumbIds[4],  50, 50);
        	    
                RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
                //card_avatar.setImageBitmap(rad.getBitmap());
                
                card_avatar.setBackground(rad);
        return v;        		
    }
    
  */  
    @Override
    public void onResume() {
        super.onResume();
        //getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);
        
        //custom = new CustomPreference(mContext, null);
        //custom = (CustomPreference) findPreference(KEY_PHOTO);
        custom.updatePPhoto();

    }

    @Override
    public void onPause() {
        //getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
        //custom = new CustomPreference(mContext, null);
        //custom = (CustomPreference) findPreference(KEY_PHOTO);
    }
 
}