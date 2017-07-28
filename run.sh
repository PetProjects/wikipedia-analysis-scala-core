#!/bin/sh
scala -J-Xmx2g -classpath wikipedia-analysis-scala-core-build.jar:`hdfs classpath` epam.idobrovolskiy.wikipedia.trending.WikiPrep --extract-plaintext
scala -J-Xmx2g -classpath wikipedia-analysis-scala-core-build.jar:`hdfs classpath` epam.idobrovolskiy.wikipedia.trending.WikiPrep --to-hdfs-seq-file --full-text ./data/out/
scala -J-XX:-UseGCOverheadLimit -J-Xms2g -J-Xmx2g -J-Xmn500m -classpath wikipedia-analysis-scala-core-build.jar:`hdfs classpath` epam.idobrovolskiy.wikipedia.trending.WikiPrep --to-hdfs-seq-file --full-text ./data/out/