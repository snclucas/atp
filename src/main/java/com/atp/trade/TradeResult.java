package com.atp.trade;

public class TradeResult {
	
	private int status;
	private double tradeValue;
	
	public TradeResult(int status, double tradeValue) {
		super();
		this.status = status;
		this.tradeValue = tradeValue;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getTradeValue() {
		return tradeValue;
	}

	public void setTradeValue(double tradeValue) {
		this.tradeValue = tradeValue;
	}
	
	

}
