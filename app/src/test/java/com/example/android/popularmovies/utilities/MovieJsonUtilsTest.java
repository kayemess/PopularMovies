package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kristenwoodward on 5/15/17.
 */
public class MovieJsonUtilsTest {
    String jsonString = "{\"page\":1,\"results\":[{\"poster_path\":\"\\/y4MBh0EjBlMuOzv9axM4qJlmhzz.jpg\"," +
            "\"adult\":false,\"overview\":\"The Guardians must fight to keep their newfound family together " +
            "as they unravel the mysteries of Peter Quill's true parentage.\",\"release_date\":\"2017-04-24\",\"" +
            "genre_ids\":[35,28,12,878],\"id\":283995,\"original_title\":\"Guardians of the Galaxy Vol. 2\",\"" +
            "original_language\":\"en\",\"title\":\"Guardians of the Galaxy Vol. 2\",\"backdrop_path\":\"" +
            "\\/aJn9XeesqsrSLKcHfHP4u5985hn.jpg\",\"popularity\":131.438863,\"vote_count\":1437,\"video\":" +
            "false,\"vote_average\":7.6}],\"total_results\":19499,\"total_pages\":975}";
    @Test
    public void getMoviesFromJson() throws Exception {
        Movie[] movies = MovieJsonUtils.getMoviesFromJson(jsonString);
        assertNotNull(movies);
        assertTrue(movies.length > 0);
    }

    @Test
    public void getMovieData() throws Throwable {
        Movie[] movies = MovieJsonUtils.getMoviesFromJson(jsonString);
        assertEquals(movies[0].getMovieId(), "283995");
        assertEquals(movies[0].getMovieName(), "Guardians of the Galaxy Vol. 2");
    }

    @Test
    public void getNumberOfMovies() throws Throwable {
        Movie[] movies = MovieJsonUtils.getMoviesFromJson(jsonString);
        assertTrue(movies.length == 1);
    }
}