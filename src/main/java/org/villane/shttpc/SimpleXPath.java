package org.villane.shttpc;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimpleXPath {
  private static final XPathFactory factory = XPathFactory.newInstance();
  private static final XPath xp = factory.newXPath();

  public static IterableNodeList evalNodeList(Node node, String xPath) throws XPathExpressionException {
    return new IterableNodeList((NodeList) xp.evaluate(xPath, node, XPathConstants.NODESET));
  }

  public static Node evalNode(Node node, String xPath) throws XPathExpressionException {
    return (Node) xp.evaluate(xPath, node, XPathConstants.NODE);
  }

  public static String evalString(Node node, String xPath) throws XPathExpressionException {
    return (String) xp.evaluate(xPath, node, XPathConstants.STRING);
  }

  public static Double evalNumber(Node node, String xPath) throws XPathExpressionException {
    return (Double) xp.evaluate(xPath, node, XPathConstants.NUMBER);
  }

  public static Boolean evalBoolean(Node node, String xPath) throws XPathExpressionException {
    return (Boolean) xp.evaluate(xPath, node, XPathConstants.BOOLEAN);
  }
}
