package com.atp.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FormatUtil {

	public static String shortDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("dd MMM yy"));
	}

	public static String shorterDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyMMdd"));
	}


	public static String small(double number) {
		NumberFormat formatter = new DecimalFormat("#0.0000");
		return formatter.format(number);
	}
	
	public static String percent(double number) {
		NumberFormat formatter = new DecimalFormat("#0.00%");
		return formatter.format(number);
	}

	public static String shortNumber(double number) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(number);
	}

	public static String currency(double number) {
		NumberFormat form = NumberFormat.getCurrencyInstance();
		return form.format(number);
	}

	public static String pad(final String target, final int count) {
		StringBuffer buffer = new StringBuffer();
		for (int current = 0; current < count; current++)
			buffer.append(" ");
		buffer.append(target);
		return buffer.toString(); 
	}


}
