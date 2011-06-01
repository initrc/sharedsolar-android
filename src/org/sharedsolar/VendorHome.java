package org.sharedsolar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VendorHome extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_home);
        ((Button)findViewById(R.id.creditSummaryBtn)).setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), CreditSummary.class);
	            startActivity(intent);
			}
        });
    }
}