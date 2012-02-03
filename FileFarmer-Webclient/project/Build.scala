import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "FileFarmer-Webclient"
    val appVersion      = "1.0"
    
    val appDependencies = Seq(
      "ch.filefarmer" % "FileFarmer-Core" % "0.1.1",
      "net.java.dev.jai-imageio" % "jai-imageio-core-standalone" % "1.2-pre-dr-b04-2010-04-30"
    )
    
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    	resolvers += (
    		"Local Maven Repository" at "file://"+ Path.userHome.absolutePath + "/.m2/repository"
    	),
    	resolvers += Resolver.url("Local play repository", url("file://" + Path.userHome.absolutePath + "/javalibs/Play20/repository/local"))(Resolver.ivyStylePatterns)
    )

}
