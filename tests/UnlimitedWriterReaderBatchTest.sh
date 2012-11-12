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
CONFIG="${ROOT}/conf/UnlimitedWriterReaderBatch.xml";
JAR='ioscm-*.jar';

WLABEL=UnlimitedWriterBatch;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k interval -v $INTERVAL -l $WLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k period -v $PERIOD -l $WLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k rsize -v $RSIZE -l $WLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k number -v ${NUM} -l $WLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k path -v "${DPATH}/Writer/" -l $WLABEL;

RLABEL=UnlimitedReaderBatch;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k interval -v $INTERVAL -l $RLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k period -v $PERIOD -l $RLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k rsize -v $RSIZE -l $RLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k number -v ${NUM} -l $RLABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k path -v "${DPATH}/Reader/" -l $RLABEL;

java $JVM_ARGS -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger \
-Dlog4j.configuration=${ROOT}/conf/log4j.properties -classpath ${ROOT}/dist/lib/${JAR} org.apache.ioscm.CongestionMeter -conf ${CONFIG} -tag ${TAG}
