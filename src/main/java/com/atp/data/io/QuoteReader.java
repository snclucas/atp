package com.atp.data.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class QuoteReader {

	private File file;
	private FileReader freader = null;
	private LineNumberReader lnreader = null;

	public QuoteReader() {

	}

	public List<String> read(String fileName) {
			
		List<String> symbols = new ArrayList<String>();
		try{
			file = new File(fileName);
			freader = new FileReader(file);
			lnreader = new LineNumberReader(freader);

			StringTokenizer st = null;
			String line = "";
			//lnreader.readLine(); // ignore header
			while ((line = lnreader.readLine()) != null){
				st = new StringTokenizer(line, ",");
				String symbol = st.nextToken();
				st.nextToken();//name
				symbols.add(symbol);
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return symbols;
	}
	
	
	public static List<PriceHistory> getQuoteHistories(LocalDateTime from, LocalDateTime to, String ... symbols) {
		List<PriceHistory> histories = new ArrayList<PriceHistory>();
		
		for(String s : symbols) {
			HistoricalDataOptions options = new HistoricalDataOptions(s, from, to, PriceHistory.Base.DAILY, false);
			histories.add(new YahooHistoricalDataReader().getHistoricalStockPrices(options)[0]);
		}
		
		return histories;
	}
	
	


}
