package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.indicators.EMA;
import com.atp.indicators.Indicator;

/* 
 Simple com.atp.strategy
 */
public class MovingAveragesStrategy extends AbstractStrategy {

	private Indicator emaSlow;
	private Indicator emaFast;
	private int periodsSinceCrossOver;
	private boolean isAbove;
	private int coolOff;

	
	public MovingAveragesStrategy(String name, int slowPeriod, int fastPeriod, int coolOff) {
		this(name, slowPeriod, fastPeriod, coolOff, false);
	}
	
	
	public MovingAveragesStrategy(String name, int slowPeriod, int fastPeriod, int coolOff, boolean saveHistory) {
		super(name);
		emaSlow = new EMA(saveHistory, slowPeriod);
		emaFast = new EMA(saveHistory, fastPeriod);
		periodsSinceCrossOver = -1;
		isAbove = false;
		this.coolOff = coolOff;
	}


	public double tick(PriceBar priceBar) {
		double slowValue = emaSlow.tick(priceBar);
		double fastValue = emaFast.tick(priceBar);

		if(isAbove == false && (fastValue > slowValue)) {
			//We have a cross over
			isAbove = true;
			periodsSinceCrossOver = 0;
		}
		else if(isAbove == true && (fastValue < slowValue)) {
			//Crossed back under
			isAbove = false;
			periodsSinceCrossOver = 0;
		}
		else if(isAbove == true && (fastValue > slowValue)) {
			//Still above
			periodsSinceCrossOver++;
		}
		else if(isAbove == false && (fastValue < slowValue)) {
			//Still below
			periodsSinceCrossOver++;
		}

		return Math.max(0, coolOff - periodsSinceCrossOver) * (isAbove?1:-1);
	}

}
