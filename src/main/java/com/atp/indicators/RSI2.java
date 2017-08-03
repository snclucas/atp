package com.atp.indicators;

import java.util.*;

import com.atp.data.PriceBar;

/**
 * Relative Strength Index. Implemented up to this specification:
 *
 * The RSI calculation appears in its original and derived form. Average Up
 * and Average Down are calculated using a simple average method for the
 * initial observation. NOTE: The initial observation is the first date shown
 * on the scrolling graph, which may or may not be seen. Subsequent values are
 * computed using these initial values in conjunction with a damping factor to
 * smooth out extreme points. The RSI equation and two averaging methods are
 * presented below.
 * RSI = 100 - [ 100/(1 + [Avg Up/Avg Dn])]
 * where
 * Avg Up: Sum of all changes for advancing periods divided by the total
 * number of RSI periods.
 * 
 * Avg Dn: Sum of all changes for declining periods divided by the total
 * number of RSI periods.
 * 
 * Subsequent RSI calculations are based on up and down sums calculated as
 * follows:
 * 
 * RSI = 100 - [100/(1 + [Next Avg Up/Next Avg Dn])]
 * 
 * Next Avg Up = [([Previous Avg Up * (RSI periods - 1)]) + today's up
 * close]/(RSI periods)
 * Next Avg Dn = [([Previous Avg Dn * (RSI periods - 1)]) + today's dn
 * close]/(RSI periods)
 * NOTE: If there is no up or down close, today's up/dn close is zero.
 */

public class RSI2 extends AbstractIndicator {

	public static int OVERBOUGHT= -1;
	public static int NORMAL = 0;
	public static int OVERSOLD = 1;


	private final int periodLength;

	double aveGains = 0, aveLosses = 0;
	double prevAveGains = 0, prevAveLosses = 0;

	private boolean first = true;

	private List<PriceBar> priceBars;

	public RSI2(boolean saveHistory, int periodLength) {
		super("RSI", saveHistory);
		this.periodLength = periodLength;
		priceBars = new ArrayList<PriceBar>();
	}



	@Override
	public double tick(PriceBar priceBar) {
		priceBars.add(priceBar);
		if(!isValid()) return 0;

		int size = priceBars.size()-1;

		PriceBar currentBar = priceBars.get(size);
		PriceBar previousBar = priceBars.get(size-1);

		double gains = 0, losses = 0;
		double delta;

		if(first) {

			for(int i=0;i<periodLength;i++) {
				delta = priceBars.get(size-periodLength+i).getClose() - 
						priceBars.get(size-periodLength-1+i).getClose();		

				gains = gains +  Math.max(0, delta);
				losses = losses + Math.max(0, -delta);			
			}
			aveGains = gains/periodLength;
			prevAveGains = aveGains;
			aveLosses = losses/periodLength;
			prevAveLosses = aveLosses;
			first = false;
		}
		else {
			delta = currentBar.getClose() - previousBar.getClose();		
			gains = Math.max(0, delta);
			losses = Math.max(0, -delta);

			aveGains = (prevAveGains * (periodLength-1) + gains) / periodLength;
			aveLosses = (prevAveLosses * (periodLength-1) + losses) / periodLength;
			prevAveGains = aveGains;
			prevAveLosses = aveLosses;
		}

		value = 100 - (100 / (1+ (aveGains / aveLosses)));

		saveHistoryValue(value);	
		return value;
	}


	@Override
	public boolean isValid() {
		return priceBars.size()-1 > periodLength;
	}


	@Override
	public void reset() {
		priceBars.clear();	
		value = 0;
	}

}
