package it.unibo

import scala.language.implicitConversions
import scribe._
import scribe.file._
package object scripting {
  scribe.Logger.root
    .withHandler(writer = FileWriter("logs" / ("app-" % year % "-" % month % "-" % day % ".log"))).replace()
    .clearHandlers()
  implicit class Unsafe(elem: Any) {
    def as[E] = elem.asInstanceOf[E]
  }
}
