package main;

import java.math.BigInteger;
import java.util.ArrayList;

public class CircularList<Object> extends ArrayList<Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6134895320910063450L;
	
	public void move(Integer index, Integer offset) {
		if (offset == 0) return;
		int start = this.indexOf(index);
		this.remove(start);
		int dest = start + offset;
		while (dest < 0) {
			dest += this.size();
		}
		dest = dest % this.size();
		this.add(dest, (Object) index);
	}

	public void move(Integer index, Long offset) {
		if (offset == 0) return;
		int start = this.indexOf(index);
		this.remove(start);
		Integer intOffset = (int) (offset > 0 ? offset % this.size() : -(Math.abs(offset) % this.size()));		
		int dest = start + intOffset;
		while (dest < 0) {
			dest += this.size();
		}
		dest = dest % this.size();
		this.add(dest, (Object) index);
	}
	
	public void move(Integer index, BigInteger offset) {
		if (offset.compareTo(BigInteger.ZERO)==0) return;
		int start = this.indexOf(index);
		this.remove(start);
		BigInteger modOffset = (int) offset.compareTo(BigInteger.ZERO) == 1 
				? offset.remainder(BigInteger.valueOf((long) this.size())) 
				: offset.abs().remainder(BigInteger.valueOf((long) this.size())).negate() ;
		int intOffset = modOffset.intValueExact();
		int dest = start + intOffset;
		while (dest < 0) {
			dest += this.size();
		}
		dest = dest % this.size();
		this.add(dest, (Object) index);
	}
} 
