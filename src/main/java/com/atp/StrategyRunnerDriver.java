package com.atp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.HistoricalDataReader;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.Util;
import com.atp.data.io.YahooHistoricalDataReader;
import com.atp.market.StrategyRunner;
import com.atp.strategy.MovingAveragesStrategy;
import com.atp.strategy.RSIStrategy;
import com.atp.strategy.Strategy;
import com.atp.strategy.StrategyResult;
import com.atp.strategy.StrategyRunnerResults;



public class StrategyRunnerDriver {

	public StrategyRunnerDriver() {

		HistoricalDataReader ydr = new YahooHistoricalDataReader();

		//String[] symbols = new String[]{"CMI", "AAPL", "F"};
		String[] symbols = Util.readSymbolData("C:/Users/simon/Dropbox/workspace/jta/com.atp.data/sandp500.symbols","\\t");

		HistoricalDataOptions options = new HistoricalDataOptions(symbols, 
				LocalDateTime.of(2013,1,1,0,0), LocalDateTime.of(2016,10,4,0,0,0), PriceHistory.Base.DAILY);

		PriceHistory[] phs = ydr.getHistoricalStockPrices(options);

		Strategy MAStrategy = new MovingAveragesStrategy("MAStrategy", 50, 20, 5);
		Strategy RSIStrategy = new RSIStrategy("RSIStrategy", 14, 20, 80);

		List<Strategy> strategies = new ArrayList<Strategy>();
		strategies.add(MAStrategy);
		//strategies.add(RSIStrategy);

		StrategyRunner strategyRunner = new StrategyRunner(StrategyRunner.LAST_RESULTS);
		strategyRunner.addStrategies(strategies);
		strategyRunner.addPriceHistory(phs);

		strategyRunner.run();

		StrategyRunnerResults results = strategyRunner.getStrategyRunnerResults();

		for(String symbol : symbols) {
			for(Strategy strategy : strategies) {
				StrategyResult[] strategyResults = results.getStrategyResultsForSymbol(symbol, strategy.getName());
				if(Math.abs(strategyResults[0].getResult()) > 0)
					System.out.println(symbol + " " + strategy.getName() + " " + strategyResults[0].getResult());
			}
		}

	}


	public static void main(String[] args) {
		new StrategyRunnerDriver();
	}



}
