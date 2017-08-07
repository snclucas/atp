package com.atp.trade;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.atp.data.PriceBar;
import com.atp.portfolio.Portfolio;
import com.atp.portfolio.Position;
import com.atp.trade.Trade.Action;
import com.atp.trade.Trade.Status;
import com.atp.trade.Trade.Type;
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
		Type buyOrSell = trade.getTradeType();

		double commission = tradingScheme.getCommission(trade.getTradeSetup());

		if(trade.getAmount()<1)
			return new TradeResult(CANNOT_SATISFY_TRADE_RULE, 0.0);

		/* Buy a security */
		if(buyOrSell == Type.BUY &&
				portfolio.numPositions()>=tradingScheme.getNumberOfConcurrentPositions() && 
				trade.getAction() == Action.TO_OPEN) //change this - this could be closing out a short
			return new TradeResult(NO_MORE_POSITIONS_ALLOWED,0.0);
		
		/* Short sell a security in the com.atp.com.atp.portfolio */

		if(buyOrSell == Type.SELL_SHORT &&
				portfolio.numPositions()>=tradingScheme.getNumberOfConcurrentPositions() && trade.getAction()== Action.TO_OPEN)
			return new TradeResult(NO_MORE_POSITIONS_ALLOWED,0.0);

		
		double portfolioCash = portfolio.getCash();

		if(portfolioCash<(tradeValue))
			return new TradeResult(NOT_ENOUGH_CAPITAL,0.0);



		if((buyOrSell == Trade.Type.BUY || buyOrSell == Trade.Type.SELL_SHORT) && trade.getAction() == Action.TO_OPEN) {
			portfolio.addToPosition(trade);
			tradeValue = trade.getTradeCost()-commission;
		}

		

		if(buyOrSell == Type.SELL && trade.getAction() == Action.TO_CLOSE) {
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
		
		
		if(buyOrSell == Type.SELL_SHORT && trade.getAction() == Action.TO_OPEN) {
			portfolio.addToPosition(trade);
			tradeValue = trade.getTradeCost()-commission;
			totalCommissionsPaid =+ commission;
		}


		if(buyOrSell == Trade.Type.SELL && trade.getAction() == Action.TO_CLOSE) {
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

				if(priceBar.getHigh()>=position.getTakeProfitPrice()) {
					Trade t = position.getCloseOutTrade();

					t.setStatus(Status.CLOSED);
					execute(t, priceBar.getDateTime());
				}
				else if(priceBar.getLow()<=position.getStopLossPrice()) {
					Trade trade = position.getCloseOutTrade();
					trade.setStatus(Status.CLOSED);
					execute(trade, priceBar.getDateTime());
				}

			}
			else {

				if(priceBar.getLow()<=position.getTakeProfitPrice()) {
					//System.out.println("take profit");

					Trade trade = position.getCloseOutTrade();
				//	System.err.println(position.getTrade().getId());

				//	System.err.println(t.getId());
					trade.setStatus(Status.CLOSED);
					execute(trade, priceBar.getDateTime());
				}
				else if(priceBar.getHigh()>=position.getStopLossPrice()) {
				//	System.out.println("stop loss");
					Trade trade = position.getCloseOutTrade();
					trade.setStatus(Status.CLOSED);
					execute(trade, priceBar.getDateTime());
				}

			}


		}



	}


	public void summary() {

		int allTrades = getTrades(Status.ANY);
		int allExitTrades = getTrades(Action.TAKE_PROFIT)+getTrades(Action.STOP_LOSS) ;
		int tpTrades = getTrades(Action.TAKE_PROFIT);
		int slTrades = getTrades(Action.STOP_LOSS);

		if(PropertiesUtil.verbose) System.out.println("---- Summary -----");
		if(PropertiesUtil.verbose) System.out.println("T - P - S ");
		System.out.println(allTrades + " " + 
				allExitTrades + " " + 
				tpTrades + " " + 
				slTrades + " " + 
				FormatUtil.percent((double)tpTrades/allExitTrades) + " " + portfolio.getCash());
	}




	public int getNumTrades() {
		return getTrades(Status.ANY);
	}


	public int getTrades(Action tradeAction) {
		int numberOfTrades = 0;

		if(tradeAction == Action.ANY)
			return trades.size();

		for(Trade t : trades)
			if(t.getAction() == tradeAction)
				numberOfTrades++;
		return numberOfTrades;
	}
	
	public int getTrades(Status tradeStatus) {
		int numberOfTrades = 0;

		if(tradeStatus == Status.ANY)
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
