package org.apache.ioscm;
import java.util.Random;

public class Tools {
	static long nextLong(Random rng, long n) {
		   // error checking and 2^x checking removed for simplicity.
		   long bits, val;
		   do {
		      bits = (rng.nextLong() << 1) >>> 1;
		      val = bits % n;
		   } while (bits-val+(n-1) < 0L);
		   return val;
		}
}
