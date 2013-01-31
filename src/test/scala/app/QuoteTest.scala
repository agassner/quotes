package app

import org.scalatest.FunSuite
import java.util.{NoSuchElementException, Date}
import Quote._

class QuoteTest extends FunSuite {

  test("Calculate the average closing price of a list of quotes") {
    val quotes = List(
      Quote("ARM.L", new Date(), 0, 0, 0, 580, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 590, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 600, 0))

    val mean = meanBy(quotes)(_.close)

    assert(mean == 590)
  }

  test("Calculate the average closing price of a list of quotes - indicators version") {
    val quotes = List(
      Quote("ARM.L", new Date(), 0, 0, 0, 580, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 590, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 585, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 592, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 591, 0),
      Quote("ARM.L", new Date(), 0, 0, 0, 600, 0))

    val quotesSma3 = sma(quotes, 3)

    assert(quotesSma3.size === 6)
    assert(quotesSma3(0).indicators("sma3") === 585)
    assert(quotesSma3(1).indicators("sma3") === 589)
    assert(quotesSma3(2).indicators("sma3") === BigDecimal("589.33"))
    assert(quotesSma3(3).indicators("sma3") === BigDecimal("594.33"))
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

  test("Calculate the difference of sma5 and closing price") {
    val quotes = List(
      Quote("ARM.L", new Date(), 1, 0, 0, 570, 0, Map("sma5" -> 575.60, "sma20" -> 577.40)),
      Quote("ARM.L", new Date(), 2, 0, 0, 590, 0, Map("sma5" -> 576.80, "sma20" -> 576.62)),
      Quote("ARM.L", new Date(), 3, 0, 0, 585, 0, Map("sma5" -> 578.40, "sma20" -> 576.20)),
      Quote("ARM.L", new Date(), 4, 0, 0, 592, 0, Map("sma5" -> 580.70, "sma20" -> 574.97)),
      Quote("ARM.L", new Date(), 5, 0, 0, 591, 0, Map("sma5" -> 582.30, "sma20" -> 573.57))
    )

    val quotesWithSmaDiff = diff(quotes, "sma5")(_.close)

    assert(quotesWithSmaDiff.size === 5)
    assert(quotesWithSmaDiff(0).indicators("sma5diff") === BigDecimal("-5.6"))
    assert(quotesWithSmaDiff(1).indicators("sma5diff") === BigDecimal("13.2"))
    assert(quotesWithSmaDiff(2).indicators("sma5diff") === BigDecimal("6.6"))
    assert(quotesWithSmaDiff(3).indicators("sma5diff") === BigDecimal("11.3"))
    assert(quotesWithSmaDiff(4).indicators("sma5diff") === BigDecimal("8.7"))
  }

}
