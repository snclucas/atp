package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.indicators.AbstractIndicator;
import com.atp.indicators.RSI2;


public class RSIStrategy extends AbstractStrategy {

	private int overBought;
	private int overSold;
	private AbstractIndicator rsi;
	private int status = RSI2.NORMAL;

	public RSIStrategy(String name, int period, int overbought, int oversold) {
		super(name);
		this.overBought = overbought;
		this.overSold = oversold;
		rsi = new RSI2(false, period);
	}

	
	public double tick(PriceBar priceBar) {
		double returnValue=0.0;
		double value = rsi.tick(priceBar);

		if(value == 0.0) return 0.0;
		
		if(value>overBought) {
			returnValue=1.0;
			if(status != RSI2.OVERBOUGHT) {
				status = RSI2.OVERBOUGHT;
				//notifyObservers("OVERBOUGHT");
			}
		}
		else if(value<overSold) {
			returnValue=-1.0;
			if(status != RSI2.OVERSOLD) {
				status = RSI2.OVERSOLD;
				//notifyObservers("OVERSOLD");
			}
		}
		else {
			if(status != RSI2.NORMAL) {
				status = RSI2.NORMAL;
				//notifyObservers("NORMAL");
			}
		}
		return returnValue;
	}

}
