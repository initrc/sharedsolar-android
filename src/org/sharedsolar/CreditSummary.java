package org.sharedsolar;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class CreditSummary extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_summary);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// repo tab
		intent = new Intent().setClass(this, CreditRepo.class);
		spec = tabHost
				.newTabSpec("repository")
				.setIndicator(getString(R.string.repository),
						res.getDrawable(R.drawable.ic_tab_credit))
				.setContent(intent);
		tabHost.addTab(spec);

		// history tab
		intent = new Intent().setClass(this, TokenHistory.class);
		spec = tabHost
				.newTabSpec("history")
				.setIndicator(getString(R.string.history),
						res.getDrawable(R.drawable.ic_tab_list))
				.setContent(intent);
		tabHost.addTab(spec);
	}
}
