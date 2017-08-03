package com.atp.maths;



import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;


@SuppressWarnings({ "unused", "deprecation" })
public class Stats {


	public static double variance(QuoteHistory qh,int window, int type) {
		return variance(qh, 0, window, type);
	}


	public static double priceMean(QuoteHistory qh, int index, int window, int type) {
		double[] data =  qh.getAllReturns(type);
		double[] resultData = new double[window];

		if(index>data.length-1) index = data.length-1;

		if(window>index) return 0.0;

		for(int i=0;i<window;i++) {
			resultData[i] = data[index-i];
		}
		return StatUtils.mean( resultData  );
	}

	public static double mean(QuoteHistory qh, int index, int window, int type) {
		double[] data =  qh.getAll(type);
		double[] resultData = new double[window];

		if(index>data.length-1) index = data.length-1;

		if(window>index) return 0.0;

		for(int i=0;i<window;i++) {
			resultData[i] = data[index-i];
		}
		return StatUtils.mean( resultData  );
	}


	public static double mean(double[] data, int window) {
		return mean(data,data.length-1,window);
	}

	public static double mean(double[] data) {
		return mean(data,data.length-1,data.length-1);
	}


	public static double mean(double[] data, int index, int window) {
		double[] resultData = new double[window];

		if(index>data.length-1) index = data.length-1;

		if(window>index) return 0.0;

		for(int i=0;i<window;i++) {
			resultData[i] = data[index-i];
		}
		return StatUtils.mean( resultData  );
	}

	public static double variance(QuoteHistory qh, int index, int window, int type) {
		double[] data = qh.getAllReturns(type);


		double[] resultData = new double[window];

		if(index>data.length-1) index = data.length-1;

		if(window>index) return 0.0;

		for(int i=0;i<window;i++) {
			resultData[i] = data[index-i];
		}
		return StatUtils.variance( resultData  );
	}

	public static double variance(List<PriceBar> priceBars, int window, int type) {

		double[] resultData = new double[window];

		if(window<priceBars.size()-1) return 0.0;

		for(int i=0;i<window;i++) {
			if(type==QuoteHistory.CLOSE) resultData[i] = priceBars.get(i).getClose();
			if(type==QuoteHistory.OPEN) resultData[i] = priceBars.get(i).getLow();
			if(type==QuoteHistory.HIGH) resultData[i] = priceBars.get(i).getHigh();
			if(type==QuoteHistory.LOW) resultData[i] = priceBars.get(i).getLow();
		}

		return StatUtils.variance(resultData);
	}

	public static double varianceReturns(List<PriceBar> priceBars, int window, int type) {

		double[] resultData = new double[window];

		if(window>priceBars.size()-1) return 0.0;

		for(int i=0;i<window;i++) {
			if(type==QuoteHistory.OPENCLOSE) resultData[i] = Math.abs(priceBars.get(i).getOpen() - priceBars.get(i).getClose());
			if(type==QuoteHistory.HIGHLOW) resultData[i] = Math.abs(priceBars.get(i).getHigh() - priceBars.get(i).getLow());
		}

		return StatUtils.variance(resultData);
	}

	public static double sd(List<PriceBar> priceBars, int window, int type) {
		return Math.sqrt(variance(priceBars,  window,  type));
	}

	public static double sdReturns(List<PriceBar> priceBars, int window, int type) {
		return Math.sqrt(varianceReturns(priceBars,  window,  type));
	}



	public static double priceVariance(QuoteHistory qh, int index, int window, int type, int logLinear) {
		double[] data = null;
		if(logLinear == QuoteHistory.LOG) {
			data = qh.getAllReturnsLog(type);
		}
		else {
			data = qh.getAllReturns(type);
		}

		double[] resultData = new double[window];

		if(index>data.length-1) index = data.length-1;

		if(window>index-1) return 0.0;

		for(int i=0;i<window;i++) {
			resultData[i] = data[index-i];
		}
		return StatUtils.variance( resultData  );
	}


	public static double priceSD(QuoteHistory qh, int index, int window, int type, int logLinear) {
		return Math.sqrt(priceVariance(qh, index, window, type, logLinear));	
	}


	public static double sd(QuoteHistory qh, int index, int window, int type) {
		return Math.sqrt(variance(qh, index, window, type));
	}

	public static double cumNormal(double mean, double sd, double value) {
		NormalDistribution n = DistributionFactory.newInstance().createNormalDistribution(mean, sd);
		try {
			return n.cumulativeProbability(value);
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static double invNormal(double mean, double sd, double value) {
		NormalDistribution n = DistributionFactory.newInstance().createNormalDistribution(mean, sd);
		try {
			return n.inverseCumulativeProbability(value);
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
