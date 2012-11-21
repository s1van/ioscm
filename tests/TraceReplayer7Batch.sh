#!/bin/bash

TESTDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
source $TESTDIR/../conf/ioscm-env.sh;

TAG=$1;
PERIOD=$2; #Seconds
DPATH=$3;
TPATH=$4;
BSIZE=$5;
TDPT=$6;
ISCALE=$7;
SYNC=$8;
ISDIR=$9;



JVM_ARGS="-Xmx2048m -XX:MaxPermSize=256m"

if [ $# -lt 8 ]
then
	echo "CMD TAG PERIOD DPATH TPATH BLK_SIZE AIO_THREAD_POOL_SIZE INTERVAL_SCALE_FACTOR SYNC_MODE ISDIR";
	exit;
fi

ROOT="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../";
CONFIG="${ROOT}/conf/TraceReplayer7Batch.xml";
JAR='ioscm-*.jar';
LABEL=TraceReplayer7Batch;

python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k period -v $PERIOD -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k dataLocation -v "${DPATH}/" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k traceDir -v "${TPATH}/" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k blockSize -v "${BSIZE}" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k AIOPoolSize -v "${TDPT}" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k intervalScaleFactor -v "${ISCALE}" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k SyncMode -v "${SYNC}" -l $LABEL;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k dataIsDir -v "${ISDIR}" -l $LABEL;

java $JVM_ARGS -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger \
	-Dlog4j.configuration=${ROOT}/conf/log4j.properties -classpath ${ROOT}/dist/lib/${JAR} org.apache.ioscm.CongestionMeter -conf ${CONFIG} -tag ${TAG}
