package com.atp;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import com.atp.data.PriceBar;
import com.atp.data.QuoteHistory;
import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.YahooHistoricalDataReader;
import com.atp.maths.CorrelationMatrix;
import com.atp.strategy.Strategy;


public class CorrelationTester implements Observer{

	public CorrelationTester() {
		CorrelationMatrix cm = new CorrelationMatrix(10, 3);
	
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minusMonths(36);
		
		String[] symbols = new String[] {
				"AMFC","MMM","GOOG","YHOO",
				"ADBE","AA","AIG","MO","BAC",
				"BBT","COF","BK","ABT","AMZN"
				
		};
		
		for(String s : symbols) {
			
			HistoricalDataOptions options = new HistoricalDataOptions(new String[]{s}, 
					from, to, PriceHistory.Base.DAILY, false);
			
			cm.addPriceHistory(new YahooHistoricalDataReader().getHistoricalStockPrices(options)[0]);
		}
		
		cm.createMatrix();
		cm.printMatrix();
		cm.printHTML();
	}

	
	public QuoteHistory makeRandomQH(int size) {
		
		QuoteHistory qh = new QuoteHistory("Random", QuoteHistory.DAILY);
		Random generator = new Random();
		
		for(int i=0;i<size;i++) {
			double close = generator.nextInt(400);
			qh.addHistoricalPriceBar(new PriceBar("Test", LocalDateTime.now(), 100,100,100,close,100000));
		}
		return qh;
	}


	public static void main(String[] args) {
		new CorrelationTester();
	}

	public void update(Observable o, Object arg) {
		Strategy strat = (Strategy)(o);
		System.err.println(strat.getName() + " " + arg.toString());

	}

}
