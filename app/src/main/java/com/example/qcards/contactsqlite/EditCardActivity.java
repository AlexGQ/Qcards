package com.example.qcards.contactsqlite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qcards.R;
import com.example.qcards.UtilsPics;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


public class EditCardActivity  extends Activity {
	
	private DatabaseHandler mydb;
	
	TextView  name;
	TextView  phone;
	TextView  email;
	TextView  street;
	TextView  place;
	TextView  idtext;
	ImageView card_image;
	int       id_To_Update = 0;
	int       imageID = 0;
	boolean   flag_new;
	Contact   contact = new Contact();
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
		setContentView(R.layout.edit_card_activity);
		 
		Bitmap bm = null;
		int ContactId;
		
		// SQLite database
		mydb = new DatabaseHandler(this);
		
	    card_image = (ImageView)findViewById(R.id.ecard_image);
		name       = (TextView) findViewById(R.id.EditTextName);
	    phone      = (TextView) findViewById(R.id.EditTextPhone);
	    email 	   = (TextView) findViewById(R.id.EditTextStreet);
	    street 	   = (TextView) findViewById(R.id.EditTextEmail);
	    place 	   = (TextView) findViewById(R.id.EditTextCity);
	    idtext 	   = (TextView)findViewById(R.id.DisplayTextId);

		// Get intent data
	    Intent i = getIntent();
	
	    // Get position in the listview
	    ContactId = i.getExtras().getInt("ContactId");
	    
    	// Get flat to determine if the card is new  
	    flag_new = i.getExtras().getBoolean("flag_new");
        
	    UtilsPics im = new UtilsPics();
	    
        if (!flag_new) {
        	
        		//Display the card to be edited.
        		int[] CId = new int[1]; 
        		List<Contact> contactList = new ArrayList<Contact>();
        		//contactList = mydb.getAllContacts();
        		CId[0] = ContactId;

        		contactList = mydb.getContactsById(CId);
        		contact = contactList.get(0);
        		//contact = contactList.get(position);
        		
        		//ContactId = contact.getID();
        		
        		id_To_Update = ContactId;
        		
        		// Get image id of the card selected        		
        		imageID = contact.getImageID();
        		
	            name.setText((CharSequence)contact.getName());
			    name.setEnabled(true);
	 		    name.setFocusableInTouchMode(true);
	 		    name.setClickable(true);
	 		    
	 		    phone.setText((CharSequence)contact.getPhoneNumber());	 		
	 		    phone.setEnabled(true);
	 		    phone.setFocusableInTouchMode(true);
	 		    phone.setClickable(true);
	 		    
	 		    email.setText((CharSequence)contact.getEmail());
	 		    email.setEnabled(true);
	 		    email.setFocusableInTouchMode(true);
	 		    email.setClickable(true);
	 		    
	 		    street.setText((CharSequence)contact.getStreet());
	 		    street.setEnabled(true);
	 		    street.setFocusableInTouchMode(true);
	 		    street.setClickable(true);
 		
	 		    place.setText((CharSequence)contact.getPlace());
	 		    place.setEnabled(true);
	 		    place.setFocusableInTouchMode(true);
	 		    place.setClickable(true);
	 		    
			    // Set image from resources -> mThumbIds
			    bm = im.decodeSampledBitmapFromRes(getApplicationContext(),im.mThumbIds[(int)imageID-1],  220, 220);
			    card_image.setImageBitmap(bm);
        		
        }
        else {
		        //Display the card layout selected, ready to create a new card.
        		int position = i.getExtras().getInt("position");
			    
			    // Get image id of the card selected
        		imageID = im.image_id[position];
			    
			    String log = String.valueOf(imageID);
		        idtext.setText(log);
			    
			    // Set image from resources -> mThumbIds
			    bm = im.decodeSampledBitmapFromRes(getApplicationContext(),im.mThumbIds[(int)imageID-1],  220, 220);
			    card_image.setImageBitmap(bm);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       // Inflate the menu; this adds items to the action bar if it is present.
       getMenuInflater().inflate(R.menu.save_card_actions, menu);

       return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) 
    { 
       super.onOptionsItemSelected(item); 
       switch(item.getItemId()) 
       { 
       	 case R.id.action_save:
       		  BtnSaveOnClick();
       		  return true;
       	 default: 
       		  return super.onOptionsItemSelected(item); 

       } 
    } 
    
    // Save card
    public void BtnSaveOnClick()
    {
    	
          contact.setName(name.getText().toString());
      	  contact.setPhoneNumber(phone.getText().toString());
     	  contact.setEmail(email.getText().toString());
     	  contact.setStreet(street.getText().toString());
     	  contact.setPlace(place.getText().toString());
     	  contact.setImageID(imageID);
     	 
          // Update card
          if(!flag_new) {
        	  
        	 contact.setID(id_To_Update);
        	 contact.setOwnerID(2);
        	 
             if(mydb.updateContact(contact)) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();	
                saveCardInCloud(contact.getName(), contact.getEmail());
                
                Intent intent = new Intent(getApplicationContext(),com.example.qcards.contactsqlite.NewContactActivity.class);
                startActivity(intent);
             }		
             else {
                Toast.makeText(getApplicationContext(), "Not updated", Toast.LENGTH_SHORT).show();	
             }
 		  }
          // Create card
          else {
        	  
        	 contact.setOwnerID(1);
        	 int ID = (int) mydb.insertContact(contact);
        	 
             if (ID > 0) {
                Toast.makeText(getApplicationContext(), "Save card done", Toast.LENGTH_SHORT).show();	
                saveCardInCloud(contact.getName(), contact.getEmail());
                    	        
             } else {
                Toast.makeText(getApplicationContext(), "Save card error", Toast.LENGTH_SHORT).show();	
             }

             Intent intent = new Intent(getApplicationContext(),com.example.qcards.contactsqlite.NewContactActivity.class);
             startActivity(intent);
          }
    }
    
    public String saveCardInCloud(String name, String email) {


        String result       = null;
		HttpClient client   = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();
        HttpGet httpget     = new HttpGet("http://appdevelopment.esy.es/PutCardData.php?email="+email+"&name="+name);

		try {
			HttpResponse response = client.execute(httpget, context);
			HttpEntity entity     = response.getEntity();
			result                = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
}
