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
        final String TMD_OVERVIEW = "overview";
        final String TMD_RELEASE_DATE = "release_date";
        final String TMD_TITLE = "original_title";
        final String TMD_SYNOPSIS = "overview";

        //movie popularity
        final String TMD_POPULARITY = "popularity";

        //movie rating
        final String TMD_RATING = "vote_average";

        final String OWM_MESSAGE_CODE = "cod";

        Movie[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieJsonStr);

        //is there an error?
        /**
        if (movieJson.has(OWM_MESSAGE_CODE)){
            int errorCode = movieJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid
                    return null;
                default:
                    /* Server probably down
                    return null;
            }
        }
         */

        JSONArray movieArray = movieJson.getJSONArray(TMD_LIST);

        parsedMovieData = new Movie[movieArray.length()];

        for (int i=0; i<movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);


            String movieTitle = movie.getString(TMD_TITLE);
            long movieRating = movie.getLong(TMD_RATING);
            long moviePopularity = movie.getLong(TMD_POPULARITY);
            String movieSynopsis = movie.getString(TMD_OVERVIEW);
            String movieReleaseDate = movie.getString(TMD_RELEASE_DATE);

            String moviePosterPath = movie.getString(TMD_POSTER_PATH);

            URL moviePosterUrl = buildImageURL(moviePosterPath);

            parsedMovieData[i] = new Movie(movieTitle, moviePosterUrl, moviePopularity, movieRating, movieSynopsis, movieReleaseDate);
        }

        return parsedMovieData;
    }

    /**
     * Builds a URL used to access the movie poster image
     * @param imagePath is the relative path to the specific movie poster
     * @return the full URL to access the movie poster
     */
    public static URL buildImageURL(String imagePath){
        Uri builtURI = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath(POSTER_IMAGE_SIZE)
                .appendEncodedPath(imagePath)
                .build();

        URL url = null;

        try { url = new URL(builtURI.toString()); }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}
