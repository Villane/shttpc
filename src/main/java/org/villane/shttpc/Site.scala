package org.villane.shttpc

case class Site(url: String) {
  val DefaultURIEncoding = "UTF-8"

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
      sb.append(encode(name))
      sb.append("=")
      sb.append(if (value != null) encode(value) else "")
    }
    sb.toString
  }

  private def encode(s: String) = java.net.URLEncoder.encode(s, DefaultURIEncoding)

  override def toString = url
}
