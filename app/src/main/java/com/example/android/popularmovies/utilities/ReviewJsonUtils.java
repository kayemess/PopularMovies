package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class ReviewJsonUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static Review[] getReviewsFromJson(Context context, String ReviewsJsonStr) throws JSONException {

        //TMB = The Movie DB

        //list of results is contained in results list
        final String TMD_LIST = "results";

        //movie trailer attributes
        final String TMD_AUTHOR = "author";
        final String TMD_CONTENT = "content";

        final String OWM_MESSAGE_CODE = "cod";

        Review[] parsedReviewsData = null;

        JSONObject ReviewsJson = new JSONObject(ReviewsJsonStr);

        JSONArray ReviewsArray = ReviewsJson.getJSONArray(TMD_LIST);

        parsedReviewsData = new Review[ReviewsArray.length()];

        for (int i=0; i<ReviewsArray.length(); i++) {
            JSONObject review = ReviewsArray.getJSONObject(i);


            String author = review.getString(TMD_AUTHOR);
            String content = review.getString(TMD_CONTENT);

            parsedReviewsData[i] = new Review(author, content);
        }

        return parsedReviewsData;
    }
}
