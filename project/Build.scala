import sbt._

object QuotesBuild extends Build {

  // Declare a project in the root directory of the build with ID "root".
  lazy val root = Project("quotes", file("."))

}
