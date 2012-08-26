package app

import java.util.Date
import org.squeryl.Schema
import org.squeryl.annotations.Column

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
            val volume: BigDecimal) {

  def this() = this("", new Date(), 0, 0, 0, 0, 0)
}

object QuoteDb extends Schema {

  val quotes = table[Quote]

}