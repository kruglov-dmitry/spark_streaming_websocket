#!/usr/bin/env bash

export SPARK_MASTER_NAME="localhost"
export SPARK_MASTER_PORT=8080

export SPARK_MASTER_URL=spark://${SPARK_MASTER_NAME}:${SPARK_MASTER_PORT}
export SPARK_HOME=/spark

SPARK_APPLICATION_MAIN_CLASS="example.Streaming"
SPARK_APPLICATION_ARGS=""
SPARK_SUBMIT_ARGS="--files /conf/stream.conf --files /conf/log4j.properties --properties-file /conf/stream.conf \
	--conf spark.eventLog.enabled=True --conf spark.eventLog.dir=/tmp/ \
	--conf spark.driver.extraJavaOptions=-Dlog4j.configuration=file:///conf/log4j.properties"

SPARK_APPLICATION_JAR_LOCATION=/jobs/binance_streaming-assembly-1.0.0.jar

echo "Submit application ${SPARK_APPLICATION_JAR_LOCATION} with main class ${SPARK_APPLICATION_MAIN_CLASS} to Spark master ${SPARK_MASTER_URL}"
echo "Passing arguments ${SPARK_APPLICATION_ARGS}"
sudo docker exec -it spark-master /spark/bin/spark-submit \
	--class ${SPARK_APPLICATION_MAIN_CLASS} \
	--master ${SPARK_MASTER_URL} \
	${SPARK_SUBMIT_ARGS} \
	${SPARK_APPLICATION_JAR_LOCATION} ${SPARK_APPLICATION_ARGS}

