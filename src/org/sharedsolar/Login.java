package org.sharedsolar;

import org.sharedsolar.R;
import org.sharedsolar.tool.Connector;
import org.sharedsolar.tool.Device;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
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
	private ProgressDialog progressDialog;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ((Button)findViewById(R.id.vendorLoginBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View view) {
				progressDialog = ProgressDialog.show(view.getContext(), "", getString(R.string.loading));
				new Thread() {
					public void run() {
						status = new Connector(Login.this).sendDeviceId(getString(R.string.validateUrl), 
								Login.this);
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
				Intent intent = new Intent(Login.this, VendorHome.class);
                startActivity(intent);
			} else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
        	Intent intent = new Intent(this, TechAuth.class);
            startActivity(intent);
            return true;
        case R.id.aboutMenuItem:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.about));
			String version = "";
			try {
				version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			builder.setMessage(getString(R.string.app_name) + "\n\n" 
					+ getString(R.string.versionLabel) + " " + version + "\n"
					+ getString(R.string.deviceLabel) + " " + Device.getId(this));
            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.show();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed() {
        // do nothing. back to tech logout page not allowed.
    }
}