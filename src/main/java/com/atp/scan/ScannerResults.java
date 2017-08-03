package com.atp.scan;

import java.util.HashMap;
import java.util.Vector;

public class ScannerResults {
	
	private String stockSymbol;
	private HashMap<String, Vector<ScanResult>> scanResults;
	private Vector<String> scans;
	
	public ScannerResults(String stockSymbol) {
		this.stockSymbol = stockSymbol;
		scanResults = new HashMap<String, Vector<ScanResult>>();
		scans = new Vector<String>();
	}
	
	public void addScanResult(String scanName, Vector<ScanResult> scanResult) {
		scanResults.put(scanName, scanResult);
		scans.add(scanName);
	}
	
	public String getStockSymbol() {
		return stockSymbol;
	}
	
	public Vector<ScanResult> getScanResult(String scanName) {
		return scanResults.get(scanName);
	}
	
	public Vector<String> getScans() {
		return scans;
	}
	
}
