package com.example.android.popularmovies.data;

import android.os.AsyncTask;

import com.example.android.popularmovies.listeners.OnFetchReviewDataCompleted;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.utilities.NetworkConstants;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.ReviewJsonUtils;

import java.net.URL;

/**
 * Created by kristenwoodward on 4/24/17.
 */

public class FetchReviewDataTask extends AsyncTask<String, Void, Review[]> {
    private OnFetchReviewDataCompleted mListener;

    public FetchReviewDataTask(OnFetchReviewDataCompleted listener){
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Review[] doInBackground(String[] movieId) {
        Review[] reviewList = null;

        URL reviewApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkConstants.REVIEWS_PATH);

        try {
            String jsonReviewResults = NetworkUtils.getResponseFromHttpUrl(reviewApiUrl);
            reviewList = ReviewJsonUtils.getReviewsFromJson(jsonReviewResults);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviewList;
    }

    @Override
    protected void onPostExecute(Review[] movieReviewResults) {
        mListener.OnFetchReviewDataCompleted(movieReviewResults);
    }
}
