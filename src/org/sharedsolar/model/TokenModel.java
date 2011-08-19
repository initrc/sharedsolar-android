package org.sharedsolar.model;

import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TokenModel {
	private String aid;
	private int denomination;
	private Date timestamp;
	
	public TokenModel(Date timestamp, String aid, int denomination) {
		this.aid = aid;
		this.denomination = denomination;
		this.timestamp = timestamp;
	}
	
	public TokenModel(String timestamp, String aid, int denomination) {
		this.aid = aid;
		this.denomination = denomination;		
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			this.timestamp = df.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.timestamp = new Date();
		}
	}

	public String getAid() {
		return aid;
	}

	public int getDenomination() {
		return denomination;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getLocalTimestampString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		df.setTimeZone(TimeZone.getDefault());
		return df.format(timestamp);
	}
}
