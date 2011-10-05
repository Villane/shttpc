package org.villane.shttpc;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * Created simply to set the SSL Socket Factory Hostname Verifier to ALLOW_ALL.
 * 
 * @author erkki.lindpere
 */
public class TrustingHttpClient extends DefaultHttpClient {

	public static final class BlindTrustStrategy implements TrustStrategy {

		@Override
		public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			return true;
		}

	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		try {
			SSLSocketFactory naiveSocketFactory = new SSLSocketFactory(new BlindTrustStrategy(),
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			registry.register(new Scheme("https", 443, naiveSocketFactory));
		} catch (Exception e) {
			throw new RuntimeException("Unable to create naive SSLSocketFactory", e);
		}

		ClientConnectionManager connManager = null;
		HttpParams params = getParams();

		ClientConnectionManagerFactory factory = null;

		String className = (String) params.getParameter(ClientPNames.CONNECTION_MANAGER_FACTORY_CLASS_NAME);
		if (className != null) {
			try {
				Class<?> clazz = Class.forName(className);
				factory = (ClientConnectionManagerFactory) clazz.newInstance();
			} catch (ClassNotFoundException ex) {
				throw new IllegalStateException("Invalid class name: " + className);
			} catch (IllegalAccessException ex) {
				throw new IllegalAccessError(ex.getMessage());
			} catch (InstantiationException ex) {
				throw new InstantiationError(ex.getMessage());
			}
		}
		if (factory != null) {
			connManager = factory.newInstance(params, registry);
		} else {
			connManager = new SingleClientConnManager(registry);
		}

		return connManager;
	}

}
