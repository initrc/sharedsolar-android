package org.sharedsolar;

import java.util.ArrayList;

import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.R;
import org.sharedsolar.adapter.AccountListAdapter;
import org.sharedsolar.model.AccountModel;
import org.sharedsolar.model.AccountModelComparator;
import org.sharedsolar.tool.Connector;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class AccountList extends ListActivity {

	private ArrayList<AccountModel> modelList;
	private AccountListAdapter accountListAdapter;
	private View view;
	private ProgressDialog progressDialog;
	private String jsonString;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_list);

		updateList();
		
		((ImageView)findViewById(R.id.accountRefreshImg)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				view = v;
				progressDialog = ProgressDialog.show(v.getContext(), "", getString(R.string.loading));
				new Thread() {
        			public void run() {
        				jsonString = new Connector(view.getContext()).requestAccountList(getString(R.string.accountListUrl), 
        						view.getContext());
        				handler.sendEmptyMessage(0);
        			}
        		}.start();
			}
        });
	}
	
	public void updateList() {
		modelList = new ArrayList<AccountModel>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String jsonString = extras.getString("accountList");
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					AccountModel model = new AccountModel(
							jsonObject.getString("aid"),
							jsonObject.getString("cid"),
							(int) (jsonObject.getDouble("cr") * 100));
					modelList.add(model);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// sort by aid
			Collections.sort(modelList, new AccountModelComparator());
			accountListAdapter = new AccountListAdapter(this,
					R.layout.account_list_item, modelList);
			setListAdapter(accountListAdapter);
			getListView().setOnItemClickListener(itemClickListener);
		}
	}
	
	OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AccountModel model = (AccountModel)(getListView().getItemAtPosition(position));
			if (model.getAid().equals("")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
	            builder.setMessage(getString(R.string.accountIdEmptyMsg) + " " + model.getCid());
	            builder.setTitle(getString(R.string.accountList));
	            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });
	            builder.show();
			} else {
				Intent intent = new Intent(view.getContext(), VendorAddCredit.class);
				intent.putExtra("aid", model.getAid());
				intent.putExtra("cid", model.getCid());
				intent.putExtra("cr", model.getCr());
	            startActivity(intent);
			}
			
		}
	};
	
	private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
        	if (jsonString != null) {
        		if (jsonString.equals(String.valueOf(Connector.CONNECTION_TIMEOUT))) {
        			AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                	builder.setTitle(getString(R.string.accountList));
                	builder.setMessage(getString(R.string.accountListTimeoutMsg));
                    builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
        		} else {
        			getIntent().putExtra("accountList", jsonString);
        			updateList();
        		}
        	}
        	else {
        		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
 				builder.setMessage(getString(R.string.loadingAccountListError));
 				builder.setTitle(getString(R.string.accountList));
        		builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        		builder.show();
        	}
        }
    };
}