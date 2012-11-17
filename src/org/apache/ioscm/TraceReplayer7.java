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

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.EnumSet;

import org.w3c.dom.Element;

public class TraceReplayer7 extends IOStream {
	String dataPath;
	String tracePath;
	long period; //seconds
	long max;
	long scale;
	boolean isBlock;
	
	public class IOReqWrap {
		
		long offset;
		int size;
		String op;
		long submit; 	//submit time
		long s; 		//start time
		long e;			//end time
		
		public IOReqWrap(long offset, int size, String op){
			this.offset = offset;
			this.size = size;
			this.op = op;
		}
		
		public IOReqWrap(long offset, int size, String op, long s){
			this.offset = offset;
			this.size = size;
			this.op = op;
			this.s = s;
		}
		
		public long startAt(){
			return s;
		}
		
		public void startAt(long s){
			this.s = s;
		}
		
		public long endAt(){
			return e;
		}
		
		public void endAt(long e){
			this.e = e;
		}
		
		public long getOffset(){
			return offset;
		}
		
		public int getSize(){
			return size;
		}
		
		public String getOP(){
			return op;
		}
		
	}

	public TraceReplayer7(String dataPath, String tracePath, long period, String label, boolean isBlock) {
		this.dataPath = dataPath;
		this.tracePath = tracePath;
		this.period = period;
		this.isBlock = isBlock;
		setLabel(label);
	}
	
	public TraceReplayer7(Element sl) {
		dataPath = getTextValue(sl,"dataPath");
		tracePath = getTextValue(sl,"tracePath");
		period = getLongValue(sl, "period");
		isBlock = getBoolValue(sl, "isBlock");
		setLabelFromXML(sl);
	}
	
	public void run() {	
		Path dp=Paths.get(dataPath);
		ExecutorService pool= new ScheduledThreadPoolExecutor(16);

		if (isBlock){
			scale = 512;
		} else {
			scale = 1;
		}
		
		LOG.info("TraceReplayer7\t" + "\t" + dataPath + "\t" + tracePath + "\t"
				+ "\t" + Long.toString(period)); 
			
		
		try {
			AsynchronousFileChannel fc=AsynchronousFileChannel.open(dp, EnumSet.of(StandardOpenOption.READ), pool);
			CompletionHandler<Integer, IOReqWrap> handler= new CompletionHandler<Integer, IOReqWrap>(){

				@Override
				public synchronized void completed(Integer result, IOReqWrap req) {
					//OPCompleteEvent(req.startAt(), System.nanoTime(), req.getOffset(), req.getSize(), req.getOP());	
				}		

				@Override
				public void failed(Throwable exc, IOReqWrap req) {
    					exc.printStackTrace();
				}
			};

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
				offset = Long.parseLong(args[0]) * scale;
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
		
		LOG.info("--TraceReplayer7");
	}
}

