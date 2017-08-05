package com.atp;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

import com.atp.commission.BasicCommissionScheme;
import com.atp.commission.CommissionScheme;
import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.YahooHistoricalDataReader;
import com.atp.market.MarketStockRunner;
import com.atp.portfolio.Portfolio;
import com.atp.strategy.Strategy;
import com.atp.trade.TradeManager;
import com.atp.trade.TradingScheme;



public class RunStrategyBackTest implements Observer{

	public RunStrategyBackTest() {

		double stopLoss = 0.1;
		double takeProfit = 0.2;
		double maxCapitalPerTrade = 0.2;
		int maxConcurrentTrades = 1;
		boolean allowShorts = false;

		int initialCash = 10000;
		int allowedTrades = 1;
		Portfolio portfolio = new Portfolio("Simple com.atp.com.atp.portfolio", initialCash);
		
		CommissionScheme basicCommissionScheme = new BasicCommissionScheme(10, 10);

		TradingScheme ts = new TradingScheme(stopLoss, takeProfit, maxCapitalPerTrade,maxConcurrentTrades, basicCommissionScheme, allowShorts);
		TradeManager tm = new TradeManager(ts, portfolio);

		MarketStockRunner runner = new MarketStockRunner(tm);

		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minusMonths(36);
		//QuoteHistory qh = new YahooHistoricalDataReader().getData("AMFC", from, to, false);
		HistoricalDataOptions options = new HistoricalDataOptions(new String[]{"AAPL"}, from, to, PriceHistory.Base.DAILY, false);
		PriceHistory qh2 = new YahooHistoricalDataReader().getHistoricalStockPrices(options)[0];

		runner.addPriceHistory(qh2);
		
		ts.setStopLoss(stopLoss);ts.setTakeProfit(takeProfit);
		runner.run();

		/*
		double ratio = 2.5;

		for(int st = 3;st<=10;st++)
			for(int tp = 5;tp<=30;tp++) 
				if(tp>=ratio*st) {
					ts.setStopLoss(st/100.0);ts.setTakeProfit(tp/100.0);
					runner.run();
				}
*/

	}



	public static void main(String[] args) {
		new RunStrategyBackTest();
	}

	public void update(Observable o, Object arg) {
		Strategy strat = (Strategy)(o);
		System.err.println(strat.getName() + " " + arg.toString());

	}

}
