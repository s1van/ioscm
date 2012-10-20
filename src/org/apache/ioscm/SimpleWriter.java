package org.apache.ioscm;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Element;

public class SimpleWriter extends IOStream {
	String dataPath;
	int interval; //milliseconds
	long size; //MB
	int rsize; //bytes
	
	public SimpleWriter(String dataPath, int interval, long size, int rsize, String label) {
		this.dataPath = dataPath;
		this.interval = interval;
		this.size = size;
		this.rsize = rsize;
		setLabel(label);
	}
	
	public SimpleWriter(Element sl) {
		dataPath = getTextValue(sl,"path");
		interval = getIntValue(sl,"interval");
		size = getLongValue(sl,"size");
		rsize = getIntValue(sl,"rsize");
		setLabelFromXML(sl);
	}
	
	public void run() {
		LOG.info("SimpleWriter\t" + "\t" + dataPath + "\t"
				+ Integer.toString(interval) + "\t" + Long.toString(size) + "\t"
				+ Integer.toString(rsize)); 
		
		char[] cbuf = new char[rsize];
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(dataPath), rsize);
			long cur = 0;
			sync();
			
			while (cur < size ) {
				timerOn();
				out.write(cbuf, 0, rsize);
				out.flush();
				timerOff(cur, rsize);
				cur += rsize;
				if (interval > 0)
					synchronized(this){
						wait(interval);
					}
			}
			
			LOG.info("-SimpleWriter");
			out.close();
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
		LOG.info("--SimpleWriter");

	}
}
