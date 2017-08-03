package com.atp;

import java.time.LocalDateTime;

import com.atp.commission.BasicCommissionScheme;
import com.atp.commission.CommissionScheme;
import com.atp.data.PriceBar;
import com.atp.portfolio.Portfolio;
import com.atp.portfolio.Position;
import com.atp.securities.Security;
import com.atp.securities.SecurityFactory;
import com.atp.trade.Trade;
import com.atp.trade.Trade.TradeAction;
import com.atp.trade.Trade.TradeType;
import com.atp.trade.TradeManager;
import com.atp.trade.TradeResult;
import com.atp.trade.TradeSetup;
import com.atp.trade.TradingScheme;

import junit.framework.TestCase;

public class TradeTests extends TestCase {

	private Portfolio portfolio;
	private TradeManager tm;
	private TradingScheme tradingScheme;
	double initialCash = 10000;
	int allowedTrades = 1;
	private LocalDateTime tradeDate  = LocalDateTime.of(2011,1,1,1,1,1,1);
	private LocalDateTime tradeDate2 = LocalDateTime.of(2012,1,1,1,1,1,1);
	private double stopLoss = 0.08;
	private double takeProfit = 0.16;
	private PriceBar priceBar;
	private CommissionScheme commissionScheme = new BasicCommissionScheme(12, 0);

