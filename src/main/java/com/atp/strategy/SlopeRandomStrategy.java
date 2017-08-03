package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.indicators.Slope;

public class SlopeRandomStrategy extends AbstractStrategy {

	private Slope slope;

	private int window;
	private int lookback;


	public SlopeRandomStrategy(String name, int window, int lookback) { 
		super(name);
		this.window = window;
		this.lookback = lookback;
		slope = new Slope(true, window,lookback);
	}

	public double tick(PriceBar priceBar) {
		double returnValue=0.0;

		double value = slope.tick(priceBar);
		if(value>0.1)
			returnValue = 1.0;
		else if(value<-0.1)
			returnValue = -1.0;

		return returnValue;
	}

	public Slope getSlope() {
		return slope;
	}

	public void setSlope(Slope slope) {
		this.slope = slope;
	}

	public int getWindow() {
		return window;
	}

	public void setWindow(int window) {
		this.window = window;
	}

	public int getLookback() {
		return lookback;
	}

	public void setLookback(int lookback) {
		this.lookback = lookback;
	}
	
	
}

