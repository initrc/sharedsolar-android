package org.sharedsolar;

import org.sharedsolar.R;
import org.sharedsolar.tool.Connector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VendorHome extends Activity {
	
	private String jsonString;
	private View view;
	private ProgressDialog progressDialog;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home);
        ((Button)findViewById(R.id.creditSummaryBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), CreditSummary.class);
	            startActivity(intent);
			}
        });
        ((Button)findViewById(R.id.accountListBtn)).setOnClickListener(new OnClickListener()
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
        ((Button)findViewById(R.id.logoutBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
        		Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
			}
        });
    }
	
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
        			Intent intent = new Intent(view.getContext(), AccountList.class);
    				intent.putExtra("accountList", jsonString);
    	            startActivity(intent);
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
    
    @Override
    public void onBackPressed() {
        // back to login view
    	Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}