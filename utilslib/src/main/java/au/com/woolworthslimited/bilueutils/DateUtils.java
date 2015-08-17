package au.com.woolworthslimited.bilueutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Copyright (c) 2014 Woolworths. All rights reserved.
 */
public class DateUtils {

    private static final Locale DefaultLocale = new Locale("en", "AU");
    
    /**
     * @param windowDate
     * @return [Tue 12 Jan]
     */
    public static String getSimplifiedDeliveryDate(String windowDate) {
        String strDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss");
        try {
            Date date = formatter.parse(windowDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            strDate = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, DefaultLocale) + " " +
                    cal.get(Calendar.DATE) + " " + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, DefaultLocale);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * @param windowDate
     * @return [Tue 12 Jan], [Today] or [Tomorrow]
     */
    public static String getFormattedDeliveryDate(String windowDate) {
        String strDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = formatter.parse(windowDate);
            Calendar now = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (isSameDay(now, cal)) {
                strDate = "Today";
            } else if (isTomorrow(now, cal)) {
                strDate = "Tomorrow";
            } else {
                strDate = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, DefaultLocale) + " " +
                        cal.get(Calendar.DATE) + " " + cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, DefaultLocale);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * @param startTime
     * @param endTime
     * @return [9am - 10pm]
     */
    public static String getFormattedDeliveryTime(String startTime, String endTime) {
        String strDate = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss");
        try {
            Date date = formatter.parse(startTime);
            cal.setTime(date);
            strDate = (cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR) )
                    + cal.getDisplayName(Calendar.AM_PM, Calendar.SHORT, DefaultLocale);
            strDate += " - ";

            date = formatter.parse(endTime);
            cal.setTime(date);
            strDate += (cal.get(Calendar.HOUR) == 0 ? 12 : cal.get(Calendar.HOUR) )
                    + cal.getDisplayName(Calendar.AM_PM, Calendar.SHORT, DefaultLocale);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isTomorrow(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }

        cal1.add(Calendar.DAY_OF_YEAR, +1);
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * @param dateString
     * @return [Monday], [Today] or [Tomorrow]
     */
    public static String getFormattedDate(String dateString) {
        String strDate = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = formatter.parse(dateString);
            Calendar now = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (isSameDay(now, cal)) {
                strDate = "Today";
            } else if (isTomorrow(now, cal)) {
                strDate = "Tomorrow";
            } else {
                strDate = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, DefaultLocale);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
}
