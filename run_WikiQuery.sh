#!/bin/sh
/usr/hdp/current/spark2-client/bin/spark-submit  --class epam.idobrovolskiy.wikipedia.trending.WikiQuery --master local[6] --executor-memory 3g wikipedia-analysis-0.1.jar --debug --tokens