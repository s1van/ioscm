#!/bin/bash

DPATH=$1;
DRES=$2;
if [ $# -lt 2 ]
then
	echo "CMD DPATH DRES";
    exit;
fi


DPATH="/tmp/WTEST";
DRES="$HOME/expr/ioscm/W-STRIP-TEST-L-HDD";
mkdir -p $DRES;
rm -rf $DPATH;

RSET="512 256 128 64 32 16 8 4 2 1";
TSIZE="32 64 128 256";

for TM in TSET; do 
for RK in $RSET; do
		TSIZE=$((TM * 1024 * 1024));
		RSIZE=$RK;
		SIZE=$RK;
		NUM=$(($TSIZE / $RSIZE));
		TAG="${NUM}T_${TM}M_${RK}K";
		mkdir -p $DPATH;
		echo "TAG=$TAG; SIZE=$SIZE; RSIZE=$RSIZE; NUM=$NUM; DPATH=$DPATH";
		
		./SimpleWriterBatchTest.sh $TAG $SIZE $RSIZE $NUM $DPATH > $DRES/$TAG;
		sleep 1;
		rm -rf $DPATH;
		sleep 2;
done
done