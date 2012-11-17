package org.apache.ioscm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;

import org.w3c.dom.Element;

public class RandomReader extends IOStream {
	String dataPath;
	int interval; //milliseconds
	long period; //seconds
	int rsize; //bytes
	long max;

	
	public RandomReader(String dataPath, int interval, long period, int rsize, String label) {
		this.dataPath = dataPath;
		this.interval = interval;
		this.period = period;
		this.rsize = rsize;
		setLabel(label);
	}
	
	public RandomReader(Element sl) {
		dataPath = getTextValue(sl,"path");
		interval = getIntValue(sl,"interval");
		period = getLongValue(sl, "period");
		rsize = getIntValue(sl, "rsize");
		setLabelFromXML(sl);
	}
	
	public void run() {
		LOG.info("RandomReader\t" + "\t" + dataPath + "\t"
				+ Integer.toString(interval) + "\t" + Long.toString(period) + "\t"
				+ Integer.toString(rsize)); 
		
		Random g = new Random();
		byte[] cbuf = new byte[rsize];
		
		try {
			RandomAccessFile rf = new RandomAccessFile(new File(dataPath), "r");
			max = rf.length();
			
			sync();
			long start = System.nanoTime();
			long offset = 0;
			
			while ((System.nanoTime() - start)/1000000000 <= period ) {
				timerOn();
				offset = Tools.nextLong(g, max - rsize);
				rf.seek(offset);
				rf.read(cbuf);
			
				timerOff(offset, rsize);
				if (interval > 0)
					synchronized(this){
						wait(interval);
					}
			}
			rf.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOG.info("--RandomReader");
	}
}
