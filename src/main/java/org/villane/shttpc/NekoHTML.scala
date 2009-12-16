package org.villane.shttpc

import xml._
import xml.factory._
import org.xml.sax.InputSource

object NekoHTML extends XMLLoader[Elem] {

  def nekoParser = {
	  val parser = new org.cyberneko.html.parsers.SAXParser
	  parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower")
	  parser
  }

  /**
   * Note: Ignores the second argument.
   */
  override def loadXML(source: InputSource, parser: javax.xml.parsers.SAXParser): Elem = {
    val newAdapter = adapter
    val p = nekoParser

    newAdapter.scopeStack push TopScope
    p.setContentHandler(newAdapter)
    p.setEntityResolver(newAdapter)
    p.setErrorHandler(newAdapter)
    p.setDTDHandler(newAdapter)
    p.parse(source)
    newAdapter.scopeStack.pop
    
    newAdapter.rootElem.asInstanceOf[Elem]
  }

}
