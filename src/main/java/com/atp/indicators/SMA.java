package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;


/**
 * Simple Moving Average.
 */
public class SMA extends AbstractIndicator {
	private final int windowLength;
	private List<PriceBar> priceBars;

	public SMA(boolean saveHistory, int windowLength) {
		super("SMA", saveHistory);
		this.windowLength = windowLength;
		priceBars = new ArrayList<PriceBar>();
	}


	@Override
	public double tick(PriceBar priceBar) {
		priceBars.add(priceBar);
		
		if(priceBars.size()>windowLength) priceBars.remove(0);

		if(priceBars.size()<windowLength) {
			value =  0.0;
		} 
		else {
			double sma = 0;
			for(PriceBar pb: priceBars)
				sma += pb.getPrice(PriceBar.Price.CLOSE);
			value = sma / (priceBars.size());
		}
		saveHistoryValue(value);	
		return value;
	}


	@Override
	public boolean isValid() {
		return priceBars.size()>=windowLength;
	}

	@Override
	public void reset() {
		priceBars.clear();
	}
}
