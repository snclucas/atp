package com.atp.portfolio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.atp.UniquelyIdentifiable;
import com.atp.logging.Message;
import com.atp.logging.Message.MessageType;
import com.atp.securities.Security;
import com.atp.trade.Trade;
import com.atp.trade.Trade.TradeAction;
import com.atp.trade.TradeSetup;


public class Position implements UniquelyIdentifiable {

	private List<Trade> trades;
	private double amount;
	private String symbol;
	private Security security;
	private String ID;

  private double takeProfitPrice;
  private double stopLossPrice;

	private LocalDateTime openPositionDate;

	
	public Position(Trade trade) {
		super();
		this.ID = UUID.randomUUID().toString();
		this.trades = new ArrayList<>();
		this.symbol = trade.getSecurity().getSymbol();
		this.security = trade.getSecurity();
		this.openPositionDate = LocalDateTime.now();
		addTrade(trade);
	}

	
	public String getUniqueID() {
		return ID;
	}


	public boolean isLong(){
	  return amount > 0;
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
		List<String> tradeIDs = new ArrayList<>();
		for(Trade trade : trades)
			tradeIDs.add(trade.getId());
		return tradeIDs.toArray(new String[tradeIDs.size()]);
	}

	public String getSymbol() {
		return this.symbol;
	}


  public LocalDateTime getOpenPositionDate() {
    return openPositionDate;
  }


  public List<LocalDateTime> getTradeDates() {
		List<LocalDateTime> tradeIDs = new ArrayList<LocalDateTime>();
		for(Trade trade : trades)
			tradeIDs.add(trade.getDate());
		return tradeIDs;
	}


	public List<Trade> getTrades() {
		return trades;
	}


	public Trade getCloseOutTrade() {
	  TradeSetup tradeSetup;
	  if(amount > 0)
      tradeSetup = new TradeSetup(amount, Trade.TradeType.SELL, TradeAction.TO_CLOSE);
	  else
      tradeSetup = new TradeSetup(amount, Trade.TradeType.BUY, TradeAction.TO_CLOSE);
	  return new Trade(getSecurity(),tradeSetup, -1, -1,  LocalDateTime.now());
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


  public double getTakeProfitPrice() {
    return takeProfitPrice;
  }


  public void setTakeProfitPrice(double takeProfitPrice) {
    this.takeProfitPrice = takeProfitPrice;
  }


  public double getStopLossPrice() {
    return stopLossPrice;
  }


  public void setStopLossPrice(double stopLossPrice) {
    this.stopLossPrice = stopLossPrice;
  }

	

}
