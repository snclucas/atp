package com.atp.indicators;

import com.atp.data.PriceBar;

public class Slope extends AbstractIndicator {

	private SMA sma;
	private int lookback;

	public Slope(boolean history, int window, int lookback) {
		super("Slope", history);
		this.lookback = lookback;
		sma = new SMA(true, window);
		//System.err.println("new");
	}

	@Override
	public double tick(PriceBar priceBar) {
		double now = sma.tick(priceBar);
		
		//System.err.println(" in slope " + now + " " + isValid() + " " + sma.isValid() +" histsize " +sma.historyVals.size() );
		
		//if(!isValid()) return 0;
		if(sma.historyVals.size()-1<lookback) return 0.0;
		
		//sma.historyVals.size()-1
		
		double slopeNow = now;
		double slopeThen = sma.getHistoryValue(lookback);
		
		//System.err.println(" slopeNow" + slopeNow + " slopeThen" + slopeThen);
		
		return (slopeNow - slopeThen)/lookback;
	}

	@Override
	public boolean isValid() {
		return sma.isValid();
	}
	
	@Override
	public void reset() {
		sma.reset();
	}


}
