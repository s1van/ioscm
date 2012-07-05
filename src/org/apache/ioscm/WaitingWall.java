package org.apache.ioscm;

import java.util.concurrent.atomic.AtomicInteger;

public class WaitingWall {
	AtomicInteger b4wall;
	
	public WaitingWall() {
		b4wall = new AtomicInteger(0);
	}
	
	void collapse(int wt){
		synchronized(this){
			try {
				while (b4wall.get() > 0)
					this.wait(512);
				this.wait(wt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.notifyAll();
		}
	}
	
	void hit(){
		synchronized(this){
			try {
				b4wall.decrementAndGet();
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void hitAndPass(){
		b4wall.decrementAndGet();
	}
	
	void ready2pass(){
		b4wall.incrementAndGet();
	}
}
