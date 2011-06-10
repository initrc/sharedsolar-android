package org.sharedsolar.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.R;
import org.sharedsolar.db.DatabaseAdapter;

import android.content.Context;
import android.util.Log;

public class Connector {
	
	public final static int CONNECTION_SUCCESS = 1;
	public final static int CONNECTION_FAILURE = 0;
	public final static int CONNECTION_TIMEOUT = -1;
	
	private final int TIMEOUT;
	private final int SOCKET_TIMEOUT;
	
	public Connector(Context context) {
		TIMEOUT = Integer.parseInt(context.getString(R.string.timeoutValue).toString());
		SOCKET_TIMEOUT = TIMEOUT; 
	}
	
	// basic http connect, return http code	
	public int connect(String url, HttpEntity entity) {
		// set timeout
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
		// http post
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			String status = response.getStatusLine().toString();
			if (status.equals("HTTP/1.1 200 OK")) {
				return CONNECTION_SUCCESS;
			} else {
				return CONNECTION_FAILURE;
				// HTTP/1.1 401 Internal Server Error
			}
		} catch (SocketTimeoutException e) {
			return CONNECTION_TIMEOUT;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// vendor token validation
	public HttpEntity getVendorEntity(Context context) {
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		dbAdapter.open();
		nameValuePairs.add(new BasicNameValuePair("vendordevice_id", dbAdapter
				.getVendorToken()));
		dbAdapter.close();
		try {
			return new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int sendVendorToken(String url, Context context) {
		return connect(url, getVendorEntity(context));
	}
	
	// vendor add credit
	public ByteArrayEntity getAccountEntity(String aid, String cid, int cr) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("aid", aid);
			jsonObject.accumulate("cid", cid);
			jsonObject.accumulate("cr", cr);
			Log.d("d", jsonObject.toString());
			return new ByteArrayEntity(jsonObject.toString().getBytes("UTF8"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	public int vendorAddCredit(String url, String aid, String cid, int cr) {
		return connect(url, getAccountEntity(aid, cid, cr));
	}
	
	// http connection, return json string
	public String requestForString(String url, HttpEntity entity) {
		// set timeout
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
		// http post
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(entity);
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
		} catch (SocketTimeoutException e) {
			return String.valueOf(CONNECTION_TIMEOUT);
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
	
	// get account list
	public String requestAccountList(String url, Context context) {
		return requestForString(url, getVendorEntity(context));
	}
}
