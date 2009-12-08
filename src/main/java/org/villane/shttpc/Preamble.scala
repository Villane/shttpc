package org.villane.shttpc

import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.apache.http.HttpResponse
import java.io.ByteArrayOutputStream

object Preamble {
  val DefaultEncoding = "UTF-8"

  class HttpResponseExtentions(val r: HttpResponse) {

    def getBytes = {
      val buf = new ByteArrayOutputStream
      r.getEntity writeTo buf
      r.getEntity.consumeContent
      buf.toByteArray
    }

    def getText(encoding: String): String = new String(getBytes, encoding)

    def getText: String = declaredEncoding match {
      case Some(encoding) => getText(encoding)
      case None => getText(DefaultEncoding)
    }

    def declaredEncoding = {
      val cTypeH = r.getEntity.getContentType
      if (cTypeH != null)
        ";charset=(.*)".r findFirstMatchIn cTypeH.getValue map (_.group(1))
      else
        None
    }

    def getXml = {
      val xml = scala.xml.XML.load(r.getEntity.getContent)
      consume
      xml
    }

    def getHtml = {
      val xml = NekoHTML.load(r.getEntity.getContent)
      consume
      xml
    }

    def consume = r.getEntity.consumeContent

  }

  implicit def httpResponseExtentions(r: HttpResponse) = new HttpResponseExtentions(r)

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

}
