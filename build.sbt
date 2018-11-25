name := "binance_streaming"
version := "1.0.0"
scalaVersion := "2.11.8"

assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly) { assemblyMergeStrategy => {
    entry =>
      val strategy = assemblyMergeStrategy (entry)
      if (strategy == MergeStrategy.deduplicate)
        MergeStrategy.first
      else
        strategy
  }}

fork in Test := true
javaOptions in Test ++= Seq("-Dconfig.file=conf/test.conf")

libraryDependencies ++= {
	val logbackVersion =	"1.2.3"
	val scalaLoggingVersion	=	"3.9.0"
	val sparkVersion = "2.1.1"
  val json4sVersion = "3.2.11"

  val akkaVersion	=	"2.5.16"
  val akkaHttpVersion = "10.1.5"

	Seq(
	  "ch.qos.logback"		%   "logback-classic" % logbackVersion,
		"com.typesafe"      %   "config" % "1.3.3",
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
		"org.apache.spark"  %%  "spark-core"    % sparkVersion,
    "org.apache.spark" %% "spark-streaming" % sparkVersion,
    "org.json4s"        %%  "json4s-native" % json4sVersion,
    "org.json4s"        %%  "json4s-ext"    % json4sVersion,
    "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
    "com.typesafe.akka" %% "akka-stream"  % akkaVersion,
    "com.typesafe.akka" %% "akka-http"    % akkaHttpVersion,
    "org.scalatest"     %%  "scalatest"     % "3.1.0-SNAP6" % Test
  )
}

