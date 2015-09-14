package com.example.qcards.contactsqlite;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qcards.R;

public class DisplayContactActivity extends Activity {

   //int from_Where_I_Am_Coming = 0;
   private DatabaseHandler mydb;
   
   TextView name ;
   TextView phone;
   TextView email;
   TextView street;
   TextView place;
   TextView idtext;
   TextView iidtext;
   int id_To_Update = 0;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.activity_display_contact);

      name = (TextView) findViewById(R.id.EditTextName);
      phone = (TextView) findViewById(R.id.EditTextPhone);
      email = (TextView) findViewById(R.id.EditTextStreet);
      street = (TextView) findViewById(R.id.EditTextEmail);
      place = (TextView) findViewById(R.id.EditTextCity);
      idtext = (TextView)findViewById(R.id.DisplayTextId);
      iidtext = (TextView)findViewById(R.id.TextImageId);

      mydb = new DatabaseHandler(this);

      Bundle extras = getIntent().getExtras(); 

      // Display or create contacts
      if(extras != null)          
      {

    	 // Get Contact ID 
         int ContactId = extras.getInt("id");
         
         // Display contact by ID
         if(ContactId > 0){
        	 
        	// Get contact details  
            //Contact contact = mydb.getData(ContactId);
    		int[] CId = new int[1]; 
    		List<Contact> contactList = new ArrayList<Contact>();
    		//contactList = mydb.getAllContacts();
    		CId[0] = ContactId;

    		contactList = mydb.getContactsById(CId);
    		Contact contact = contactList.get(0);

            
            id_To_Update = ContactId;
            
            //rs.moveToFirst();
            /*String nam = rs.getString(rs.getColumnIndex(DatabaseHandler.CONTACTS_COLUMN_NAME));
            String phon = rs.getString(rs.getColumnIndex(DatabaseHandler.CONTACTS_COLUMN_PHONE));
            String emai = rs.getString(rs.getColumnIndex(DatabaseHandler.CONTACTS_COLUMN_EMAIL));
            String stree = rs.getString(rs.getColumnIndex(DatabaseHandler.CONTACTS_COLUMN_STREET));
            String plac = rs.getString(rs.getColumnIndex(DatabaseHandler.CONTACTS_COLUMN_CITY));*/
            /*if (!rs.isClosed()) 
            {
               rs.close();
            }*/
            // Button save invisible

            // Display contact details
            
            name.setText((CharSequence)contact.getName());
            //name.setText(contact.getName());
            name.setFocusable(false);
            name.setClickable(false);

            phone.setText((CharSequence)contact.getPhoneNumber());
            phone.setFocusable(false); 
            phone.setClickable(false);

            email.setText((CharSequence)contact.getEmail());
            email.setFocusable(false);
            email.setClickable(false);

            street.setText((CharSequence)contact.getStreet());
            street.setFocusable(false); 
            street.setClickable(false);

            place.setText((CharSequence)contact.getPlace());
            place.setFocusable(false);
            place.setClickable(false);
            
            String log = String.valueOf(contact.getID());
            idtext.setText(log);
            
            mydb.numberOfRows();
            
            
            String logi = String.valueOf(contact.getImageID());
            //String logi = String.valueOf(mydb.numberOfRows());
            iidtext.setText(logi);
            
           }
      }
   }
}