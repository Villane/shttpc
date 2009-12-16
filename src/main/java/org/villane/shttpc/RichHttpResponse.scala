package org.villane.shttpc

import xml._
import xml.factory.XMLLoader

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
}
