package org.apache.ioscm;

import org.w3c.dom.Element;

public class SimpleWriterBatch extends IOStream {
	String dirPath = "/tmp/";
	int interval = 0; //milliseconds
	long size = 0; //MB
	int rsize = 0; //bytes
	int num = 0;
	
	public SimpleWriterBatch(int num, int interval, long size, int rsize, String dirPath, String label) {
		this.num = num;
		this.interval = interval;
		this.size = size;
		this.rsize = rsize;
		this.dirPath = dirPath;
		setLabel(label);
	}
	
	public SimpleWriterBatch(Element sl) {
		num = getIntValue(sl,"number");
		interval = getIntValue(sl,"interval");
		size = getLongValue(sl,"size");
		rsize = getIntValue(sl,"rsize");
		dirPath = getTextValue(sl, "path");
		setLabelFromXML(sl);	
	}
	
	public void run() {
		LOG.info("SimpleWriterBatch\t" + "\t" + Integer.toString(num) + "\t"
				+ Integer.toString(interval) + "\t" + Long.toString(size) + "\t"
				+ Integer.toString(rsize));
		for (int i = 1; i <= num; i++) {
			String dataPath = dirPath + Integer.toString(i);
			launcher.submit(new SimpleWriter(dataPath, interval, size, rsize, label));
		}
		usync();		
	}

}
