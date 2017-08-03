package com.atp.trade;

import com.atp.commission.BasicCommissionScheme;

public class TradingSchemeFactory {


	public static TradingScheme getTradingScheme(String id) {

		if(id.equalsIgnoreCase("simple")) {

			return new TradingScheme(0.08, 0.1, -1, 
					5, new BasicCommissionScheme(5, 5), false);

		}
		else {
			return new TradingScheme(0.08, 0.1, -1, 
					5, new BasicCommissionScheme(5, 5), false);
		}

	}


}
