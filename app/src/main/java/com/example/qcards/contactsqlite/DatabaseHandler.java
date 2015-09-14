package com.example.qcards.contactsqlite;

import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qcards.groups.Groups;


public class DatabaseHandler extends SQLiteOpenHelper {
	
	public ArrayList<Integer> AccNLet; 
	public ArrayList<Integer> NLet; 
	
   // All Static variables
	
   // Database Version
   private static final int DATABASE_VERSION = 1;

   // Database Name
   public static final String DATABASE_NAME = "MyDBName.db";
   
   // Table Names
   public static final String CONTACTS_TABLE_NAME = "contacts";
   public static final String TABLE_GROUPS_NAME = "groups";
   public static final String TABLE_GROUPS_CONTACTS_NAME = "groups_contacts";
   
   // Contacts Table columns names
   public static final String CONTACTS_COLUMN_IMAGEID = "image_id";
   public static final String CONTACTS_COLUMN_NAME = "name";
   public static final String CONTACTS_COLUMN_OWNERID = "owner_id";
   public static final String CONTACTS_COLUMN_PHONE = "phone";
   public static final String CONTACTS_COLUMN_EMAIL = "email";
   public static final String CONTACTS_COLUMN_STREET = "street";
   public static final String CONTACTS_COLUMN_CITY = "place";
   
   // Common column names
   public static final String KEY_ID = "id";
   public static final String KEY_CREATED_AT = "date_time";
   
   // Groups Table - column groups
   public static final String KEY_GROUP_NAME = "group_name";

   public static final String KEY_CONTACTS_ID = "contacts_id";
   public static final String KEY_GROUP_ID = "group_id";
   
   public DatabaseHandler(Context context)
   {
      super(context, DATABASE_NAME , null, DATABASE_VERSION);
   }
   
   @Override
   public void onOpen(SQLiteDatabase db) {
       super.onOpen(db);
       if (!db.isReadOnly()) {
           // Enable foreign key constraints
           db.execSQL("PRAGMA foreign_keys=ON;");
       }
   }
   
   // Creating Tables
   @Override
   public void onCreate(SQLiteDatabase db) {
	   
	// creating tables
	   
	   String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE_NAME + "("
               + KEY_ID + " INTEGER PRIMARY KEY," + CONTACTS_COLUMN_IMAGEID + " INTEGER," 
               + CONTACTS_COLUMN_OWNERID + " INTEGER,"
			   + KEY_CREATED_AT + " DATETIME," + CONTACTS_COLUMN_NAME + " TEXT,"
               + CONTACTS_COLUMN_PHONE + " TEXT," + CONTACTS_COLUMN_EMAIL + " TEXT," + CONTACTS_COLUMN_STREET
               + " TEXT," + CONTACTS_COLUMN_CITY + " TEXT" + ")";
       db.execSQL(CREATE_CONTACTS_TABLE);
       
       String CREATE_TABLE_GROUPS = "CREATE TABLE " + TABLE_GROUPS_NAME
	            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GROUP_NAME + " TEXT,"
	            + KEY_CREATED_AT + " DATETIME" + ")";
	   db.execSQL(CREATE_TABLE_GROUPS);
	   
       /*String CREATE_TABLE_GROUPS_CONTACTS = "CREATE TABLE "
	            + TABLE_GROUPS_CONTACTS_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
	            + KEY_GROUP_ID + " INTEGER," + KEY_CONTACTS_ID + " INTEGER,"
	            + KEY_CREATED_AT + " DATETIME" + ")";
       db.execSQL(CREATE_TABLE_GROUPS_CONTACTS);*/
       
	   
	   String CREATE_TABLE_GROUPS_CONTACTS = "CREATE TABLE "
	            + TABLE_GROUPS_CONTACTS_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
	            + KEY_CREATED_AT + " DATETIME, "
	            + KEY_CONTACTS_ID + " INTEGER,"
	            + KEY_GROUP_ID + " INTEGER, "
	            + "FOREIGN KEY ("	+ KEY_CONTACTS_ID	+") REFERENCES " + CONTACTS_TABLE_NAME 	+ " (" + KEY_ID + ") ON DELETE CASCADE, "
	            + "FOREIGN KEY ("	+ KEY_GROUP_ID 		+") REFERENCES " + TABLE_GROUPS_NAME	+ " (" + KEY_ID + ") ON DELETE CASCADE)";
      db.execSQL(CREATE_TABLE_GROUPS_CONTACTS);
      
	   /*String CREATE_TABLE_GROUPS_CONTACTS = "CREATE TABLE "
	            + TABLE_GROUPS_CONTACTS_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
	            + KEY_GROUP_ID + " INTEGER, "
	            + "FOREIGN KEY ("	+ KEY_GROUP_ID 		+") REFERENCES " + TABLE_GROUPS_NAME	+ " (" + KEY_ID + ") ON DELETE CASCADE, "
	            + KEY_CONTACTS_ID + " INTEGER,"
	            + "FOREIGN KEY ("	+ KEY_CONTACTS_ID	+") REFERENCES " + CONTACTS_TABLE_NAME 	+ " (" + KEY_ID + ") ON DELETE CASCADE, "
	            + KEY_CREATED_AT + " DATETIME)";
     db.execSQL(CREATE_TABLE_GROUPS_CONTACTS);*/
      
      
   }
   
