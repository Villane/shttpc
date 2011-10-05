package org.villane.shttpc

import xml._
import xml.factory._
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl

object TagSoup extends XMLLoader[Elem] {
  private val saxFactory = new SAXFactoryImpl

  override def parser = saxFactory.newSAXParser
}
