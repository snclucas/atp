package com.atp.trade;

import com.atp.trade.Trade.TradeAction;
import com.atp.trade.Trade.TradeType;

public class TradeSetup {
	
	private TradeType tradeType;
	private TradeAction tradeAction;
	private double amount;
	
	public TradeSetup(double amount, TradeType tradeType, TradeAction tradeAction) {
		super();
		this.tradeType = tradeType;
		this.tradeAction = tradeAction;
		this.amount = amount;
	}


	public TradeType getTradeType() {
		return tradeType;
	}


	public TradeAction getTradeAction() {
		return tradeAction;
	}


	public double getAmount() {
		return amount;
	}
	
}
