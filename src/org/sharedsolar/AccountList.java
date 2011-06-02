package org.sharedsolar;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.adapter.AccountListAdapter;
import org.sharedsolar.model.AccountListModel;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class AccountList extends ListActivity {
	
	private ArrayList<AccountListModel> items;
	private AccountListAdapter accountListAdapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list);
        
        items = new ArrayList<AccountListModel>();
        accountListAdapter = new AccountListAdapter(this, R.layout.account_list_item);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	String jsonString = extras.getString("accountList");
        	Log.d("d", jsonString);
        	try {
				JSONArray jsonArray =new JSONArray(jsonString);
				for (int i=0; i<jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject)jsonArray.get(i);
					AccountListModel model = new AccountListModel(jsonObject.getString("aid"), 
							jsonObject.getDouble("cr"));
					items.add(model);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i=0; i<items.size(); i++) {
				Log.d("d", items.get(i).getAid()+" -- "+items.get(i).getCr());
			}
			
			accountListAdapter.setItems(items);
			setListAdapter(accountListAdapter);
			accountListAdapter.notifyDataSetChanged();
        }
        	
    }
}