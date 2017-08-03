package com.atp;

import com.atp.data.PriceBar;

public interface Tickable {
	
	 double tick(PriceBar priceBar);

}
