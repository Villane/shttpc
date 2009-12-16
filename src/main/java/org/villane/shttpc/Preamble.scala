package org.villane.shttpc

import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.apache.http.HttpResponse
import java.io.ByteArrayOutputStream
import scala.xml.NodeSeq

object Preamble {
  class RichHttpResponse(val response: SimpleHttpResponse) {
    def asXml = {
      val xml = scala.xml.XML.load(response.asInputStream)
      response.consume
      xml
    }

    def asHtml = {
      val xml = NekoHTML.load(response.asInputStream)
      response.consume
      xml
    }

    def header(name: String) = response.response.getFirstHeader(name) match {
      case null => None
      case h => Some(h)
    }

    def getRedirect = header("Location") match {
      case Some(loc) => Some((response.statusCode, loc.getValue))
      case None => None
    }
  }

  implicit def richHttpResponse(r: SimpleHttpResponse) = new RichHttpResponse(r)

  implicit def scala2javaMap[K,V](sm: Map[K,V]): java.util.Map[K,V] = {
    val jm = new java.util.HashMap[K,V]
    for ((k,v) <- sm) jm.put(k,v)
    jm
  }

  implicit def scalaMap2nameValuePair(sm: Map[String,String]): java.util.List[NameValuePair] = {
    val nvp = new java.util.ArrayList[NameValuePair]
    for ((k,v) <- sm) nvp.add(new BasicNameValuePair(k,v))
    nvp
  }

  implicit def site2url(site: Site) = site.url
  implicit def url2site(url: String) = Site(url)

  implicit def betterXPath(node: NodeSeq) = BetterXPath.betterXPath(node)

}
