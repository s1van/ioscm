#!/bin/bash

DPATH=$1;
DRES=$2;
MAXTPUT=$3;
PERIOD=$4; #Seconds

TESTDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
TOOLDIR=$TESTDIR/../tool;

mkdir -p $DRES;
rm -rf $DPATH;

#RSET="4 16 64 256 1024 4096"; #KB
#INTVSET="0 128 256 512 1024 2048";
#NUMSET="1 2 4 8 16 32 64";

RSET="1 2 4 8 16 32 64 128"; #KB
INTVSET="0 1 4 16 64"; #msec
NUMSET="1 2 4 8 16 32";

mkdir -p $DPATH;
NUM=$($TOOLDIR/getmax.sh -s "$NUMSET"); RK=8192; INTV=0;
RSIZE=$(($RK * 1024)); 
SIZE=$(($MAXTPUT * 1024 * 1024 * $PERIOD));
TAG="${NUM}T_${INTV}INT_${RK}K";
echo "TAG=$TAG; SIZE=$SIZE; NUM=$NUM; RSIZE=$RSIZE; DPATH=$DPATH"; 
$TESTDIR/SimpleWriterBatchTest.sh $TAG $SIZE $RSIZE 1 $DPATH > /dev/null;
for file in $(seq 2 $NUM); do
	ln $DPATH/1 $DPATH/$file;
done

$TOOLDIR/cache-cleanup.sh -f; sleep ${PERIOD};

for INTV in $INTVSET; do
	for NUM in $NUMSET; do
		for RK in $RSET; do
			RSIZE=$(($RK * 1024));
			TAG="${NUM}T_${INTV}INT_${RK}K";
			$TOOLDIR/cache-cleanup.sh -f;
			sleep ${PERIOD};
			echo "TAG=$TAG; INTERVAL=$INTV; NUM=$NUM; RSIZE=$RSIZE; PERIOD=$PERIOD; DPATH=$DPATH";
			$TESTDIR/RandomReaderBatchTest.sh $TAG $PERIOD $NUM $INTV $RSIZE $DPATH > $DRES/$TAG;
		done
	done
done
