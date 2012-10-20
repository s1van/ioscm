package org.apache.ioscm;

import org.w3c.dom.Element;

public class UnlimitedReaderBatch extends IOStream {
	String dirPath = "/tmp/";
	int interval = 0; //milliseconds
	long period = 0; //seconds
	int rsize = 0; //bytes
	int num = 0;
	
	public UnlimitedReaderBatch(int num, int interval, long period, int rsize, String label) {
		this.num = num;
		this.interval = interval;
		this.period = period;
		this.rsize = rsize;
		setLabel(label);
	}
	
	public UnlimitedReaderBatch(Element sl) {
		num = getIntValue(sl,"number");
		interval = getIntValue(sl,"interval");
		period = getIntValue(sl,"period");
		rsize = getIntValue(sl,"rsize");
		dirPath = getTextValue(sl,"path");
		setLabelFromXML(sl);	
	}
	
	public void run() {
		LOG.info("UnlimitedReaderBatch\t" + "\t" + Integer.toString(num) + "\t"
				+ Integer.toString(interval) + "\t" + Long.toString(period) + "\t"
				+ Integer.toString(rsize));
		for (int i = 1; i <= num; i++) {
			String dataPath = dirPath + Integer.toString(i);
			launcher.submit(new UnlimitedReader(dataPath, interval, period, rsize, label));
		}
		usync();
	}
}
