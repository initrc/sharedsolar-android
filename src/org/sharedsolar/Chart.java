package org.sharedsolar;

import org.sharedsolar.chart.CreditChart;
import org.sharedsolar.chart.EnergyChart;
import org.sharedsolar.chart.PowerChart;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class Chart extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);
		
		// tab host and spec
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// energy
		intent = new EnergyChart().execute(this);
		spec = tabHost.newTabSpec("energy").setIndicator(getString(R.string.energy)).setContent(intent);
		tabHost.addTab(spec);

		// power
		intent = new PowerChart().execute(this);
		spec = tabHost.newTabSpec("power").setIndicator(getString(R.string.power)).setContent(intent);
		tabHost.addTab(spec);
		
		// credit
		intent = new CreditChart().execute(this);
		spec = tabHost.newTabSpec("credit").setIndicator(getString(R.string.credit)).setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
}
