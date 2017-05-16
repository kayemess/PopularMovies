package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kristenwoodward on 5/15/17.
 */
public class TrailerJsonUtilsTest {
    String jsonString = "{\"id\":321612,\"results\":[{\"id\":\"589219bfc3a368096a009a41\",\"iso_639_1\":\"en\",\"iso_3166_1\":\"US\",\"key\":\"tWapqpCEO7Y\",\"name\":\"Belle Motion Poster\",\"site\":\"YouTube\",\"size\":720,\"type\":\"Clip\"}]}";

    @Test
    public void getTrailersFromJson() throws Exception {
        Trailer[] trailers = TrailerJsonUtils.getMovieTrailersFromJson(jsonString);
        assertNotNull(trailers);
        assertTrue(trailers.length > 0);
    }

    @Test
    public void getTrailersData() throws Throwable {
        Trailer[] trailers = TrailerJsonUtils.getMovieTrailersFromJson(jsonString);
        assertEquals(trailers[0].getTrailerId(), "589219bfc3a368096a009a41");
        assertEquals(trailers[0].getTrailerKey(), "tWapqpCEO7Y");
    }

    @Test
    public void getNumberOfTrailer() throws Throwable {
        Trailer[] trailers = TrailerJsonUtils.getMovieTrailersFromJson(jsonString);
        assertTrue(trailers.length == 1);
    }
}