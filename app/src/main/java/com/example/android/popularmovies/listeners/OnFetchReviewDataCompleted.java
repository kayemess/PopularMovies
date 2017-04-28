package com.example.android.popularmovies.listeners;

import com.example.android.popularmovies.models.Review;

/**
 * Created by kristenwoodward on 4/24/17.
 */

public interface OnFetchReviewDataCompleted {
    void OnFetchReviewDataCompleted(Review[] movieReviewResults);
}
