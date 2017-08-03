package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.indicators.EMA;
import com.atp.indicators.Indicator;


public class MovingAveragesDivergenceStrategy extends AbstractStrategy {

	private Indicator emaSlow;
	private Indicator emaFast;
	private double lastDifference = 0;

	public MovingAveragesDivergenceStrategy(String name, int slowPeriod, int fastPeriod) {
		super(name);
		emaSlow = new EMA(false, slowPeriod);
		emaFast = new EMA(false, fastPeriod);
	}


	public double tick(PriceBar priceBar) {
		double returnValue=0.0;
		double slowValue = emaSlow.tick(priceBar);
		double fastValue = emaFast.tick(priceBar);
		
		double difference = fastValue - slowValue;
		returnValue = difference-lastDifference;
		lastDifference = returnValue;	

		if(returnValue>0)
			returnValue=1.0;
		else
			returnValue=-1.0;

		return returnValue;
	}


}
