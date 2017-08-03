package com.atp.strategy;

import com.atp.data.PriceBar;

public class StopLoss extends AbstractStrategy {

	private double stopLoss;
	private double initialPrice;

	public StopLoss(String name, double stopLoss) { 
		super(name);
		this.stopLoss = stopLoss;
		if(stopLoss>1) stopLoss = stopLoss/100.0;
	}
	
	public double tick(PriceBar priceBar) {
		double returnValue=0.0;
		double price = priceBar.getClose();

		if(( Math.abs(price-initialPrice)/initialPrice) > stopLoss) {
			returnValue = -1.0;
		}
		return returnValue;
	}
	

	public double getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}

	public double getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(double initialPrice) {
		this.initialPrice = initialPrice;
	}
}

