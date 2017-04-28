package com.example.android.popularmovies.listeners;

import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

/**
 * Created by kristenwoodward on 4/24/17.
 */

public interface OnFetchTrailerDataCompleted {
    void OnFetchTrailerDataCompleted(Trailer[] movieTrailerResults);
}
