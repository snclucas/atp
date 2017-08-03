package com.atp;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

import com.atp.data.PriceBar;
import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.YahooHistoricalDataReader;
import com.atp.indicators.AbstractIndicator;
import com.atp.indicators.CandleStick;
import com.atp.scan.CandleStickScan;
import com.atp.strategy.Strategy;



public class RunScan implements Observer{

	public RunScan() {

		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minusMonths(36);
		HistoricalDataOptions options = new HistoricalDataOptions(new String[]{"MMM"}, 
				from, to, PriceHistory.Base.DAILY, true);
		PriceHistory qh = new YahooHistoricalDataReader().getHistoricalStockPrices(options)[0];

		AbstractIndicator scan = new CandleStick(CandleStickScan.ENGULFING);
		
		
		for(PriceBar priceBar : qh.getPriceBars()) {
			double result = scan.tick(priceBar);
			System.err.println(priceBar.getDateTime() + " " + result);
		}
		

	}



	public static void main(String[] args) {
		new RunScan();
	}

	public void update(Observable o, Object arg) {
		Strategy strat = (Strategy)(o);
		System.err.println(strat.getName() + " " + arg.toString());

	}

}
