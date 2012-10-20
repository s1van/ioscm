#!/bin/bash

TAG=$1;
SIZE=$2; 	#Bytes
RSIZE=$3;	#Bytes
NUM=$4;
DPATH=$5;

if [ $# -lt 5 ]
then
	echo "CMD TAG SIZE RSIZE NUM DPATH";
    exit;
fi

ROOT="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/../";
DCONF=$ROOT/conf/;
CONFIG="${ROOT}/conf/SimpleWriterBatch.xml";
JAR='ioscm-*.jar';

python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k interval -v 0 -l DataPrep;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k size -v ${SIZE} -l DataPrep;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k rsize -v ${RSIZE} -l DataPrep;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k number -v ${NUM} -l DataPrep;
python ${ROOT}/tool/ioscm-conf.py -f $CONFIG -k path -v "${DPATH}/" -l DataPrep;

java -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.Log4JLogger \
-Dlog4j.configuration=${ROOT}/conf/log4j.properties -classpath ${ROOT}/dist/lib/${JAR} org.apache.ioscm.CongestionMeter -conf ${CONFIG} -tag ${TAG}
