package com.atp.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Vector;


public class DateUtil {
	
	public static Vector<LocalDateTime> holidays = new Vector<LocalDateTime>();
	
	static {
		//holidays.add(new DateTime(2011, 2, 21, 0, 0, 0, 0));
	}
	
	public static boolean isTradingDay(LocalDateTime inDate) {	
		
		if(inDate.getDayOfWeek() == DayOfWeek.SATURDAY || inDate.getDayOfWeek() == DayOfWeek.SUNDAY)
			return false;
			
		for(LocalDateTime date : holidays) 
			if(date.toLocalDate().compareTo(inDate.toLocalDate()) == 0) return false;
		return true;
	}
	
}
