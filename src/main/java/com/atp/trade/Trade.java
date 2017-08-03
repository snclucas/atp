package com.atp.trade;

import java.time.LocalDateTime;
import java.util.UUID;

import com.atp.UniquelyIdentifiable;
import com.atp.securities.Security;

public class Trade implements UniquelyIdentifiable {

	public enum TradeType { 
		BUY("Buy", -1), SELL("Sell", 1), SELL_SHORT("Sell short", 1); // Buying costs you money therefore -1

		private String tag;
		private int value;

		TradeType(String tag, int value) { 
			this.tag = tag; this.value = value; 
		} 

		public String getTag() { 
			return tag; 
		}

		public int getValue() { 
			return value; 
		}

	};


	public enum TradeAction { 
		TO_OPEN("To open"), TO_CLOSE("To close"), STOP_LOSS("Stop loss"), TAKE_PROFIT("Take profit"), HOLDING_PERIOD("Holding period"), ANY("Any");
		private String tag;
		TradeAction(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};


	public enum TradeStatus { 
		ACTIVE("Active"), SUCCESSFULL("Successful"), INACTIVE("Inactive"), CLOSED("Closed"), ANY("Any");
		private String tag;
		TradeStatus(String tag) { this.tag = tag;}
		public String getTag() { return tag; }
	};


	private Security security;
	private String id;
	private TradeSetup tradeSetup;
	private TradeStatus status;
	private LocalDateTime dateTime;
	private boolean active;
	protected double tradeValue;
	private String uniqueID;


	public Trade(Security security, TradeSetup tradeSetup, LocalDateTime dateTime){
		this.security = security;
		this.tradeSetup = tradeSetup;
		this.dateTime = dateTime;
		this.status = TradeStatus.INACTIVE;
		this.uniqueID = UUID.randomUUID().toString();
	}



	public Security getSecurity() {
		return security;
	}


	public double getTradeCost() {
		return getAmount() * security.getBookCost() * tradeSetup.getTradeType().getValue();
	}


	public String getUniqueID() {
		return uniqueID;
	}

	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public TradeStatus getStatus() {
		return status;
	}

	public void setStatus(TradeStatus status) {
		this.status = status;
	}


	public String getId() {
		return id;
	}


	public LocalDateTime getDate() {
		return dateTime;
	}


	public TradeAction getAction() {
		return tradeSetup.getTradeAction();
	}





	// Delegate methods

	public TradeType getTradeType() {
		return tradeSetup.getTradeType();
	}


	public double getAmount() {
		return tradeSetup.getAmount();
	}


	public TradeSetup getTradeSetup() {
		return tradeSetup;
	}






}
