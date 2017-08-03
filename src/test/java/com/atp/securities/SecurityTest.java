package com.atp.securities;
import java.time.LocalDate;
import java.time.Month;

import junit.framework.TestCase;

import com.atp.securities.Security;
import com.atp.securities.SecurityFactory;
import com.atp.securities.Option.OptionType;

public class SecurityTest extends TestCase {
	
	public void testStockSecurityEquality() {
		Security security1 = SecurityFactory.getStock("GOOG", 654.34);
		Security security2 = SecurityFactory.getStock("MSFT", 54.34);
		Security security3 = SecurityFactory.getStock("GOOG", 754.34);
		assertEquals("Security 1 and security 3 should be equal", security1, security3);
		assertEquals("Security 1 and security 2 should not be equal", false, security1.equals(security2));
	}
	
	
	public void testOptionSecurityEquality() {
		Security security1 = SecurityFactory.getOption("GOOG", OptionType.CALL, 458, LocalDate.of(2016, Month.JANUARY, 1), 23.34);
		Security security2 = SecurityFactory.getOption("GOOG", OptionType.CALL, 458, LocalDate.of(2016, Month.JANUARY, 1), 23.34);		
		Security security3 = SecurityFactory.getOption("GOOG", OptionType.PUT, 458, LocalDate.of(2016, Month.JANUARY, 1), 23.34);
		Security security4 = SecurityFactory.getOption("GOOG", OptionType.PUT, 458, LocalDate.of(2016, Month.MARCH, 1), 23.34);
		Security security5 = SecurityFactory.getOption("GOOG", OptionType.PUT, 458, LocalDate.of(2016, Month.MARCH, 1), 123.34);
		Security security6 = SecurityFactory.getOption("MSFT", OptionType.PUT, 458, LocalDate.of(2016, Month.MARCH, 1), 123.34);
		
		assertEquals("Option 1 and Option 2 should be equal", security1, security2);
		assertEquals("Option 1 and Option 3 should not be equal", false, security1.equals(security3));
		assertEquals("Option 3 and Option 4 should not be equal", false, security3.equals(security4));
		assertEquals("Option 4 and Option 5 should be equal", true, security4.equals(security5));
		assertEquals("Option 4 and Option 5 not should be equal", false, security5.equals(security6));
	}


}
