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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sharedsolar.R;
import org.sharedsolar.db.DatabaseAdapter;
import org.sharedsolar.model.CreditSummaryModel;

import android.content.Context;

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
		return CONNECTION_TIMEOUT;
	}
	
	public int sendDeviceId(String url, Context context) {
		return connect(url, Device.getIdHttpEntity(context));
	}
	
	// vendor add credit
	// vendor token validation
	public HttpEntity getAccountEntity(Context context, String aid, String cid, int cr) {
		DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		dbAdapter.open();
		nameValuePairs.add(Device.getIdPair(context));
		nameValuePairs.add(new BasicNameValuePair("aid", aid));
		nameValuePairs.add(new BasicNameValuePair("cid", cid));
		nameValuePairs.add(new BasicNameValuePair("cr", String.valueOf(cr)));
		dbAdapter.close();
		try {
			return new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int vendorAddCredit(String url, Context context, String aid, String cid, int cr) {
		return connect(url, getAccountEntity(context, aid, cid, cr));
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
			// Log.d("d", status + " from " + url);
			if (status.equals("HTTP/1.1 200 OK")) {
				InputStream is = response.getEntity().getContent();
				String s = new Scanner(is).useDelimiter("\\A").next();
				return s;
			} else {
				// TODO DEBUG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				return status;
				// return null;
				// HTTP/1.1 401 Internal Server Error
			}
		} catch (SocketTimeoutException e) {
			return String.valueOf(CONNECTION_TIMEOUT);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	// get account list
	public String requestForString(String url, Context context) {
		return requestForString(url, Device.getIdHttpEntity(context));
	}
	
	// get tokens
	public String requestToken(String url, Context context, ArrayList<CreditSummaryModel> modelList) {
		JSONObject json = new JSONObject();
		HttpEntity entity = null;
		try {
			json.put("device_id", Device.getId(context));
			JSONArray tokenArray = new JSONArray();
			for (CreditSummaryModel model : modelList) {
				JSONObject tokenObject = new JSONObject();
				tokenObject.put("denomination", model.getDenomination());
				tokenObject.put("count", model.getCount());
				tokenArray.put(tokenObject);
			}
			json.put("tokens", tokenArray);
			entity = new ByteArrayEntity(json.toString().getBytes("UTF8"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestForString(url, entity);
	}
	
	// upload tokens
	public int uploadToken(String url, JSONObject json) {
		HttpEntity entity = null;
		try {
			entity = new ByteArrayEntity(json.toString().getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect(url, entity);
	}
}