   // Upgrading database
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // TODO Auto-generated method stub
	// Drop older table if existed
      db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS_NAME);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS_CONTACTS_NAME);
   // Create tables again
      onCreate(db);
   }
   

   /**
    * All CRUD(Create, Read, Update, Delete) Operations
    */
   
   // Adding new contact
   public long insertContact  (Contact contact)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      
      contentValues.put(CONTACTS_COLUMN_IMAGEID, contact.getImageID());
      contentValues.put(CONTACTS_COLUMN_OWNERID, contact.getOwnerID());
      contentValues.put(KEY_CREATED_AT, getDateTime());
      contentValues.put(CONTACTS_COLUMN_NAME, contact.getName());
      contentValues.put(CONTACTS_COLUMN_PHONE, contact.getPhoneNumber());
      contentValues.put(CONTACTS_COLUMN_EMAIL, contact.getEmail());	
      contentValues.put(CONTACTS_COLUMN_STREET, contact.getStreet());
      contentValues.put(CONTACTS_COLUMN_CITY, contact.getPlace());

      // Inserting Row
      long contactId = db.insert(CONTACTS_TABLE_NAME, null, contentValues);
      db.close(); // Closing database connection
      return contactId;
   }
   
   // Adding a new group
   public long insertGroup (Groups group)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      
      //contentValues.put(KEY_ID, group.getId());
      contentValues.put(KEY_GROUP_NAME, group.getGroupName());
      //contentValues.put(KEY_GROUP_NAME, "fgfgf");
      
      contentValues.put(KEY_CREATED_AT, getDateTime());
      
      // Inserting Row
      long group_id = db.insert(TABLE_GROUPS_NAME, null, contentValues);
      db.close(); // Closing database connection
      return group_id;
   }

   
   // Getting single contact
   public Contact getOneContact(int i, SQLiteDatabase db){
	  
	  String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME + " WHERE " + KEY_ID + "="+i+"";
	  
      //SQLiteDatabase db = this.getReadableDatabase();
	  //SQLiteDatabase db = this.getReadableDatabase();
	  
	  Cursor cursor =  db.rawQuery(countQuery, null );
	  
	  cursor.moveToFirst();
	  
	  Contact contact = new Contact(i,
			  						cursor.getInt(cursor.getColumnIndex(CONTACTS_COLUMN_IMAGEID)),
			  						cursor.getInt(cursor.getColumnIndex(CONTACTS_COLUMN_OWNERID)),
			  						cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)),
			  						cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_NAME)),
			  						cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_PHONE)),
			  						cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_EMAIL)),
			  						cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_STREET)),
			  						cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_CITY)));
	  
	  //cursor.close();
      
      return contact;
   }
   
   // Getting single group by Id
   public Groups getGrupo(int id){
	  
	  String countQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME + " WHERE " + KEY_ID + "="+id+"";
	  
      //SQLiteDatabase db = this.getReadableDatabase();
	  SQLiteDatabase db = this.getReadableDatabase();
	  
	  Cursor cursor =  db.rawQuery(countQuery, null );
	  
	  cursor.moveToFirst();
	  
	  Groups group = new Groups(id,
			  					cursor.getString(cursor.getColumnIndex(KEY_GROUP_NAME)),
			  					cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
	  
	  cursor.close();
	  
      
      return group;
   }
   
   // Getting single group by name
   public Groups getGrupoByName(String grupo_name){
	  
	  String countQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME + " WHERE " + KEY_GROUP_NAME + "='"+grupo_name+"'";
	  
      //SQLiteDatabase db = this.getReadableDatabase();
	  SQLiteDatabase db = this.getReadableDatabase();
	  
	  Cursor cursor =  db.rawQuery(countQuery, null );
	  
	  cursor.moveToFirst();
	  
	  Groups group = new Groups(cursor.getInt(cursor.getColumnIndex(KEY_ID)),
			  					grupo_name,
			  					cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
	  
	  cursor.close();
	  
      
      return group;
   }


   
   
   // Getting contacts Count
   public int numberOfRows(){
      SQLiteDatabase db = this.getReadableDatabase();
      int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
      db.close();
      return numRows;
      
   }
   
   public boolean ifGroupNameExist(String grupo_name) {
	   int count = -1;
	   
	   String selectQuery = "SELECT count(*) FROM " + TABLE_GROUPS_NAME + " WHERE " + KEY_GROUP_NAME + " = " +
			   				"'"+grupo_name+"'";
	   
	   SQLiteDatabase db = this.getReadableDatabase();
	   Cursor cursor =  db.rawQuery(selectQuery, null );
	   
	   if (cursor.moveToFirst()) {
	      count = cursor.getInt(0);
	   }
	   cursor.close();
	   return count > 0;
   
   }

   public boolean ifCardIdExist(int contactId) {
	   int count = -1;
	   
	   String selectQuery = "SELECT count(*) FROM " + CONTACTS_TABLE_NAME + " WHERE " + KEY_ID + " = " +
			   				"contactId";
	   
	   SQLiteDatabase db = this.getReadableDatabase();
	   Cursor cursor =  db.rawQuery(selectQuery, null );
	   
	   if (cursor.moveToFirst()) {
	      count = cursor.getInt(0);
	   }
	   cursor.close();
	   return count > 0;
   
   }
   
   
   // Updating single contact
   public boolean updateContact (Contact contact)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      
      contentValues.put(CONTACTS_COLUMN_IMAGEID, contact.getImageID());
      contentValues.put(CONTACTS_COLUMN_OWNERID, contact.getOwnerID());
      contentValues.put(KEY_CREATED_AT, getDateTime());
      contentValues.put(CONTACTS_COLUMN_NAME, contact.getName());
      contentValues.put(CONTACTS_COLUMN_PHONE, contact.getPhoneNumber());
      contentValues.put(CONTACTS_COLUMN_EMAIL, contact.getEmail());
      contentValues.put(CONTACTS_COLUMN_STREET, contact.getStreet());
      contentValues.put(CONTACTS_COLUMN_CITY, contact.getPlace());
      db.update(CONTACTS_TABLE_NAME, contentValues, KEY_ID + "= ? ", new String[] { Integer.toString(contact.getID()) } );
      db.close();
      return true;
   }
   
   /*
    * Updating a group
    */
   public int updateGroup(Groups group) {
       SQLiteDatabase db = this.getWritableDatabase();
    
       ContentValues values = new ContentValues();
       values.put(KEY_GROUP_NAME, group.getGroupName());
       values.put(KEY_CREATED_AT, getDateTime());
    
       // updating row
       return db.update(TABLE_GROUPS_NAME, values, KEY_ID + " = ?",
               new String[] { String.valueOf(group.getId()) });
   }
   
   // Deleting single contact
   public void deleteContact (int id)
   {
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete(CONTACTS_TABLE_NAME, KEY_ID + "= ? ", new String[] { Integer.toString(id) });
      db.close();
      
   }
   
   // Delete a single group from table groups. Does not remove the cards.
   //public void deleteGroup (int id, SQLiteDatabase db)
   public void deleteOneGroup (int id, SQLiteDatabase db)
   {
      //SQLiteDatabase db = this.getWritableDatabase();
      db.delete(TABLE_GROUPS_NAME, KEY_ID + "= ? ", new String[] { Integer.toString(id) });
      //db.close();
      
   }
   
   // Delete a group from table groups/contacts. Does not remove the cards.
   public void deleteGroups (int []group_id)
   {
      SQLiteDatabase db = this.getReadableDatabase();
      // delete group
      for (int j = 0; j < group_id.length; j++)
      {	  
           deleteOneGroup(group_id[j],db);
      }
      
      db.close();
      
   }
   
   
   /**
    * Creating groups_contacts
    */
   public void insertCardsToGroup(int group_id, int[] contacts_ids) {
       SQLiteDatabase db = this.getWritableDatabase();

    // insert contacts_ids
       for (int i = 0; i < contacts_ids.length; i++ ) {
    	   insertOneCardToGroup(group_id, contacts_ids[i],db);
       }
       db.close();
   }
   
   public long insertOneCardToGroup(int group_id, int contacts_id, SQLiteDatabase db) {
       //SQLiteDatabase db = this.getWritableDatabase();
       
       ContentValues values = new ContentValues();
       values.put(KEY_CONTACTS_ID, contacts_id);
       values.put(KEY_GROUP_ID, group_id);
       values.put(KEY_CREATED_AT, getDateTime());

       long id = db.insert(TABLE_GROUPS_CONTACTS_NAME, null, values);

       return id;
   }
   
   // Getting All Contacts
   public List<Contact> getContactsById(int []contactsId)
   {
      //ArrayList array_list = new ArrayList();
      List<Contact> contactList = new ArrayList<Contact>();
      
      // Select All Query
      //String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;

      SQLiteDatabase db = this.getReadableDatabase();
      
	  for (int i = 0; i < contactsId.length; i++)
	  {
		   
		   Contact contact = new Contact();
		   contact = getOneContact(contactsId[i],db);
		   contactList.add(contact);
	  }

      db.close();
      // return contact list
      return contactList;
   }



   
   // Getting All Contacts
   public List<Contact> getAllContacts()
   {
      //ArrayList array_list = new ArrayList();
      List<Contact> contactList = new ArrayList<Contact>();
      
      
      // Select All Query
      String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor =  db.rawQuery(countQuery, null );
      
      // looping through all rows and adding to list
      if (cursor.moveToFirst()) 
      { 
      
         do {
    	
    		  Contact contact = new Contact();	  
    		  contact.setID(Integer.parseInt(cursor.getString(0)));
              contact.setImageID(Integer.parseInt(cursor.getString(1)));
              contact.setOwnerID(Integer.parseInt(cursor.getString(2)));
              contact.setDateTimeC(cursor.getString(3));
              contact.setName(cursor.getString(4));
              contact.setPhoneNumber(cursor.getString(5));
              contact.setEmail(cursor.getString(6));
              contact.setStreet(cursor.getString(7));
              contact.setPlace(cursor.getString(8));
              // Adding contact to list
              contactList.add(contact);
              
    		  //contactList.add(cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_NAME)));
    		 // cursor.moveToNext();
          } while (cursor.moveToNext());
      }

      db.close();
      // return contact list
      return contactList;
   }
   

   /**
    * getting a group with contacts by group name
    * */
   //public List<Contact> getGroupWithContacts(String group_name) {
   public List<Contact> getContactsWithOneGroupId(int group_id,SQLiteDatabase db) {
	   List<Contact> contactList = new ArrayList<Contact>();
	   //HashMap<String, List<Contact>> listData = new HashMap<String, List<Contact>>();
	   //List<Contact> contactList;
	   //contactList = new ArrayList<Contact>();
	   
	   /*String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME + " tc, "
	           + TABLE_GROUPS_NAME + " tg, " 
			   + TABLE_GROUPS_CONTACTS_NAME + " tcn WHERE tg."
	           + KEY_ID + " = " + group_id + " AND tg."
			   + KEY_ID + " = " + "tcn." + KEY_GROUP_ID + " AND tc."
	           + KEY_ID + " = " + "tcn." + KEY_CONTACTS_ID;
	   */
	   String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME + " tc, "
       + TABLE_GROUPS_CONTACTS_NAME + " tcn WHERE tcn." + 
       KEY_GROUP_ID + " = "+ group_id + " AND tc." + KEY_ID + " = "
       + "tcn." + KEY_CONTACTS_ID;
	   
	   //SQLiteDatabase db = this.getReadableDatabase();
	   Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	   if (cursor.moveToFirst()) {
	        do {
	      	  Contact contact = new Contact();	  
    		  contact.setID(Integer.parseInt(cursor.getString(0)));
              contact.setImageID(Integer.parseInt(cursor.getString(1)));
              contact.setOwnerID(Integer.parseInt(cursor.getString(2)));
              contact.setDateTimeC(cursor.getString(3));
              contact.setName(cursor.getString(4));
              contact.setPhoneNumber(cursor.getString(5));
              contact.setEmail(cursor.getString(6));
              contact.setStreet(cursor.getString(7));
              contact.setPlace(cursor.getString(8));
              // Adding contact to list
              contactList.add(contact);
	        } while (cursor.moveToNext());
	    }
	 
	    return contactList;
	   
   }
   
   //public LinkedHashMap<Integer, List<Contact>> getContactsWithGroupIds(int []group_id) {
   public LinkedHashMap<String, List<Contact>> getContactsWithGroupIds(int []group_id) {
	   
	   //LinkedHashMap<Integer, List<Contact>> listData = new LinkedHashMap<Integer, List<Contact>>();
	   LinkedHashMap<String, List<Contact>> listData = new LinkedHashMap<String, List<Contact>>();
       
	   SQLiteDatabase db = this.getReadableDatabase();
	   
	   for (int i = 0; i < group_id.length; i++)
	   {
		   Groups g = getGrupo(group_id[i]);
		   List<Contact> contactList = new ArrayList<Contact>();
		   contactList = getContactsWithOneGroupId(group_id[i],db);
           //listData.put(group_id[i],contactList);
		   listData.put(g.getGroupName(),contactList);
	   }
	   db.close();
	   return listData;
   }
   
   /**
    * getting all groups
    * */
   public List<Groups> getAllGroups(boolean orderByDate) {
       List<Groups> groups = new ArrayList<Groups>();
       //String selectQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME;
       String selectQuery;
       
       if (!orderByDate)
  	  {
  		  // Order alphabetivally
     	   selectQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME + " ORDER BY LOWER(" + KEY_GROUP_NAME + ")";
  	  }
  	  else
  	  {
  		  // Order create date
  		    selectQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME + " ORDER BY date(" + KEY_CREATED_AT + ")" + " DESC";
  	  }

    
       //Log.e(LOG, selectQuery);
    
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               Groups g = new Groups();
               g.setId(c.getInt((c.getColumnIndex(KEY_ID))));
               g.setGroupName(c.getString(c.getColumnIndex(KEY_GROUP_NAME)));
    
               // adding to tags list
               groups.add(g);
           } while (c.moveToNext());
       }
       db.close();
       return groups;
   }
   
   /**
    * getting all groups with contacts
    * */
   
   //public HashMap<String, List<Contact>> getAllGroupsWithContacts(boolean orderByDate) {
   public LinkedHashMap<String, List<Contact>> getAllGroupsWithContacts(boolean orderByDate) {
   
       //List<Groups> groups = new ArrayList<Groups>();
       //List<Contact> contactList = new ArrayList<Contact>();
       
	   LinkedHashMap<String, List<Contact>> listData = new LinkedHashMap<String, List<Contact>>();
       //listDataChild = new HashMap<String, List<Contact>>();
       
       String selectQuery;
       
       //= "SELECT  * FROM " + TABLE_GROUPS_NAME;
       //String selectQuery = "SELECT KEY_ID, KEY_GROUP_NAME " + TABLE_GROUPS_NAME;
    
       //Log.e(LOG, selectQuery);
       
       if (!orderByDate)
 	  {
 		  // Order alphabetivally
    	   selectQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME + " ORDER BY LOWER(" + KEY_GROUP_NAME + ")";
 	  }
 	  else
 	  {
 		  // Order create date
 		    selectQuery = "SELECT  * FROM " + TABLE_GROUPS_NAME + " ORDER BY date(" + KEY_CREATED_AT + ")" + " DESC";
 	  }
    
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(selectQuery, null);
       
       
       
       //c.getColumnIndex(columnName)
       // looping through all rows and adding to list
       if (c.moveToFirst()) {
           do {
               //Groups g = new Groups();
               //Contact contact = new Contact();
        	   List<Contact> contactList = new ArrayList<Contact>(); 
        	   //contactList = getGroupWithContacts(c.getString(c.getColumnIndex(KEY_GROUP_NAME)));
        	   contactList = getContactsWithOneGroupId(c.getInt((c.getColumnIndex(KEY_ID))),db);
        	   
               listData.put(c.getString(c.getColumnIndex(KEY_GROUP_NAME)),contactList);
               

           } while (c.moveToNext());
       }
       
       db.close();
       
       return listData;
   }
   
   public ArrayList getAllContactsName()
   {
      ArrayList array_list = new ArrayList();
      // Select All Query
      String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;
      
      //hp = new HashMap();
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor cursor =  db.rawQuery(countQuery, null );
      cursor.moveToFirst();
      while(cursor.isAfterLast() == false)
      {
    	  array_list.add(cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_NAME)));
    	  cursor.moveToNext();
      }
      db.close();   
      return array_list;
   }
   
   
   // Sort alphabetically
   public List<Contact> sortbyAlphaWithoutMyCards(int owner_id, boolean orderByDate){
	  
	  //NLetters = new ArrayList();
	  List<Contact> contactList = new ArrayList<Contact>();
	  
	  String createdDate, tempDate = "XXXX-XX-XX";
	  char tempFirstLetter = 'X', firstLetter;
	  
	  int temp_acc;
	  int count;
	  
	  String countQuery;
	  
	  createdDate = tempDate;
	  //int i;
	  
	  //AccNLet = new ArrayList<Integer>();
	  //NLet = new ArrayList<Integer>();
	  
	  if (!orderByDate)
	  {
		  // Order alphabetivally
		  countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME +  " WHERE " + CONTACTS_COLUMN_OWNERID + 
		  	" != "+owner_id+"" +  " ORDER BY LOWER(" + CONTACTS_COLUMN_NAME + ")";
	  }
	  else
	  {
		  // Order create date
		  countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME +  " WHERE " + CONTACTS_COLUMN_OWNERID + 
		  	" != "+owner_id+"" +  " ORDER BY date(" + KEY_CREATED_AT + ")" + " DESC";
		  
	  }
	  
      //SQLiteDatabase db = this.getReadableDatabase();
	  SQLiteDatabase db = this.getReadableDatabase();
	  
	  Cursor cursor =  db.rawQuery(countQuery, null );
	  
      // looping through all rows and adding to list
      if (cursor.moveToFirst()) 
      { 
    	  // Initialize variables
    	  //i = 0;
    	  //count = 0;
    	  count = 0;
    	  //temp_acc = 0;
    	  if (AccNLet.isEmpty())
    	  {
    	      AccNLet.add(0);
    	      AccNLet.add(0);
          
    	      NLet.add(0);
          
    	      AccNLet.set(1,0);
    	  	  NLet.set(0,0);
    	  }
    	  temp_acc = AccNLet.get(1);
    	  
    	  if (!orderByDate){
    		  tempFirstLetter = Character.toLowerCase(cursor.getString(4).charAt(0));
    	  	  //tempFirstLetter = Character.toLowerCase(strSort.charAt(0));
    	  }
    	  else{
    		  //System.arraycopy(cursor.getString(3), 0, tempDate, 0, 10);
    		  tempDate = cursor.getString(3).substring(0,10);
    	  }

       do {
        	  
   		  	 Contact contact = new Contact();	  
   		     contact.setID(Integer.parseInt(cursor.getString(0)));
             contact.setImageID(Integer.parseInt(cursor.getString(1)));
             contact.setOwnerID(Integer.parseInt(cursor.getString(2)));
             contact.setDateTimeC(cursor.getString(3));
             contact.setName(cursor.getString(4));
             contact.setPhoneNumber(cursor.getString(5));
             contact.setEmail(cursor.getString(6));
             contact.setStreet(cursor.getString(7));
             contact.setPlace(cursor.getString(8));
              // Adding contact to list
              contactList.add(contact);
              
              //strSort = cursor.getString(4);
              
              if (!orderByDate){
	              // Check the first letter of the contact name  
	              firstLetter = Character.toLowerCase(cursor.getString(4).charAt(0));
	              if (tempFirstLetter != firstLetter){ 
	            	  temp_acc = temp_acc + count;
	            	  tempFirstLetter = firstLetter;
	            	  // Store index of the letters
	            	  AccNLet.add(temp_acc); 
	            	  // Store number of the letters with type NameC.charAt(0)
	            	  NLet.add(count);
	            	  count = 1;
	              }
	              else
	               	  count++;
             }
	         else{
	        	 
	        	// Check the created date of the contact  
	        	 //System.arraycopy(cursor.getString(3), 0, createdDate, 0, 10);
	        	 createdDate = cursor.getString(3).substring(0,10);
	              if (!(tempDate.equals(createdDate))){ 
	            	  temp_acc = temp_acc + count;
	            	  tempDate = createdDate;
	            	  // Store index of the letters
	            	  AccNLet.add(temp_acc); 
	            	  // Store number of the letters with type NameC.charAt(0)
	            	  NLet.add(count);
	            	  count = 1;
	              }
	              else
	               	  count++;
	        }
	              
	            	  
            	  
              //i++;	  
    		  //contactList.add(cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_NAME)));
    		 // cursor.moveToNext();
          } while (cursor.moveToNext());
          NLet.add(count);
      }

      //db.close();
      // return contact list
      return contactList;
   }
   
   // Sort alphabetically
   public List<Contact> getMyCards(int owner_id, boolean orderByDate){
	  int i = 0;
	  //NLetters = new ArrayList();
	  
	  String countQuery;
	  AccNLet = new ArrayList<Integer>();
	  NLet = new ArrayList<Integer>();
	  
	  List<Contact> contactList = new ArrayList<Contact>();
	  
	  //AccNLet = new ArrayList<Integer>();
	  //NLet = new ArrayList<Integer>();
	  
	  if (!orderByDate)
	  {
		  // Order alphabetivally
		  countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_OWNERID + "="+owner_id+""
			   +  " ORDER BY LOWER(" + CONTACTS_COLUMN_NAME + ")";
	  }
	  else
	  {
		  // Order alphabetivally
		  countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_OWNERID + "="+owner_id+""
			   +  " ORDER BY date(" + KEY_CREATED_AT + ")" + "DESC";

	  }
	  
      //SQLiteDatabase db = this.getReadableDatabase();
	  SQLiteDatabase db = this.getReadableDatabase();
	  
	  Cursor cursor =  db.rawQuery(countQuery, null );
	  
      // looping through all rows and adding to list
      if (cursor.moveToFirst()) 
      { 

         do {
        	  
    		  Contact contact = new Contact();	  
    		  contact.setID(Integer.parseInt(cursor.getString(0)));
              contact.setImageID(Integer.parseInt(cursor.getString(1)));
              contact.setOwnerID(Integer.parseInt(cursor.getString(2)));
              contact.setDateTimeC(cursor.getString(3));
              contact.setName(cursor.getString(4));
              contact.setPhoneNumber(cursor.getString(5));
              contact.setEmail(cursor.getString(6));
              contact.setStreet(cursor.getString(7));
              contact.setPlace(cursor.getString(8));
              // Adding contact to list
              contactList.add(contact);
              i++;
              
          } while (cursor.moveToNext());
          //i--;
         AccNLet.add(0);
         AccNLet.add(0);
         
         NLet.add(0);
         
         AccNLet.set(1,i);
   	  	 NLet.set(0,i);
   	  	 
      }
      //db.close();
      return contactList;
   }


   
   // getting Date time
   public String getDateTime() {
       SimpleDateFormat dateFormat = new SimpleDateFormat(
               "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
       Date date = new Date();
       return dateFormat.format(date);
   }

   
}


