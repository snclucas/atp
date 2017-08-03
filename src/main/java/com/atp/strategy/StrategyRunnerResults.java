package com.atp.strategy;

import java.util.HashMap;
import java.util.Map;

public class StrategyRunnerResults {
	
	private Map<String, Map<String, StrategyResult[]>> resultsForSymbols;
	
	
	public StrategyRunnerResults() {
		super();
		resultsForSymbols = new HashMap<String, Map<String, StrategyResult[]>>();
	}


	public void addResultForSymbol(String securitySymbol, Map<String, StrategyResult[]> strategiesResult) {
		resultsForSymbols.put(securitySymbol, strategiesResult);
	}


	public Map<String, StrategyResult[]> getAllResultsForSymbol(String securitySymbol) {
		return resultsForSymbols.get(securitySymbol);
	}
	
	
	public StrategyResult[] getAllResultsForSymbolLast(String securitySymbol) {
		return resultsForSymbols.get(securitySymbol).get(resultsForSymbols.size()-1);
	}
	
	
	public StrategyResult[] getStrategyResultsForSymbol(String securitySymbol, String strategy) {
		if(resultsForSymbols.get(securitySymbol).size() > 0)
			return resultsForSymbols.get(securitySymbol).get(strategy);
		else 
			return new StrategyResult[0];
	}
	
	
}
