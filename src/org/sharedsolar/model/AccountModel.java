package org.sharedsolar.model;

import java.text.DecimalFormat;

public class AccountModel {
	private String aid;
	private String cid;
	private int cr;
	private boolean status;
	
	public AccountModel(String aid, String cid, int cr, boolean status) {
		this.aid = aid;
		this.cid = cid;
		this.cr = cr;
		this.status = status;
	}
	
	public AccountModel(String aid, String cid, int cr) {
		this.aid = aid;
		this.cid = cid;
		this.cr = cr;
	}
	
	public String getAid() {
		return aid;
	}
	
	public String getCid() {
		return cid;
	}

	public int getCr() {
		return cr;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public String getCrString() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(cr/100.00);
	}
	
	public static String crIntToString(int cr) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(cr/100.00);
	}
}
