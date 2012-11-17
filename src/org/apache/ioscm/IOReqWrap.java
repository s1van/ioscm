package org.apahce.ioscm;


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
