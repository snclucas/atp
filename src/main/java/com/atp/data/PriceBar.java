package com.atp.data;

import java.text.DecimalFormat;
import java.time.LocalDateTime;


public class PriceBar implements Comparable<PriceBar> {
	
	public enum Price { 
		OPEN("Open"), CLOSE("Close"), HIGH("High"), LOW("Low"), VOLUME("Volume");
		private String tag;
		Price(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};
	
	DecimalFormat df = new DecimalFormat("+0.000;-0.000");

	private String symbol;
	private LocalDateTime dateTime;
	private double volume;
	private double high;
	private double low;
	private double open;
	private double close;
	private double ret;



	public PriceBar(String symbol, LocalDateTime dateTime, double open, double high, double low, double close, double volume) {
		this.dateTime = dateTime;
		this.symbol = symbol;
		this.high = high;
		this.low = low;
		this.open = open;
		this.close = close;
		this.volume = volume;
	}


	public double getFormattedPrice(Price type) {
		switch(type) {
		case CLOSE: 
			return Double.parseDouble(df.format(getClose()));
		case OPEN: 
			return Double.parseDouble(df.format(getOpen()));
		case HIGH: 
			return Double.parseDouble(df.format(getHigh()));
		case LOW: 
			return Double.parseDouble(df.format(getLow()));
		default:
			return Double.parseDouble(df.format(getClose()));
		}
	}
	
	
	public double getRet() {
		return ret;
	}


	public void setRet(double ret) {
		this.ret = ret;
	}


	public double getPrice(Price type) {
		switch(type) {
		case CLOSE: 
			return getClose();
		case OPEN: 
			return getOpen();
		case HIGH: 
			return getHigh();
		case LOW: 
			return getLow();
		default:
			return getClose();
		}
	}


	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	public double getOpen() {
		return open;
	}
	public void setOpen(double open) {
		this.open = open;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDate(LocalDateTime date) {
		this.dateTime = date;
	}


	public long getDateLong() {
		return dateTime.getNano() / 1000000;
	}


	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}


	public static int WHITE = 0;
	public static int BLACK = 1;
	
	public int candleType() {
		if(close>open) return WHITE;
		return BLACK;
	}


	public boolean isWhiteCandle() {
		return (candleType() == WHITE);
	}


	public boolean isBlackCandle() {
		return (candleType() == BLACK);
	}


	public double getHighLow() {
		return (high - low);
	}


	public double getOpenClose() {
		return Math.abs(open - close);
	}


	public double getUpperStick() {
		if(open > close)
			return high - open;
		return high - close;
	}


	public double getLowerStick() {
		if(open > close)
			return close - low;
		return open - low;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	@Override
	public String toString() {
		return "PriceBar [symbol=" + symbol + ", date=" + dateTime + ", volume="
				+ volume + ", high=" + high + ", low=" + low + ", open=" + open
				+ ", close=" + close + "]";
	}


	@Override
	public int compareTo(PriceBar arg0) {
		return this.dateTime.compareTo(arg0.dateTime);
	}


}
