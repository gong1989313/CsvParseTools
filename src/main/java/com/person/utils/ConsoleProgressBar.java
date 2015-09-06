package com.person.utils;

import java.text.DecimalFormat;

public class ConsoleProgressBar {
	private long minimum = 0;
	private long maximum = 100; 
	private long barLen = 100; 
	private char showChar = '='; 
	private long record = -1;
	private DecimalFormat formater = new DecimalFormat("#.#%");
	
	/**
	 * user system out
	 */
	public ConsoleProgressBar() {
	}
	
	/**
	 * @param minimum init value
	 * @param maximum max value
	 * @param barLen bar lenth
	 */
	public ConsoleProgressBar(long minimum, long maximum, 
	        				  long barLen) {
	    this(minimum, maximum, barLen, '=');
	}
	
	/**
	 * put rate
	 * 
	 * @param minimum init value
	 * @param maximum max value
	 * @param barLen bar lenth
	 * @param showChar show char
	 */
	public ConsoleProgressBar(long minimum, long maximum, 
	        				  long barLen, char showChar) {
	    this.minimum = minimum;
	    this.maximum = maximum;
	    this.barLen = barLen;
	    this.showChar = showChar;
	}
	
	/**
	 * show progress Bar¡£
	 * 
	 * @param value current (start <= current <= end)¡£
	 */
	public void show(long value) {
	    if (value < minimum || value > maximum) {
	        return;
	    }
	    minimum = value;
	    float rate = (float) (minimum*1.00 / maximum);
	    long len = (long) (rate * barLen);
	    if(record != len){
	    	if(len == 0){
	    		draw(len, rate, value);
	    	}else{
		    	reset();
		    	draw(len, rate, value);
	    	}
	    }
	    record = len;
	    if (minimum == maximum) {
	        afterComplete();
	    }
	}
	
	private void draw(long len, float rate, long curr) {
	    for (int i = 0; i < len; i++) {
	        System.out.print(showChar);
	    }
	    System.out.print(' ');
	    System.out.print(format(rate));
	}
	
	private void reset() {
	    System.out.print('\r');
	}
	
	private void afterComplete() {
	    System.out.print('\n');
	}
	
	private String format(float num) {
	    return formater.format(num);
	}
}
