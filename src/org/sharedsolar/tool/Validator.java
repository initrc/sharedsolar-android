package org.sharedsolar.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

public class Validator {
	public static boolean validate(String url, List<NameValuePair> entity) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(entity));
			HttpResponse response = httpClient.execute(httpPost);
			String status = response.getStatusLine().toString();
			Log.d("d", status);
			if (status.equals("HTTP/1.1 200 OK")) {
				return true;
			} else {
				return false;
				// HTTP/1.1 500 Internal Server Error
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

	public static boolean validate(String url, Context context) {
		return validate(url, Device.getIdEntity(context));
	}
}
