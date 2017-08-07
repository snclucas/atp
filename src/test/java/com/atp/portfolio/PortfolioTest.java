package com.atp.portfolio;


import java.time.LocalDateTime;

import junit.framework.TestCase;

import com.atp.securities.Security;
import com.atp.securities.SecurityFactory;
import com.atp.trade.Trade;
import com.atp.trade.Trade.Action;
import com.atp.trade.Trade.Type;
import com.atp.trade.TradeSetup;

public class PortfolioTest extends TestCase {

	private Portfolio portfolio;
	double initialCash = 10000;
	int numberOfAllowedConcurrentTrades = 1;

	protected void setUp() {
		portfolio = new Portfolio("Test com.atp.portfolio", initialCash);
	}
	
	
	public void testAddingTradesToPortfolio() {
		
		Security security1 = SecurityFactory.getStock("GOOG", 654.34);
		Security security2 = SecurityFactory.getStock("MSFT", 54.34);
		Security security3 = SecurityFactory.getStock("MSFT", 64.34);
		
		double tradeAmount = 1.0;
		TradeSetup tradeSetup = new TradeSetup(tradeAmount, Type.BUY, Action.TO_OPEN);
		Trade trade1 = new Trade(security1, tradeSetup, security1.getBookCost()*1.08, security1.getBookCost()*0.95,  LocalDateTime.now());
		Trade trade2 = new Trade(security2, tradeSetup, security2.getBookCost()*1.08, security2.getBookCost()*0.95,  LocalDateTime.now());
		
		portfolio.addTrade(trade1);
		
		double cashAfterTrade1 = initialCash - tradeAmount*security1.getBookCost();
		assertEquals("Wrong initial cash after 1 com.atp.trade", cashAfterTrade1, portfolio.getCash());
		
		portfolio.addTrade(trade2);
		double cashAfterTrade2 = cashAfterTrade1 - tradeAmount*security2.getBookCost();
		assertEquals("Wrong initial cash after 2 com.atp.trade", cashAfterTrade2, portfolio.getCash());
		
		TradeSetup tradeSetup2 = new TradeSetup(tradeAmount, Type.SELL, Action.TO_OPEN);
		Trade trade3 = new Trade(security3, tradeSetup2, security3.getBookCost()*1.08, security3.getBookCost()*0.95, LocalDateTime.now());
		portfolio.addTrade(trade3);
		double cashAfterTrade3 = cashAfterTrade2 + tradeAmount*security3.getBookCost();
		assertEquals("Wrong initial cash after 3 com.atp.trade", cashAfterTrade3, portfolio.getCash());
		
		
		assertEquals("Number of positions should be 2", 2, portfolio.getNumPositions());
	
	}


}
