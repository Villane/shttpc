package org.villane.shttpc;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * Created simply to set the SSL Socket Factory Hostname Verifier to ALLOW_ALL.
 * 
 * @author erkki.lindpere
 */
public class TrustingHttpClient extends DefaultHttpClient {

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory
				.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", socketFactory, 443));

		ClientConnectionManager connManager = null;
		HttpParams params = getParams();

		ClientConnectionManagerFactory factory = null;

		String className = (String) params
				.getParameter(ClientPNames.CONNECTION_MANAGER_FACTORY_CLASS_NAME);
		if (className != null) {
			try {
				Class<?> clazz = Class.forName(className);
				factory = (ClientConnectionManagerFactory) clazz.newInstance();
			} catch (ClassNotFoundException ex) {
				throw new IllegalStateException("Invalid class name: "
						+ className);
			} catch (IllegalAccessException ex) {
				throw new IllegalAccessError(ex.getMessage());
			} catch (InstantiationException ex) {
				throw new InstantiationError(ex.getMessage());
			}
		}
		if (factory != null) {
			connManager = factory.newInstance(params, registry);
		} else {
			connManager = new SingleClientConnManager(getParams(), registry);
		}

		return connManager;
	}

}
