package com.example.android.popularmovies.utilities;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class DateUtils {

    public DateUtils(){}

    // returns a year given a date formatted from the movie database
    public static String getYearFromDate(String fullDateString) throws ParseException {
        String yearFromDate = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        cal.setTime(dateFormat.parse(fullDateString));

        yearFromDate = String.valueOf(cal.get(Calendar.YEAR));

        return yearFromDate;
    }
}
