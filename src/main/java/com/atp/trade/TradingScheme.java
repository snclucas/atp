package com.atp.trade;

import com.atp.commission.CommissionScheme;

public class TradingScheme {

	private double maxCapitalPerTradeAsPercent;
	private int numberOfConcurrentPositions;
	private double stopLoss;
	private double takeProfit;
	private boolean allowShortSelling = false;
	private int maxHoldingPeriod = 8;
	private CommissionScheme commissionScheme;

	public TradingScheme(double stopLoss, double takeProfit, double maxCapitalPerTradeAsPercent, int numberOfConcurrentPositions, 
			CommissionScheme commissionScheme, boolean allowShortSelling) {
		this.maxCapitalPerTradeAsPercent = maxCapitalPerTradeAsPercent;
		this.numberOfConcurrentPositions = numberOfConcurrentPositions;
		this.stopLoss = stopLoss;
		this.takeProfit = takeProfit;
		this.commissionScheme = commissionScheme;
		this.allowShortSelling = allowShortSelling;
	}


	public int getNumberOfConcurrentPositions() {
		return numberOfConcurrentPositions;
	}


	public void setNumConcurrentPositions(int numConcurrentPositions) {
		this.numberOfConcurrentPositions = numConcurrentPositions;
	}


	public double getMaxCapitalPerTrade() {
		return maxCapitalPerTradeAsPercent;
	}

	public void setMaxCapitalPerTrade(double maxCapitalPerTrade) {
		this.maxCapitalPerTradeAsPercent = maxCapitalPerTrade;
	}


	public double getCommission(TradeSetup tradeSetup) {
		return commissionScheme.getCommission(tradeSetup);
	}

	public void setCommission(CommissionScheme commissionScheme) {
		this.commissionScheme = commissionScheme;
	}


	public double getStopLoss() {
		return stopLoss;
	}


	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}


	public double getTakeProfit() {
		return takeProfit;
	}


	public void setTakeProfit(double takeProfit) {
		this.takeProfit = takeProfit;
	}


	public boolean allowShortSelling() {
		return allowShortSelling;
	}


	public void allowShortSelling(boolean allowShorts) {
		this.allowShortSelling = allowShorts;
	}


	public int getMaxHoldingPeriod() {
		return maxHoldingPeriod;
	}


	public void setMaxHoldingPeriod(int maxHoldingPeriod) {
		this.maxHoldingPeriod = maxHoldingPeriod;
	}

}
