ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.19"

lazy val root = (project in file("."))
  .settings(
    name := "23027964_Monopoly",
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafx" % "8.0.192-R14",
      "org.scalafx" %% "scalafxml-core-sfx8" % "0.5",
      // https://mvnrepository.com/artifact/org.scalikejdbc/scalikejdbc
      "org.scalikejdbc" %% "scalikejdbc" % "3.4.2",

      // https://mvnrepository.com/artifact/org.apache.derby/derby
      "org.apache.derby" % "derby" % "10.14.2.0",

      // https://mvnrepository.com/artifact/com.h2database/h2
      "com.h2database" % "h2" % "1.4.200",
      "com.opencsv" % "opencsv" % "5.5.2"
    )
  )

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

