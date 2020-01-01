package com.ryanworks.fishery.shared.util;

import java.util.Calendar;
import java.util.StringTokenizer;

public class DateTimeUtil {

    public static Calendar convertToCalendarObject(String val)
        throws Exception
    {
        //Format: 8/22/2010 16:30:11
        StringTokenizer st1 = new StringTokenizer(val, " ");

        String date = st1.nextToken();
        String time = st1.nextToken();

        StringTokenizer st2 = new StringTokenizer(date, "/");
        StringTokenizer st3 = new StringTokenizer(time, ":");

        int month   = Integer.parseInt(st2.nextToken());
        int day     = Integer.parseInt(st2.nextToken());
        int year    = Integer.parseInt(st2.nextToken());

        int hour    = Integer.parseInt(st3.nextToken());
        int minute  = Integer.parseInt(st3.nextToken());
        int second  = Integer.parseInt(st3.nextToken());

        Calendar c = Calendar.getInstance();
        c.set(year, month-1, day, hour, minute, second);

        return c;
    }
}
