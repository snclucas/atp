package com.atp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Holds and validates the priceBar history for a com.atp.strategy.
 */
public class QuoteHistory {

	private static final String lineSep = System.getProperty("line.separator");

	public static int LOG =  0;
	public static int LINEAR = 1;

	public static int BUY =  10;
	public static int SELL = -10;

	public static int OPEN = 1;
	public static int CLOSE = 2;
	public static int HIGH = 3;
	public static int LOW = 4;
	
	

	public static int HIGHLOW = 1;
	public static int OPENCLOSE = 2;

	public static int MONTHLY = 0;
	public static int WEEKLY = 1;
	public static int DAILY = 2;
	public static int HOURLY = 3;
	public static int FIVEMIN = 4;
	public static int ONEMIN = 5;
	public static int TICKER = 6;

	private final List<PriceBar> priceBars;
	private final List<String> validationMessages;
	private final String symbol;
	private double priceAction;
	private int timeBase;


	public QuoteHistory(String symbol, int timeBase) {
		this.symbol = symbol;
		priceBars = new ArrayList<PriceBar>();
		validationMessages = new ArrayList<String>();
		this.timeBase = timeBase;
	}

	public List<PriceBar> getPriceBars() {
		return priceBars;
	}

	public List<PriceBar> getAll() {
		return priceBars;
	}


	public String getSymbol() {
		return symbol;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (PriceBar priceBar : priceBars) {
			sb.append(priceBar).append(lineSep);
		}

		return sb.toString();
	}
	

	public void reverseData() {
		Collections.reverse(priceBars);
	}

	public List<String> getValidationMessages() {
		return validationMessages;
	}

	public int size() {
		return priceBars.size();
	}

	public void addHistoricalPriceBar(PriceBar priceBar) {
		if(isValid(priceBar)) priceBars.add(priceBar);
	}

	public PriceBar getPriceBar(int index) {
		return priceBars.get(index);
	}

	public int getSize() {
		return priceBars.size();
	}

	public PriceBar getLastPriceBar() {
		return priceBars.get(priceBars.size() - 1);
	}

	public PriceBar getFirstPriceBar() {
		return priceBars.get(0);
	}


	public double[] getAll(int type) {
		List<PriceBar> bars = getAll();
		double[] data = new double[bars.size()];
		for(int i=0;i<data.length;i++) {
			if(type == OPEN)
				data[i] = bars.get(i).getOpen();
			if(type == CLOSE)
				data[i] = bars.get(i).getClose();
			if(type == HIGH)
				data[i] = bars.get(i).getHigh();
			if(type == LOW)
				data[i] = bars.get(i).getLow();
		}
		return data;
	}

	public double[] getAllReturns(int type) {
		List<PriceBar> bars = getAll();
		double[] data = new double[bars.size()];
		for(int i=1;i<data.length;i++) {
			if(type == OPEN)
				data[i] = bars.get(i).getOpen()-bars.get(i-1).getOpen();
			if(type == CLOSE)
				data[i] = bars.get(i).getClose()-bars.get(i-1).getClose();
			if(type == HIGH)
				data[i] = bars.get(i).getHigh()-bars.get(i-1).getHigh();
			if(type == LOW)
				data[i] = bars.get(i).getLow()-bars.get(i-1).getLow();
		}
		return data;
	}

	public double[] getAllReturnsLog(int type) {
		List<PriceBar> bars = getAll();
		double[] data = new double[bars.size()];
		for(int i=1;i<data.length;i++) {
			if(type == OPEN)
				data[i] = Math.log(bars.get(i).getOpen() / bars.get(i-1).getOpen());
			if(type == CLOSE)
				data[i] = Math.log(bars.get(i).getClose() / bars.get(i-1).getClose());
			if(type == HIGH)
				data[i] = Math.log(bars.get(i).getHigh() / bars.get(i-1).getHigh());
			if(type == LOW)
				data[i] = Math.log(bars.get(i).getLow() / bars.get(i-1).getLow());
		}
		return data;
	}


	public double getPriceAction() {
		return priceAction;
	}

	public void setPriceAction(double priceAction) {
		this.priceAction = priceAction;
	}

	public int getTimeBase() {
		return timeBase;
	}

	public void setTimeBase(int timeBase) {
		this.timeBase = timeBase;
	}
	
	
	private boolean isValid(PriceBar priceBar) {
		if(priceBar.getOpen()>=0 && priceBar.getClose() >= 0 &&
				priceBar.getHigh() >= 0 && priceBar.getLow() >= 0 &&
				priceBar.getHigh()>=priceBar.getLow())
			return true;
		return false;
	}

}
