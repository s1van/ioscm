#!/bin/bash

usage() {
        echo "Usage: 	trace-splitter.sh -p TRACE_PATH -s SEPARATOR -c SPLIT_ON_COLUMN "
	echo "e.g.	trace-splitter.sh -p mytrace -s '\t' -c 1			"
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
                        SEP=$OPTARG;
                        ;;
                ?)
                        usage;
                        ;;
        esac
done

SC_KEYS=$(cat $TPATH| awk -v c=$C -F"$SEP" '{print $c}'|sort|uniq);

mkdir -p ${TPATH}_s
for key in $SC_KEYS; do
	cat $TPATH| awk -v c=$C -v key=$key -F"$SEP" 'BEGIN{OFS="|"}{if ($c == key) {$1="";print $0;}}'| awk '{print substr($1,2);}' > ${TPATH}_s/${key}
done
