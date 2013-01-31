package app

import DbUtils._
import QuoteDb._
import Quote._

object QuotesApp extends App {

  initialiseDatabaseConnection()
//  setupDatabaseSchema()
//  loadDatabase()

  val quotes = listAllBySymbol("ARM.L")
  val quotesSma5 = sma(quotes, 5)
  val quotesSma5And20 = sma(quotesSma5, 20)
  println(quotesSma5And20)

}
