#!/bin/bash

TAG=$1;
PERIOD=$2; #Seconds
NUM=$3;
INTERVAL=$4;
RSIZE=$5;
DPATH=$6;

JVM_ARGS="-Xmx2048m -XX:MaxPermSize=256m"

if [ $# -lt 6 ]
then
	echo "CMD TAG PERIOD NUM INTERVAL RSIZE DPATH";
    exit;
fi

ROOT="$(dirname $0)/..";
CONFIG="${ROOT}/conf/UnlimitedWriterBatch.xml";
JAR='ioscm-*.jar';
LABEL=UnlimitedWriterBatch;

python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k interval -v $INTERVAL -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k period -v $PERIOD -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k rsize -v $RSIZE -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k number -v ${NUM} -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k path -v "${DPATH}/" -l $LABEL;

java $JVM_ARGS -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger \
-Dlog4j.configuration=${ROOT}/conf/log4j.properties -classpath ${ROOT}/dist/lib/${JAR} org.apache.ioscm.CongestionMeter -conf ${CONFIG} -tag ${TAG}
