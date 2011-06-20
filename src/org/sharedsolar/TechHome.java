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

public class TechHome extends Activity {
	
	private int status;
	private View view;
	private ProgressDialog progressDialog;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_home);
        ((Button)findViewById(R.id.syncDeviceBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
        		view = v;
        		progressDialog = ProgressDialog.show(v.getContext(), "", getString(R.string.synchronizing));
        		new Thread() {
        			public void run() {
        				status = new Connector(view.getContext()).sendDeviceId(getString(R.string.syncUrl), 
        						view.getContext());
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
        	AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        	builder.setTitle(getString(R.string.sync));
            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
			if (status == Connector.CONNECTION_SUCCESS) {
				builder.setMessage(getString(R.string.syncCompleted));
			} else if (status == Connector.CONNECTION_TIMEOUT) {
				builder.setMessage(getString(R.string.syncTimeout));
			} else {
				builder.setMessage(getString(R.string.syncError));
			}
			builder.show();
        }
    };
}