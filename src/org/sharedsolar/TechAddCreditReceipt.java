package org.sharedsolar;

import org.sharedsolar.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TechAddCreditReceipt extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tech_add_credit_receipt);
		
		String info = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) info = extras.getString("info");
		if (info == null) info = "";
		
		((TextView)findViewById(R.id.techAddCreditReceiptInfo)).setText(info);
		
		((Button)findViewById(R.id.techAddCreditReceiptOkBtn)).setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) {
        		Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
			}
        });
	}
	
	@Override
    public void onBackPressed() {
        // do nothing. back to tech_add_credit page not allowed.
    }
}