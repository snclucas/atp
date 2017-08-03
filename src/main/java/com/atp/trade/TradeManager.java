package com.atp.trade;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.atp.data.PriceBar;
import com.atp.portfolio.Portfolio;
import com.atp.portfolio.Position;
import com.atp.trade.Trade.TradeAction;
import com.atp.trade.Trade.TradeStatus;
import com.atp.trade.Trade.TradeType;
import com.atp.util.FormatUtil;
import com.atp.util.PropertiesUtil;


public class TradeManager {

	int counter = 0;

	public final static int SUCCSESFUL_TRADE = 0;
	public final static int NO_POSITION_TO_CLOSE = -3;
	public final static int NOT_ENOUGH_CAPITAL = -2;
	public final static int NO_MORE_POSITIONS_ALLOWED = -1;
	public final static int CANNOT_SATISFY_TRADE_RULE = -5;
	public final static int INVALID_TRADE = -6;


	private TradingScheme tradingScheme;
	private Portfolio portfolio;
	private List<Trade> trades;
	private double tradeValue;
	private double totalCommissionsPaid;




	public static String getStatusString(int status) {
		String returnString;
		switch (status) {
		case SUCCSESFUL_TRADE:
			returnString = "SUCCESSFUL";
			break;
		case NO_POSITION_TO_CLOSE: 
			returnString = "NO_POSITION_TO_CLOSE";
			break;
		case NOT_ENOUGH_CAPITAL:
			returnString = "NOT_ENOUGH_CAPITAL";
			break;
		case NO_MORE_POSITIONS_ALLOWED:
			returnString = "NO_MORE_POSITIONS_ALLOWED";
			break;
		case CANNOT_SATISFY_TRADE_RULE:         //  Cases can simply fall thru.
			returnString = "CANNOT_SATISFY_TRADE_RULE";
			break;
		case INVALID_TRADE:         //  Cases can simply fall thru.
			returnString = "IVALID_TRADE";
			break;
		default:
			returnString = "ERROR";
		}
		return returnString;
	}




	public TradeManager(TradingScheme tradingScheme, Portfolio portfolio) {
		this.portfolio = portfolio;
		this.tradingScheme = tradingScheme;
		this.trades = new ArrayList<>();
	}


	public boolean canTrade() {
		return portfolio.numPositions() < tradingScheme.getNumberOfConcurrentPositions();
	}


