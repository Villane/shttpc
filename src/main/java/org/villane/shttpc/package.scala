package org.villane

import xml._
import xml.factory.XMLLoader
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair

package object shttpc {
  var DefaultHtmlLoader: XMLLoader[Elem] = NekoHTML

  implicit def richHttpResponse(r: SimpleHttpResponse) = new RichHttpResponse(r)

  implicit def scala2javaMap[K, V](sm: Map[K, V]): java.util.Map[K, V] = {
    val jm = new java.util.HashMap[K, V]
    for ((k, v) <- sm) jm.put(k, v)
    jm
  }

  implicit def scalaMap2nameValuePair(sm: Map[String, String]): java.util.List[NameValuePair] = {
    val nvp = new java.util.ArrayList[NameValuePair]
    for ((k, v) <- sm) nvp.add(new BasicNameValuePair(k, v))
    nvp
  }

  implicit def site2url(site: Site) = site.url
  implicit def url2site(url: String) = Site(url)

  implicit def betterXPath(node: NodeSeq) = BetterXPath.betterXPath(node)

}
