package org.villane.shttpc

case class Site(url: String) {

  def /(path: String) = {
    val sb = new StringBuilder(url)
    if (!url.endsWith("/") && !path.startsWith("/")) sb.append("/")
    sb.append(path)
    Site(sb.toString)
  }

  def ?(params: String) = url + "?" + params

  def ?(params: Map[String, String]) = {
    val sb = new StringBuilder(url)
    if (!params.isEmpty) { sb.append("?") }
    var first = true
    for ((name, value) <- params) {
      if (first) first = false else sb.append("&")
      sb.append(Http.uriEncode(name))
      sb.append("=")
      sb.append(if (value != null) Http.uriEncode(value) else "")
    }
    sb.toString
  }

  override def toString = url
}
