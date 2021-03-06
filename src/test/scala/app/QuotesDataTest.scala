package app

import org.scalatest.FunSuite
import java.text.SimpleDateFormat
import QuotesData._

class QuotesDataTest extends FunSuite {

  test("Load file with constituents") {
    val data = loadConstituentsFromFile()
    assert(data.size === 79)
  }

  test("Load file with ARM.L quotes") {
    val data = loadFromFile("ARM.L")
    assert(!data.isEmpty)
  }

  test("Load file with XYZ.L quotes (File not found)") {
    val data = loadFromFile("XYZ.L")
    assert(data.isEmpty)
  }

  test("Build a list of Quote from some data") {
    val data = List("ARM.L,20120824,580.00,583.65,575.00,579.00,1908500,0", "ARM.L,20120823,585.00,586.00,575.50,580.00,3030600,0")
    val quotes = buildListOfQuotes(data)
    assert(quotes.size === 2)

    assert(quotes.head.symbol === "ARM.L")
    assert(quotes.head.datetime === new SimpleDateFormat("yyyyMMdd").parse("20120824"))
    assert(quotes.head.open === 580)
    assert(quotes.head.high === BigDecimal("583.65"))
    assert(quotes.head.low === 575)
    assert(quotes.head.close === 579)
    assert(quotes.head.volume === 1908500)

    assert(quotes.last.symbol === "ARM.L")
    assert(quotes.last.datetime === new SimpleDateFormat("yyyyMMdd").parse("20120823"))
    assert(quotes.last.open === 585)
    assert(quotes.last.high === 586)
    assert(quotes.last.low === 575.50)
    assert(quotes.last.close === 580)
    assert(quotes.last.volume === 3030600)
  }

}
