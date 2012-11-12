#!/bin/bash

TAG=$1;
PERIOD=$2; #Seconds
DPATH=$3;
TPATH=$4;

JVM_ARGS="-Xmx2048m -XX:MaxPermSize=256m"

if [ $# -lt 4 ]
then
	echo "CMD TAG PERIOD DPATH TPATH";
    exit;
fi

ROOT="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../";
CONFIG="${ROOT}/conf/TraceReplayerBatch.xml";
JAR='ioscm-*.jar';
LABEL=TraceReplayerBatch;

python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k period -v $PERIOD -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k dataDir -v "${DPATH}/" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k traceDir -v "${TPATH}/" -l $LABEL;

java $JVM_ARGS -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger \
	-Dlog4j.configuration=${ROOT}/conf/log4j.properties -classpath ${ROOT}/dist/lib/${JAR} org.apache.ioscm.CongestionMeter -conf ${CONFIG} -tag ${TAG}
