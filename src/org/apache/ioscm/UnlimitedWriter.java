package org.apache.ioscm;

import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.w3c.dom.Element;

public class UnlimitedWriter extends IOStream {
	String dataPath;
	int interval; //milliseconds
	long period; //seconds
	int rsize; //bytes
	
	public UnlimitedWriter(String dataPath, int interval, long period, int rsize, String label) {
		this.dataPath = dataPath;
		this.interval = interval;
		this.period = period;
		this.rsize = rsize;
		setLabel(label);
	}
	
	public UnlimitedWriter(Element sl) {
		dataPath = getTextValue(sl,"path");
		interval = getIntValue(sl,"interval");
		period = getLongValue(sl, "period");
		rsize = getIntValue(sl, "rsize");
		setLabelFromXML(sl);
	}
	
	public void run() {
		LOG.info("UnlimitedWriter\t" + "\t" + dataPath + "\t"
				+ Integer.toString(interval) + "\t" + Long.toString(period) + "\t"
				+ Integer.toString(rsize)); 
		
		byte[] cbuf = new byte[rsize];
		try {
			FileOutputStream out = new FileOutputStream(dataPath);
			FileDescriptor fd = out.getFD();
			//FileWriter fwriter = new FileWriter(dataPath);
			//BufferedWriter out = new BufferedWriter(fwriter, rsize);
			sync();
			long start = System.nanoTime();
			long cur = 0;
			
			while ((System.nanoTime() - start)/1000000000 <= period ) {
				timerOn();
				out.write(cbuf, 0, rsize);
				out.flush();
				fd.sync();
				timerOff(cur, rsize);
				cur += rsize;
				if (interval > 0)
					synchronized(this){
						wait(interval);
					}
			}
			
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
		LOG.info("--UnlimitedWriter");
	}
}
