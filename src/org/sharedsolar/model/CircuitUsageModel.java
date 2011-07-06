package org.sharedsolar.model;

public class CircuitUsageModel {
	private String aid;
	private double watts;
	private double wh_today;
	private double cr;
	
	public CircuitUsageModel(String aid, double watts, double wh_today, double cr) {
		this.aid = aid;
		this.watts = watts;
		this.wh_today = wh_today;
		this.cr = cr;
	}
	
	public String getAid() {
		return aid;
	}
	public double getWatts() {
		return watts;
	}
	public double getWh_today() {
		return wh_today;
	}
	public double getCr() {
		return cr;
	}
}
