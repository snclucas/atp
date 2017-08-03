package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;
import com.atp.maths.Stats;


/**
 * Simple Moving Average.
 */
public class SD extends AbstractIndicator {
	private final int windowLength;
	private List<PriceBar> priceBars;

	public SD(boolean saveHistory, int length) {
		super("SD", saveHistory);
		this.windowLength = length;
		priceBars = new ArrayList<PriceBar>();
	}


	@Override
	public double tick(PriceBar priceBar) {
		priceBars.add(priceBar);
		if(priceBars.size()>windowLength) 
			priceBars.remove(0);
		
		if(isValid())
			value =  0.0;
		else
			value = Stats.sd(priceBars, windowLength, QuoteHistory.CLOSE);
		
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
