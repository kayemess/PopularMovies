package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class TrailerJsonUtils {
    public static Trailer[] getMovieTrailersFromJson(String movieTrailerJsonStr) throws JSONException {
        Trailer[] parsedMovieTrailerData;

        JSONObject movieTrailerJson = new JSONObject(movieTrailerJsonStr);
        JSONArray movieTrailerArray = movieTrailerJson.getJSONArray("results");

        parsedMovieTrailerData = new Trailer[movieTrailerArray.length()];

        for (int i = 0; i < movieTrailerArray.length(); i++) {
            JSONObject movieTrailer = movieTrailerArray.getJSONObject(i);

            String trailerId = movieTrailer.getString("id");
            String trailerKey = movieTrailer.getString("key");
            String trailerName = movieTrailer.getString("name");
            String trailerType = movieTrailer.getString("type");

            parsedMovieTrailerData[i] = new Trailer(trailerId, trailerKey, trailerName, trailerType);
        }

        return parsedMovieTrailerData;
    }
}
