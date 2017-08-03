package com.atp.data.io;

public interface HistoricalDataReader {
	public String getSourceName();
	public PriceHistory[] getHistoricalStockPrices(HistoricalDataOptions options);
}
