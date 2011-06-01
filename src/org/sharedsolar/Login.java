package org.sharedsolar;

import org.sharedsolar.tool.Validator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ((Button)findViewById(R.id.vendorLoginBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				boolean status = Validator.validate(getString(R.string.validateURL), v.getContext());
				Log.d("d", String.valueOf(status));
				if (status == true)
				{
					Intent intent = new Intent(v.getContext(), VendorHome.class);
	                startActivity(intent);
				}
				else
				{
					
				}
			}
        });
    }
    
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
}