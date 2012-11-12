#!/bin/bash

DPATH=$1;
TOOLDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";

echo 'TNUM|INTV|RSIZE|TP|ATP';
find $DPATH -type f|xargs -I % basename %| sort -n| xargs -I % echo $DPATH/% | $TOOLDIR/ioscm-raw.sh | awk '{print $1"|"$2"|"$3"|"$4"|"$4*$3/(1024*4)"|"$4*$3/(1024*4*$1)}';
