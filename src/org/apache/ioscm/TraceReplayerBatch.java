package org.apache.ioscm;

import org.w3c.dom.Element;
import java.io.File;

public class TraceReplayerBatch extends IOStream {
	String dataDir = "/tmp/";
	String traceDir = "/tmp/";
	long period = 0; //seconds
	int num = 0;
	
	public TraceReplayerBatch(long period, String label) {
		this.period = period;
		setLabel(label);
	}
	
	public TraceReplayerBatch(Element sl) {
		period = getIntValue(sl,"period");
		dataDir = getTextValue(sl,"dataDir");
		traceDir = getTextValue(sl,"traceDir");
		setLabelFromXML(sl);	
	}
	
	public void run() {
		LOG.info("TraceReplayerBatch\t" + "\t" + Integer.toString(num) + "\t"
				+ Long.toString(period) );
		File tdir = new File(traceDir);
		for (File trace : tdir.listFiles()) {
			String dataPath = dataDir + trace.getName();
			String tracePath = traceDir + trace.getName();
			launcher.submit(new TraceReplayer(dataPath, tracePath, period, label));
		}
		usync();
	}
}
