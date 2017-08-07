package com.atp.trade;

import java.time.LocalDateTime;
import java.util.UUID;

import com.atp.UniquelyIdentifiable;
import com.atp.securities.Security;

public class Trade implements UniquelyIdentifiable {

	public enum Type {
		BUY("Buy", -1), SELL("Sell", 1), SELL_SHORT("Sell short", 1); // Buying costs you money therefore -1

		private String tag;
		private int value;

		Type(String tag, int value) {
			this.tag = tag; this.value = value; 
		} 

		public String getTag() { 
			return tag; 
		}

		public int getValue() { 
			return value; 
		}

	};


	public enum Action {
		TO_OPEN("To open"), TO_CLOSE("To close"), STOP_LOSS("Stop loss"), TAKE_PROFIT("Take profit"), HOLDING_PERIOD("Holding period"), ANY("Any");
		private String tag;
		Action(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};


	public enum Status {
		ACTIVE("Active"), SUCCESSFULL("Successful"), INACTIVE("Inactive"), CLOSED("Closed"), ANY("Any");
		private String tag;
		Status(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};


	private Security security;
	private TradeSetup tradeSetup;
	private Status status;
	private LocalDateTime dateTime;
	private boolean active;
	protected double tradeValue;
	private String id;

	private double takeProfitPrice;
  private double stopLossPrice;


	public Trade(Security security, TradeSetup tradeSetup, double takeProfitPrice, double stopLossPrice, LocalDateTime dateTime){
		this.security = security;
		this.tradeSetup = tradeSetup;
		this.dateTime = dateTime;
		this.status = Status.INACTIVE;
		this.takeProfitPrice = takeProfitPrice;
		this.stopLossPrice = stopLossPrice;
		this.id = UUID.randomUUID().toString();
	}


  public double getTakeProfitPrice() {
    return takeProfitPrice;
  }


  public double getStopLossPrice() {
    return stopLossPrice;
  }


  public Security getSecurity() {
		return security;
	}


	public double getTradeCost() {
		return getAmount() * security.getBookCost() * tradeSetup.getTradeType().getValue();
	}


	public String getId() {
		return id;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public LocalDateTime getDate() {
		return dateTime;
	}


	public Action getAction() {
		return tradeSetup.getTradeAction();
	}





	// Delegate methods

	public Type getTradeType() {
		return tradeSetup.getTradeType();
	}


	public double getAmount() {
		return tradeSetup.getAmount();
	}


	public TradeSetup getTradeSetup() {
		return tradeSetup;
	}






}
