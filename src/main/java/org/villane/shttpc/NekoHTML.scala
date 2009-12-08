package org.villane.shttpc

import scala.xml._
import scala.xml.parsing._
import org.xml.sax.InputSource
import org.cyberneko.html.parsers.SAXParser

object NekoHTML extends NoBindingFactoryAdapter {

  override def loadXML(source: InputSource): Elem = {
    val parser = new SAXParser
    scopeStack.push(TopScope)
    parser.setContentHandler(this)
    parser.setEntityResolver(this)
    parser.setErrorHandler(this)
    parser.setDTDHandler(this)
    parser.parse(source.asInstanceOf[org.xml.sax.InputSource])
    scopeStack.pop
    rootElem.asInstanceOf[Elem]
  }

}