	protected void setUp() {
		portfolio = new Portfolio("Test portfolio", initialCash, allowedTrades);
		tradingScheme = new TradingScheme(stopLoss, takeProfit, 0.02,1,commissionScheme, false);
		tm = new TradeManager(tradingScheme, portfolio);
		
		priceBar = new PriceBar("Test",tradeDate, 34,54,22,67,100000);
	}
	
	
	public void testCloseOutTrade() {
		tradingScheme.setNumConcurrentPositions(2);
		Trade trade;
		TradeResult tradeResult;
		
		double tradeAmount = 100.0;
		TradeSetup tradeSetup = new TradeSetup(tradeAmount, TradeType.BUY, TradeAction.TO_OPEN);
		
		trade = new Trade("Test1", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade, tradeDate);
		assertEquals( TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		
		Trade closeTrade = tm.getPortfolio().getPosition(trade.getId()).getCloseOutTrade(priceBar, true, Trade.TAKE_PROFIT);
		assertEquals(TradeType.SELL, closeTrade.getTradeType());
		assertEquals(Trade.LONG, closeTrade.getLongOrShort());
		assertEquals(Trade.CLOSE, closeTrade.getAction());
		assertEquals(priceBar.getLocalDateTime(),closeTrade.getLocalDateTime());
		assertEquals(priceBar.getClose(),closeTrade.getPrice());
		assertEquals(1,closeTrade.getNumShares());
		assertEquals(tradingScheme.getStopLoss(),closeTrade.getStopLossPercent());
		assertEquals(tradingScheme.getTakeProfit(),closeTrade.getTakeProfitPercent());
		
		
		trade = new Trade("Test2", TradeType.SELL, Trade.SHORT, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade, tradeDate);
		assertEquals( TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		
		Trade closeTrade2 = tm.getPortfolio().getPosition(trade.getId()).getCloseOutTrade(priceBar, true, Trade.TAKE_PROFIT);
		assertEquals(TradeType.BUY,closeTrade2.getTradeType());
		assertEquals(TradeSide.LONG,closeTrade2.getLongOrShort());
		assertEquals(Trade.CLOSE,closeTrade2.getAction());
		assertEquals(priceBar.getLocalDateTime(),closeTrade2.getLocalDateTime());
		assertEquals(priceBar.getClose(),closeTrade2.getPrice());
		assertEquals(1,closeTrade2.getNumShares());
		assertEquals(tradingScheme.getStopLoss(),closeTrade2.getStopLossPercent());
		assertEquals(tradingScheme.getTakeProfit(),closeTrade2.getTakeProfitPercent());
	}

	/* Tests the correct trade value for each combination of trades
	 * BUY/LONG, BUY/SHORT, SELL/LONG and SELL/SHORT
	 * */
	public void testTradeValue () {
		tradingScheme.setNumConcurrentPositions(2);
		TradeResult tradeResult;
		
		TradeSetup tradeSetup = new TradeSetup(1, TradeType.BUY, TradeAction.TO_OPEN);
		Security security = SecurityFactory.getStock("TEST", 100.0);
		Trade trade1 = new Trade(security, tradeSetup, tradeDate);
		tradeResult = tm.execute(trade1, tradeDate);
		assertEquals( TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		assertEquals(initialCash-(100*1)-commissionScheme.getBuyCommission(), portfolio.getCash());

		//reset cash to make math easier
		tm.getPortfolio().setCash(initialCash);
		
		Trade trade2 = new Trade("Test2", TradeType.SELL, Trade.SHORT, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade2, tradeDate);
		assertEquals( TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		assertEquals(initialCash+(100*1),portfolio.getCash());
	
		//reset cash to make math easier
		tm.getPortfolio().setCash(initialCash);
		
		Trade trade3 = new Trade(trade1.getId(), "Test1", TradeType.SELL, Trade.LONG, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade3, tradeDate);
		assertEquals( TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		assertEquals(initialCash+(100*1)-sellCommission,portfolio.getCash());
		
		//reset cash to make math easier
		tm.getPortfolio().setCash(initialCash);
		
		
		Trade trade4 = new Trade(trade2.getId(), "Test2", TradeType.BUY, Trade.SHORT, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade4, tradeDate);
		assertEquals( TradeManager.INVALID_TRADE, tradeResult.getStatus());
		
		
	}
	
	public void testTradeBasics() {
		
		Trade trade = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,100,100, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		
		assertEquals(TradeType.BUY, trade.getTradeType());
		assertEquals("BUY", trade.getBuyOrSellStr());
		
		assertEquals(Trade.LONG, trade.getLongOrShort());
		assertEquals("LONG", trade.getLongOrShortStr());
		
		assertEquals(Trade.ENTRY, trade.getAction());
		assertEquals("ENTRY", trade.getActionStr());
		
		
		trade = new Trade("Test", TradeType.SELL_SHORT, Trade.SHORT, Trade.EXIT, tradeDate,100,100, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		
		assertEquals(TradeType.SELL, trade.getTradeType());
		assertEquals("SELL", trade.getTradeType().getTag());
		
		assertEquals(Trade.SHORT, trade.getLongOrShort());
		assertEquals("SHORT", trade.getLongOrShortStr());
		
		assertEquals(Trade.EXIT, trade.getAction());
		assertEquals("EXIT", trade.getActionStr());
		
		trade = new Trade("Test", TradeType.SELL, Trade.SHORT, Trade.STOP_LOSS, tradeDate,100,100, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		
		assertEquals(Trade.STOP_LOSS, trade.getAction());
		assertEquals("STOP LOSS", trade.getActionStr());
		
		trade = new Trade("Test", TradeType.SELL, Trade.SHORT, Trade.TAKE_PROFIT, tradeDate,100,100, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		
		assertEquals(Trade.TAKE_PROFIT, trade.getAction());
		assertEquals("TAKE PROFIT", trade.getActionStr());
	}
	
	public void testPortfolio() {
		System.err.println("Starting testPortfolio");
		int numShares = 100;

		Trade trade = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,10,100, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		Trade trade2 = new Trade("Test2", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,10,100, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		
		Position p1 = new Position(trade);
		Position p2 = new Position(trade2);

		assertEquals(null, portfolio.getPosition(p1.getID()));
		portfolio.addPosition(p1);
		assertEquals(p1, portfolio.getPosition(p1.getID()));
		assertEquals(numShares, portfolio.getPosition(p1.getID()).getNumShares());

		portfolio.addPosition(p2);

		assertEquals(p1, portfolio.getPosition(p1.getID()));
		assertEquals(p2, portfolio.getPosition(p2.getID()));

		assertEquals(2, portfolio.numPositions());

		//portfolio.getPositions(trade.getId());

		assertEquals(true, portfolio.removePosition(p1.getID()));

		assertEquals(1, portfolio.numPositions());

		portfolio.removePosition(p2.getID());
		assertEquals(0, portfolio.numPositions());
	}
	
	
	public void testTradeShort() {
		
		TradeResult tradeResult;
		
		double price = 10.0;
		int numShares = 10;

		double expectPortfolioCash = 0;
		
		Trade trade1 = new Trade("Test", TradeType.SELL, Trade.SHORT, Trade.ENTRY, tradeDate,price,numShares, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade1, tradeDate);
		
		assertEquals(TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		
		
	}


	public void testTradeLong() {
		System.err.println("Starting testTradeLong");
		TradeResult tradeResult;

		double price = 10.0;
		int numShares = 10;

		double expectPortfolioCash = 0;

		Trade trade1 = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,price,numShares, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade1, tradeDate);

		assertEquals(tradeResult.getStatus(), TradeManager.SUCCSESFUL_TRADE);

		expectPortfolioCash = initialCash - (price*numShares) - buyCommission;
		assertEquals(expectPortfolioCash,portfolio.getCash());

		//Try to add another position, this should fail
		Trade trade2 = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate2,price,numShares, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade2, tradeDate);
		assertEquals(tradeResult.getStatus(), TradeManager.NO_MORE_POSITIONS_ALLOWED);


		//Lets try to sell a stock we dont have
		Trade trade3 = new Trade("Test2", TradeType.SELL, Trade.LONG, Trade.EXIT, new LocalDateTime(),price,numShares, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade3, tradeDate);
		//Check for correct status
		assertEquals(TradeManager.NO_POSITION_TO_CLOSE, tradeResult.getStatus());
		//Portfolio cash should be unchanged.
		assertEquals(expectPortfolioCash,portfolio.getCash());


		//Sell the stock
		price = 11;
		//trade = new Trade("Test", TradeType.SELL, Trade.LONG, Trade.EXIT, tradeDate,price,numShares, ts.getStopLoss(), ts.getTakeProfit());
		
		Trade trade4 = portfolio.getPosition(trade1.getId()).getCloseOutTrade(priceBar, true, Trade.TAKE_PROFIT);
		
		tradeResult = tm.execute(trade4, tradeDate);

		assertEquals(TradeManager.SUCCSESFUL_TRADE,tradeResult.getStatus());

		expectPortfolioCash = expectPortfolioCash + (priceBar.getClose()*numShares) - sellCommission;

		assertEquals(expectPortfolioCash,portfolio.getCash());
	}

	public void testExitCondtions() {
		System.err.println("Starting testExitCondtions");
		
		double close = 100;
		Trade trade = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		TradeResult tradeResult = tm.execute(trade, tradeDate);
		assertEquals(tradeResult.getStatus(), TradeManager.SUCCSESFUL_TRADE);

		tm.checkExitConditions(new PriceBar("Test",tradeDate, 100,100,100,close,100000));

		int num = portfolio.getPositionsWithSymbol(trade.getSymbol()).size();
		//Should not have triggered stop loss
		assertEquals(1, num);

		close = (1-0.09)*close;

		tm.checkExitConditions(new PriceBar("Test",tradeDate, 100,100,100,close,100000));

		num = portfolio.getNumPositions("Test");
		//Should  have triggered stop loss
		assertEquals(0, num);
		
		
		//Test the take profit exit
		trade = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,100,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade, tradeDate);
		assertEquals(tradeResult.getStatus(), TradeManager.SUCCSESFUL_TRADE);
		
		close = 100;
		close = (1+0.18)*close;
		
		tm.checkExitConditions(new PriceBar("Test",tradeDate, 100,100,100,close,100000));

		num = portfolio.getNumPositions("Test");
		//Should  have triggered take profit
		assertEquals(0, num);
	}

	
	public void testStopLossTakePercent() {
		
		double close = 100;
		Trade trade1 = new Trade("Test", TradeType.BUY, Trade.LONG, Trade.ENTRY, tradeDate,close,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		TradeResult tradeResult = tm.execute(trade1, tradeDate);
		assertEquals(TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		
		assertEquals(116.0, trade1.getTakeProfitPrice());
		assertEquals(92.0, trade1.getStopLossPrice());
		
		portfolio.removePosition(trade1.getId());
		
		Trade trade2 = new Trade("Test2", TradeType.SELL, Trade.SHORT, Trade.ENTRY, tradeDate,close,1, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit());
		tradeResult = tm.execute(trade2, tradeDate);
		assertEquals(TradeManager.SUCCSESFUL_TRADE, tradeResult.getStatus());
		
		assertEquals(84.0, trade2.getTakeProfitPrice());
		assertEquals(108.0, trade2.getStopLossPrice());
		
		
	}


}
