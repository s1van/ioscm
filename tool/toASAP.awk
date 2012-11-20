BEGIN {
	FS=OFS="|";
}
{
	if ($3 == "R") $3="r";
	if ($3 == "W") $3="w";
	$4 = 0;
	print $0;
}
