package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.indicators.Bollinger;


public class BollingerStrategy extends AbstractStrategy{

	private Bollinger boll;

	public BollingerStrategy(String name, int period) {
		this(name, period, 1.0);
	}

	public BollingerStrategy(String name, int period, double mult) {
		super(name);
		boll = new Bollinger(false, period);
	}


	public double tick(PriceBar priceBar) {
		boll.tick(priceBar);
		double returnValue=0.0;

		double close = priceBar.getClose();	

		if(close > boll.getUpperBand())
			returnValue = -1.0;

		if(close < boll.getLowerBand())
			returnValue = 1.0;
		
		return returnValue;
	}
	

}
