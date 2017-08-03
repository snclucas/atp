package com.atp.portfolio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.atp.UniquelyIdentifiable;
import com.atp.logging.Message;
import com.atp.logging.Message.MessageType;
import com.atp.securities.Security;
import com.atp.trade.Trade;
import com.atp.trade.Trade.TradeAction;

public class Position implements UniquelyIdentifiable {
	
	private Map<String, LimitOrder> limitOrders;
	private List<Trade> trades;
	private double amount;
	private String symbol;
	private Security security;
	private String ID;

	
	public Position(Trade trade) {
		super();
		this.ID = UUID.randomUUID().toString();
		this.trades = new ArrayList<Trade>();
		this.symbol = trade.getSecurity().getSymbol();
		this.security = trade.getSecurity();
		this.limitOrders = new HashMap<String, LimitOrder>();
		addTrade(trade);
	}
	
	
	public Position(Trade trade, Map<String, LimitOrder> limitOrders) {
		this(trade);
		this.limitOrders = limitOrders;
	}
	
	
	public String getUniqueID() {
		return ID;
	}
	
	
	public Message addTrade(Trade trade) {
		if(!trade.getSecurity().equals(this.security)) {
			return new Message(MessageType.FAILURE, LocalDateTime.now(), "Cannot add " + trade.getSecurity().getType() + " / " + trade.getSecurity().getSymbol() + " security type to existing " + 
					this.security.getType() + " / " + security.getSymbol() + "  position");
		}
		trades.add(trade);
		this.amount += trade.getAmount() * trade.getTradeType().getValue(); // Add to amount if BUY, subtract id SELL
		
		return new Message(MessageType.SUCCESS, LocalDateTime.now(), "Added " + 
							trade.getSecurity().getType() + " / " + 
							trade.getSecurity().getSymbol() + 
							" security type to existing " + 
				this.security.getType() + " / " + security.getSymbol() + "  position");
	}
	
	
	
	public LimitOrder addLimitOrder(String limitOrderName, LimitOrder limitOrder) {
		return limitOrders.put(limitOrderName, limitOrder);
	}
	
	
	public LimitOrder addStopLossLimitOrder(LimitOrder limitOrder) {
		return limitOrders.put(TradeAction.STOP_LOSS.getTag(), limitOrder);
	}
	
	
	public LimitOrder addTakeProfitLimitOrder(LimitOrder limitOrder) {
		return limitOrders.put(TradeAction.TAKE_PROFIT.getTag(), limitOrder);
	}

	
	public double getCurrentValueOfPosition(double currentMarketPrice) {
		double currentValue = 0.0;
		for(Trade trade : trades)
			currentValue += trade.getSecurity().getPayoff(currentMarketPrice);
		return currentValue;
	}
	
	
	public double getAmount() {
		return amount;
	}
	

	public Security getSecurity() {
		return security;
	}
	
	//Delegate methods

	public String[] getTradeIDs() {
		List<String> tradeIDs = new ArrayList<String>();
		for(Trade trade : trades)
			tradeIDs.add(trade.getId());
		return tradeIDs.toArray(new String[tradeIDs.size()]);
	}

	public String getSymbol() {
		return this.symbol;
	}



	public LocalDateTime[] getTradeDates() {
		List<LocalDateTime> tradeIDs = new ArrayList<LocalDateTime>();
		for(Trade trade : trades)
			tradeIDs.add(trade.getDate());
		return tradeIDs.toArray(new LocalDateTime[tradeIDs.size()]);
	}


	public List<Trade> getTrades() {
		return trades;
	}

	
	//Bookeeping utility methods

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	

}


/*
public Trade getCloseOutTrade(PriceBar priceBar, boolean assumeTriggeredLimits, TradeAction action) {
	TradeSide tradeSide = com.atp.com.atp.trade.getTradeSide();
	TradeType tradeType = com.atp.com.atp.trade.getTradeType();
	
	TradeSide closeTradeSide = TradeSide.LONG;
	TradeType closeTradeType = TradeType.BUY;
	
	if(tradeSide == TradeSide.LONG && tradeType == TradeType.BUY) {
		closeTradeType = TradeType.SELL;
		closeTradeSide = TradeSide.LONG;
	}
	
	if(tradeSide == TradeSide.SHORT && tradeType == TradeType.SELL) {
		closeTradeType = TradeType.BUY;
		closeTradeSide = TradeSide.LONG;
	}
	
	double price = (action==TradeAction.TAKE_PROFIT)?getTakeProfitPrice():getStopLossPrice();
	
	if(action == TradeAction.HOLDING_PERIOD) {
		price = priceBar.getClose();
	}

	TradeSetup closeTradeSetup = new TradeSetup(getSymbol(), assumeTriggeredLimits?price:priceBar.getClose(), getAmount(), closeTradeType, closeTradeSide);
	
	return new Trade(closeTradeSetup, TradeAction.TO_CLOSE, priceBar.getDate(), 0, 0);
}
*/
