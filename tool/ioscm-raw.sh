#/bin/bash

while read line; do
	file=$(basename $line);
	echo -ne $file|sed 's/[a-zA-Z_]\+/\t/g';
	cat $line| grep MeterTimer| wc -l;
done
