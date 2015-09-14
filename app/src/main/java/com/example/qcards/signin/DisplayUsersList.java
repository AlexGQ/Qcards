package com.example.qcards.signin;

import com.example.qcards.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
*/
import org.json.JSONArray;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

public class DisplayUsersList extends Activity {

    // DB connection (write mode)
    UserDB DBconnection = new UserDB(this, "PB_COM_USERS", null, 1);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_users_list);
		Thread tr = new Thread(){
			@Override
			public void run(){
				final String Result = ReadURL();
				runOnUiThread(
						new Runnable() {
				
							@Override
							public void run() {
									LoadList(obtDatosJSON(Result));
							}
						});
			}			
		};
		tr.start();
	}

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
	
	public void LoadList(ArrayList<String> data) {
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
		ListView list                  = (ListView) findViewById(R.id.listViewUsers);
		list.setAdapter(adaptador);
	}
	
	public String ReadURL() {
        String result       = null;
		/*HttpClient client   = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();

        // User to obtain her cards
        SQLiteDatabase db = DBconnection.getReadableDatabase();

        // User in BBDD
        Cursor c = db.rawQuery(" SELECT EMAIL, PASSWORD FROM PB_COM_USERS WHERE ID_USER = 0", null);
        c.moveToFirst();
        String email           = c.getString(0);
        String password        = c.getString(1);
        db.close();

        String encodepassword  = md5(password);
        HttpGet httpget        = new HttpGet("http://appdevelopment.esy.es/GetData.php?email="+email+"&password="+encodepassword);

		try {
			HttpResponse response = client.execute(httpget, context);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		return result;
	}
	
	public ArrayList<String> obtDatosJSON(String response) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONArray json     = new JSONArray(response);
			String text        = "";
			for (int i = 0; i < json.length(); i++){
                text = json.getJSONObject(i).getString("name") +" - "+
                       json.getJSONObject(i).getString("email");
				list.add(text);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
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

}