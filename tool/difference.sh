#!/bin/bash

CMD=`basename $0`;
usage() {
        echo "Usage:    $CMD -p TRACE_PATH -s SEPARATOR -c COLUMN 							"
		echo "e.g.      $CMD -p mytrace -s '\t' -c 1										"
		echo "	This command will perform a 'difference' on the given column. So if input 	"
		echo "	has n rows, output will have n-1 rows. Given column must be all numbers."
}

if [ $# -lt 3 ]
then
        usage
        exit
fi

while getopts "c:p:s:" OPTION
do
        case $OPTION in
                c)
                        C=$OPTARG;
                        ;;
                p)
                        TPATH=$OPTARG;
                        ;;
                s)
                        SEP="$OPTARG";
                        ;;
                ?)
                        usage;
                        ;;
        esac
done

sed '1!G;h;$!d' $TPATH| awk -v c=$C -v key=$key -v sep="$SEP" -F"$SEP" 'BEGIN{OFS=sep; lc="NULL"}{if (lc == "NULL") {lc=$c;} else {diff = lc - $c; lc =$c; $c = diff; print $0;}}'| sed '1!G;h;$!d';
