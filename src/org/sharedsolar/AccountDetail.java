package org.sharedsolar;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class AccountDetail extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_detail);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		Bundle extras = getIntent().getExtras();
		
		// repo tab
		intent = new Intent().setClass(this, VendorAddCredit.class);
		intent.putExtra("aid", extras.getString("aid"));
		intent.putExtra("cid", extras.getString("cid"));
		intent.putExtra("cr", extras.getInt("cr"));
		spec = tabHost
				.newTabSpec("credit")
				.setIndicator(getString(R.string.addCredit),
						res.getDrawable(R.drawable.ic_tab_credit))
				.setContent(intent);
		tabHost.addTab(spec);

		// history tab
		intent = new Intent().setClass(this, AccountTokenHistory.class);
		intent.putExtra("aid", extras.getString("aid"));
		spec = tabHost
				.newTabSpec("history")
				.setIndicator(getString(R.string.history),
						res.getDrawable(R.drawable.ic_tab_list))
				.setContent(intent);
		tabHost.addTab(spec);
	}
}
