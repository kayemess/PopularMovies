package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;

/**
 * Created by kristenwoodward on 12/7/16.
 */

public class MovieJsonUtils {
    public static Movie[] getMoviesFromJson(String movieJsonStr) throws JSONException {
        Movie[] parsedMovieData;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");

        parsedMovieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);

            String movieTitle = movie.getString("original_title");
            long movieRating = movie.getLong("vote_average");
            long moviePopularity = movie.getLong("popularity");
            String movieSynopsis = movie.getString("overview");
            String movieReleaseDate = movie.getString("release_date");
            String movieId = movie.getString("id");
            String moviePosterPath = movie.getString("poster_path");

            URL moviePosterUrl = NetworkUtils.buildImageURL(moviePosterPath);

            parsedMovieData[i] = new Movie(movieTitle, moviePosterUrl.toString(), moviePopularity, movieRating, movieSynopsis, movieReleaseDate, movieId);
        }

        return parsedMovieData;
    }
}
