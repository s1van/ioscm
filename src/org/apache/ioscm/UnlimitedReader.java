package org.apache.ioscm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Element;

public class UnlimitedReader extends IOStream {
	String dataPath;
	int interval; //milliseconds
	long period; //seconds
	int rsize; //bytes
	
	public UnlimitedReader(String dataPath, int interval, long period, int rsize, String label) {
		this.dataPath = dataPath;
		this.interval = interval;
		this.period = period;
		this.rsize = rsize;
		setLabel(label);
	}
	
	public UnlimitedReader(Element sl) {
		dataPath = getTextValue(sl,"path");
		interval = getIntValue(sl,"interval");
		period = getLongValue(sl, "period");
		rsize = getIntValue(sl, "rsize");
		setLabelFromXML(sl);
	}
	
	public void run() {
		LOG.info("UnlimitedReader\t" + "\t" + dataPath + "\t"
				+ Integer.toString(interval) + "\t" + Long.toString(period) + "\t"
				+ Integer.toString(rsize)); 
		
		char[] cbuf = new char[rsize];
		try {
			BufferedReader in = new BufferedReader(new FileReader(dataPath), rsize);
			sync();
			long start = System.nanoTime();
			long cur = 0;
			
			while ((System.nanoTime() - start)/1000000000 <= period ) {
				timerOn();
				in.read(cbuf, 0, rsize);
				timerOff(cur, rsize);
				cur += rsize;
				if (interval > 0)
					synchronized(this){
						wait(interval);
					}
			}			
			in.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info("--UnlimitedReader");
	}
}
