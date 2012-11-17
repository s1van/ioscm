#!/bin/bash

ISBLK=false;
TDPT=8;

TESTDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
source $TESTDIR/../conf/ioscm-env.sh;

TAG=$1;
PERIOD=$2; #Seconds
DPATH=$3;
TPATH=$4;
ISBLK=$5;
TDPT=$6;

JVM_ARGS="-Xmx2048m -XX:MaxPermSize=256m"

if [ $# -lt 6 ]
then
	echo "CMD TAG PERIOD DPATH TPATH ISBLK THREAD_PER_TRACE";
    exit;
fi

ROOT="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../";
CONFIG="${ROOT}/conf/TraceReplayer7Batch.xml";
JAR='ioscm-*.jar';
LABEL=TraceReplayer7Batch;

python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k period -v $PERIOD -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k dataDir -v "${DPATH}/" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k traceDir -v "${TPATH}/" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k isBlock -v "${ISBLK}" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k threadPerTrace -v "${TDPT}" -l $LABEL;

java $JVM_ARGS -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger \
	-Dlog4j.configuration=${ROOT}/conf/log4j.properties -classpath ${ROOT}/dist/lib/${JAR} org.apache.ioscm.CongestionMeter -conf ${CONFIG} -tag ${TAG}
