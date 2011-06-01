package org.sharedsolar;

import org.sharedsolar.tool.Validator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TechHome extends Activity {
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_home);
        ((Button)findViewById(R.id.syncDeviceBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
				boolean status = Validator.validate(getString(R.string.syncURL), v.getContext());
				Log.d("d", String.valueOf(status));
			}
        });
    }
}