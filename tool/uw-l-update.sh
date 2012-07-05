#!/bin/bash

DPATH=$1;

cd $DPATH;
RESS=$(ls|sort -n -s -t_ -k1 -k2 -k3);

TMP=$(mktemp);
for RES in $RESS; do
	TAG=$(echo $RES| sed -e 's/_/\t/g'| sed -e 's/[T|INT|KB]//g');
	echo -ne "$TAG\t";
	#echo "#Thread, INTERVAL(ms), KB, [Req Serving->] AVG, DEV, MAX, MIN, MEDIAN, TOTALSIZE" 
	cat $RES| grep MeterTimer|awk '{print $9,$10,$11,$12,$13,$14}' > $TMP;
	echo "$(octave -q --eval "ioscm_req_portal(\"$(dirname $TMP)\", \"$(basename $TMP)\", \"simple\")")";
done
rm $TMP;