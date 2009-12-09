package org.villane.shttpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
		String enc = declaredCharSet();
		return enc != null ? asText(enc) : asText(DefaultEncoding);
	}

	public Document asDomXml() throws IOException,
			ParserConfigurationException, SAXException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(response.getEntity().getContent());
		response.getEntity().consumeContent();
		return doc;
	}

	public Document asDomHtml() throws IOException,
			ParserConfigurationException, SAXException {
		DOMParser parser = new DOMParser();
		parser.setProperty(
				"http://apache.org/xml/properties/dom/document-class-name",
				"org.apache.html.dom.HTMLDocumentImpl");
		parser.parse(new InputSource(response.getEntity().getContent()));
		response.getEntity().consumeContent();
		return parser.getDocument();
	}

	public String declaredCharSet() {
		return EntityUtils.getContentCharSet(response.getEntity());
	}

	public void consume() throws IOException {
		response.getEntity().consumeContent();
	}

}
