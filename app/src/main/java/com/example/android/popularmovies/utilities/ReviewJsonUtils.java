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
    public static Review[] getReviewsFromJson(String ReviewsJsonStr) throws JSONException {
        Review[] parsedReviewsData;

        JSONObject ReviewsJson = new JSONObject(ReviewsJsonStr);
        JSONArray ReviewsArray = ReviewsJson.getJSONArray("results");

        parsedReviewsData = new Review[ReviewsArray.length()];

        for (int i = 0; i < ReviewsArray.length(); i++) {
            JSONObject review = ReviewsArray.getJSONObject(i);

            String author = review.getString("author");
            String content = review.getString("content");

            parsedReviewsData[i] = new Review(author, content);
        }

        return parsedReviewsData;
    }
}
