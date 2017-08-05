package com.atp.market;


import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.atp.data.PriceBar;
import com.atp.data.io.PriceHistory;
import com.atp.portfolio.Portfolio;
import com.atp.securities.Security;
import com.atp.securities.Stock;
import com.atp.strategy.BollingerMedianStrategy;
import com.atp.strategy.Strategy;
import com.atp.trade.Trade;
import com.atp.trade.TradeManager;
import com.atp.trade.TradeResult;
import com.atp.trade.TradeSetup;
import com.atp.trade.TradingScheme;
import com.atp.trade.Trade.TradeAction;
import com.atp.trade.Trade.TradeType;

public class MarketStockRunner {

	private List<Strategy> strategies;
	private List<PriceHistory> quoteHistories;
	private Portfolio portfolio;
	private TradeManager tradeManager;
	private TradingScheme tradingScheme;

	public MarketStockRunner(TradeManager tradeManager) {
		quoteHistories = new LinkedList<>();
		this.tradeManager = tradeManager;
		this.portfolio = tradeManager.getPortfolio();
		tradingScheme = tradeManager.getTradingScheme();
	}


	public List<Strategy> getStrategies() {
		return strategies;
	}


	public void setStrategies(List<Strategy> strategies) {
		this.strategies = strategies;
	}


	public void addPriceHistory(PriceHistory ... quoteHist) {
		for(PriceHistory qh : quoteHist)
			quoteHistories.add(qh);
	}



	public void run() {
		tradeManager.reset();

		for(PriceHistory qh:quoteHistories ) {
			List<PriceBar> prices = qh.getPriceBars();

			//IStrategy com.atp.strategy = new RandomStrategy("Random");
			//IStrategy com.atp.strategy = new CandleStickStrategy("Candle", CandleStickScan.ENGULFING);
			//IStrategy com.atp.strategy = new MovingAveragesDivergenceStrategy("SlopeRandom", 20,10);
			Strategy strategy = new BollingerMedianStrategy("BollMedian", 13,2);

			for(int i=0;i<prices.size();i++) {
				PriceBar currentBar = qh.getPriceBar(i);

				strategy.tick(currentBar);
				double indicator = currentBar.getPriceAction();

				LocalDateTime date = prices.get(i).getDateTime();
				double close = prices.get(i).getClose();

				if(indicator >= 1) {
					if(tradeManager.canTrade()) {
						
						int numShares = (int)((portfolio.getCash()*tradeManager.getTradingScheme().getMaxCapitalPerTrade()) / close);

						TradeSetup tradeSetup = new TradeSetup(numShares, Trade.TradeType.BUY, TradeAction.TO_OPEN);

						Trade trade = new Trade(new Stock(qh.getSymbol(), close),  tradeSetup, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit(), date);

						TradeResult result = tradeManager.execute(trade, date);
						
						if(result.getStatus()!=TradeManager.SUCCSESFUL_TRADE)
							System.err.println("Error code " + TradeManager.getStatusString(result.getStatus()));

					}
				}
				else if(indicator <=-1 && tradeManager.getTradingScheme().allowShortSelling()) {
					if(tradeManager.canTrade()) {

						int numShares = (int)((portfolio.getCash()*tradeManager.getTradingScheme().getMaxCapitalPerTrade()) / close);

						TradeSetup tradeSetup = new TradeSetup(numShares, Trade.TradeType.SELL, TradeAction.TO_OPEN);

						Trade trade = new Trade(new Stock(qh.getSymbol(), close), tradeSetup, tradingScheme.getStopLoss(), tradingScheme.getTakeProfit(), date);

						TradeResult result = tradeManager.execute(trade, date);
						
						if(result.getStatus()!=TradeManager.SUCCSESFUL_TRADE)
							System.err.println("Error code " + TradeManager.getStatusString(result.getStatus()));
					}
				}

				tradeManager.checkExitConditions(currentBar);
			}
			
			tradeManager.summary();

		}



	}

	public void reset() {
		portfolio.setCash(portfolio.getInitialCash());
		portfolio.getAllPositions().clear();
	}


}
