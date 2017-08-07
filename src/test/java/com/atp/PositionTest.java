package com.atp;

import java.time.LocalDateTime;

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
import com.atp.trade.Trade.Action;
import com.atp.trade.Trade.Type;
import com.atp.trade.TradeSetup;

public class PositionTest extends TestCase {
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testAddingTradesToPortfolio() {
		Security security1 = SecurityFactory.getStock("GOOG", 654.34);
		
		double tradeAmount = 100.0;
		TradeSetup tradeSetup = new TradeSetup(tradeAmount, Type.BUY, Action.TO_OPEN);
		Trade trade1 = new Trade(security1, tradeSetup,  security1.getBookCost()*1.08, security1.getBookCost()*0.95,  LocalDateTime.now());
		
		Position position = new Position(trade1);
		assertEquals("Trade amount not correct", tradeAmount, position.getAmount());
		assertEquals("Trade security not correct", security1, position.getSecurity());
		
		Security security2 = SecurityFactory.getStock("MSFT", 54.34);
		Trade trade2 = new Trade(security2, tradeSetup,  security2.getBookCost()*1.08, security2.getBookCost()*0.95,  LocalDateTime.now());
		Message message1 = position.addTrade(trade2);
		assertEquals("Should have failure message", MessageType.FAILURE, message1.getMessageType());		
		
		Security security3 = SecurityFactory.getStock("GOOG", 54.34);
		Trade trade3 = new Trade(security3, tradeSetup,  security3.getBookCost()*1.08, security3.getBookCost()*0.95,  LocalDateTime.now());
		Message message2 = position.addTrade(trade3);
		assertEquals("Should have success message", MessageType.SUCCESS, message2.getMessageType());	
	}


}
