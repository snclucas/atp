package com.atp.scan;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

import com.atp.data.PriceBar;
import com.atp.data.io.HistoricalDataOptions;
import com.atp.data.io.PriceHistory;
import com.atp.data.io.Util;
import com.atp.data.io.YahooHistoricalDataReader;
import com.atp.util.FormatUtil;

public class EquityScanner {

	private Vector<AbstractScan> scans;

	public EquityScanner() {}

	public void scan() {
		System.out.println("Scanning.");

		scans = new Vector<AbstractScan>();

		scans.add(new ADXScan(14));
		scans.add(new RSIScan(14, 70, 30));
		scans.add(new TAZScan(30,10));

		String[] symbols = Util.readSymbolData("C:/Users/simon/Dropbox/workspace/jta/com.atp.data/sandp100.symbols","\\t");
		
		LocalDateTime to = LocalDateTime.now();
		LocalDateTime from = to.minusMonths(36);

		HashMap<String, ScannerResults> results = new HashMap<String, ScannerResults>();

		for(String stockSymbol: symbols) {
			HistoricalDataOptions options = new HistoricalDataOptions(stockSymbol, from, to, PriceHistory.Base.DAILY, false);
			PriceHistory[] qh = new YahooHistoricalDataReader().getHistoricalStockPrices(options);
			
			if(qh[0].size() < 1)
				continue;

			ScannerResults scannerResult = new ScannerResults(stockSymbol);

			for(AbstractScan scan: scans) {

				Vector<ScanResult> resultsForEachScan = new Vector<ScanResult>();

				for(PriceBar priceBar:qh[0].getPriceBars()) {
					ScanResult result = scan.tick(priceBar);		
					resultsForEachScan.add(result);
				}
				scannerResult.addScanResult(scan.getName(), resultsForEachScan);
				scan.reset();
			}
			results.put(stockSymbol, scannerResult);
		}
		parseResults(results);
	}


	public void parseResults(HashMap<String, ScannerResults> results) {
		StringBuffer sb = new StringBuffer();
		
		for(AbstractScan scan: scans) {
			sb.append(scan.getName());
		}

		for( String symbolName : results.keySet()) {
			ScannerResults resultVector = results.get(symbolName);
			
			for(String scan : resultVector.getScans()){
				Vector<ScanResult> scanResult = resultVector.getScanResult(scan);
				System.out.println(scanResult.size() + " " + scan + " " + symbolName);
				ScanResult lastResult = scanResult.get(scanResult.size()-1);

				sb.append(FormatUtil.shortNumber(lastResult.getResult()));
			}

		}
		
	}


}
