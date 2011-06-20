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
			//      1234567890123456
			return "0000000000000000";
		return androidId;
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
}
