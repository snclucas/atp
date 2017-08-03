package com.atp.data.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.atp.data.PriceBar;


public class YahooHistoricalDataReader implements HistoricalDataReader {

	private String sourceName = "Yahoo Finance";
	private File file;
	private FileReader freader = null;
	private LineNumberReader lnreader = null;


	public void open(String fileName) {
		try{
			file = new File(fileName);
			freader = new FileReader(file);
			lnreader = new LineNumberReader(freader);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void close() {
		try {
			freader.close();
			lnreader.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	public PriceHistory read(String symbol) {
		PriceHistory qh = new PriceHistory(symbol, PriceHistory.Base.DAILY);
		return read(qh);
	}


	public PriceHistory read(PriceHistory ph) {		

		StringTokenizer st = null;
		try{
			String line = "";
			lnreader.readLine(); // ignore header
			while ((line = lnreader.readLine()) != null){

				st = new StringTokenizer(line, ",");

				LocalDateTime dateTime = getDateTime(st.nextToken());
				double open = Double.parseDouble(st.nextToken());
				double high = Double.parseDouble(st.nextToken());
				double low = Double.parseDouble(st.nextToken());
				double close = Double.parseDouble(st.nextToken());
				double volume = Double.parseDouble(st.nextToken());
				ph.addPriceBar(new PriceBar(ph.getSymbol(), dateTime, open, high, low, close, volume));
			}
			ph.reverseData();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		catch(NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return ph;
	}


	public PriceHistory[] getHistoricalStockPrices(HistoricalDataOptions options) {
		List<PriceHistory> histories = new ArrayList<PriceHistory>();
		for(String sym : options.getSymbols()) {
			System.out.println("Getting " + sym);
			histories.add(getHistoricalStockPrices(sym, options));
		}
		return histories.toArray(new PriceHistory[0]);
	}


	public PriceHistory getHistoricalStockPrices(String symbol, HistoricalDataOptions options) {

		PriceHistory history = new PriceHistory(symbol, options.getBase());

		int fromDay = options.getFrom().getDayOfMonth();
		int fromMonth = options.getFrom().getMonthValue()-1;
		int fromYear = options.getFrom().getYear();

		int toDay = options.getTo().getDayOfMonth();
		int toMonth = options.getTo().getMonthValue()-1;
		int toYear = options.getTo().getYear();

		try {
			URL yahoofin = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + symbol + 
					"&a="+fromMonth+"&b="+fromDay+"&c="+fromYear+"&d="+toMonth+"&e="+toDay+"&f="+toYear+"&g=d");

			URLConnection yc = yahoofin.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			in.readLine(); // ignore header
			while ((inputLine = in.readLine()) != null) {

				String[] yahooStockInfo = inputLine.split(",");

				LocalDateTime dateTime = getDateTime(yahooStockInfo[0]);

				double open = Double.valueOf(yahooStockInfo[1]);
				double high = Double.valueOf(yahooStockInfo[2]);
				double low = Double.valueOf(yahooStockInfo[3]);
				double close = Double.valueOf(yahooStockInfo[4]);
				double volume = Double.valueOf(yahooStockInfo[5]);

				PriceBar priceBar = new PriceBar(symbol, dateTime, open, high, low, close,  volume);
				history.addPriceBar(priceBar);
			}
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to get stockinfo for: " + symbol + ex);
			//log.error("Unable to get stockinfo for: " + symbol + ex);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			//log.error("Unable to get stockinfo for: " + symbol + ex);
		}
		if(false == options.isLatestFirst()) history.reverseData();
		return history;
	}


	private LocalDateTime getDateTime(String dateStr) {
		String monthStr="";
		int year=-1;
		int month=-1;
		int day=-1;
		try {
			StringTokenizer st = new StringTokenizer(dateStr,"-");
			String yearStr = st.nextToken();
			monthStr = st.nextToken();
			String dayStr = st.nextToken();


			year = Integer.parseInt(yearStr);
			if(year<2000) year = year+2000;
			day = Integer.parseInt(dayStr);


			month = Integer.parseInt(monthStr);



		}
		catch(NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return LocalDateTime.of(year,month, day,0,0,0,0);
	}



	public String getSourceName() {
		return sourceName;
	}

}
