package com.example.qcards;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
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


import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
 
public class SearchResultsActivity extends Activity {
 
    private TextView txtQuery;
    	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
 
        // get the action bar
        ActionBar actionBar = getActionBar();
 
        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);
 
        txtQuery = (TextView) findViewById(R.id.txtQuery);
        handleIntent(getIntent());
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);

            Thread tr = new Thread(){
    			@Override
    			public void run(){
    				final String Result = requestSearch(query);
    				runOnUiThread(
    						new Runnable() {
    							@Override
    							public void run() {
    								txtQuery.setText(Result);
    							}
    						});
    			}			
    		};
    		tr.start();
        }
 
    }
    

	public String requestSearch(String name) {
		
        String result       = null;
		HttpClient client   = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();
        HttpGet httpget     = new HttpGet("http://appdevelopment.esy.es/FindCards.php?name="+name);

		try {
			HttpResponse response = client.execute(httpget, context);
			HttpEntity entity     = response.getEntity();
			result                = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
    
}