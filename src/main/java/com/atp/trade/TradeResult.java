package com.atp.trade;

public class TradeResult {
	
	private TradeResultStatus status;
	private double tradeValue;
	
	public TradeResult(TradeResultStatus status, double tradeValue) {
		super();
		this.status = status;
		this.tradeValue = tradeValue;
	}

	public TradeResultStatus getStatus() {
		return status;
	}

	public void setStatus(TradeResultStatus status) {
		this.status = status;
	}

	public double getTradeValue() {
		return tradeValue;
	}

	public void setTradeValue(double tradeValue) {
		this.tradeValue = tradeValue;
	}
	
	

}
