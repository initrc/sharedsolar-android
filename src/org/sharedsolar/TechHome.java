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

public class TechHome extends Activity {
	
	private int status;
	private ProgressDialog progressDialog;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_home);
        ((Button)findViewById(R.id.syncWithMeterBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View view) {
        		progressDialog = ProgressDialog.show(view.getContext(), "", getString(R.string.synchronizing));
        		new Thread() {
        			public void run() {
        				status = new Connector(TechHome.this).sendDeviceId(getString(R.string.syncUrl), 
        						TechHome.this);
        				handler.sendEmptyMessage(0);
        			}
        		}.start();
			}
        });
        ((Button)findViewById(R.id.techAddCreditBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
        		Intent intent = new Intent(v.getContext(), TechAddCredit.class);
                startActivity(intent);
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
        	int message;
			if (status == Connector.CONNECTION_SUCCESS) {
				message = R.string.syncCompleted;
			} else if (status == Connector.CONNECTION_TIMEOUT) {
				message = R.string.syncTimeout;
			} else {
				message = R.string.syncError;
			}
			MyUI.showNeutralDialog(TechHome.this, R.string.sync, message, R.string.ok);
        }
    };
}