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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {
	
	private int status;
	private View view;
	private ProgressDialog progressDialog;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ((Button)findViewById(R.id.vendorLoginBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				view = v;
				progressDialog = ProgressDialog.show(v.getContext(), "", getString(R.string.loading));
				new Thread() {
					public void run() {
						status = new Connector(view.getContext()).sendVendorToken(getString(R.string.validateUrl), 
								view.getContext());
						handler.sendEmptyMessage(0);
					}
				}.start();
			}
        });
    }
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
			if (status == Connector.CONNECTION_SUCCESS) {
				Intent intent = new Intent(view.getContext(), VendorHome.class);
                startActivity(intent);
			} else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				builder.setTitle(getString(R.string.loginError));
				// timeout
				if (status == Connector.CONNECTION_TIMEOUT) {
					builder.setMessage(getString(R.string.vendorLoginTimeoutMsg));
				} else {
					builder.setMessage(getString(R.string.vendorLoginErrorMsg));
				}
					
	            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });
	            builder.show();
			}
        }
    };
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.techLoginMenuItem:
        	Intent intent = new Intent(this.getApplicationContext(), TechAuth.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
        // do nothing. back to tech logout page not allowed.
    }
}