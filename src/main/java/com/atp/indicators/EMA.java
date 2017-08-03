package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;


/**Exponential Moving Average.*/
public class EMA extends AbstractIndicator {
	private final int windowLength;
	private final double multiplier;

	private List<PriceBar> priceBars;


	public EMA(boolean saveHistory, int windowLength) {
		super("EMA", saveHistory);
		this.windowLength = windowLength;
		multiplier = 2. / (windowLength + 0.);
		priceBars = new ArrayList<PriceBar>();
	}

	@Override
	public double tick(PriceBar priceBar) {
		priceBars.add(priceBar);

		if(priceBars.size()-1>windowLength) priceBars.remove(0);

		if(isValid()) {
			value =  0.0;
		} 
		else {
			double ema = priceBars.get(0).getClose();
			for(PriceBar pb: priceBars) {
				double barClose = pb.getClose();
				ema += (barClose - ema) * multiplier;
			}
			value = ema;
		}
		saveHistoryValue(value);	
		return value;
	}



	@Override
	public boolean isValid() {
		return (priceBars.size()-1<windowLength);
	}
	
	@Override
	public void reset() {
		priceBars.clear();	
		historyVals.clear();	
	}
	
}
