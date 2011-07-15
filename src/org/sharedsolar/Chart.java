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
import org.sharedsolar.tool.Connector;
import org.sharedsolar.tool.MyUI;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Chart extends TabActivity {
	
	private TabHost tabHost;
	private TabSpec energySpec, powerSpec, creditSpec;
	private int currentTab = 0;
	private ProgressDialog progressDialog;
	private String jsonString;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			jsonString = extras.getString("circuitUsage");
			initTab(buildModel());
		}
	}

	public ArrayList<CircuitUsageModel> buildModel() {
		ArrayList<CircuitUsageModel> modelList = new ArrayList<CircuitUsageModel>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
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
		// sort by aid
		Collections.sort(modelList, new CircuitUsageModelComparator());
		return modelList;
	}		
	
	public void initTab(ArrayList<CircuitUsageModel> modelList) {
		// tab host & spec
		tabHost = getTabHost();
		Resources res = getResources();
		
		// energy
		energySpec = tabHost.newTabSpec("energy");
		energySpec.setIndicator(getString(R.string.energy),
				res.getDrawable(R.drawable.ic_tab_energy));
		// power
		powerSpec = tabHost.newTabSpec("power");
		powerSpec.setIndicator(getString(R.string.power),
				res.getDrawable(R.drawable.ic_tab_power));
		// credit
		creditSpec = tabHost.newTabSpec("credit");
		creditSpec.setIndicator(getString(R.string.credit),
				res.getDrawable(R.drawable.ic_tab_credit));
		
		// set tab content
		setTabContent(modelList);
	}
	
	public void setTabContent(ArrayList<CircuitUsageModel> modelList) {
		energySpec.setContent(buildIntent(modelList, "energy"));
		powerSpec.setContent(buildIntent(modelList, "power"));
		creditSpec.setContent(buildIntent(modelList, "credit"));
		
		tabHost.addTab(energySpec);
		tabHost.addTab(powerSpec);
		tabHost.addTab(creditSpec);
		tabHost.setCurrentTab(currentTab);
	}
	
	public void updateData(ArrayList<CircuitUsageModel> modelList) {
		// dirty fix for android's stupid bug
		tabHost.setVisibility(View.GONE);
		tabHost.setCurrentTab(0);
		
		tabHost.clearAllTabs();
		setTabContent(modelList);
		
		// dirty fix for android's stupid bug
		tabHost.setCurrentTab(currentTab);
		tabHost.setVisibility(View.VISIBLE);
	}
	
	public String[] getLabels(ArrayList<CircuitUsageModel> modelList) {
		String[] labels = new String[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			labels[i] = modelList.get(i).getAid();
		}
		return labels;
	}
	
	public Intent buildIntent(ArrayList<CircuitUsageModel> modelList, String type) {
		ArrayList<double[]> values = new ArrayList<double[]>();
		String[] labels = getLabels(modelList);
		double[] value = new double[modelList.size()];
		for (int i = 0; i < modelList.size(); i++) {
			if (type.equals("energy"))
				value[i] = modelList.get(i).getWh_today();
			else if (type.equals("power"))
				value[i] = modelList.get(i).getWatts();
			else if (type.equals("credit"))
				value[i] = modelList.get(i).getCr();
		}		
		values.add(value);
		if (type.equals("energy"))
			return new EnergyChart(values, labels).execute(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		else if (type.equals("power"))
			return new PowerChart(values, labels).execute(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		else if (type.equals("credit"))
			return new CreditChart(values, labels).execute(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		else
			return null;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chart_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.chartRefreshMenuItem:
			currentTab = tabHost.getCurrentTab();
			Log.d("d", "menu: current tab = " + currentTab);
			progressDialog = ProgressDialog.show(this, "", getString(R.string.loading));
			new Thread() {
    			public void run() {
    				/*
    				jsonString = "[{'aid': 'a', 'watts': '" + (int)(Math.random()*10000)/100
						+ "', 'wh_today': '" + (int)(Math.random()*10000)/100
						+ "', 'cr': '" + (int)(Math.random()*10000)/100 + "'}]";
					*/
					jsonString = new Connector(Chart.this).requestForString(getString(R.string.circuitUsageUrl),
							Chart.this);
					circuitUsageHandler.sendEmptyMessage(0);
    			}
    		}.start();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private Handler circuitUsageHandler = new Handler() {
        public void handleMessage(Message msg) {
        	progressDialog.dismiss();
        	if (jsonString != null) {
        		if (jsonString.equals(String.valueOf(Connector.CONNECTION_TIMEOUT))) {
                    MyUI.showNeutralDialog(Chart.this, R.string.chart,
                    		R.string.circuitUsageTimeoutMsg, R.string.ok);
        		} else {
        			updateData(buildModel());
        		}
        	}
        	else {
                MyUI.showNeutralDialog(Chart.this, R.string.chart,
                		R.string.loadingCircuitUsageError, R.string.ok);
        	}
        }
    };
}
