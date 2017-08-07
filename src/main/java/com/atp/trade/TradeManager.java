package com.atp.trade;

import java.time.temporal.ChronoUnit;
import java.util.*;

import com.atp.data.PriceBar;
import com.atp.portfolio.Portfolio;
import com.atp.portfolio.Position;
import com.atp.util.FormatUtil;
import com.atp.util.PropertiesUtil;



public class TradeManager {

	private int counter = 0;

	private TradingScheme tradingScheme;
	private Portfolio portfolio;
	private List<Trade> trades;

	private double totalCommissionsPaid;





	public TradeManager(TradingScheme tradingScheme, Portfolio portfolio) {
		this.portfolio = portfolio;
		this.tradingScheme = tradingScheme;
		this.trades = new ArrayList<>();
	}


	public boolean canTrade() {
		return portfolio.numPositions() < tradingScheme.getNumberOfConcurrentPositions();
	}


	public TradeResult execute(Trade trade) {
		double tradeValue = 0.0;
		String executeDateStr = FormatUtil.shortDate(trade.getDate());
		TradeType buyOrSell = trade.getTradeType();

		double commission = tradingScheme.getCommission(trade.getTradeSetup());

		if(trade.getAmount()<1)
			return new TradeResult(TradeResultStatus.CANNOT_SATISFY_TRADE_RULE, 0.0);


		if((buyOrSell == TradeType.BUY || buyOrSell == TradeType.SELL) &&
            trade.getAction() == TradeAction.TO_OPEN &&
				portfolio.numPositions()>=tradingScheme.getNumberOfConcurrentPositions()) //change this - this could be closing out a short
			return new TradeResult(TradeResultStatus.NO_MORE_POSITIONS_ALLOWED,0.0);


    if(buyOrSell == TradeType.SELL && trade.getAction() == TradeAction.TO_OPEN &&
           !tradingScheme.allowShortSelling())
      return new TradeResult(TradeResultStatus.NO_SHORT_SELLING,0.0);

    tradeValue = trade.getTradeCost(trade.getPrice())-commission;

		if(portfolio.getCash() < (tradeValue))
			return new TradeResult(TradeResultStatus.NOT_ENOUGH_CAPITAL,0.0);


    if(trade.getAction() == TradeAction.TO_OPEN) {
      trade.setStatus(TradeStatus.ACTIVE);
      portfolio.addTrade(trade);
      tradeValue = trade.getTradeCost(trade.getPrice())-commission;
    }
    else if(trade.getAction() == TradeAction.TO_CLOSE) {
      if(portfolio.getPosition(trade.getSecurity().getSecurityId())==null) {
        return new TradeResult(TradeResultStatus.NO_POSITION_TO_CLOSE,0.0);
      }

      boolean removed = portfolio.removePosition(trade.getSecurity().getSecurityId());
      if(!removed) {
        System.err.println("No position removed!");
      }
      else {
        portfolio.addTrade(trade);
        tradeValue = trade.getTradeCost(trade.getPrice())-commission;
        totalCommissionsPaid =+ commission;
      }
    }



		//addTrade(trade);

		//tradeValue is negative for LONG BUY, SHORT SELL and positive for LONG SELL, SHORT BUYs
		//portfolio.setCash(portfolioCash+tradeValue);

		if(PropertiesUtil.verbose) 
			System.out.println(counter++ + " " + executeDateStr+ 
					" " + trade.getId() + " " +
					trade.getAmount() + " " + trade.getAction().getTag() + " " + trade.getTradeType().getTag() + " @ " + 
					FormatUtil.currency(trade.getSecurity().getBookCost()) + "/" + 
					" [" + FormatUtil.currency(tradeValue) + "] -- Portfolio cash {" + FormatUtil.currency(portfolio.getCash()) + "}");

		return new TradeResult(TradeResultStatus.SUCCSESFUL_TRADE, tradeValue);
	}


	public void checkExitConditions(PriceBar priceBar) {
		double currentPrice = priceBar.getClose();

		for(Position position : portfolio.getPositionsWithSymbol(priceBar.getSymbol())) {
			
			long holdingPeriod = ChronoUnit.DAYS.between(
			        position.getOpenPositionDate(),
              priceBar.getDateTime());
			
			if(holdingPeriod >= tradingScheme.getMaxHoldingPeriod()) {
        Trade trade = position.getCloseOutTrade(priceBar);
        execute(trade);
        return;
      }


			if(position.isLong()) {

				if(priceBar.getHigh()>=position.getTakeProfitPrice()) {
					Trade t = position.getCloseOutTrade(priceBar);

					t.setStatus(TradeStatus.CLOSED);
					execute(t);
				}
				else if(priceBar.getLow()<=position.getStopLossPrice()) {
					Trade trade = position.getCloseOutTrade(priceBar);
					trade.setStatus(TradeStatus.CLOSED);
					execute(trade);
				}

			}
			else {

				if(priceBar.getLow()<=position.getTakeProfitPrice()) {
					//System.out.println("take profit");

					Trade trade = position.getCloseOutTrade(priceBar);
				//	System.err.println(position.getTrade().getId());

				//	System.err.println(t.getId());
					trade.setStatus(TradeStatus.CLOSED);
					execute(trade);
				}
				else if(priceBar.getHigh()>=position.getStopLossPrice()) {
				//	System.out.println("stop loss");
					Trade trade = position.getCloseOutTrade(priceBar);
					trade.setStatus(TradeStatus.CLOSED);
					execute(trade);
				}
			}
		}
	}



	public void summary() {

		int allTrades = getTrades(TradeStatus.ANY);
		int allExitTrades = getTrades(TradeAction.TAKE_PROFIT)+getTrades(TradeAction.STOP_LOSS) ;
		int tpTrades = getTrades(TradeAction.TAKE_PROFIT);
		int slTrades = getTrades(TradeAction.STOP_LOSS);

		if(PropertiesUtil.verbose) System.out.println("---- Summary -----");
		if(PropertiesUtil.verbose) System.out.println("T - P - S ");
		System.out.println(allTrades + " " + 
				allExitTrades + " " + 
				tpTrades + " " + 
				slTrades + " " + 
				FormatUtil.percent((double)tpTrades/allExitTrades) + " " + portfolio.getCash());
	}




	public int getNumTrades() {
		return getTrades(TradeStatus.ANY);
	}


	public int getTrades(TradeAction tradeAction) {
		int numberOfTrades = 0;

		if(tradeAction == TradeAction.ANY)
			return trades.size();

		for(Trade t : trades)
			if(t.getAction() == tradeAction)
				numberOfTrades++;
		return numberOfTrades;
	}
	
	public int getTrades(TradeStatus tradeStatus) {
		int numberOfTrades = 0;

		if(tradeStatus == TradeStatus.ANY)
			return trades.size();

		for(Trade trade : trades)
			if(trade.getStatus() == tradeStatus)
				numberOfTrades++;
		return numberOfTrades;
	}


	public TradingScheme getTradingScheme() {
		return tradingScheme;
	}


	public void setTradingScheme(TradingScheme tradingScheme) {
		this.tradingScheme = tradingScheme;
	}


	public Portfolio getPortfolio() {
		return portfolio;
	}


	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public List<Trade> getTrades() {
		return trades;
	}

	public void addTrade(Trade trade) {
		this.trades.add(trade);
	}



	public void reset() {
		trades.clear();
		portfolio.reset();
	}




}
