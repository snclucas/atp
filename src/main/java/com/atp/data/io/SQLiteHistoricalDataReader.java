package com.atp.data.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.atp.data.PriceBar;

public class SQLiteHistoricalDataReader implements HistoricalDataReader, HistorialDataWriter {

	private static String db_name = "historical_data.db";
	private static String table_name_prefix = "_data";

	public SQLiteHistoricalDataReader() {
	}

	public String getSourceName() {
		return "SQLite";
	}

	public PriceHistory[] getHistoricalStockPrices(HistoricalDataOptions options) {
		List<PriceHistory> histories = new ArrayList<PriceHistory>();

		for(String symbol : options.getSymbols())
			histories.add(getData(symbol, options));

		return histories.toArray(new PriceHistory[0]);
	}



	private PriceHistory getData(String symbol, HistoricalDataOptions options)
	{
		PriceHistory history = new PriceHistory(symbol, options.getBase());

		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection c = connect();
			c.setAutoCommit(false);
			//	System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String table_name = symbol + table_name_prefix;
			String sql = "SELECT * FROM "+table_name +";";

			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				double open = rs.getDouble("open");
				double close = rs.getDouble("close");
				double high = rs.getDouble("high");
				double low = rs.getDouble("low");
				double volume = rs.getDouble("volume");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); //TODO PROB WRONG!!!

				LocalDateTime dateTime = LocalDateTime.parse(rs.getDate("date").toString(), formatter);
				PriceBar priceBar = new PriceBar(symbol, dateTime, open, high, low, close, volume);
				history.addPriceBar(priceBar);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		return history;
	}



	private Connection connect()
	{
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//	System.out.println("Opened database successfully");
		return c;
	}



	public void createTable(String name)
	{
		Statement stmt = null;
		try {
			Connection c = connect();
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + name + 
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" symbol         TEXT    NOT NULL, " + 
					" datetime           DATE     NOT NULL, " + 
					" open           REAL, " + 
					" close          REAL, " + 
					" high           REAL, " + 
					" low            REAL, " + 
					" volume         REAL)"; 
			stmt.executeUpdate(sql);

			sql = "CREATE UNIQUE INDEX IF NOT EXISTS index_unique on "+name+" (symbol, datetime);";
			stmt.executeUpdate(sql);

			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Table created successfully");
	}



	public void writeHistoricalPriceBars(String symbol, PriceBar[] priceBars)
	{		
		if(priceBars.length == 0)
			return;

		System.out.println("Writing " + priceBars[0].getSymbol());

		Statement stmt = null;
		try {
			Connection c = connect();
			c.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			createTable(symbol + table_name_prefix);

			for(PriceBar priceBar: priceBars) {
				stmt = c.createStatement();

				String sql = "INSERT OR IGNORE INTO "+ priceBar.getSymbol() +"_data (symbol, datetime, open, close, high, low, volume) " +
						"VALUES ('"+priceBar.getSymbol()+"' , '"+priceBar.getDateTime()+"', "+
						priceBar.getOpen()+", "+priceBar.getClose()+", "+priceBar.getHigh()+", "+
						priceBar.getLow()+", "+priceBar.getVolume()+");"; 
				stmt.executeUpdate(sql);
			}
			stmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Records created successfully");
	}


}
