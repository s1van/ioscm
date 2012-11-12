#!/bin/bash

DPATH=$1;
DRES=$2;
MAXTPUT=$3;
PERIOD=$4; #Seconds

mkdir -p $DRES;
rm -rf $DPATH;

RSET="4 16 64 256 1024 4096";
INTVSET="0 128 256 512 1024 2048";
NUMSET="1 2 4 8 16 32 64";

mkdir -p $DPATH;
NUM=$(getmax.sh -s "$NUMSET"); RK=8192; INTV=0;
RSIZE=$(($RK * 1024)); 
SIZE=$(($MAXTPUT * 1024 * 1024 * $PERIOD));
TAG="${NUM}T_${INTV}INT_${RK}K";
echo "TAG=$TAG; SIZE=$SIZE; NUM=$NUM; RSIZE=$RSIZE; DPATH=$DPATH"; 
./SimpleWriterBatchTest.sh $TAG $SIZE $RSIZE $NUM $DPATH > /dev/null;
cache-cleanup.sh -f; sleep 32;

for INTV in $INTVSET; do
	for NUM in $NUMSET; do
		for RK in $RSET; do
			RSIZE=$(($RK * 1024));
			TAG="${NUM}T_${INTV}INT_${RK}K";
			cache-cleanup.sh -f;
			sleep 16;
			echo "TAG=$TAG; INTERVAL=$INTV; NUM=$NUM; RSIZE=$RSIZE; PERIOD=$PERIOD; DPATH=$DPATH";
			./UnlimitedReaderBatchTest.sh $TAG $PERIOD $NUM $INTV $RSIZE $DPATH > $DRES/$TAG;
		done
	done
done