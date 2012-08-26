package app

import DbUtils._

object QuotesApp extends App {

  initialiseDatabaseConnection()
  setupDatabaseSchema()
  loadDatabase()

}
