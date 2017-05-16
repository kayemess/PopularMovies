package com.example.android.popularmovies.utilities;

import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by kristenwoodward on 5/16/17.
 */
public class NetworkUtilsTest {
    @Test
    public void buildApiUrl() throws Exception {
        URL popularUrl = NetworkUtils.buildApiUrl("popular/");
        assertEquals(popularUrl, new URL("http://api.themoviedb.org/3/movie/popular/?api_key=74ed321b4814256e3d029c9005696147"));
    }

    @Test @Ignore
    public void buildMovieDetailsApiUrl() throws Exception {
        URL movieURL = NetworkUtils.buildMovieDetailsApiUrl("283995","");
    }

    @Test
    public void getResponseFromHttpUrl() throws Exception {

    }

    @Test
    public void buildImageURL() throws Exception {

    }

    @Test
    public void buildTrailerURL() throws Exception {

    }

}