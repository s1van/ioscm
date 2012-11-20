#!/bin/bash

CMD=`basename $0`;
TOOLDIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
EXEC="awk -f $TOOLDIR/toASAP.awk";

usage() {
        echo "Usage:    $CMD -p TRACE_PATH				"
	echo "e.g.      $CMD -p my_|_separated_trace			"
	echo "	This command will transform all trace to ASAP mode	"
}

if [ $# -lt 1 ]
then
        usage
        exit
fi

while getopts "c:p:s:" OPTION
do
        case $OPTION in
                p)
                        TPATH=$OPTARG;
                        ;;
                ?)
                        usage;
                        ;;
        esac
done

cat $TPATH| $EXEC;
