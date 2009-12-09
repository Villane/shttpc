package org.villane.shttpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class SimpleHttpResponse {
	public static final String DefaultEncoding = "UTF-8";
	public final HttpResponse response;

	public SimpleHttpResponse(HttpResponse response) {
		this.response = response;
	}

	public InputStream asInputStream() throws IOException {
		return response.getEntity().getContent();
	}

	public byte[] asBytes() throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		response.getEntity().writeTo(buf);
		response.getEntity().consumeContent();
		return buf.toByteArray();
	}

	public String asText(String encoding) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		response.getEntity().writeTo(buf);
		response.getEntity().consumeContent();
		return buf.toString(encoding);
	}

	public String asText() throws IOException {
		String enc = declaredEncoding();
		return enc != null ? asText(enc) : asText(DefaultEncoding);
	}

	public String declaredEncoding() {
		Header cTypeH = response.getEntity().getContentType();
		for (HeaderElement elem : cTypeH.getElements()) {
			for (NameValuePair param : elem.getParameters()) {
				if ("charset".equals(param.getName())) {
					return param.getValue();
				}
			}
		}
		return null;
	}

	public void consume() throws IOException {
		response.getEntity().consumeContent();
	}

}
