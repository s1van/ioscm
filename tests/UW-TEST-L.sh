#!/bin/bash

DPATH=$1;
DRES=$2;
if [ $# -lt 2 ]
then
	echo "CMD DPATH RESD";
    exit;
fi

PERIOD=16; #Seconds
mkdir -p $DRES;
rm -rf $DPATH;

RSET="4 16 64 256 1024 4096";
INTVSET="0 128 256 512 1024 2048";
#NUMSET="1 2 4 8 16 32 64";
NUMSET="64";

for INTVK in $INTVSET; do
	for NUM in $NUMSET; do
		for RK in $RSET; do
			RSIZE=$(($RK * 1024));
			INTV=$INTVK;
			TAG="${NUM}T_${INTVK}INT_${RK}K";
			mkdir -p $DPATH;
			echo "TAG=$TAG; INTERVAL=$INTV; NUM=$NUM; RSIZE=$RSIZE; PERIOD=$PERIOD; DPATH=$DPATH";
			./UnlimitedWriterBatchTest.sh $TAG $PERIOD $NUM $INTV $RSIZE $DPATH > $DRES/$TAG;
			sleep 1;
			rm -rf $DPATH;
			sleep 1;
		done
	done
done