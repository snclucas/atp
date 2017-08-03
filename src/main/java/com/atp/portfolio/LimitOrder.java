package com.atp.portfolio;

import com.atp.trade.Trade;
import com.atp.trade.Trade.TradeAction;

public class LimitOrder {

	private double limitPrice;

	public LimitOrder(Trade trade, double limit, boolean asPercent, TradeAction tradeAction) {
		this(trade.getSecurity().getBookCost(), limit, asPercent, tradeAction);
	}


	public LimitOrder(double price, double limit, boolean asPercent, TradeAction tradeAction){
		if(limit < 0)
			throw new RuntimeException("Negative values for limits are not acceptable");

		if(asPercent) {
			double percentBetweenZeroandOne = (limit > 1.0) ? limit/100.0 : limit;
			if(tradeAction == TradeAction.STOP_LOSS)
				limitPrice = price * (1-percentBetweenZeroandOne);
			else if (tradeAction == TradeAction.TAKE_PROFIT) {
				limitPrice = price * (1+percentBetweenZeroandOne);
			}
			else {
				throw new RuntimeException("Only STOP_LOSS and TAKE_PROFIT com.atp.com.atp.trade actions are supported in limit orders");
			}
		}
		else {
			limitPrice = limit;
		}
	}


	public double getLimitPrice() {
		return limitPrice;
	}

}
