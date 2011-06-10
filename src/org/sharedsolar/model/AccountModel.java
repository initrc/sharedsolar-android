package org.sharedsolar.model;

public class AccountModel {
	private String aid;
	private String cid;
	private int cr;
	
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
}
