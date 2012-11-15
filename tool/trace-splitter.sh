#!/bin/bash

CMD=`basename $0`;
usage() {
        echo "Usage:    $CMD -p TRACE_PATH -s SEPARATOR -c SPLIT_ON_COLUMN "
		echo "e.g.      $CMD -p mytrace -s '\t' -c 1						"
		echo "	This command will perform a 'GROUP BY' on the given column, and dump all  "
		echo "	rows that have the same column value into a file named as the value. All"
		echo "	result files are located in folder TRACE_PATH_s"
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
	cat $TPATH| awk -v c=$C -v key=$key -F"$SEP" 'BEGIN{OFS="|"}{if ($c == key) {$c="\b";print $0;}}' > ${TPATH}_s/${key}
done
