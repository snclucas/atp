package com.atp.securities;

import java.time.LocalDate;

import com.atp.securities.Option.OptionType;

public class SecurityFactory {
	
	
	
	public static Security getStock(String symbol, double bookPrice) {
		return new Stock(symbol, bookPrice);
	}
	
	
	public static Security getOption(String symbol, OptionType optionType, double strike, LocalDate expiry, double premium) {
		return new Option(symbol, optionType, strike, expiry, premium);
	}
	

}
