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

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static Trailer[] getMovieTrailersFromJson(Context context, String movieTrailerJsonStr) throws JSONException {

        //TMB = The Movie DB

        //list of results is contained in results list
        final String TMD_LIST = "results";

        //movie trailer attributes
        final String TMD_TRAILER_ID = "id";
        final String TMD_TRAILER_KEY = "key";
        final String TMD_TRAILER_NAME = "name";
        final String TMD_TRAILER_TYPE = "type";

        final String OWM_MESSAGE_CODE = "cod";

        Trailer[] parsedMovieTrailerData = null;

        JSONObject movieTrailerJson = new JSONObject(movieTrailerJsonStr);

        JSONArray movieTrailerArray = movieTrailerJson.getJSONArray(TMD_LIST);

        parsedMovieTrailerData = new Trailer[movieTrailerArray.length()];

        for (int i=0; i<movieTrailerArray.length(); i++) {
            JSONObject movieTrailer = movieTrailerArray.getJSONObject(i);


            String trailerId = movieTrailer.getString(TMD_TRAILER_ID);
            String trailerKey = movieTrailer.getString(TMD_TRAILER_KEY);
            String trailerName = movieTrailer.getString(TMD_TRAILER_NAME);
            String trailerType = movieTrailer.getString(TMD_TRAILER_TYPE);

            parsedMovieTrailerData[i] = new Trailer(trailerId, trailerKey, trailerName, trailerType);
        }

        return parsedMovieTrailerData;
    }
}
