package com.atp.commission;

import com.atp.trade.TradeSetup;
import com.atp.trade.TradeType;

public class BasicCommissionScheme implements CommissionScheme {

	private double buyCommission;
	private double sellCommission;
	
	
	public BasicCommissionScheme(double buyCommission, double sellCommission) {
		super();
		this.buyCommission = buyCommission;
		this.sellCommission = sellCommission;
	}
	

	@Override
	public double getBuyCommission() {
		return buyCommission;
	}
	

	@Override
	public double getSellCommission() {
		return sellCommission;
	}


	@Override
	public double getCommission(TradeSetup tradeSetup) {
		if(TradeType.BUY.equals(tradeSetup.getTradeType().getTag()))
			return getBuyCommission();
		return getSellCommission();
	}
	
	

}
