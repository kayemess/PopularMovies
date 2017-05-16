package com.example.android.popularmovies.utilities;

import static org.junit.Assert.*;

import com.example.android.popularmovies.data.FetchReviewDataTask;
import com.example.android.popularmovies.listeners.OnFetchReviewDataCompleted;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.utilities.DateUtils;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by kristenwoodward on 5/15/17.
 */


public class DateUtilsTest {

    @Test
    public final void testCorrectlyFormattedDate() throws Throwable {
        String year = DateUtils.getYearFromDate("2008-12-12");
        assertEquals(year, "2008");
    }

    @Test(expected = ParseException.class)
    public final void testParseOfJibberishDate() throws Throwable {
        DateUtils.getYearFromDate("abc");
    }

    @Test(expected = ParseException.class)
    public final void testEmptyDateString() throws Throwable {
        DateUtils.getYearFromDate("");
    }

    @Test
    public final void testForIncorrectYear() throws Throwable {
        String year = DateUtils.getYearFromDate("2010-12-12");
        assertNotEquals(year, "2008");
    }
}