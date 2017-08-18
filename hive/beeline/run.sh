#!/bin/bash
# file: run.sh
beeline --hiveconf hive.tez.container.size=1024 -u jdbc:hive2://localhost:10000/wikitrending -n hive -p hive -f wikiquery_50bc-50ad.hsql
