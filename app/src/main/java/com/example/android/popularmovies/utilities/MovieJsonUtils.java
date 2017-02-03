package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.android.popularmovies.utilities.NetworkUtils.BASE_IMAGE_URL;
import static com.example.android.popularmovies.utilities.NetworkUtils.POSTER_IMAGE_SIZE;
import static com.example.android.popularmovies.utilities.NetworkUtils.buildImageURL;

/**
 * Created by kristenwoodward on 12/7/16.
 */

public class MovieJsonUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static Movie[] getMoviesFromJson(Context context, String movieJsonStr) throws JSONException {

        //TMB = The Movie DB

        //list of results is contained in results list
        final String TMD_LIST = "results";

        //movie attributes
        final String TMD_POSTER_PATH = "poster_path";
        final String TMD_ADULT = "adult";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_TITLE = "original_title";
        final String TMD_SYNOPSIS = "overview";
        final String TMD_MOVIE_ID = "id";

        //movie popularity
        final String TMD_POPULARITY = "popularity";

        //movie rating
        final String TMD_RATING = "vote_average";

        final String OWM_MESSAGE_CODE = "cod";

        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(TMD_LIST);

        parsedMovieData = new Movie[movieArray.length()];

        for (int i=0; i<movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);


            String movieTitle = movie.getString(TMD_TITLE);
            long movieRating = movie.getLong(TMD_RATING);
            long moviePopularity = movie.getLong(TMD_POPULARITY);
            String movieSynopsis = movie.getString(TMD_SYNOPSIS);
            String movieReleaseDate = movie.getString(TMD_RELEASE_DATE);
            String movieId = movie.getString(TMD_MOVIE_ID);

            String moviePosterPath = movie.getString(TMD_POSTER_PATH);

            URL moviePosterUrl = NetworkUtils.buildImageURL(moviePosterPath);

            parsedMovieData[i] = new Movie(movieTitle, moviePosterUrl.toString(), moviePopularity, movieRating, movieSynopsis, movieReleaseDate, movieId);
        }

        return parsedMovieData;
    }
}
