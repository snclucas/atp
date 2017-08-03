package com.atp.scan;

import java.time.LocalDateTime;

import com.atp.data.PriceBar;
import com.atp.indicators.SMA;
import com.atp.util.DateUtil;

public class TAZScan extends AbstractScan{

	private SMA slowSMA;
	private SMA fastSMA;

	
	public TAZScan(int slow, int fast) {
		this("TAZ", slow, fast);
	}
	

	public TAZScan(String quoteSymbol, int slow, int fast) {
		super(quoteSymbol);
		slowSMA = new SMA(true,slow );
		fastSMA = new SMA(true,fast );
	}


	public ScanResult tick(PriceBar priceBar) {
		priceBars.add(priceBar);
		double slowAve = slowSMA.tick(priceBar);
		double fastAve = fastSMA.tick(priceBar);

		if(priceBars.size()<3) 
			return new ScanResult(null,name,name,0,0, 0);

		PriceBar previousBar = priceBars.get(priceBars.size()-1-1);	

		LocalDateTime compare = LocalDateTime.now().minusDays(1);

		while(!DateUtil.isTradingDay(compare))
			compare = compare.minusDays(1);

		if(slowAve==0.0 || fastAve==0.0) return new ScanResult(null,name,name,0,0, 0);

		ScanResult result = new ScanResult(null,name,name,0,0, 0);
		double yesterdayClose = previousBar.getClose();
		double todayClose = priceBar.getClose();

		if(compare.toLocalDate().compareTo(priceBar.getDateTime().toLocalDate()) ==0) {

			if( (todayClose > slowAve) && (todayClose < fastAve) ) {
				if( (yesterdayClose > slowAve) && (yesterdayClose > fastAve))
					result = new ScanResult(priceBar.getDateTime(), name, name, 10, ScanResult.BULLISH, 1);
				else 
					result = new ScanResult(priceBar.getDateTime(), name, name, 1, ScanResult.BULLISH, 1);
			}
			else if( (todayClose < slowAve) && (todayClose > fastAve) ) {
				if( (yesterdayClose < slowAve) && (yesterdayClose< fastAve)) 
					result = new ScanResult(priceBar.getDateTime(), name, name, 10, ScanResult.BEARISH, 1);
				else
					result = new ScanResult(priceBar.getDateTime(), name, name, 1, ScanResult.BEARISH, 1);
			}
			else {
				result = new ScanResult(priceBar.getDateTime(), name, name, 0, ScanResult.NOTHING, 0);
			}
		}
		return result;
	}
}
