from xml.dom.minidom import parse
import getopt
import sys
import ast

def usage():
	print "Usage: ioscm-xonf.py [--help] --file=path --key=key --value=value [--append] [--print]"
	print "--append "
	print "		append a stream conf to the bottotm of the configuration"
	print """Example: python ioscm-conf.py --file="./SimpleWriterBatch.xml" --label='test' --key='number' --value=2 """

def main():
	if len(sys.argv) < 2:
		print "not enough arguments"
		usage()
		sys.exit()
	
	try:                                
		opts, args = getopt.getopt(sys.argv[1:], "arhpk:v:f:l:", ["append", "remove", "help", "print", "key=", "value=", "file=", "label="]) 
	except getopt.GetoptError:           
		usage()                          
		sys.exit(2)
                     
	appendEnabled = False;
	printEnabled = False;
	removeEnabled = False;
	for opt, arg in opts:                
		if opt in ("-h", "--help"):      
			usage()                     
			sys.exit()                  
		elif opt in ("-k", "--key"):
			key = arg    
		elif opt in ("-l", "--label"): 
			label = arg    
		elif opt in ("-v", "--value"): 
			value = arg    
		elif opt in ("-f", "--file"): 
			path = arg    
		elif opt in ("-p", "--print"): 
			printEnabled = True;
		elif opt in ("-a", "--append"): 
			appendEnabled = True;
		elif opt in ("-r", "--remove"): 
			removeEnabled = True;
		else:
			print "unhandled option"
			usage()
			sys.exit()

	isFound = False;
	dom = parse(path);
	conf = dom.childNodes[0];
	streams=conf.childNodes[1::2];
	
	if appendEnabled:
		newStream = dom.createElement('Stream');
		newStream.setAttribute('label', label);

		entries=ast.literal_eval(value);
		for key in entries.iterkeys():
			if key == 'type':
				newStream.setAttribute('type', entries[key]);
				continue;
			newEntry=dom.createElement(key);
			newValue=dom.createTextNode(entries[key]);
			newEntry.appendChild(newValue);
			newStream.appendChild(dom.createTextNode('\n'));
			newStream.appendChild(newEntry);
		newStream.appendChild(dom.createTextNode('\n'));
		conf.appendChild(newStream);
		conf.appendChild(dom.createTextNode('\n'));
	elif removeEnabled:
		for stream in streams:
			if stream.attributes['label'].nodeValue == label:
				conf.removeChild(stream);
				break;
	else:
		for stream in streams:
			if stream.attributes['label'].nodeValue == label:
				isFound = True;
				if printEnabled:
					if key == 'type':
						print stream.attributes['type'].nodeValue;
					else:
						print stream.getElementsByTagName(key)[0].childNodes[0].data;
				else:
					if key == 'type':
						stream.attributes['type'].nodeValue = value;
					else:
						stream.getElementsByTagName(key)[0].childNodes[0].data = value;
				break;
			
	f = open(path, 'w')
	dom.writexml(f)
	f.close()

if __name__ == "__main__":
	main()
