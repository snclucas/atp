package com.atp.indicators;

import com.atp.data.QuoteHistory;


public class Statistics {

	public static double calcStanDev(int n, double[] s) {
		return Math.pow(calcVariance(n, s), 0.5);
	}


	public static double calcVariance(int n, double[] s) {
		double total = 0;
		double sTotal = 0;
		double scalar = 1/(double)(n-1);
		for (int i = 0; i < n; i++) {
			total += s[i];
			sTotal += Math.pow(s[i], 2);
		}
		return (scalar*(sTotal - (Math.pow(total, 2)/n)));
	}
	
	public static double calcVariance(QuoteHistory qh, int start,  int period) {
		
		double[] data = new double[period];
		int last = qh.size()-1;
		if(start<0) start = last;
		
		for(int i=0;i<period;i++) {
			data[i] = qh.getPriceBar(start - i).getClose();
		}
		
		return calcVariance(period, data);
	}
	

}
