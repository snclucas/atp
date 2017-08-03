package com.atp.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.atp.data.PriceBar;
import com.atp.data.io.PriceHistory;
import com.atp.strategy.Strategy;
import com.atp.strategy.StrategyResult;
import com.atp.strategy.StrategyRunnerResults;

public class StrategyRunner {

	public static int ALL_RESULTS = 1;
	public static int LAST_RESULTS = 2;
	
	private int resultsMode;
	private List<Strategy> strategies;
	private List<PriceHistory> priceHistories;

	private StrategyRunnerResults strategyRunnerResults;

	public StrategyRunner() {
		priceHistories = new LinkedList<PriceHistory>();
		strategies = new LinkedList<Strategy>();
		strategyRunnerResults = new StrategyRunnerResults();
		this.resultsMode = ALL_RESULTS;
	}

	
	public StrategyRunner(int resultsMode) {
		priceHistories = new LinkedList<PriceHistory>();
		strategies = new LinkedList<Strategy>();
		strategyRunnerResults = new StrategyRunnerResults();
		this.resultsMode = resultsMode;
	}
	

	public List<Strategy> getStrategies() {
		return strategies;
	}


	public void setStrategies(List<Strategy> strategies) {
		this.strategies = strategies;
	}
	

	public void addStrategies(Strategy ... strategies) {
		for(Strategy strategy : strategies)
			this.strategies.add(strategy);
	}


	public void addStrategies(List<Strategy> strategies) {
		this.strategies.addAll(strategies);
	}


	public void addPriceHistory(PriceHistory ph) {
		priceHistories.add(ph);
	}


	public void addPriceHistory(PriceHistory ... priceHistories) {
		for(PriceHistory ph : priceHistories)
			this.priceHistories.add(ph);
	}


	public StrategyRunnerResults getStrategyRunnerResults() {
		return strategyRunnerResults;
	}


	public void run() {
		for(PriceHistory ph : priceHistories) {
			List<PriceBar> priceBars = ph.getPriceBars();

			Map<String, StrategyResult[]> results = new HashMap<String, StrategyResult[]>();
			for(Strategy strategy : strategies) {
				List<StrategyResult> strategyResultsForStrategy = getStrategyResults(priceBars, strategy);
				results.put(strategy.getName(), strategyResultsForStrategy.toArray(new StrategyResult[0]));
			}
			strategyRunnerResults.addResultForSymbol(ph.getSymbol(), results);	
		}	
	}


	private List<StrategyResult> getStrategyResults(List<PriceBar> priceBars, Strategy strategy) {
		int cnt = 0;
		List<StrategyResult> strategyResults = new ArrayList<StrategyResult>();
		for(PriceBar priceBar : priceBars) {
			double strategyResult = strategy.tick(priceBar);
			if(resultsMode == ALL_RESULTS || cnt == (priceBars.size() - 1))
				strategyResults.add(new StrategyResult(priceBar.getDateTime(), strategyResult));
			cnt++;
		}
		return strategyResults;
	}


	public int getResultsMode() {
		return resultsMode;
	}


	public void setResultsMode(int resultsMode) {
		this.resultsMode = resultsMode;
	}
	
	
	

}
