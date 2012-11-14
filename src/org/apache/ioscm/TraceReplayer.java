package org.apache.ioscm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.logging.Logger;
import java.lang.Integer;
import java.lang.Long;

import org.w3c.dom.Element;

public class TraceReplayer extends IOStream {
	String dataPath;
	String tracePath;
	long period; //seconds
	long max;

	public TraceReplayer(String dataPath, String tracePath, long period, String label) {
		this.dataPath = dataPath;
		this.tracePath = tracePath;
		this.period = period;
		setLabel(label);
	}
	
	public TraceReplayer(Element sl) {
		dataPath = getTextValue(sl,"dataPath");
		tracePath = getTextValue(sl,"tracePath");
		period = getLongValue(sl, "period");
		setLabelFromXML(sl);
	}
	
	public void run() {		
		LOG.info("TraceReplayer\t" + "\t" + dataPath + "\t" + tracePath + "\t"
				+ "\t" + Long.toString(period)); 
			
		
		try {
			long offset;
			int rsize = 65536; //bytes
			String op;
			int interval; //milliseconds
			String btrl;
			String args[];
			
			byte[] cbuf = new byte[rsize];
		
			File trace = new File(tracePath);
			FileReader tr = new FileReader(trace);
			BufferedReader btr  = new BufferedReader(tr, 65536);
			
			RandomAccessFile rf = new RandomAccessFile(new File(dataPath), "rw");
			max = rf.length();
			
			sync();
			long start = System.nanoTime();
			
			while ( ( (btrl = btr.readLine()) != null) &&
					(System.nanoTime() - start)/1000000000 <= period ) {
				args = btrl.split("[|]");
				
				//LOG.info("\nER#@R3R#@\t" + btrl + "\t" + args[0] + "\t" + args[1] + "\t" + args[2] + "\t" + args[3]);
				offset = Long.parseLong(args[0]);
				rsize = Integer.parseInt(args[1]);
				op = args[2];
				interval = Math.round( Float.parseFloat(args[3])); //millisecond
				
				if (rsize > cbuf.length) 
					cbuf = new byte[rsize * 2];
			
				timerOn();
				rf.seek(offset);
				if (op.contentEquals("R"))
					rf.read(cbuf);				//blocks until at least one byte of input is available.
				else if (op.contentEquals("r"))
					rf.readFully(cbuf); 		//blocks until the requested number of bytes are read
				else if (op.contentEquals("W") )
					rf.write(cbuf, 0, rsize);
				else if (op.contentEquals("w")) {
					rf.write(cbuf, 0, rsize);
					rf.getFD().sync();
				}
				else
					;
				timerOff(offset, rsize, op);
				if (interval > 0)
					synchronized(this){
						wait(interval);
					}
			}
			
			rf.close();
			btr.close();
			
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
		
		LOG.info("--TraceReplayer");
	}
}