	public TradeResult execute(Trade trade, LocalDateTime executeDate) {
		String executeDateStr = FormatUtil.shortDate(executeDate);
		TradeType buyOrSell = trade.getTradeType();

		double commission = tradingScheme.getCommission(trade.getTradeSetup());

		if(trade.getAmount()<1)
			return new TradeResult(CANNOT_SATISFY_TRADE_RULE, 0.0);

		/* Buy a security */
		if(buyOrSell == TradeType.BUY && 
				portfolio.numPositions()>=tradingScheme.getNumberOfConcurrentPositions() && 
				trade.getAction() == TradeAction.TO_OPEN) //change this - this could be closing out a short
			return new TradeResult(NO_MORE_POSITIONS_ALLOWED,0.0);
		
		/* Short sell a security in the com.atp.com.atp.portfolio */

		if(buyOrSell == TradeType.SELL_SHORT && 
				portfolio.numPositions()>=tradingScheme.getNumberOfConcurrentPositions() && trade.getAction()== TradeAction.TO_OPEN)
			return new TradeResult(NO_MORE_POSITIONS_ALLOWED,0.0);

		
		double portfolioCash = portfolio.getCash();

		if(portfolioCash<(tradeValue))
			return new TradeResult(NOT_ENOUGH_CAPITAL,0.0);



		if((buyOrSell == TradeType.BUY || buyOrSell == TradeType.SELL_SHORT) && trade.getAction() == TradeAction.TO_OPEN) {
			portfolio.addToPosition(trade);
			tradeValue = trade.getTradeCost()-commission;
		}

		

		if(buyOrSell == TradeType.SELL && trade.getAction() == TradeAction.TO_CLOSE) {
			if(portfolio.getPosition(trade.getId())==null) {
				return new TradeResult(NO_POSITION_TO_CLOSE,0.0);
			}

			boolean removed = portfolio.removePosition(trade.getId());
			if(!removed) {
				System.err.println("No position removed!");
			}
			else {
				tradeValue = trade.getTradeCost()-commission;
				totalCommissionsPaid =+ commission;
			}
		}
		
		
		if(buyOrSell == TradeType.SELL_SHORT && trade.getAction() == TradeAction.TO_OPEN) {
			portfolio.addToPosition(trade);
			tradeValue = trade.getTradeCost()-commission;
			totalCommissionsPaid =+ commission;
		}


		if(buyOrSell == TradeType.SELL && trade.getAction() == TradeAction.TO_CLOSE) {
			//Just find first
			if(portfolio.getPositionsWithSymbol(trade.getSecurity().getSymbol()).size() < 1) {
				return new TradeResult(NO_POSITION_TO_CLOSE,0.0);
			}

			boolean removed = portfolio.removePosition(trade.getId());
			if(!removed) {
				System.err.println("No position removed!");
			}
			else {
				tradeValue = trade.getTradeCost() - commission;
			}
		}


		addTrade(trade);

		//tradeValue is negative for LONG BUY, SHORT SELL and positive for LONG SELL, SHORT BUYs
		portfolio.setCash(portfolioCash+tradeValue);

		if(PropertiesUtil.verbose) 
			System.out.println(counter++ + " " + executeDateStr+ 
					" " + trade.getId() + " " + 
					trade.getAmount() + " " + trade.getAction().getTag() + " " + trade.getTradeType().getTag() + " @ " + 
					FormatUtil.currency(trade.getSecurity().getBookCost()) + "/" + 
					" [" + FormatUtil.currency(tradeValue) + "] -- Portfolio cash {" + FormatUtil.currency(portfolio.getCash()) + "}");

		return new TradeResult(SUCCSESFUL_TRADE, tradeValue);
	}


	public void checkExitConditions(PriceBar priceBar) {
		double currentPrice = priceBar.getClose();

		for(Position position : portfolio.getPositionsWithSymbol(priceBar.getSymbol())) {
			
			long holdingPeriod = ChronoUnit.DAYS.between(
			        position.getOpenPositionDate(),
              priceBar.getDateTime());
			
			if(holdingPeriod >= tradingScheme.getMaxHoldingPeriod()) {
        Trade trade = position.getCloseOutTrade();
        execute(trade, priceBar.getDateTime());
        return;
      }




			if(position.isLong()) {

				if(priceBar.getHigh()>=position.getTrade().getTakeProfitPrice()) {	
					Trade t = position.getTrade().getCloseOutTrade(priceBar, true, TradeAction.TAKE_PROFIT);

					t.setAction(TradeAction.TAKE_PROFIT);
					t.setStatus(TradeStatus.CLOSED);
					execute(t, priceBar.getDateTime());
				}
				else if(priceBar.getLow()<=position.getTrade().getStopLossPrice()) {	
					Trade trade = position.getTrade().getCloseOutTrade(priceBar, true, TradeAction.STOP_LOSS);
					trade.setAction(TradeAction.STOP_LOSS);
					trade.setStatus(TradeStatus.CLOSED);
					execute(trade, priceBar.getDateTime());
				}

			}
			else {

				if(priceBar.getLow()<=position.getTrade().getTakeProfitPrice()) {
					//System.out.println("take profit");

					Trade trade = position.getTrade().getCloseOutTrade(priceBar, true, TradeAction.TAKE_PROFIT);
				//	System.err.println(position.getTrade().getId());

				//	System.err.println(t.getId());
					trade.setAction(TradeAction.TAKE_PROFIT);
					trade.setStatus(TradeStatus.CLOSED);
					execute(trade, priceBar.getDateTime());
				}
				else if(priceBar.getHigh()>=position.getTrade().getStopLossPrice()) {
				//	System.out.println("stop loss");
					Trade trade = position.getTrade().getCloseOutTrade(priceBar, true, TradeAction.STOP_LOSS);
					trade.setAction(TradeAction.STOP_LOSS);
					trade.setStatus(TradeStatus.CLOSED);
					execute(trade, priceBar.getDateTime());
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
