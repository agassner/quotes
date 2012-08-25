package app

import org.scalatest.FunSuite
import java.text.SimpleDateFormat

class QuotesDataTest extends FunSuite {

  test("Load file with ARM.L quotes") {
    val data = QuotesData.loadFromFile("ARM.L")
    assert(data.size > 0)
  }

  test("Build a list of Quote from some data") {
    val data = List("ARM.L,20120824,580.00,583.65,575.00,579.00,1908500,0","ARM.L,20120823,585.00,586.00,575.50,580.00,3030600,0").toIterator
    val quotes = QuotesData.build(data).toList
    assert(quotes.size === 2)
    assert(quotes.head.symbol === "ARM.L")
    assert(quotes.head.datetime === new SimpleDateFormat("YYYYMMdd").parse("20120824"))
    assert(quotes.head.open === 580)
    assert(quotes.head.high === 583.65)
    assert(quotes.head.low === 575)
    assert(quotes.head.close === 579)
    assert(quotes.head.volume === 1908500)
  }

}
