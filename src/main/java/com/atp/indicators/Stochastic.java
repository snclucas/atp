package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;


/**
 * Stochastic oscillator %K
 * About: http://en.wikipedia.org/wiki/Stochastic_oscillator
 */
public class Stochastic extends AbstractIndicator {
    private final int length;
    
    private List<PriceBar> priceBars;

    public Stochastic(boolean history, QuoteHistory qh, int length) {
        super("Stochastics", history);
        this.length = length;
        priceBars = new ArrayList<PriceBar>();
    }

    @Override
	public double tick(PriceBar priceBar) {
    	priceBars.add(priceBar);
    	
    	
    	if(!isValid()) return 0;
    	
    	if(priceBars.size()>length) priceBars.remove(0);
    	
    	double last;
    	double max = 0;
        double min = priceBars.get(0).getLow();
        
    	for(PriceBar pb: priceBars) {
    		max = Math.max(pb.getHigh(), max);
        	min = Math.min(pb.getLow(), min);
    		
    	}
    	last = priceBars.get(priceBars.size()-1).getClose();//see if this needs the -1
        value = ((last-min) / (max-min)) * 100;
        
	  return value;
	}


	
	@Override
	public boolean isValid() {
		return priceBars.size()>length;
	}
	
	@Override
	public void reset() {
		priceBars.clear();
	}

	
}
