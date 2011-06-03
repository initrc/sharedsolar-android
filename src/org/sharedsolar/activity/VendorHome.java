package org.sharedsolar.activity;

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
        				jsonString = Connector.requestAccountList(getString(R.string.accountListUrl), view.getContext());
        				handler.sendEmptyMessage(0);
        			}
        		}.start();
			}
        });
    }
	
	private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
        	if (jsonString != null) {
				Intent intent = new Intent(view.getContext(), AccountList.class);
				intent.putExtra("accountList", jsonString);
	            startActivity(intent);
        	}
        	else {
        		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
 				builder.setMessage(getString(R.string.loadingAccountListError));
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