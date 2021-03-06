#!/bin/bash

TESTDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
source $TESTDIR/../conf/ioscm-env.sh;
EXEC=$TESTDIR/TraceReplayer7Batch.sh;

TAG=$1;
PERIOD=$2; #Seconds
DPATH=$3;
TPATH=$4;
ISBLK=$(echo $5| tr '[A-Z]' '[a-z]');
TDPT=$6;

JVM_ARGS="-Xmx2048m -XX:MaxPermSize=256m"

if [ $# -lt 6 ]
then
	echo "CMD TAG PERIOD DPATH TPATH ISBLK AIO_THREAD_POOL_SIZE";
    exit;
fi

BLKSIZE=1;
if [ "$ISBLK" == "true" ]; then
	BLKSIZE=512;
fi

$EXEC $TAG $PERIOD $DPATH $TPATH $BLKSIZE $TDPT 0 SYNC_ALL false;
