package com.atp.indicators;

import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;

/**
 * TrueTrend
 */
public class TrueTrend extends AbstractIndicator {
    private final int periodLength;
    private List<PriceBar> priceBars;

    public TrueTrend(Boolean history, QuoteHistory qh, int periodLength) {
        super("True Trend", history);
        this.periodLength = periodLength;
        priceBars = new ArrayList<PriceBar>();
    }

    @Override
    public double tick(PriceBar priceBar) {
    	priceBars.add(priceBar);
    	if(!isValid()) return 0;
    	
        int qhSize = priceBars.size();
        int lastBar = qhSize - 1;
        
        int firstBar = lastBar - periodLength + 1;

        double gains = 0, losses = 0;

        for (int barIndex = firstBar + 1; barIndex <= lastBar; barIndex++) {

            PriceBar bar = priceBar;
            PriceBar prevBar = priceBars.get(barIndex - 1);

            //double barMidPoint = (bar.getHigh() + bar.getLow()) / 2;
            //double prevBarMidPoint = (prevBar.getHigh() + prevBar.getLow()) / 2;

            //double midPointChange = barMidPoint - prevBarMidPoint;
            double closeChange = bar.getClose() - prevBar.getClose();
            //double volume = bar.getVolume();

            //double trueChange = (closeChange + midPointChange) / 2;
            double change = closeChange;// * volume;// * volume;

            gains += Math.max(0, change);
            losses += Math.max(0, -change);
        }

        double totalChange = gains + losses;

        double trend = (totalChange == 0) ? 0 : (100 * gains / totalChange);
        // rescale so that the range is from -100 to +100
        value = (trend - 50) * 2;

        return value;
    }

 

    
    
	@Override
	public boolean isValid() {
		return (priceBars.size() > periodLength);
	}

	@Override
	public void reset() {
		priceBars.clear();
	}

	
}
