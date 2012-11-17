package org.apache.ioscm;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CongestionMeter {
	public static final Logger LOG = Logger.getLogger(CongestionMeter.class);
	
	public static void main(String[] args) throws Exception{
		String confPath = null;
		String tag = "";
		Document dom;
		String className = CongestionMeter.class.getSimpleName();
		String version = className + ".0.0.1";
		String usage = "Usage: " + className +
		    		" [-conf fileName]";
		    
		BasicConfigurator.configure();
		System.out.println(version);
		if (args.length == 0) {
			System.err.println(usage);
			System.exit(0);
		}
		    
		for (int i = 0; i < args.length; i++) {       // parse command line
		    if (args[i].startsWith("-conf")) {
		    	confPath = args[++i];
		    }
		    if (args[i].startsWith("-tag")) {
		    	tag = args[++i];
			}
		}
		  
		StreamLauncher slauncher = new StreamLauncher(LOG, tag);
		dom = parseConfig(confPath);
		IOStream stream;
		//LOG.info(confPath + "\t" + dom.toString());
		
		Class streamClass;
		Constructor elemArgConstructor;
		Class[] elemArgClass = new Class[] { Element.class };
		Object[] elemArg;
		
		if (dom != null){
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("Stream");
			if(nl != null && nl.getLength() > 0) {
				for(int i = 0 ; i < nl.getLength();i++) {
					Element sl = (Element)nl.item(i);
					
					String type = sl.getAttribute("type");					
					elemArg = new Object[] { sl };
					streamClass = Class.forName("org.apache.ioscm." + type);
					elemArgConstructor = streamClass.getConstructor(elemArgClass);
					stream = (IOStream) createObject(elemArgConstructor, elemArg);
					
					LOG.info("main\t" + Integer.toString(i) + "\t" + type);
					slauncher.submit(stream);
					
				}
			}
		}
		slauncher.startAll();		  		    
		slauncher.awaitAll();
		System.exit(0);
	}
	  
	private static Object createObject(Constructor constructor,
		      Object[] arguments) {
		
		Object object = null;
		try {
			object = constructor.newInstance(arguments);
			return object;
		} catch (InstantiationException e) {
			System.out.println(e);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}
		return object;
	}

	public static Document parseConfig(String confPath) {
		  
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			return db.parse(confPath);

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}

		return null;
	}
}
