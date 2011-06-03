package org.sharedsolar;

import org.sharedsolar.R;
import org.sharedsolar.tool.Connector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TechHome extends Activity {
	
	private boolean status;
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
        				status = Connector.sendVendorToken(getString(R.string.syncUrl), view.getContext());
        				handler.sendEmptyMessage(0);
        			}
        		}.start();
			}
        });
    }
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
        	AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
			if (status)	{
				builder.setMessage(getString(R.string.syncCompleted));
			}
			else {
				builder.setMessage(getString(R.string.syncError));
			}
			builder.show();
        }
    };
}