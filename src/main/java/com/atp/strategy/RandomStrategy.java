package com.atp.strategy;

import com.atp.data.PriceBar;

public class RandomStrategy extends AbstractStrategy {

	int last = 0;
	
	public RandomStrategy(String name) {
		super(name);
	}


	public double tick(PriceBar priceBar) {
		double returnValue=0.0;

		if(last==1) {
			last = 0;
			returnValue = 1.0;
		}
		else {
			last = 1;
			returnValue = -1.0;
		}
		return returnValue;
	}
	
}
