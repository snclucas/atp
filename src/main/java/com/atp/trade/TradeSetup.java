package com.atp.trade;

public class TradeSetup {
	
	private TradeType tradeType;
	private TradeAction tradeAction;
	private double amount;
	private double takeProfitPrice;
	private double stopLossPrice;
	
	public TradeSetup(double amount, TradeType tradeType, TradeAction tradeAction, double takeProfitPrice, double stopLossPrice) {
		super();
		this.tradeType = tradeType;
		this.tradeAction = tradeAction;
		this.amount = amount;
    this.takeProfitPrice = takeProfitPrice;
    this.stopLossPrice = stopLossPrice;
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

  public double getTakeProfitPrice() {
    return takeProfitPrice;
  }


  public double getStopLossPrice() {
    return stopLossPrice;
  }
	
}
