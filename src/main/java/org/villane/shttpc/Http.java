package org.villane.shttpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * Simplifies the use of Apache HTTP Client.
 * 
 * @author erkki.lindpere
 */
public class Http {
	public final HttpClient client;
	public static String postEncoding = "UTF-8";

	public Http() {
		this.client = new DefaultHttpClient();
	}

	public Http(HttpClient client) {
		this.client = client;
	}

	public static Http trustingInstace() {
		return new Http(new TrustingHttpClient());
	}

	public HttpResponse get(String uri) throws ClientProtocolException,
			IOException {
		HttpGet get = new HttpGet(uri);
		return client.execute(get);
	}

	public HttpResponse post(String uri, Map<String, String> params)
			throws ClientProtocolException, IOException {
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : params.entrySet()) {
			paramsList.add(new BasicNameValuePair(param.getKey(), param
					.getValue()));
		}
		HttpPost post = new HttpPost(uri);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramsList,
				postEncoding);
		post.setEntity(entity);
		return client.execute(post);
	}
}
