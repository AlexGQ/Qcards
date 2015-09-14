package com.example.qcards.signin;

import com.example.qcards.MainActivity;
import com.example.qcards.R;
import com.example.qcards.hviewcards.DisplayCardsLayouts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
*/
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;

public class SignInActivity extends Activity {

    // DB connection (write mode)
    UserDB DBconnection = new UserDB(this, "PB_COM_USERS", null, 1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_item);
	}
	
	public void DisplayListOnClick(View view){
		startActivity(new Intent(this, DisplayUsersList.class));
	}

	// Loggin
	public void LogginOnClick(View view) {
        Thread nt = new Thread() {
            @Override
            public void run() {
                EditText email      = (EditText) findViewById(R.id.et_email2);
                EditText password   = (EditText) findViewById(R.id.et_password2);

           /*     try {
                    final String res;
                    // Loggin user cloud
                    res = logginUser(email.getText().toString(), md5(password.getText().toString()));

                    // Obtain Json
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        	JSONArray json;
							try {
								json = new JSONArray(res);
	                        	String name         = json.getJSONObject(0).getString("name");
	                        	String lastName     = json.getJSONObject(0).getString("lastName");
	                        	String email        = json.getJSONObject(0).getString("email");
	                        	String password     = json.getJSONObject(0).getString("password");
	                        	Toast.makeText(SignInActivity.this, "Loggin", Toast.LENGTH_LONG).show();	            
	                        	// Save in local storage
	                            saveUserInLocalDatabase(name, lastName, email, password);
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(SignInActivity.this, "Loggin error", Toast.LENGTH_LONG).show();
							}

                        }
                    });

                } catch (Exception e) {
                	e.printStackTrace();
                	Toast.makeText(SignInActivity.this, "Loggin error", Toast.LENGTH_LONG).show();
                }*/
            }
        };
        nt.start();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
	
    public void SaveOnClick(View view) {
       /* Thread nt = new Thread() {
            @Override
            public void run() {
                EditText name       = (EditText) findViewById(R.id.et_name);
                EditText lastName   = (EditText) findViewById(R.id.et_lastName);
                EditText email      = (EditText) findViewById(R.id.et_email);
                EditText password   = (EditText) findViewById(R.id.et_password);
                try {
                    // Save in the cloud
                    saveUserInCloud(name.getText().toString(), lastName.getText().toString(),
                                   email.getText().toString(), password.getText().toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignInActivity.this, "Create user", Toast.LENGTH_LONG).show();
                        }
                    });

                    // Save in local storage
                    saveUserInLocalDatabase(name.getText().toString(), lastName.getText().toString(),
                                            email.getText().toString(), password.getText().toString());

                } catch (Exception e) {
                	e.printStackTrace();
                	Toast.makeText(SignInActivity.this, "Create user error", Toast.LENGTH_LONG).show();
                }
            }
        };
        nt.start();*/
        Intent intent = new Intent(getApplicationContext(),DisplayCardsLayouts.class);
        startActivity(intent);
    }

    /*public String saveUserInCloud(String name, String lastName, String email, String password) {

        HttpClient httpClient    = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost        = new HttpPost("http://appdevelopment.esy.es/PutData.php");
        HttpResponse response    = null;
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("lastName", lastName));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost, localContext);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return response.toString();

    }
      */
    /*public String logginUser(String email, String password) {
		
        String result       = null;
		HttpClient client   = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();
        HttpGet httpget     = new HttpGet("http://appdevelopment.esy.es/loggin.php?email="+email+"&password="+password);

		try {
			HttpResponse response = client.execute(httpget, context);
			HttpEntity entity     = response.getEntity();
			result                = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView       = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return true;
	}

    public void saveUserInLocalDatabase(String name, String lastName, String email, String password) {

        /*SQLiteDatabase db = DBconnection.getWritableDatabase();
        if(db != null)
        {
            db.execSQL("DROP TABLE IF EXISTS PB_COM_USERS");
            String sqlCreate = "CREATE TABLE PB_COM_USERS (ID_USER INTEGER, NAME TEXT, LAST_NAME TEXT, EMAIL TEXT, PASSWORD TEXT)";
            db.execSQL(sqlCreate);
            // Insert User in BBDD
            db.execSQL("INSERT INTO PB_COM_USERS (ID_USER, NAME, LAST_NAME, EMAIL, PASSWORD) " +
                    "VALUES (0, '" + name +"', '" + lastName +"', '" + email +"', '" + password +"')");
            db.close();
        }*/

    }
    
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public void ShowSignInOnClick(View view) {
    	LinearLayout Main       = (LinearLayout) findViewById(R.id.Main2);    	
        LinearLayout NewAccount = (LinearLayout) findViewById(R.id.NewAccount);
        Main.setVisibility(View.GONE);
        NewAccount.setVisibility(View.VISIBLE);
        

    }
    
    public void ShowLogginOnClick(View view) {
    	startActivity(new Intent(this, UploadImage.class));
    	/*
    	 * 
    	LinearLayout Main       = (LinearLayout) findViewById(R.id.Main2); 
    	LinearLayout Loggin     = (LinearLayout) findViewById(R.id.Loggin);
    	Main.setVisibility(View.GONE);
    	Loggin.setVisibility(View.VISIBLE);
    	*/
    }
}
/*


package com.example.qcards.signin;

import com.example.qcards.R;

import java.io.IOException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class SignInActivity extends Activity implements ConnectionCallbacks,
              OnConnectionFailedListener {


       private static final int RC_SIGN_IN = 0;

    
       private GoogleApiClient mGoogleApiClient;


       private boolean mIntentInProgress;
       TextView textView;
       ImageView profileImage;
       com.google.android.gms.common.SignInButton signInBtn;

       public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);

              StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                           .permitAll().build();
              StrictMode.setThreadPolicy(policy);

              setContentView(R.layout.sign_in_google_button);
              mGoogleApiClient = new GoogleApiClient.Builder(this)
                           .addConnectionCallbacks(this)
                           .addOnConnectionFailedListener(this).addApi(Plus.API)
                           .addScope(Plus.SCOPE_PLUS_LOGIN).build();

              textView = (TextView) findViewById(R.id.username);
              profileImage = (ImageView) findViewById(R.id.profileImage);
              signInBtn = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
              signInBtn.setOnClickListener(new OnClickListener() {

                     @Override
                     public void onClick(View arg0) {
                           mGoogleApiClient.connect();

                     }
              });
       }

       protected void onStart() {
              super.onStart();

       }

       protected void onStop() {
              super.onStop();

              if (mGoogleApiClient.isConnected()) {
                     mGoogleApiClient.disconnect();
              }
       }

       @Override
       public void onConnectionFailed(ConnectionResult arg0) {

              if (!mIntentInProgress && arg0.hasResolution()) {
                     try {
                           mIntentInProgress = true;
                            arg0.startResolutionForResult(this, RC_SIGN_IN);
                     } catch (SendIntentException e) {
                           // The intent was canceled before it was sent. Return to the
                           // default
                           // state and attempt to connect to get an updated
                           // ConnectionResult.
                           mIntentInProgress = false;
                           mGoogleApiClient.connect();
                     }

              }

              // Log.e("error", "error code" + arg0.getResolution());
              Toast.makeText(this, "User is onConnectionFailed!", Toast.LENGTH_LONG)
                           .show();
       }

       @Override
       protected void onActivityResult(int requestCode, int responseCode,
                     Intent intent) {
              if (requestCode == RC_SIGN_IN) {
                     mIntentInProgress = false;
                     if (!mGoogleApiClient.isConnecting()) {
                           mGoogleApiClient.connect();
                     }
              }
       }

       @Override
       public void onConnected(Bundle arg0) {

              Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
              signInBtn.setVisibility(View.GONE);
              if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                     Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                     textView.setText("Welcome : " + person.getDisplayName());
                     try {
                           JSONObject jsonObject = new JSONObject(person.getImage()
                                         .toString());
                           String imageUrl = jsonObject.getString("url");

                           try {
                                  URL url = new URL(imageUrl);
                                  Bitmap bmp;
                                  bmp = BitmapFactory.decodeStream(url.openConnection()
                                                .getInputStream());
                                  profileImage.setImageBitmap(bmp);
                           } catch (IOException e) {
                                  // TODO Auto-generated catch block
                                  e.printStackTrace();
                            }

                     } catch (JSONException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                     }

              }

       }

       @Override
       public void onConnectionSuspended(int arg0) {

              Toast.makeText(this, "User is onConnectionSuspended!",
                           Toast.LENGTH_LONG).show();
       }

}
*/