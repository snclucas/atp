package com.atp.indicators;

import com.atp.data.PriceBar;


/**
 * MACD
 */
public class MACD extends AbstractIndicator {
	private final int fastLength, slowLength;
	private AbstractIndicator fEMA;
	private AbstractIndicator sEMA;

	public MACD(boolean saveHistory, int slowL, int fastL) {
		super("MACD", saveHistory);
		this.fastLength = fastL;
		this.slowLength = slowL;
		fEMA = new EMA(saveHistory, fastLength);
		sEMA = new EMA(saveHistory, slowLength);
	}


	@Override
	public double tick(PriceBar priceBar) {
		double fastEMA = fEMA.tick(priceBar);
		double slowEMA = sEMA.tick(priceBar);
		value = fastEMA - slowEMA;
		saveHistoryValue(value);	
		return value;
	}

	@Override
	public boolean isValid() {
		return fEMA.isValid() && sEMA.isValid();
	}


	@Override
	public void reset() {
		fEMA.reset();
		sEMA.reset();
	}



}
