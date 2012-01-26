import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "FileFarmer-Webclient"
    val appVersion      = "1.0"
    
    val appDependencies = Seq(
      "ch.filefarmer" % "FileFarmer-Core" % "0.1.1"
    )
    
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    	resolvers += (
    		"Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
    	)
    )

}