package com.atp.indicators;

import com.atp.data.PriceBar;

public class Bollinger extends AbstractIndicator  {

	private final double multiple;
	private double mean, sigma;
	private Indicator sd, sma;

	public Bollinger(boolean history, int period) {
		this(history, period, 1.0);
	}

	public Bollinger(boolean history, int p, double multiple) {
		super("Bolinger bands", history);
		this.multiple = multiple;
		sd = new SD(false, p);
		sma = new SMA(false, p);
	}


	@Override
	public double tick(PriceBar priceBar) {
		mean = sma.tick(priceBar);
		sigma = sd.tick(priceBar);	
		if(!isValid()) return 0.0;	
		return getUpperBand() - getLowerBand(); 
	}


	/**
	 * This returns the mean of the sample of prices and is the same as the midpoint of the Bollinger Bands
	 * 
	 * @return Midpoint of the Bollinger Bands or the mean of the prices sampled.
	 */
	public double getMidpoint() {
		return mean;
	}
	

	/**
	 * @return The upper band of the Bollinger Bands specified by the period and multiple.
	 */
	public double getUpperBand() {
		return mean + sigma * multiple;
	}
	

	/**
	 * @return The lower band of the Bollinger Bands specified by the period and multiple.
	 */
	public double getLowerBand() {
		return mean - sigma * multiple;
	}
	

	@Override
	public boolean isValid() {
		return sma.isValid() && sd.isValid();
	}


	@Override
	public void reset() {
		sma.reset();	
		sd.reset();	
		historyVals.clear();	
	}

}
