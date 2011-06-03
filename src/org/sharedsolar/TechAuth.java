package org.sharedsolar;

import org.sharedsolar.R;
import org.sharedsolar.db.DatabaseAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
				((EditText)findViewById(R.id.techPassword)).setText("");
				dbAdapter.open(); 
				if (dbAdapter.userAuth(username, password))
				{
					Intent intent = new Intent(v.getContext(), TechHome.class);
	                startActivity(intent);
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		            builder.setMessage(getString(R.string.wrongPassword));
		            builder.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });
		            builder.show();
				}
				dbAdapter.close();
			}
        });
    }
}