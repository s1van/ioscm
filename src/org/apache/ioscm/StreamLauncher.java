package org.apache.ioscm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class StreamLauncher {
	WaitingWall wall;
	public final Logger LOG;
	AtomicInteger count;
	String tag;
	ExecutorService taskExecutor;
	
	public StreamLauncher(Logger log, String tag){
		wall = new WaitingWall();
		this.LOG = log;
		count = new AtomicInteger(0);
		this.tag = tag;
		taskExecutor = Executors.newFixedThreadPool(65536); 
	}
	
	void submit(IOStream stream){
		stream.setID(count.getAndIncrement());
		stream.buildWall(wall);
		stream.setLauncher(this);
		stream.setLogger(LOG);
		stream.setTag(tag);
		wall.ready2pass();
		taskExecutor.execute(stream);
		
		synchronized(tag) {
			try {
				tag.wait(128);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void startAll() {
		LOG.info("Launcher\t" + tag + "\t" + "startAll.beforeCollapse\t"+ Integer.toString(count.get()) ); 
		wall.collapse(count.get() * 32);
		LOG.info("Launcher\t" + tag + "\t" + "startAll.afterCollapse\t" + Integer.toString(count.get()) );
		taskExecutor.shutdown(); 
	}
	
	void awaitAll(){
		try {
			taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
