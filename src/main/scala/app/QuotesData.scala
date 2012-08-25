package app

import io.Source
import java.text.SimpleDateFormat

object QuotesData {

  def loadFromFile(symbol: String) = {
    // TODO: Change to use relative path
    val path = "/home/agassner/workspace/github/quotes/python/ftse_data/" + symbol + ".txt"
    Source.fromFile(path).getLines()
  }

  def build(data: Iterator[String]) = {
    data.map(buildQuote).toList
  }

  def buildQuote(values: String) = {
    // TODO: It can be improved
    val listOfValues = values.split(",")
    new Quote(listOfValues(0), new SimpleDateFormat("YYYYMMdd").parse(listOfValues(1)), BigDecimal(listOfValues(2)), BigDecimal(listOfValues(3)), BigDecimal(listOfValues(4)), BigDecimal(listOfValues(5)), BigDecimal(listOfValues(6)))
  }

  def addToDatabase(quotes: List[Quote]) {

  }

}
