package com.atp.trade;

import java.time.LocalDateTime;
import java.util.UUID;

import com.atp.UniquelyIdentifiable;
import com.atp.data.PriceBar;
import com.atp.securities.Security;

public class Trade implements UniquelyIdentifiable {

	private Security security;
	private TradeSetup tradeSetup;
	private TradeStatus status;
	private boolean active;
	protected PriceBar priceBar;
	private String id;


	public Trade(Security security, PriceBar priceBar, TradeSetup tradeSetup){
		this.security = security;
		this.tradeSetup = tradeSetup;
		this.status = TradeStatus.INACTIVE;
		this.id = UUID.randomUUID().toString();
		this.priceBar = priceBar;
	}

  public double getPrice() {
    return priceBar.getPrice(PriceBar.Price.CLOSE);
  }

  public void setPriceBar(PriceBar price) {
    this.priceBar = priceBar;
  }


  public Security getSecurity() {
		return security;
	}


	public double getTradeCost(double price) {
		return getAmount() * price * tradeSetup.getTradeType().getValue();
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


	public TradeStatus getStatus() {
		return status;
	}


	public void setStatus(TradeStatus status) {
		this.status = status;
	}


	public LocalDateTime getDate() {
		return priceBar.getDateTime();
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
