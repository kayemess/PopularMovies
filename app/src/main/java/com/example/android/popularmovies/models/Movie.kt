package com.example.android.popularmovies.models

import java.io.Serializable
import java.net.URL

/**
 * Created by kristenwoodward on 12/7/16.
 */

class Movie(val movieName: String,
            val posterPath: String,
            val popularity: Long,
            val rating: Long,
            val movieSynopsis: String,
            val releaseDate: String,
            val movieId: String) : Serializable {

    var isFavorite = false

    companion object {
        private const val serialVersionUID = 7526472295622776147L
    }
}
