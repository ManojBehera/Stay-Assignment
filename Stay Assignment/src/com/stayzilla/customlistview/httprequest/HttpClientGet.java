
package com.stayzilla.customlistview.httprequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.stayzilla.customlistview.model.Model;

import android.os.AsyncTask;
import android.util.Log;


public class HttpClientGet extends AsyncTask<String, Void, String> {
	private HttpClient mClient;
	private HttpGet mGet;
	private HttpParams mHttpParams;
	private HttpResponse mResponse;
	private String response;
	private static String Tag = HttpClientGet.class.getName();

	@SuppressWarnings("deprecation")
	@Override
	protected String doInBackground(String... params) {
		mHttpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(mHttpParams,
				Model.getSingleton().CONNECTION_TIMEOUT * 1000);


		mClient = new DefaultHttpClient(mHttpParams);

		mGet = new HttpGet(params[0]);
		try {
			
			mResponse = mClient.execute(mGet);

			int status = mResponse.getStatusLine().getStatusCode();
			if (status == 200 || status == 201) {
				response = convertStreamToString(mResponse.getEntity()
						.getContent());
			} else {
				response = null;
			}

		} catch (Exception e) {
			Log.e(Tag, e.getMessage());
		}

		return response;
	}

	/**
	 * Convert data from url into a string
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				// System.gc();
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			Log.e(Tag, e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e(Tag, e.getMessage());
			}
		}
		return sb.toString();
	}
}
