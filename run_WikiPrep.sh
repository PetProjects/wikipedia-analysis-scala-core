#!/bin/sh
#Follow https://www.vultr.com/docs/how-to-install-scala-on-centos-7 to install scala, e.g.:
# wget http://downloads.lightbend.com/scala/2.11.8/scala-2.11.8.rpm
# sudo yum install scala-2.11.8.rpm

scala -J-Xmx2g -classpath wikipedia-analysis-0.1.jar:`hdfs classpath` epam.idobrovolskiy.wikipedia.trending.WikiPrep --extract-plaintext
scala -J-Xmx2g -classpath wikipedia-analysis-0.1.jar:`hdfs classpath` epam.idobrovolskiy.wikipedia.trending.WikiPrep --to-hdfs-seq-file --full-text ./data/out/
scala -J-XX:-UseGCOverheadLimit -J-Xms2g -J-Xmx2g -J-Xmn500m -classpath wikipedia-analysis-0.1.jar:`hdfs classpath` epam.idobrovolskiy.wikipedia.trending.WikiPrep --to-hdfs-seq-file --full-text ./data/out/