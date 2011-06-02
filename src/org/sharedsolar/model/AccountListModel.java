package org.sharedsolar.model;

public class AccountListModel {
	private String aid;
	private double cr;
	
	public AccountListModel(String aid, double cr) {
		this.aid = aid;
		this.cr = cr;
	}
	
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public double getCr() {
		return cr;
	}
	public void setCr(double credit) {
		this.cr = credit;
	}
}
