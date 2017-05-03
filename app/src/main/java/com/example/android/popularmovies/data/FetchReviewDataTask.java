package com.example.android.popularmovies.data;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.listeners.OnFetchReviewDataCompleted;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.ReviewJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by kristenwoodward on 4/24/17.
 */

public class FetchReviewDataTask extends AsyncTask<String, Void, Review[]> {

    OnFetchReviewDataCompleted mListener;
    Context mContext;
    private Review[] mReviewList;

    public FetchReviewDataTask(Context context, OnFetchReviewDataCompleted listener){
        mListener = listener;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Review[] doInBackground(String[] movieId) {
        URL reviewApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkUtils.REVIEWS_PATH);

        mReviewList = null;

        try {
            // get JSON of movie results from the api URL, sorted based on the user's preferences
            String jsonReviewResults
                    = NetworkUtils.getResponseFromHttpUrl(reviewApiUrl);

            // create an array of movie objects using the JSON
            mReviewList = ReviewJsonUtils.getReviewsFromJson(mContext, jsonReviewResults);

            return mReviewList;
        } catch (IOException e) {
            e.printStackTrace();
            return mReviewList;
        } catch (JSONException e) {
            e.printStackTrace();
            return mReviewList;
        }
    }

    @Override
    protected void onPostExecute(Review[] movieReviewResults) {
        mListener.OnFetchReviewDataCompleted(movieReviewResults);
    }
}
