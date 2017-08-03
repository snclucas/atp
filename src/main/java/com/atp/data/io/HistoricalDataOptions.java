package com.atp.data.io;

import java.time.LocalDateTime;

public class HistoricalDataOptions {
	
	private String[] symbols;
	private LocalDateTime from; 
	private LocalDateTime to;
	private PriceHistory.Base base;
	private boolean latestFirst;
	
	
	public HistoricalDataOptions(String symbol, LocalDateTime from, LocalDateTime to, PriceHistory.Base base, boolean latestFirst) {
		this(new String[]{symbol}, from, to, base, latestFirst);
	}
	
	
	public HistoricalDataOptions(String[] symbols, LocalDateTime from, LocalDateTime to, PriceHistory.Base base, boolean latestFirst) {
		super();
		this.symbols = symbols;
		this.from = from;
		this.to = to;
		this.base = base;
		this.latestFirst = latestFirst;
	}

	
	public HistoricalDataOptions(String[] symbols, LocalDateTime from, LocalDateTime to, PriceHistory.Base base) {
		this(symbols, from, to, base, false);
	}
	
	
	public HistoricalDataOptions(String[] symbols, LocalDateTime date, PriceHistory.Base base) {
		this(symbols, date, date, base, false);
	}

	
	public String[] getSymbols() {
		return symbols;
	}


	public void setSymbols(String[] symbols) {
		this.symbols = symbols;
	}


	public LocalDateTime getFrom() {
		return from;
	}


	public void setFrom(LocalDateTime from) {
		this.from = from;
	}


	public LocalDateTime getTo() {
		return to;
	}


	public void setTo(LocalDateTime to) {
		this.to = to;
	}


	public PriceHistory.Base getBase() {
		return base;
	}


	public void setBase(PriceHistory.Base base) {
		this.base = base;
	}


	public boolean isLatestFirst() {
		return latestFirst;
	}


	public void setLatestFirst(boolean latestFirst) {
		this.latestFirst = latestFirst;
	}

}
