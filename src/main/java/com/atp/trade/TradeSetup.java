package com.atp.trade;

import com.atp.trade.Trade.Action;
import com.atp.trade.Trade.Type;

public class TradeSetup {
	
	private Trade.Type tradeType;
	private Action tradeAction;
	private double amount;
	
	public TradeSetup(double amount, Type tradeType, Action tradeAction) {
		super();
		this.tradeType = tradeType;
		this.tradeAction = tradeAction;
		this.amount = amount;
	}


	public Trade.Type getTradeType() {
		return tradeType;
	}


	public Action getTradeAction() {
		return tradeAction;
	}


	public double getAmount() {
		return amount;
	}
	
}
