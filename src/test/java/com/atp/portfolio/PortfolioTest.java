package com.atp.portfolio;


import java.time.LocalDateTime;

import com.atp.data.PriceBar;
import com.atp.trade.TradeAction;
import com.atp.trade.TradeType;
import junit.framework.TestCase;

import com.atp.securities.Security;
import com.atp.securities.SecurityFactory;
import com.atp.trade.Trade;
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
		TradeSetup tradeSetup1 = new TradeSetup(tradeAmount, TradeType.BUY, TradeAction.TO_OPEN, security1.getBookCost()*1.08, security1.getBookCost()*0.95);
    TradeSetup tradeSetup2 = new TradeSetup(tradeAmount, TradeType.BUY, TradeAction.TO_OPEN, security2.getBookCost()*1.08, security2.getBookCost()*0.95);

		PriceBar priceBar1 = new PriceBar("Test", LocalDateTime.now(), 100,100,100, 100,100000);
		PriceBar priceBar2 = new PriceBar("Test", LocalDateTime.now(), 100,100,100, 100,100000);
		PriceBar priceBar3 = new PriceBar("Test", LocalDateTime.now(), 100,100,100, 100,100000);

		Trade trade1 = new Trade(security1, priceBar1, tradeSetup1);
		Trade trade2 = new Trade(security2, priceBar2, tradeSetup2);
		
		portfolio.addTrade(trade1);
		
		double cashAfterTrade1 = initialCash - tradeAmount*security1.getBookCost();
		assertEquals("Wrong initial cash after 1 com.atp.trade", cashAfterTrade1, portfolio.getCash());
		
		portfolio.addTrade(trade2);
		double cashAfterTrade2 = cashAfterTrade1 - tradeAmount*security2.getBookCost();
		assertEquals("Wrong initial cash after 2 com.atp.trade", cashAfterTrade2, portfolio.getCash());
		
		TradeSetup tradeSetup3 = new TradeSetup(tradeAmount, TradeType.SELL, TradeAction.TO_OPEN, security3.getBookCost()*1.08, security3.getBookCost()*0.95);

		Trade trade3 = new Trade(security3, priceBar3, tradeSetup3);
		portfolio.addTrade(trade3);
		double cashAfterTrade3 = cashAfterTrade2 + tradeAmount*security3.getBookCost();
		assertEquals("Wrong initial cash after 3 com.atp.trade", cashAfterTrade3, portfolio.getCash());
		
		
		assertEquals("Number of positions should be 2", 2, portfolio.getNumPositions());
	
	}


}
