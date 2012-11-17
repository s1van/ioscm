package org.apache.ioscm;

import org.w3c.dom.Element;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class TraceReplayer7Batch extends IOStream {
	String dataDir = "/tmp/";
	String traceDir = "/tmp/";
	long period = 0; //seconds
	boolean isBlock = false;
	int num = 0;
	int aio_pool_size = 1;
	
	public TraceReplayer7Batch(long period, String label) {
		this.period = period;
		setLabel(label);
	}
	
	public TraceReplayer7Batch(Element sl) {
		period = getIntValue(sl,"period");
		dataDir = getTextValue(sl,"dataDir");
		traceDir = getTextValue(sl,"traceDir");
		isBlock = getBoolValue(sl, "isBlock");
		aio_pool_size = getIntValue(sl,"AIOPoolSize");
		setLabelFromXML(sl);	
	}
	
	public void run() {
		ExecutorService pool= new ScheduledThreadPoolExecutor(aio_pool_size);
		LOG.info("TraceReplayer7Batch\t" + "\t" + Integer.toString(num) + "\t"
				+ Long.toString(period) );
		File tdir = new File(traceDir);
		for (File trace : tdir.listFiles()) {
			String dataPath = dataDir + trace.getName();
			String tracePath = traceDir + trace.getName();
			launcher.submit(new TraceReplayer7(dataPath, tracePath, period, label, isBlock, pool));
		}
		usync();
	}
}
