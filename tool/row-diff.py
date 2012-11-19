#!/usr/bin/python

import sys
import getopt
import string

def usage():
	print "Usage:   cat log| row-diff.py [-s separator] [-c col] [-d forward/backward] [-k]	"
	print "Option:  -s: separators in table													"
	print "         -c: number of the column												"
	print "         -d: direction of the difference											"
	print "         -k: keep the first or last row											"
	print "Example: cat log| row-diff.py -s ',' -c 3 -d forward								"
	print "Description: perform difference on specific column.								"
	print "             the specified column must be all numbers							"

def num (s):
	try:
		return int(s)
	except ValueError:
		return float(s)

def is_number(s):
	try:
		float(s)
		return True
	except ValueError:
		return False

def main():
	try:
		opts, args = getopt.getopt(sys.argv[1:], "hs:d:kc:", ["help", "separator", "direction", "keep", "column"])
	except getopt.GetoptError:
		usage()
		sys.exit(2)

	keep = False;
	isForward = True;

	for opt, arg in opts:
		if opt in ("-h", "--help"):
			usage()
			sys.exit()
		elif opt in ("-s", "--separator"):
			sep = arg
		elif opt in ("-d", "--direction"):
			d = arg
			if d == "forward":
				isForward = True
			elif d == "backward":
				isForward = False
			else:
				print "direction must be forward or backward"
				usage()
				sys.exit()
		elif opt in ("-k", "--keep"):
			keep = True;
		elif opt in ("-c", "--column"):
			col = int(arg) - 1
		else:
			print "unhandled option"
			usage()
			sys.exit()

	r = 1;
	while 1:
		inline = sys.stdin.readline()
		if not inline:
			break
		row = inline.split(sep)

		if (len(row) <= col):
			continue
		if (not is_number(row[col]) ):
			continue
		
		if (not isForward):					# backward difference
			if (r == 1):					# first line
				last = num(row[col])		# need to keep the value of previous line
				if (keep):
					sys.stdout.write(inline)
			else:							# not first line
				cur = num(row[col])
				diff = cur - last
				head = sep.join(row[0:col]);
				tail = sep.join(row[col+1:]);
				if col != 0:
					head = head + sep;
				if col != len(row) - 1:
					tail = sep + tail;
				else:
					tail = "\n"

				sys.stdout.write(head + str(diff) + tail)
				last = num(row[col])
		else:								# forward difference
			if (r == 1):
				last_row = row
			else:
				cur = num(row[col])
				last = num(last_row[col])
				diff = last - cur
				head = sep.join(last_row[0:col]);
				tail = sep.join(last_row[col+1:]);
				if col != 0:
					head = head + sep;
				if col != len(row) - 1:
					tail = sep + tail;
				else:
					tail = "\n"

				sys.stdout.write(head + str(diff) + tail)
				last_row = row
				

		r = r + 1;

if __name__ == "__main__":
	main()
