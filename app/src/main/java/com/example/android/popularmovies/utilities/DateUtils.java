package com.example.android.popularmovies.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class DateUtils {

    public static String getYearFromDate(String fullDateString) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(fullDateString));

        return String.valueOf(cal.get(Calendar.YEAR));
    }

    private static Date getDateFromString(String fullDateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return dateFormat.parse(fullDateString);
    }
}
