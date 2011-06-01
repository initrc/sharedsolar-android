package org.sharedsolar;

import org.sharedsolar.db.DatabaseAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TechAuth extends Activity {
	
	private DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_auth);
        ((Button)findViewById(R.id.techAuthBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
				String username = ((EditText)findViewById(R.id.techUsername)).getText().toString();
				String password = ((EditText)findViewById(R.id.techPassword)).getText().toString();
				dbAdapter.open(); 
				if (dbAdapter.userAuth(username, password))
				{
					Intent intent = new Intent(v.getContext(), TechHome.class);
	                startActivity(intent);
				}
				dbAdapter.close();
			}
        });
    }
}