#!/bin/sh
/usr/hdp/current/spark2-client/bin/spark-submit  --class epam.idobrovolskiy.wikipedia.trending.WikiIndex --master local[6] --executor-memory 3g wikipedia-analysis-scala-core-build.jar