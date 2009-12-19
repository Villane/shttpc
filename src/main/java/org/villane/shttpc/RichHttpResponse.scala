package org.villane.shttpc

import xml._
import xml.factory.XMLLoader
import net.liftweb.json._

/**
 * Adds convenience methods to SimpleHttpResponse. Should be used only via implicit conversions
 */
class RichHttpResponse(val response: SimpleHttpResponse) {
	def asXml: Elem = asXml(XML)
	def asXml[T <: Node](loader: XMLLoader[T]) = {
		val xml = loader.load(response.asInputStream)
		response.consume
		xml
	}

	def asHtml: Elem = asHtml(DefaultHtmlLoader)
	def asHtml[T <: Node](loader: XMLLoader[T]) = {
		val xml = loader.load(response.asInputStream)
		response.consume
		xml
    }

	def asJson: JsonAST.JValue = JsonParser.parse(response.asText)

	def header(name: String) = response.response.getFirstHeader(name) match {
      case null => None
      case h => Some(h)
    }

    def getRedirect = header("Location") match {
      case Some(loc) => Some((response.statusCode, loc.getValue))
      case None => None
    }
}
