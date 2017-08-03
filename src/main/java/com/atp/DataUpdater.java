package com.atp;

import java.time.LocalDateTime;

import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.HistoricalDataReader;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.SQLiteHistoricalDataReader;
import com.atp.data.io.Util;
import com.atp.data.io.YahooHistoricalDataReader;


public class DataUpdater {

	public DataUpdater() {
		
		populateDB();

		HistoricalDataReader ydr = new YahooHistoricalDataReader();

		HistoricalDataOptions options = new HistoricalDataOptions(new String[]{"CMI"}, 
			 LocalDateTime.of(2013,1,1,0,0,0), LocalDateTime.of(2013,10,4,0,0,0), PriceHistory.Base.DAILY);
		
		SQLiteHistoricalDataReader sql_hd = new SQLiteHistoricalDataReader();

		PriceHistory[] ph = sql_hd.getHistoricalStockPrices(options);
		
		System.out.println(ph[0].size());
		
		String s = "";

	}


	private void populateDB() {

		String[] symbols = Util.readSymbolData("C:/Users/simon/Dropbox/workspace/jta/com.atp.data/sandp500.symbols","\\t");
		//symbols = new String[]{"AAPL"};
		
		HistoricalDataReader ydr = new YahooHistoricalDataReader();
		
		
		for(String symbol : symbols) {
			SQLiteHistoricalDataReader sql_hd = new SQLiteHistoricalDataReader();
			HistoricalDataOptions options = new HistoricalDataOptions(new String[]{symbol}, 
					LocalDateTime.of(2010,1,1,0,0,0), LocalDateTime.now(), PriceHistory.Base.DAILY);
			
			PriceHistory[] priceHistories = ydr.getHistoricalStockPrices(options);	
			
			for(PriceHistory priceHistory : priceHistories)
				sql_hd.writeHistoricalPriceBars(symbol, priceHistory.getPriceBarsAsArray());
			
			
		}
		
	}


	public static void main(String[] args) {
		new DataUpdater();
	}

}
