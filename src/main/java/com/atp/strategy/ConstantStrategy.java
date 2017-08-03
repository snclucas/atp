package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;

public class ConstantStrategy extends AbstractStrategy {

	private QuoteHistory qh;
	public static int  LONG = 1;
	public static int  SHORT = -1;
	private int type;
	

	public ConstantStrategy(String name, int type) {
		super(name);
		this.type = type;
	}


	public double tick(PriceBar priceBar) {
		double returnValue=0.0;
		
		for(int i=0;i<qh.size();i++) {
			if(type==LONG) {
				returnValue = 1.0;
			}
			else {
				returnValue = -1.0;
			}
		}
		return returnValue;
	}
	
}
