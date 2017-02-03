package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.CustomLinearLayoutManager;
import com.example.android.popularmovies.utilities.DateUtils;
import com.example.android.popularmovies.utilities.ReviewJsonUtils;
import com.example.android.popularmovies.utilities.TrailerJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by kristenwoodward on 12/9/16.
 */

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    private String mTitle;
    private String mSynopsis;
    private String mRating;
    private String mPoster;
    private String mReleaseDate;
    private String mMovieId;

    private Trailer[] mMovieTrailerList;
    private Review[] mReviewList;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mMovieTrailerAdapter;

    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private Button mAddRemoveFavorite;
    private TextView mTrailerTitleTV;
    private TextView mReviewTitleTV;

    private boolean mButtonIsAdd = true;

    private String[] mMovieIds = {""};

    public MovieDetailsActivity() {
        //default public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailer_recycler_view);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.reviews_recycler_view);
        mAddRemoveFavorite = (Button) findViewById(R.id.add_remove_favorite_button);
        mTrailerTitleTV = (TextView) findViewById(R.id.trailers_title_tv);
        mReviewTitleTV = (TextView) findViewById(R.id.reviews_title_tv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent launchDetailsIntent = getIntent();

        if (launchDetailsIntent.hasExtra("movieTitle")) {
            mTitle = launchDetailsIntent.getStringExtra("movieTitle");
            TextView movieTitleTV = (TextView) findViewById(R.id.details_movie_title_tv);
            movieTitleTV.setText(mTitle);
        }

        if (launchDetailsIntent.hasExtra("movieRating")) {
            mRating = launchDetailsIntent.getStringExtra("movieRating");
            TextView movieRatingTV = (TextView) findViewById(R.id.details_rating_tv);
            movieRatingTV.setText(mRating + getString(R.string.rating_denominator));
        }

        if (launchDetailsIntent.hasExtra("moviePoster")) {
            mPoster = launchDetailsIntent.getStringExtra("moviePoster");
            ImageView moviePosterIV = (ImageView) findViewById(R.id.details_poster_iv);
            Picasso.with(this).load(mPoster).into(moviePosterIV);
        }

        if (launchDetailsIntent.hasExtra("movieReleaseDate")) {
            String releaseDate = launchDetailsIntent.getStringExtra("movieReleaseDate");
            try {
                mReleaseDate = DateUtils.getYearFromDate(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
                mReleaseDate = releaseDate;
            }
            TextView releaseDateTV = (TextView) findViewById(R.id.details_release_date_tv);
            releaseDateTV.setText(mReleaseDate);
        }

        if (launchDetailsIntent.hasExtra("movieSynopsis")) {
            mSynopsis = launchDetailsIntent.getStringExtra("movieSynopsis");
            TextView movieSynopsisTV = (TextView) findViewById(R.id.details_movie_synopsis_tv);
            movieSynopsisTV.setText(mSynopsis);
        }

        if (launchDetailsIntent.hasExtra("movieId")) {
            mMovieId = launchDetailsIntent.getStringExtra("movieId");
            mMovieIds[0] = mMovieId;
        }

        if (mMovieIds[0] != "") {
            // create a linear layout manager for recylcer view that holds trailer links
            CustomLinearLayoutManager trailerLayoutManager = new CustomLinearLayoutManager(this);
            CustomLinearLayoutManager reviewLayoutManager = new CustomLinearLayoutManager(this);

            mMovieTrailerAdapter = new TrailerAdapter(this);
            mReviewAdapter = new ReviewAdapter();

            mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
            mTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);

            mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
            mReviewRecyclerView.setAdapter(mReviewAdapter);

            new FetchTrailerData().execute(mMovieIds);
            new FetchReviewData().execute(mMovieIds);

            // see if movie is already on favorites -- if it is, updated button to "remove"
/*            Uri movieUri = MovieContract.MovieEntry.buildMovieUriWithId(mMovieId);
            Cursor favoriteMovies = getContentResolver().query(movieUri, new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID}, null, null, null);

            // if returned query doesn't contain a movie, set add to true, else to false
            if (favoriteMovies.getCount() <0 -1){
                mButtonIsAdd = true;
            } else {
                mButtonIsAdd = false;
            }*/
            isMovieAFavorite(mMovieId);

            // update text on add button using a helper method
            updateAddButton();

        }

    }

    private void updateAddButton() {
        if (mButtonIsAdd) {
            mAddRemoveFavorite.setText(R.string.add_to_favorites);
        } else {
            mAddRemoveFavorite.setText(R.string.remove_from_favorites);
        }
    }

    private void isMovieAFavorite(String movieId) {
        //Uri movieUri = MovieContract.MovieEntry.buildMovieUriWithId(movieId);

        Cursor allMovies = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{String.valueOf(MovieContract.MovieEntry.COLUMN_MOVIE_ID)},
                null,
                null,
                null);

        for (int position = 0; position < allMovies.getCount(); position++) {
            allMovies.moveToPosition(position);
            int movieIdColumn = allMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

            if (allMovies.getString(movieIdColumn).equals(movieId)) {
                mButtonIsAdd = false;
                return;
            }
        }

        mButtonIsAdd = true;
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onItemClickListener(int position) {
        String trailerKey = mMovieTrailerList[position].getTrailerKey();
        URL trailerUrl = NetworkUtils.buildTrailerURL(trailerKey);

        Intent playVideo = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl.toString()));
        startActivity(playVideo);
    }

    public class FetchTrailerData extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressBar.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.INVISIBLE);
            //Log.v("main,onPreExecute","preExecute executed");
        }

        @Override
        protected Trailer[] doInBackground(String[] movieId) {
            String apiKey = getString(R.string.api_key);
            URL trailerApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkUtils.VIDEO_PATH, apiKey);
            URL reviewApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkUtils.REVIEWS_PATH, apiKey);

            mMovieTrailerList = null;
            mReviewList = null;

            try {
                // get JSON of movie results from the api URL, sorted based on the user's preferences
                String jsonMovieTrailerResults
                        = NetworkUtils.getResponseFromHttpUrl(trailerApiUrl);

                // create an array of movie objects using the JSON
                mMovieTrailerList = TrailerJsonUtils.getMovieTrailersFromJson(MovieDetailsActivity.this, jsonMovieTrailerResults);

                return mMovieTrailerList;
            } catch (IOException e) {
                e.printStackTrace();
                return mMovieTrailerList;
            } catch (JSONException e) {
                e.printStackTrace();
                return mMovieTrailerList;
            }
        }

        @Override
        protected void onPostExecute(Trailer[] movieTrailerResults) {
            //mProgressBar.setVisibility(View.INVISIBLE);
            if (movieTrailerResults != null) {
                if (movieTrailerResults.length == 0) {
                    mTrailerTitleTV.setVisibility(View.GONE);
                    mTrailerRecyclerView.setVisibility(View.GONE);
                } else {
                    mMovieTrailerAdapter.setMovieTrailerData(movieTrailerResults);
                    mTrailerRecyclerView.setVisibility(View.VISIBLE);
                    mTrailerTitleTV.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    public class FetchReviewData extends AsyncTask<String, Void, Review[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressBar.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.INVISIBLE);
            //Log.v("main,onPreExecute","preExecute executed");
        }

        @Override
        protected Review[] doInBackground(String[] movieId) {
            String apiKey = getString(R.string.api_key);
            URL reviewApiUrl = NetworkUtils.buildMovieDetailsApiUrl(movieId[0], NetworkUtils.REVIEWS_PATH, apiKey);

            mReviewList = null;

            try {
                // get JSON of movie results from the api URL, sorted based on the user's preferences
                String jsonReviewResults
                        = NetworkUtils.getResponseFromHttpUrl(reviewApiUrl);

                // create an array of movie objects using the JSON
                mReviewList = ReviewJsonUtils.getReviewsFromJson(MovieDetailsActivity.this, jsonReviewResults);

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
            //mProgressBar.setVisibility(View.INVISIBLE);
            if (movieReviewResults != null) {
                if (movieReviewResults.length == 0) {
                    mReviewTitleTV.setVisibility(View.GONE);
                    mReviewRecyclerView.setVisibility(View.GONE);
                } else {
                    mReviewAdapter.setReviewData(movieReviewResults);
                    mReviewRecyclerView.setVisibility(View.VISIBLE);
                    mReviewTitleTV.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void addRemoveFavorite(View view) {
        if (mButtonIsAdd) {

            ContentValues cv = new ContentValues();

            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, mPoster);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mRating);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mReleaseDate);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, mSynopsis);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, mTitle);

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

            if (uri != null) {
                Toast m = Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG);
                m.show();
            }
            mButtonIsAdd = false;
            updateAddButton();
        } else {
            Uri uriOfMovie = MovieContract.MovieEntry.buildMovieUriWithId(mMovieId);
            int rowsRemoved = getContentResolver().delete(uriOfMovie, null, null);

            Toast m = Toast.makeText(getBaseContext(), String.valueOf(rowsRemoved), Toast.LENGTH_LONG);
            m.show();

            mButtonIsAdd = true;
            updateAddButton();
        }

    }
}
