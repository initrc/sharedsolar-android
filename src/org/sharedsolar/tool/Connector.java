package org.sharedsolar.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.sharedsolar.db.DatabaseAdapter;

import android.content.Context;

public class Connector {
	public static boolean validate(String url, List<NameValuePair> entity) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(entity));
			HttpResponse response = httpClient.execute(httpPost);
			String status = response.getStatusLine().toString();
			if (status.equals("HTTP/1.1 200 OK")) {
				return true;
			} else {
				return false;
				// HTTP/1.1 401 Internal Server Error
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static String requestForString(String url, List<NameValuePair> entity) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(entity));
			HttpResponse response = httpClient.execute(httpPost);
			String status = response.getStatusLine().toString();
			if (status.equals("HTTP/1.1 200 OK")) {
				InputStream is = response.getEntity().getContent();
				String s = new Scanner(is).useDelimiter("\\A").next();
				return s;
			} else {
				return null;
				// HTTP/1.1 401 Internal Server Error
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String requestAccountList(String url, Context context) {
		return requestForString(url, getVendorEntity(context));
	}

	public static boolean sendVendorToken(String url, Context context) {
		return validate(url, getVendorEntity(context));
	}

	public static List<NameValuePair> getVendorEntity(Context context) {
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		dbAdapter.open();
		nameValuePairs.add(new BasicNameValuePair("vendordevice_id", dbAdapter
				.getVendorToken()));
		dbAdapter.close();
		return nameValuePairs;
	}
}
