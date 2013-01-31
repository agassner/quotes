package app

import io.Source
import java.text.SimpleDateFormat
import java.nio.file.{Paths, Files}

object QuotesData {

  def loadConstituentsFromFile() = {
    // TODO: Change to use relative path
    val path = "/home/agassner/workspace/github/quotes/python/ftse_data/ftse_constituents.txt"
    Source.fromFile(path).getLines().toList
  }

  def loadFromFile(symbol: String) = {
    // TODO: Change to use relative path
    val path = "/home/agassner/workspace/github/quotes/python/ftse_data/" + symbol + ".txt"
    if (Files.exists(Paths.get(path))) {
      Source.fromFile(path).getLines().toList
    } else {
      Nil
    }
  }

  def buildListOfQuotes(data: List[String]) = {
    data.map(buildQuote).toList
  }

  def buildQuote(values: String) = {
    val listOfValues = values.split(",")
    Quote(
      listOfValues(0),
      new SimpleDateFormat("yyyyMMdd").parse(listOfValues(1)),
      BigDecimal(listOfValues(2)),
      BigDecimal(listOfValues(3)),
      BigDecimal(listOfValues(4)),
      BigDecimal(listOfValues(5)),
      BigDecimal(listOfValues(6))
    )
  }

}
