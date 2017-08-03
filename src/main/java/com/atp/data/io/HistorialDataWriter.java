package com.atp.data.io;

import com.atp.data.PriceBar;

public interface HistorialDataWriter {	
	public void writeHistoricalPriceBars(String symbol, PriceBar[] priceBars);
}
