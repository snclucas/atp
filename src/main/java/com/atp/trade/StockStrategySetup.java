package com.atp.trade;

import com.atp.portfolio.Portfolio;

public class StockStrategySetup{

	private double commissionPerTrade;
	private Portfolio portfolio;
	
	
	public StockStrategySetup(Portfolio portfolio, double commissionPerTrade) {
		this.portfolio = portfolio;
		this.commissionPerTrade = commissionPerTrade;
	}


	public Portfolio getPortfolio() {
		return portfolio;
	}


	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public double getCommissionPerTrade() {
		return commissionPerTrade;
	}


	public void setCommissionPerTrade(double commissionPerTrade) {
		this.commissionPerTrade = commissionPerTrade;
	}
	

}
