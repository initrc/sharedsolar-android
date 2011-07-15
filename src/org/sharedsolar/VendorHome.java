package org.sharedsolar;

import org.sharedsolar.R;
import org.sharedsolar.tool.Connector;
import org.sharedsolar.tool.MyUI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VendorHome extends Activity {
	
	private String jsonString;
	private ProgressDialog progressDialog;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home);
        ((Button)findViewById(R.id.creditSummaryBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), CreditSummary.class);
	            startActivity(intent);
			}
        });
        ((Button)findViewById(R.id.accountListBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View view) {
				progressDialog = ProgressDialog.show(view.getContext(), "", getString(R.string.loading));
				new Thread() {
        			public void run() {
        				jsonString = new Connector(VendorHome.this).requestForString(getString(R.string.accountListUrl), 
        						VendorHome.this);
        				accountListHandler.sendEmptyMessage(0);
        			}
        		}.start();
			}
        });
        ((Button)findViewById(R.id.chartBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View view) {
        		
				progressDialog = ProgressDialog.show(view.getContext(), "", getString(R.string.loading));
				new Thread() {
        			public void run() {
        				/*
        				jsonString = "[{'aid': 'a', 'watts': '" + (int)(Math.random()*10000)/100
    						+ "', 'wh_today': '" + (int)(Math.random()*10000)/100
    						+ "', 'cr': '" + (int)(Math.random()*10000)/100 + "'}]";
    					*/
        				jsonString = new Connector(VendorHome.this).requestForString(getString(R.string.circuitUsageUrl), 
        						VendorHome.this);
    					circuitUsageHandler.sendEmptyMessage(0);
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
	
	private Handler accountListHandler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
        	if (jsonString != null) {
        		if (jsonString.equals(String.valueOf(Connector.CONNECTION_TIMEOUT))) {
                    MyUI.showNeutralDialog(VendorHome.this, R.string.accountList,
                    		R.string.accountListTimeoutMsg, R.string.ok);
        		} else {
        			Intent intent = new Intent(VendorHome.this, AccountList.class);
    				intent.putExtra("accountList", jsonString);
    	            startActivity(intent);
        		}
        	}
        	else {
                MyUI.showNeutralDialog(VendorHome.this, R.string.accountList,
                		R.string.loadingAccountListError, R.string.ok);
        	}
        }
    };
    
    private Handler circuitUsageHandler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
        	if (jsonString != null) {
        		if (jsonString.equals(String.valueOf(Connector.CONNECTION_TIMEOUT))) {
                    MyUI.showNeutralDialog(VendorHome.this, R.string.chart,
                    		R.string.circuitUsageTimeoutMsg, R.string.ok);
        		} else {
        			Intent intent = new Intent(VendorHome.this, Chart.class);
    				intent.putExtra("circuitUsage", jsonString);
    	            startActivity(intent);
        		}
        	}
        	else {
                MyUI.showNeutralDialog(VendorHome.this, R.string.chart,
                		R.string.loadingCircuitUsageError, R.string.ok);
        	}
        }
    };
}