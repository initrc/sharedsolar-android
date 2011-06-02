package org.sharedsolar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AccountList extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	String jsonString = extras.getString("accountList");
        	Log.d("d", jsonString);
        	try {
				JSONArray jsonArray =new JSONArray(jsonString);
				for (int i=0; i<jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject)jsonArray.get(i);
					Log.d("d", "----"+jsonObject.getString("aid"));
					Log.d("d", jsonObject.getString("credit"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        	
    }
}