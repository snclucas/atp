package com.atp.strategy;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;
import com.atp.indicators.Bollinger;


public class BollingerMedianStrategy extends AbstractStrategy{

	private List<PriceBar> priceBars;
	private Bollinger boll;

	public BollingerMedianStrategy(String name, int period) {
		this(name, period, 1.0);
		priceBars = new ArrayList<PriceBar>();
	}

	public BollingerMedianStrategy(String name, int period, double mult) {
		super(name);
		boll = new Bollinger(false, period, mult);
		priceBars = new ArrayList<PriceBar>();
	}

	private double closeToday;
	private double closeYesterday;
	private double closeDayBeforeYesterday;
	private double highYesterday;
	
	public double tick(PriceBar priceBar) {
		priceBars.add(priceBar);
		
		boll.tick(priceBar);
		
		//System.err.println("Returning : " + boll.isValid());
		
		if(!boll.isValid()) return 0.0;
		
		closeToday = priceBar.getClose();
		closeYesterday = priceBars.get(priceBars.size()-2).getClose();
		closeDayBeforeYesterday = priceBars.get(priceBars.size()-3).getClose();
		highYesterday = priceBars.get(priceBars.size()-2).getHigh();
		
//		System.err.println(closeToday + " " + closeYesterday + " " + closeDayBeforeYesterday);
		
		double returnValue=0.0;


		if( 	(closeYesterday < boll.getMidpoint()) && 
				//(priceBar.getVolume() > 1.1e7) && 
				(closeToday < boll.getUpperBand()) &&
				(closeToday > boll.getMidpoint())) {
			
			returnValue=1.0;
		}

		
		return returnValue;
	}
	

}
