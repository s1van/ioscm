#!/bin/sh

usage()
{
	echo "Usage: `echo $0| awk -F/ '{print $NF}'`  [-option]"
	echo "[option]:"
	echo "  -s  size"
	echo "          file size in GB "
	echo "  -p  path"
	echo "          folder to write in"
	echo "  -f  "
	echo "          standard way to clean up the cache"
	echo "  -r  "
	echo "          remove the temporary file"
	echo
	echo "  e.g cache-cleanup.sh -s 30 -p /mnt/shm"
	echo
	echo "Copyright by Siyuan Ma  2011-12."
	echo
}

# For compatibility
SIZE=$1
TMP=$(mktemp);
rm $TMP;
RFLAG=false;

if [ $# -lt 1 ]
then
	usage
	exit
fi

while getopts "rfs:p:" OPTION
do
	case $OPTION in
		s)
			SIZE=$OPTARG;
			;;
		p)
			TMP=$(mktemp -p $OPTARG);
			;;
		f)
			echo "Fast Flushing..."
			echo "echo 3 > /proc/sys/vm/drop_caches"|sudo su;
			exit
			;;
		r)
			RFLAG=true;
			;;
		?)
			echo "unknown arguments"
			usage
			exit
			;;
	esac
done

echo "Write to temp file $TMP"
#dd if=/dev/zero of=$TMP count=$SIZE bs=1073741824 > /dev/null 2>&1
dd if=/dev/zero of=$TMP count=$SIZE bs=1073741824

if [[ $RFLAG = "true" ]];
then
	rm $TMP
fi
