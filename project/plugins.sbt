// Comment to get more information during initialization
logLevel := Level.Info

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//The Eligotech repository
resolvers += "Eligotech Snapshots repository" at "http://repo.eligotech.com/nexus/content/repositories/snapshots"

//should be added in your sbt base not here

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.0.1")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.2.0")

credentials += Credentials(Path.userHome / ".sbt" / "sonatype.credentials")


