package com.example.qcards;

//import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qcards.cardlayouts.RoundedAvatarDrawable;
import com.example.qcards.contactsqlite.Contact;
import com.example.qcards.contactsqlite.DatabaseHandler;
import com.example.qcards.dialogs.SortByDialog;
import com.example.qcards.groups.Groups;
import com.example.qcards.hviewcards.DisplayCardsFragment;
import com.example.qcards.sharecards.ShareCard;
import com.example.qcards.signin.SignInActivity;
import com.example.qcards.slidingmenu.NavDrawerItem;
import com.example.qcards.slidingmenu.NavDrawerListAdapter;
import com.example.qcards.tabpanel.Tab1Activity;
import com.example.qcards.tabpanel.Tab2Activity;
import com.example.qcards.tabpanel.TabsFragment;
import com.example.qcards.tabpanel.TabsPagerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends FragmentActivity implements Tab1Activity.OnHeadCardSelectedListener, 
Tab2Activity.OnHeadCardSelectedListener,CreateNdefMessageCallback,OnNdefPushCompleteCallback{
	
	private static boolean mDualPane;
	static private int prevCId;
	
	boolean x1, x2;
	
	private static final int MESSAGE_SENT = 1;
	
	public static final String PREFS_NAME = "MyPrefsFile";
	public static SharedPreferences settings;
	public static boolean init_orderCardsbyDate;
	
	public static boolean mPPhoto;

	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public LinearLayout mDrawerLinearLayout; 
    private ActionBarDrawerToggle mDrawerToggle;
 
    // nav drawer title
    private CharSequence mDrawerTitle;
    private NfcAdapter mNfcAdapter;
 
    // used to store app title
    private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
//    private static int [] CId = {0};
    private Context mContext;
    
    private UtilsPics im = new UtilsPics();
    
    public DatabaseHandler mydb;
    
	
	/** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      
      
      setContentView(R.layout.activity_main);
      
      mContext = getApplicationContext();
      
      // Preferences
      settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
      
      // Database
      mydb = new DatabaseHandler(this);
      
      /**
       * Setup the Drawer with slide menu items on the left.
       */

      mTitle = mDrawerTitle = getTitle();
  	 
      // load slide menu items
      navMenuTitles = getResources().getStringArray(R.array.drawer_array);

      // nav drawer icons from resources
      navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
      
      mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
      mDrawerLinearLayout = (LinearLayout) findViewById(R.id.drawer_view);
      mDrawerList = (ListView) findViewById(R.id.left_drawer);
      //mDrawerList

      navDrawerItems = new ArrayList<NavDrawerItem>();

      // adding nav drawer items to array
      // Home
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
      // Find People
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
      // Photos
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
      // Communities, Will add a counter here
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
      // Pages
      navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
      
       
      // Recycle the typed array
      navMenuIcons.recycle();
      
      mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

      // Setting the nav drawer list adapter
      adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
      mDrawerList.setAdapter(adapter);

      // Enabling action bar app icon and behaving it as toggle button
      getActionBar().setDisplayHomeAsUpEnabled(true);
      getActionBar().setHomeButtonEnabled(true);

      mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
              R.drawable.ic_drawer, //nav menu toggle icon
              R.string.app_name, // nav drawer open - description for accessibility
              R.string.app_name // nav drawer close - description for accessibility
      ){
          public void onDrawerClosed(View view) {
              getActionBar().setTitle(mTitle);
              // calling onPrepareOptionsMenu() to show action bar icons
              invalidateOptionsMenu();
          }

          public void onDrawerOpened(View drawerView) {
         	  Bitmap bm = null;
              
              ImageView card_avatar = (ImageView)findViewById(R.id.avatar);

              TextView person_name = (TextView)findViewById(R.id.person_named);
              person_name.setText("Mauri");
      
              TextView email = (TextView)findViewById(R.id.email);
              email.setText("mauri@appliscience.com");

              
              // calling onPrepareOptionsMenu() to hide action bar icons
              if (mPPhoto)
              {
            	  //String transferFile = "/storage/emulated/legacy/OGQ/BackgroundsHD/Images/04261_BG.jpg";
              	  //orgUri = Uri.fromFile(new File(transferFile));
            	  File FolderDir = im.FindDir(mContext);
                  
                  String filename = UtilsPics.MPPHOTO + ".jpg";
                  //String fn = filename + ".jpg";
                  File requestFile = new File(FolderDir,filename);
        	  
        		  //File requestFile = new File(transferFile);
        	      requestFile.setReadable(true, false);
        	      Uri orgUri = Uri.fromFile(requestFile);
        	      
        	      try {
        	    	  	bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(orgUri));
        			    } catch (FileNotFoundException e) {
        			     e.printStackTrace(); 
        			    }
              }
              else
            	  bm = im.decodeSampledBitmapFromRes(mContext,im.pphoto[0], 55, 55);

              //Bitmap bm = im.decodeSampledBitmapFromRes(getApplicationContext(),im.mThumbIds[4],  50, 50);
        	    
              RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bm);
              //card_avatar.setImageBitmap(rad.getBitmap());
              
              card_avatar.setBackground(rad);

              getActionBar().setTitle(mDrawerTitle);
              invalidateOptionsMenu();
          }
      };
      
      //TextView person_name = (TextView)findViewById(R.id.person_name);
      //person_name.setText("Mauri");
 	  
      mDrawerLayout.setDrawerListener(mDrawerToggle);
      
     
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
		      return;
		      
		}
		// Register callback to set NDEF message
		mNfcAdapter.setNdefPushMessageCallback(this, this);
		// Register callback to listen for message-sent success
		mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

      
   /**
   *  Check whether the activity is using the layout version with
   *  the fragment_container FrameLayout (one panel). If so, we must add the first fragment
   */
      
  prevCId = -1;
  
  if (findViewById(R.id.fragment_container) != null) {
	  // One panel
	  
      // However, if we're being restored from a previous state,
      // then we don't need to do anything and should return or else
      // we could end up with overlapping fragments.
	  
      if (savedInstanceState != null) {
          return;
      }

      // Create an instance of TabsFragment (it includes two fragments that correspond
      // to the two tabs: All cards and groups)
      TabsFragment firstFragment = new TabsFragment();

      // In case this activity was started with special instructions from an Intent,
      // pass the Intent's extras to the fragment as arguments
      firstFragment.setArguments(getIntent().getExtras());

      // Add the fragment to the 'fragment_container' FrameLayout
      getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_container, firstFragment, "TABS_FRAGMENTS").commit();
  }
  else
  {
	  	// Dual panel 
	  
    	// We have a frame to embed two fragments (dual-panel), so we must add the first fragment.
	    	
	    	if (savedInstanceState != null) {
	            return;
	        }
	    	
	    	
	        // Create an instance of TabsFragment
	        TabsFragment firstFragment = new TabsFragment();
	
	        // In case this activity was started with special instructions from an Intent,
	        // pass the Intent's extras to the fragment as arguments
	        firstFragment.setArguments(getIntent().getExtras());
	
	        // Add the first fragment that will be on the left.
	        getSupportFragmentManager().beginTransaction()
	                .add(R.id.headlinescards, firstFragment, "TABS_FRAGMENTS").commit();
	        
	        // We must call onCardSelected in order to set the second fragment that 
	        // will show the card details. So, the first time (prevCId = -1) we show the
	        // card that is on the top of the database.  	   
	        
	        if (prevCId == -1)
	        {
	        	List<Contact> mContacts = new ArrayList<Contact>();
	    		mContacts = mydb.getAllContacts();
	    		if (mContacts.size() != 0)
	    			onCardSelected(mContacts.get(0).getID(),0);
	        }
    }
  }
  
  /**
  *  This method is called every time the user select a card. It adds the second 
  *  fragment (dual-panel) or replaces the fragment container (one-panel) 
  *  with the card details fragment (DisplayCardActivity).
  */

  
  public void onCardSelected(int ContactId, int posiCard)
  {
	 
    // Check if the activity is using the dual-panel.  
	View detailsFrame = this.findViewById(R.id.cardsdetails);
    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
    
	if (prevCId != ContactId) {
			
		// Create fragment and give it an argument for the selected card
	    // Replace whatever is in the fragment with DisplayCardFragment
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		DisplayCardsFragment newFragment = new DisplayCardsFragment();

		
        Bundle args = new Bundle();
        args.putInt("ContactId", ContactId);
        args.putInt("posiCard", posiCard);
        newFragment.setArguments(args);
        
        
        
	  	if (mDualPane)
	  	{
	            transaction.replace(R.id.cardsdetails, newFragment, "DISPLAY_CARD_FRAGMENT");
	  	}
		else {
	
		        // Replace whatever is in the fragment_container view with this fragment,
		        // and add the transaction to the back stack so the user can navigate back
		        transaction.replace(R.id.fragment_container, newFragment, "DISPLAY_CARD_FRAGMENT");
		        transaction.addToBackStack(null);
	    }
	
	    // Commit the transaction
	    transaction.commit();
	}
	prevCId = ContactId;

  }
  
  @Override
  protected void onResume() {
      super.onResume();

      // If the app is run for first time
      
      if (settings.getBoolean("firstRun", true)) {
          // Do first run stuff here then set 'firstRun' as false.
    	  
    	  SharedPreferences.Editor editor = settings.edit();
    	  editor.putBoolean("mProfilePhoto", false);
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
    	  // Get attributes from preferences 
    	  SortByDialog.orderCardsByDate = settings.getBoolean("orderCardsByDate", false);
    	  SortByDialog.orderGroupsByDate = settings.getBoolean("orderGroupsByDate", false);
    	  mPPhoto = settings.getBoolean("mProfilePhoto",false);
      }
  }
  
  /**
   * Slide menu item click listener
   * */
  
  private class SlideMenuClickListener implements
          ListView.OnItemClickListener {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
              long id) {
          // Display view for selected nav drawer item
      	selectItem(position);
      }
  }
  
  /**
   * Displaying fragment view for selected nav drawer list item
   * */
  
  private void selectItem(int position) {
      // update the main content by replacing fragments
      /*Fragment fragment = new PlanetFragment();
      Bundle args = new Bundle();
      
      args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
      fragment.setArguments(args);
      
      FragmentManager fragmentManager = getSupportFragmentManager();
      
      fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
      */

	    switch (position) {
	        case 0: // Sign in  
        		startActivity(new Intent(mContext, SignInActivity.class));
	             break;
	        case 1:  
				ShareCard InvFriends = new ShareCard(mContext);
				Intent shareIntent = InvFriends.InviteFriends();
				startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.text_share_card_to)));

	             break;
	        case 2:  
	             break;
	        case 3:  // Settings    	                     
	             Intent intent= new Intent(mContext, com.example.qcards.preferences.SettingsActivity.class);
	             startActivity(intent);
	             break;
	        case 4:  // Help                     
	        	break;
	        default: 
	             break;
	    }
      // update selected item and title, then close the drawer
      mDrawerList.setItemChecked(position, true);
      mDrawerList.setSelection(position);
      setTitle(navMenuTitles[position]);
      mDrawerLayout.closeDrawer(mDrawerLinearLayout);
  }

  //@Override
  public void setTitle(CharSequence title) {
      mTitle = title;
      getActionBar().setTitle(mTitle);
  }

  /**
   * When using the ActionBarDrawerToggle, you must call it during
   * onPostCreate() and onConfigurationChanged()...
   */

  //@Override
  protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
      mDrawerToggle.syncState();
  }
  

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      // Pass any configuration change to the drawer toggle
      mDrawerToggle.onConfigurationChanged(newConfig);
  }
  

  /***
   * Called when invalidateOptionsMenu() is triggered
   */
  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
      // if nav drawer is opened, hide the action items
      boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinearLayout);
      //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
      /*menu.findItem(R.id.action_sign_in).setVisible(!drawerOpen);
      menu.findItem(R.id.action_location_found).setVisible(!drawerOpen);
      menu.findItem(R.id.action_new).setVisible(!drawerOpen);
      menu.findItem(R.id.action_location_found).setVisible(!drawerOpen);
      menu.findItem(R.id.action_help).setVisible(!drawerOpen);
      menu.findItem(R.id.action_sortby).setVisible(!drawerOpen);
      menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
      menu.findItem(R.id.action_delete_groups).setVisible(!drawerOpen);
      menu.findItem(R.id.action_import_cards).setVisible(!drawerOpen);
      menu.findItem(R.id.action_search).setVisible(!drawerOpen);
      */
   // If the nav drawer is open, hide action items related to the content view
      
      hideMenuItems(menu, !drawerOpen);
      
      
      return super.onPrepareOptionsMenu(menu);
  }
  
  private void hideMenuItems(Menu menu, boolean visible)
  {

      for(int i = 0; i < menu.size(); i++){

          menu.getItem(i).setVisible(visible);

      }
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.activity_main_actions, menu);
      SearchManager searchManager = (SearchManager) getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
      SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
      searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
      return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
	  
	  if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
      }
  	return super.onOptionsItemSelected(item);
  }
  
	/**
	* Implementation for the CreateNdefMessageCallback interface
	*/
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		
	List<Contact> contactListToShare;
	NdefMessage msg = null;
	Fragment myFragment = getSupportFragmentManager().findFragmentByTag("DISPLAY_CARD_FRAGMENT");
	Fragment fragmentTab2 = TabsPagerAdapter.getFragment(TabsPagerAdapter.INDEX_TAB_2);
	//Fragment fragmentTab2 = TabsPagerAdapter.getFragment(TabsPagerAdapter.INDEX_TAB_2);
	
	
	//x2 = fragmentTab2.isVisible();
	//x1 = Tab2Activity.tab2Visible; 
	//x1 = myFragment.isVisible();
	x2 = fragmentTab2.getUserVisibleHint();
	//if (myFragment.isVisible() || mDualPane) {
	
    //if (fragmentTab2.getUserVisibleHint()){
	if (Tab2Activity.tab2Visible){
		
		List<Groups> mygroups = new ArrayList<Groups>();
		
  		mygroups = (ArrayList<Groups>) mydb.getAllGroups(SortByDialog.orderGroupsByDate);
  		SparseBooleanArray expanGroups = Tab2Activity.toggleCollExp;
  		
  		int[] groupIdsToShare = new int[expanGroups.size()];
  		LinkedHashMap<String, List<Contact>> groupListToShare = new LinkedHashMap<String, List<Contact>>();
  		int k = 0;
  		
  		Groups gs = new Groups();

  		// Save the groups ids that were checked in the Dialog
	      for (int j = 0; j < expanGroups.size(); j++) {
	              gs = mygroups.get(expanGroups.keyAt(j));
	              groupIdsToShare[k++] = gs.getId();
	      }
	      
	      
	     	if (k != 0)
	     	{
	
				groupListToShare = mydb.getContactsWithGroupIds(groupIdsToShare);
				String groupsData = UtilsJson.GroupsToJSon(groupListToShare);
				
				msg = new NdefMessage(
				        new NdefRecord[] { createMimeRecord(
				                "application/com.example.qcards", groupsData.getBytes()) /**
				           	  * The Android Application Record (AAR) is commented out. When a device
				           	  * receives a push with an AAR in it, the application specified in the AAR
				           	  * is guaranteed to run. The AAR overrides the tag dispatch system.
				           	  * You can add it back in to guarantee that this
				           	  * activity starts when receiving a beamed message. For now, this code
				           	  * uses the tag dispatch system.
				           	  */
				           	  ,NdefRecord.createApplicationRecord("com.example.qcards")
				           	});
	     	}else
	     	{
	     		 Toast.makeText(getApplicationContext(), "0 group(s) expanded, please expand a group", Toast.LENGTH_LONG).show();
	     	}

	}
	else if (myFragment.isVisible()) {
		  //contactListToShare = mydb.getContactsById(CId);
		 //int[] ci = ScreenSlicePageFragCards.CCId;
		 int []ci = {0};
		 int itemPos = DisplayCardsFragment.mPager.getCurrentItem();
		 ci[0] = Tab1Activity.contactList.get(itemPos).getID();
		 //ci[0]--;
		contactListToShare = mydb.getContactsById(ci);
		  String cardData = UtilsJson.ContactsToJSon(contactListToShare);
		  msg = new NdefMessage(
			        new NdefRecord[] { createMimeRecord(
			                "application/com.example.qcards", cardData.getBytes()) /**
			           	  * The Android Application Record (AAR) is commented out. When a device
			           	  * receives a push with an AAR in it, the application specified in the AAR
			           	  * is guaranteed to run. The AAR overrides the tag dispatch system.
			           	  * You can add it back in to guarantee that this
			           	  * activity starts when receiving a beamed message. For now, this code
			           	  * uses the tag dispatch system.
			           	  */
			           	  ,NdefRecord.createApplicationRecord("com.example.qcards")
			           	});
				
	} else
	{
         Toast.makeText(getApplicationContext(), "0 card(s) selected, please select a card", Toast.LENGTH_LONG).show();
	}
	return msg;
	}
	
	/**
	* Implementation for the OnNdefPushCompleteCallback interface
	*/
	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		// A handler is needed to send messages to the activity when this
		// callback occurs, because it happens from a binder thread
		mHandler.obtainMessage(MESSAGE_SENT).sendToTarget();
	}
	
	/** This handler receives a message from onNdefPushComplete */
	private final Handler mHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    
	    case MESSAGE_SENT:
	        //Toast.makeText(getApplicationContext(), "Card(s) sent!", Toast.LENGTH_LONG).show();
	        Toast.makeText(getApplicationContext(), "" + x1 + x2, Toast.LENGTH_LONG).show();
	        break;
	    }
	}
	};
	
	/**
	* Creates a custom MIME type encapsulated in an NDEF record
	*
	* @param mimeType
	*/
	
	public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
	byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
	NdefRecord mimeRecord = new NdefRecord(
	        NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
	return mimeRecord;
	}
}
