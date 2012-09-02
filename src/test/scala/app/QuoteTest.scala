package app

import org.scalatest.FunSuite
import java.util.{NoSuchElementException, Date}
import Quote._

class QuoteTest extends FunSuite {

  test("Calculate the average closing price of a list of quotes") {
    val quotes = List(
      new Quote("ARM.L", new Date(), 0, 0, 0, 580, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 590, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 600, 0))
    val mean = meanBy(quotes)(_.close)
    assert(mean == 590)
  }

  test("Calculate the average closing price of a list of quotes - indicators version") {
    val quotes = List(
      new Quote("ARM.L", new Date(), 0, 0, 0, 580, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 590, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 585, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 592, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 591, 0),
      new Quote("ARM.L", new Date(), 0, 0, 0, 600, 0))
    val quotesSma3 = sma(quotes, 3)
    assert(quotesSma3.size === 6)
    assert(quotesSma3(0).indicators("sma3") === 585)
    assert(quotesSma3(1).indicators("sma3") === 589)
    assert(quotesSma3(2).indicators("sma3") === 589.33)
    assert(quotesSma3(3).indicators("sma3") === 594.33)
    checkForIndicatorInMap(quotesSma3, 4, "sma3")
    checkForIndicatorInMap(quotesSma3, 5, "sma3")
  }

  def checkForIndicatorInMap(quotes: List[Quote], index: Int, indicator: String) {
    try {
      quotes(index).indicators(indicator)
      fail("It should not have '" + indicator + "' in the indicators map quote index " + index)
    } catch {
      case ex: NoSuchElementException => None
    }
  }

}
