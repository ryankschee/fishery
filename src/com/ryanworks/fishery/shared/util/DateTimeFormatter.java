package com.ryanworks.fishery.shared.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeFormatter {

    // Example output: Wed, 4 Jul 2001 12:08
    public static String formatDateTime_Day(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    
    // Example output: Wed, 4 Jul 2001 12:08
    public static String formatDateTime_Day(long timeInMillis) {
        return formatDateTime_Day(new Date(timeInMillis));
    }
    
    // Example output: Wed, 4 Jul 2001 12:08
    public static String formatDateTime_DayHourMinute(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        return sdf.format(date);
    }

    public static String formatDateTime_DayHourMinute(Calendar calendar) {

        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        return sdf.format(date);
    }

    public static String formatDateTime_HourMinute(Calendar calendar) {

        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }
}
