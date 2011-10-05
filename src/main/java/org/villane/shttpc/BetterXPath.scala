package org.villane.shttpc

import xml._

object BetterXPath {
  implicit def betterXPath(node: NodeSeq) = new BetterXPath(node)
}

/**
 * Supports a subset of XPath expressions in the form of
 *
 * /tag1/tag2[@attr='stringLiteral']/tag2
 * /tag1/tag2[tag4=@attr2]/tag2
 */
class BetterXPath(node: NodeSeq) {
  import BetterXPath._

  def /(path: String): NodeSeq = {
    if (path.contains("/")) {
      val segs = path.split("/")
      var n = node
      for (seg <- segs) n /= seg
      n
    } else if (path.contains("[")) {
      val segs = path.split("[\\[\\]]")
      val ns = node \ segs(0)
      val Array(left, right) = segs(1).split("=")
      ns filter { n =>
        evaluatePredicate(n, left) == evaluatePredicate(n, right)
      }
    } else {
      node \ path
    }
  }

  private def evaluatePredicate(n: NodeSeq, expr: String) = {
    if (expr.startsWith("'") && expr.endsWith("'")) {
      // string literal
      expr.substring(1, expr.length - 1)
    } else {
      // usual attribute or element name
      n \ expr
    }
  }
}
