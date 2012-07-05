#!/bin/bash

usage() {
        echo "Usage: res-update.sh -p PATH "
}

SBIN=$(pwd);
PATH=$PATH:$SBIN;

if [ $# -lt 1 ]
then
        usage
        exit
fi

while getopts "p:" OPTION
do
        case $OPTION in
                p)
                        DIR=$OPTARG;
                        ;;
                ?)
                        #usage
                        ;;
        esac
done

TARGETS=$(ls $DIR|grep -v res);
for t in $TARGETS; do
        if [ -e ${DIR}/${t}.res ]; then
                continue;
        fi
        echo $t;
        cd $DIR && $SBIN/uw-l-update.sh ${t} > ${t}.res;
done