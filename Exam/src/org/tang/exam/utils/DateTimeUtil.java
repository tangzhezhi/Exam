package org.tang.exam.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

public class DateTimeUtil {

	private static final String TAG = "DateTimeUtil";

	public static String getCompactTime() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String result = sdf.format(curDate);
		return result;
	}
	
	public static String getShortTime() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(curDate);
	}
	
	public static String getLongTime() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String result = sdf.format(curDate);
		return result;
	}

	public static String getToday00Time() {
		Date curDate = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd000000");
		String result = sdf.format(curDate);
		return result;
	}

	public static String addHourTime(int hour) {
		GregorianCalendar gcalendar = new GregorianCalendar();
		gcalendar.add(Calendar.HOUR, hour);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(gcalendar.getTime());
	}

	public static boolean isBeforeHourTime(String compactTime, int hour) {
		Log.d(TAG, "compateTime: " + compactTime);
		if (compactTime.trim().length() < 14) {
			/** Illegal format look as original time */
			return true;
		}

		String beforeTime = addHourTime(hour);
		Log.d(TAG, "beforeTime: " + beforeTime);
		if (compactTime.compareTo(beforeTime) < 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String toStandardTime(String compactTime) {
		if (compactTime.trim().length() < 14) {
			return "";
		} else {
			return String.format("%s-%s-%s %s:%s:%s", compactTime.substring(0, 4),
					compactTime.substring(4, 6), compactTime.substring(6, 8), compactTime.substring(8, 10),
					compactTime.substring(10, 12), compactTime.substring(12, 14));
		}
	}
}