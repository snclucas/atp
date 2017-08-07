package com.atp;

import java.time.LocalDateTime;

import com.atp.data.PriceBar;
import com.atp.trade.TradeAction;
import com.atp.trade.TradeType;
import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.atp.logging.Message;
import com.atp.logging.Message.MessageType;
import com.atp.portfolio.Position;
import com.atp.securities.Security;
import com.atp.securities.SecurityFactory;
import com.atp.trade.Trade;
import com.atp.trade.TradeSetup;

public class PositionTest extends TestCase {
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testAddingTradesToPortfolio() {
    double tradeAmount = 100.0;

		Security security1 = SecurityFactory.getStock("goog", 654.34);
    Security security2 = SecurityFactory.getStock("MSFT", 54.34);
    Security security3 = SecurityFactory.getStock("coke", 6540.34);


		PriceBar priceBar1 = new PriceBar("Test", LocalDateTime.now(), 100,100,100, 100,100000);
		PriceBar priceBar2 = new PriceBar("Test", LocalDateTime.now(), 100,100,100, 100,100000);
		PriceBar priceBar3 = new PriceBar("Test", LocalDateTime.now(), 100,100,100, 100,100000);

    TradeSetup tradeSetup1 = new TradeSetup(tradeAmount, TradeType.BUY, TradeAction.TO_OPEN, security1.getBookCost()*1.08, security1.getBookCost()*0.95);
    TradeSetup tradeSetup2 = new TradeSetup(tradeAmount, TradeType.BUY, TradeAction.TO_OPEN, security2.getBookCost()*1.08, security2.getBookCost()*0.95);
    TradeSetup tradeSetup3 = new TradeSetup(tradeAmount, TradeType.BUY, TradeAction.TO_OPEN, security3.getBookCost()*1.08, security3.getBookCost()*0.95);


		Trade trade1 = new Trade(security1, priceBar1, tradeSetup1);
		
		Position position = new Position(trade1);
		assertEquals("Trade amount not correct", tradeAmount, position.getAmount());
		assertEquals("Trade security not correct", security1, position.getSecurity());
		

		Trade trade2 = new Trade(security2, priceBar2, tradeSetup2);
		Message message1 = position.addTrade(trade2);
		assertEquals("Should have failure message", MessageType.FAILURE, message1.getMessageType());		


		Trade trade3 = new Trade(security3, priceBar3, tradeSetup3);
		Message message2 = position.addTrade(trade3);
		assertEquals("Should have success message", MessageType.SUCCESS, message2.getMessageType());	
	}


}
