package com.atp.trade;


import junit.framework.TestCase;

import com.atp.portfolio.Portfolio;

public class TradeManagerTest extends TestCase {

	private TradingScheme tradingScheme;
	private Portfolio portfolio;
	

	protected void setUp() {
		tradingScheme = TradingSchemeFactory.getTradingScheme("simple");
		portfolio = new Portfolio("Test com.atp.portfolio", 1000);
	}
	
	
	public void testExecuteTrade() {
		TradeManager tradeManager = new TradeManager(tradingScheme, portfolio);
		
		assertEquals(tradeManager.canTrade(), true);
		
	
	}


}
