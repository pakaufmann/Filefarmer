resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.file("Local Play Repository",file(Path.userHome.absolutePath + "/javalibs/Play20/repository/local"))( Resolver.ivyStylePatterns)
)

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC1-SNAPSHOT")