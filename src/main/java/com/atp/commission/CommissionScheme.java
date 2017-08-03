package com.atp.commission;

import com.atp.trade.TradeSetup;

public interface CommissionScheme {
	double getCommission(TradeSetup tradeSetup);
	double getBuyCommission();
	double getSellCommission();
}
