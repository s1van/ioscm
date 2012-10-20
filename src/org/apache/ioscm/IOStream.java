package org.apache.ioscm;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class IOStream extends Thread{
	WaitingWall wall;
	StreamLauncher launcher;
	public Logger LOG;
	String tag = "";
	String label = "";
	int ID;
	long s;
	long e;
	
	public void timerOn() {
		s = System.nanoTime();
	}
	
	public void timerOff(long val1, int val2) {
		e = System.nanoTime();
		LOG.info("MeterTimer\t" + tag + "\t" + label + "\t" 
				+ Integer.toString(ID) + "\t" 
				+ Long.toString(s) + "\t" + Long.toString(e) + "\t" 
				+ Long.toString(e-s) + "\t" + Long.toString(val1) + "\t"
				+ Integer.toString(val2));
	}
	
	public void buildWall(WaitingWall wall) {
		this.wall = wall;
	}
	
	public void setLauncher(StreamLauncher launcher) {
		this.launcher = launcher;
	}
	
	public void setLogger(Logger  log) {
		this.LOG = log;
	}
	
	public void setID(int id){
		this.ID = id;
	}
	
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public void setLabel(String label){
		this.label = label;
	}
	
	public void setLabelFromXML(Element sl){
		setLabel(sl.getAttribute("label"));
	}
	
	
	public void sync(){
		LOG.info("Sync.start\t" + Integer.toString(ID)); 
		wall.hit();
		LOG.info("Sync.end\t" + Integer.toString(ID)); 
	}
	
	public void usync(){
		LOG.info("USync\t" + Integer.toString(ID)); 
		wall.hitAndPass();
	}
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 */
	protected String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}


	/**
	 * Calls getTextValue and returns a int value
	 */
	protected int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	protected long getLongValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Long.parseLong(getTextValue(ele,tagName));
	}
}
