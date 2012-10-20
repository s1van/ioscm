#!/bin/bash

usage()
{
    echo "Usage: `echo $0| awk -F/ '{print $NF}'`  [-option]"
    echo "[option]:"
    echo "  -f  filename "
    echo "  -s  string "
    echo
    echo "Copyright by Siyuan Ma  2011-12."
    echo
}

if [ $# -lt 2 ]
then
        usage;
        exit
fi

while getopts "f:s:" OPTION
do
    case $OPTION in
        f)
            FILENAME=$OPTARG;
			cat $FILENAME| awk 'NR==1 {max=$1}; 
						{for(i=1; i<=NF; i++) {if($i>max) max=$i}}; 
						END{print max}'
            ;;
        s)
            STR=$OPTARG;
			echo $STR| awk 'NR==1 {max=$1}; 
						{for(i=1; i<=NF; i++) {if($i>max) max=$i}}; 
						END{print max}'
            ;;
        ?)
            usage
            ;;
    esac
done

