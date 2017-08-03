package com.atp.scan;

import java.time.LocalDateTime;

import com.atp.data.PriceBar;
import com.atp.indicators.ADX;
import com.atp.util.DateUtil;

public class ADXScan extends AbstractScan{

	private ADX adx;

	public ADXScan(int period) {
		this("ADX", period);
	}

	public ADXScan(String quoteSymbol, int period) {
		super(quoteSymbol);
		adx = new ADX(false,period);
	}


	public ScanResult tick(PriceBar priceBar) {
		double val = adx.tick(priceBar);


		LocalDateTime compare = LocalDateTime.now().minusDays(1);

		while(!DateUtil.isTradingDay(compare))
			compare = compare.minusDays(1);

		ScanResult result = new ScanResult(null,name,name,0,0, 0);

		if(compare.toLocalDate().compareTo(priceBar.getDateTime().toLocalDate()) ==0) {

			if( val >= 25 && val <= 38) {
				result = new ScanResult(priceBar.getDateTime(), name, name, val, ScanResult.SIGNAL, 1);
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
		adx.reset();
	}

}
