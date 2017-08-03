package com.atp.scan;

import java.time.LocalDateTime;

import com.atp.data.PriceBar;
import com.atp.indicators.RSI2;
import com.atp.util.DateUtil;

public class RSIScan extends AbstractScan {

	/** RSI com.atp.indicator */
	private RSI2 rsi;
	private double upper;
	private double lower;

	public RSIScan(int period, double upper, double lower) {
		this("RSI", period, upper, lower);
	}

	public RSIScan(String quoteSymbol, int period, double upper, double lower) {
		super(quoteSymbol);
		rsi = new RSI2(false,period);
		this.upper = upper;
		this.lower = lower;
	}

	public ScanResult tick(PriceBar priceBar) {
		double val = rsi.tick(priceBar);

		LocalDateTime compare = LocalDateTime.now().minusDays(1);

		while(!DateUtil.isTradingDay(compare))
			compare = compare.minusDays(1);

		ScanResult result = new ScanResult(null,name,name,0,0, 0);

		if(compare.toLocalDate().compareTo(priceBar.getDateTime().toLocalDate()) ==0) {
			if( val >= upper ) {
				result = new ScanResult(priceBar.getDateTime(), name, name, val, ScanResult.BEARISH, (val-upper)/lower);
			}
			else if( val <= lower) {
				result = new ScanResult(priceBar.getDateTime(), name, name, val, ScanResult.BULLISH, (lower-val)/lower);
			}
			else {
				result = new ScanResult(priceBar.getDateTime(), name, name, val, ScanResult.NOTHING, 0);
			}
		}
		return result;
	}

	@Override
	public void reset() {
		super.reset();
		rsi.reset();
	}
}
