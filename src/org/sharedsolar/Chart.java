package org.sharedsolar;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.chart.CreditChart;
import org.sharedsolar.chart.EnergyChart;
import org.sharedsolar.chart.PowerChart;
import org.sharedsolar.model.CircuitUsageModel;
import org.sharedsolar.model.CircuitUsageModelComparator;
import org.sharedsolar.tool.MyUI;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Chart extends TabActivity {
	
	private ArrayList<CircuitUsageModel> modelList;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);
		
		modelList = new ArrayList<CircuitUsageModel>();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String jsonString = extras.getString("circuitUsage");
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				Log.d("d", jsonString);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					CircuitUsageModel model = new CircuitUsageModel(
							jsonObject.getString("aid"),
							jsonObject.getDouble("watts"),
							jsonObject.getDouble("wh_today"),
							jsonObject.getDouble("cr"));
					modelList.add(model);
				}
			} catch (JSONException e) {
				MyUI.showNeutralDialog(this,
						R.string.invalidCircuitUsage, R.string.invalidCircuitUsageMsg,
						R.string.ok);
			}
			// sort by cid
			Collections.sort(modelList, new CircuitUsageModelComparator());
		}
		
		// tab host & spec
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		Resources res = getResources();

		// data
		ArrayList<double[]> values;
		String[] labels = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			labels[i] = modelList.get(i).getAid();
		}
		
		// energy
		double[] energy = new double[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			energy[i] = modelList.get(i).getWh_today();
		}
		values = new ArrayList<double[]>();
		values.add(energy);
		intent = new EnergyChart(values, labels).execute(this);
		spec = tabHost.newTabSpec("energy").setIndicator(getString(R.string.energy),
				res.getDrawable(R.drawable.ic_tab_energy)).setContent(intent);
		tabHost.addTab(spec);

		// power
		double[] power = new double[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			power[i] = modelList.get(i).getWatts();
		}
		values = new ArrayList<double[]>();
		values.add(power);
		intent = new PowerChart(values, labels).execute(this);
		spec = tabHost.newTabSpec("power").setIndicator(getString(R.string.power),
				res.getDrawable(R.drawable.ic_tab_power)).setContent(intent);
		tabHost.addTab(spec);
		
		// credit
		double[] cr = new double[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			cr[i] = modelList.get(i).getCr();
		}
		values = new ArrayList<double[]>();
		values.add(cr);
		intent = new CreditChart(values, labels).execute(this);
		spec = tabHost.newTabSpec("credit").setIndicator(getString(R.string.credit),
				res.getDrawable(R.drawable.ic_tab_credit)).setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}
}
