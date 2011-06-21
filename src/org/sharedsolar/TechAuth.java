package org.sharedsolar;

import org.sharedsolar.R;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.tool.MyUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TechAuth extends Activity {
	
	private DatabaseAdapter dbAdapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech_auth);
        dbAdapter = new DatabaseAdapter(this);
        
        ((Button)findViewById(R.id.techAuthBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View view) {
				String username = ((EditText)findViewById(R.id.techUsername)).getText().toString();
				String password = ((EditText)findViewById(R.id.techPassword)).getText().toString();
				((EditText)findViewById(R.id.techPassword)).setText("");
				dbAdapter.open(); 
				if (dbAdapter.userAuth(username, password))
				{
					Intent intent = new Intent(view.getContext(), TechHome.class);
	                startActivity(intent);
				}
				else
				{
					MyUI.showNeutralDialog(view.getContext(), R.string.loginError, 
							R.string.wrongPassword, R.string.ok);
				}
				dbAdapter.close();
			}
        });
    }
}