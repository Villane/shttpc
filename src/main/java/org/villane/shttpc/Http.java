package org.villane.shttpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.helpers.MessageFormatter;

/**
 * Simplifies the use of Apache HTTP Client.
 * 
 * @author erkki.lindpere
 */
public class Http {
	public static String DefaultPostEncoding = "UTF-8";
	public static String DefaultURIEncoding = "UTF-8";

	public final HttpClient client;

	public Http() {
		this.client = new DefaultHttpClient();
	}

	public Http(HttpClient client) {
		this.client = client;
	}

	public static Http newTrustingInstace() {
		return new Http(new TrustingHttpClient());
	}

	public SimpleHttpResponse get(String uri) throws ClientProtocolException,
			IOException {
		HttpGet get = new HttpGet(uri);
		return new SimpleHttpResponse(client.execute(get));
	}

	public SimpleHttpResponse get(String uri, Object... params)
			throws ClientProtocolException, IOException {
		return get(formatURI(uri, params));
	}

	public SimpleHttpResponse post(String uri, Object[] uriParams,
			Map<String, String> postParams) throws ClientProtocolException,
			IOException {
		return post(formatURI(uri, uriParams), postParams);
	}

	public SimpleHttpResponse post(String uri, Map<String, String> postParams)
			throws ClientProtocolException, IOException {
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> param : postParams.entrySet()) {
			paramsList.add(new BasicNameValuePair(param.getKey(), param
					.getValue()));
		}
		HttpPost post = new HttpPost(uri);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramsList,
				DefaultPostEncoding);
		post.setEntity(entity);
		return new SimpleHttpResponse(client.execute(post));
	}

	public SimpleHttpResponse delete(String uri)
			throws ClientProtocolException, IOException {
		HttpDelete del = new HttpDelete(uri);
		return new SimpleHttpResponse(client.execute(del));
	}

	public SimpleHttpResponse delete(String uri, Object... params)
			throws ClientProtocolException, IOException {
		return delete(formatURI(uri, params));
	}

	protected static String formatURI(String uri, Object... params) {
		if (params.length > 0) {
			String[] encodedParams = new String[params.length];
			for (int i = 0; i < params.length; i++) {
				encodedParams[i] = uriEncode(params[i].toString());
			}
			return MessageFormatter.arrayFormat(uri, encodedParams).getMessage();
		}
		return uri;
	}

	@SuppressWarnings("deprecation")
	protected static String uriEncode(String value) {
		try {
			return URLEncoder.encode(value, DefaultURIEncoding);
		} catch (UnsupportedEncodingException e) {
			return URLEncoder.encode(value);
		}
	}
}
