package com.atp.scan;

import java.time.LocalDateTime;

public class ScanResult {

	public static int BULLISH =  1;
	public static int SIGNAL = 0;
	public static int BEARISH = -1;
	public static int NOTHING = -99;
	
	private LocalDateTime dateTime;
	private String symbol;
	private String scan;
	private double result;
	private int bullOrBear;
	private double pcOfMax;


	public ScanResult(LocalDateTime dateTime, String symbol, String scan, double result, int bullOrBear, double pcOfMax) {
		super();
		this.dateTime = dateTime;
		this.symbol = symbol;
		this.scan = scan;
		this.result = result;
		this.bullOrBear = bullOrBear;
		this.pcOfMax = pcOfMax;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getScan() {
		return scan;
	}


	public void setScan(String scan) {
		this.scan = scan;
	}


	public double getResult() {
		return result;
	}


	public void setResult(double result) {
		this.result = result;
	}


	public int getBullOrBear() {
		return bullOrBear;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public void setDate(LocalDateTime date) {
		this.dateTime = date;
	}


	public double getPcOfMax() {
		return pcOfMax;
	}


	public void setPcOfMax(double pcOfMax) {
		this.pcOfMax = pcOfMax;
	}

	
}
