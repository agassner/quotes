package app

import java.util.Date
import org.squeryl.Schema
import org.squeryl.annotations.Column
import org.squeryl.PrimitiveTypeMode._
import math.BigDecimal.RoundingMode._

class Quote(val symbol: String,
            val datetime: Date,
            @Column(length = 20, scale = 2)
            val open: BigDecimal,
            @Column(length = 20, scale = 2)
            val high: BigDecimal,
            @Column(length = 20, scale = 2)
            val low: BigDecimal,
            @Column(length = 20, scale = 2)
            val close: BigDecimal,
            @Column(length = 20, scale = 2)
            val volume: BigDecimal,
            val indicators: Map[String, BigDecimal] = Map()) {

  def this() = this("", new Date(), 0, 0, 0, 0, 0)

  def this(quote: Quote, indicators: Map[String, BigDecimal]) = {
    this(quote.symbol, quote.datetime, quote.open, quote.high, quote.low, quote.close, quote.volume, indicators)
  }

  override def toString = {
    Array(symbol, datetime, open, high, low, close, volume, indicators).mkString(",")
  }
}

object QuoteDb extends Schema {

  val quotes = table[Quote]

  def listAll() = {
    transaction {
      from(quotes)(s => select(s)).toList
    }
  }

  def listAllBySymbol(symbol: String) = {
    transaction {
      from(quotes)(s =>
        where(s.symbol === symbol)
          select (s)
          orderBy (s.datetime desc)).toList
    }
  }

}

object Quote {

  // Too generic for now
  def meanBy[N, T](quotes: Traversable[T])(ext: (T) => N)(implicit n: Numeric[N]) = {
    quotes.foldLeft(0.0d)((total, item) => total + n.toDouble(ext(item))) / quotes.size
  }

  def sma(quotes: List[Quote], days: Int): List[Quote] = {
    if (quotes.size < days) {
      quotes
    } else {
      val closePrices = quotes.take(days).map(_.close)
      val mean = (closePrices.sum / days).setScale(2, HALF_DOWN)
      val quote = quotes.head
      new Quote(quote, quote.indicators + ("sma" + days -> mean)) :: sma(quotes.drop(1), days)
    }
  }

}