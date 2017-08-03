package com.atp.strategy;

import com.atp.data.PriceBar;
import com.atp.scan.CandleStickScan;
import com.atp.scan.ScanResult;

public class CandleStickStrategy extends IStrategy {

	int last = 0;
	CandleStickScan css;
	
	public CandleStickStrategy(String name, int type) {
		super(name);
		css = new CandleStickScan(name, type, false);
	}


	public double tick(PriceBar priceBar) {
		double returnValue=0.0;
		
		ScanResult result = css.tick(priceBar);
		System.err.println(priceBar.getDateTime() + " " + result.getBullOrBear());

		if(result.getBullOrBear()==ScanResult.BULLISH) {
			last = 0;
			returnValue = 1.0;
		}
		else if(result.getBullOrBear()==ScanResult.BEARISH) {
			last = 1;
			returnValue = -1.0;
		}
		return returnValue;
	}
	
}
