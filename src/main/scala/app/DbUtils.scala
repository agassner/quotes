package app

import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.MySQLAdapter
import org.squeryl.PrimitiveTypeMode._
import app.QuotesData._
import app.QuoteDb._
import scala.Some

object DbUtils {

  def initialiseDatabaseConnection() {
    Class.forName("com.mysql.jdbc.Driver")

    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/quotes", "quotes", "quotes"),
        new MySQLAdapter
      )
    )
  }

  def setupDatabaseSchema() {
    transaction {
      QuoteDb.drop
      QuoteDb.create
    }
  }

  def loadDatabase() {
    for (symbol <- loadConstituentsFromFile())
      transaction {
        quotes.insert(buildListOfQuotes(loadFromFile(symbol)))
      }
  }

}
