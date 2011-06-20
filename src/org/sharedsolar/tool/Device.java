package org.sharedsolar.tool;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.provider.Settings.Secure;
import android.provider.Settings.System;

public class Device {

	public static String getId(Context context) {
		String androidId = System.getString(context.getContentResolver(), Secure.ANDROID_ID);
		if (androidId == null)
			//      12345678
			return "00000000";
		return convert(androidId);
	}
	
	public static NameValuePair getIdPair(Context context) {
		return new BasicNameValuePair("vendordevice_id", Device.getId(context));
	}
	
	public static List<NameValuePair> getIdEntity(Context context) {
		List<NameValuePair> entity = new ArrayList<NameValuePair>(1);
		entity.add(Device.getIdPair(context));
		return entity;
	}
	
	public static HttpEntity getIdHttpEntity(Context context) {
		try {
			return new UrlEncodedFormEntity(getIdEntity(context));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String convert(String s) {
		char[] cs = new char[s.length() / 2];
		for (int i = 0 ; i < cs.length; i++) {
			int c = (int)(s.charAt(i * 2) + s.charAt(i * 2 +1 )) % 16;
			char base = c < 10 ? '0' : 'a' - 10;
			cs[i] = (char)(base + c);
		}
		return String.valueOf(cs);
	}
	
	public static String generatePassword() {
		char[] cs = new char[6];
		for (int i = 0; i < cs.length; i++) {
			int t;
			if (i < cs.length / 2)
				t = (int)(Math.random()*26 + 'a');
			else
				t = (int)(Math.random()*10 + '0');
			cs[i] = (char)t;
		}
		return String.valueOf(cs);
	}
}
