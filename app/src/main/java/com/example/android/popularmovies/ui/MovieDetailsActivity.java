package com.example.android.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.data.FetchReviewDataTask;
import com.example.android.popularmovies.data.FetchTrailerDataTask;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieProvider;
import com.example.android.popularmovies.listeners.OnFetchReviewDataCompleted;
import com.example.android.popularmovies.listeners.OnFetchTrailerDataCompleted;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.DateUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kristenwoodward on 12/9/16.
 */

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private Movie mSelectedMovie;
    private Trailer[] mMovieTrailerList;
    private Review[] mReviewList;

    private TrailerAdapter mMovieTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    @BindView(R.id.trailer_recycler_view)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView mReviewRecyclerView;
    @BindView(R.id.add_remove_favorite_button)
    FloatingActionButton mAddRemoveFavorite;
    @BindView(R.id.trailers_title_tv)
    TextView mTrailerTitleTV;
    @BindView(R.id.reviews_title_tv)
    TextView mReviewTitleTV;
    @BindView(R.id.details_poster_iv)
    ImageView mPosterImageView;
    @BindView(R.id.details_movie_title_tv)
    TextView mMovieTitleTV;
    @BindView(R.id.details_rating_tv)
    TextView mMovieRatingTV;
    @BindView(R.id.details_release_date_tv)
    TextView mReleaseDateTV;
    @BindView(R.id.details_movie_synopsis_tv)
    TextView mSynopsisTV;

    public MovieDetailsActivity() {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        postponeEnterTransition();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSelectedMovie = (Movie) getIntent().getSerializableExtra("selectedMovie");

        if (mSelectedMovie != null) {
            showMovieDetails();
            setUpTrailerList();
            setUpReviewsList();
            toggleFAB();
        } else {
            Log.e(TAG,"Error finding movie details from calling intent");
        }
    }

    private void showMovieDetails(){
        mMovieTitleTV.setText(mSelectedMovie.getMovieName());
        mMovieRatingTV.setText(mSelectedMovie.getRating() + getString(R.string.rating_denominator));
        mReleaseDateTV.setText(formatReleaseDate(mSelectedMovie.getReleaseDate()));
        mSynopsisTV.setText(mSelectedMovie.getMovieSynopsis());

        mPosterImageView.setTransitionName(createTransitionName());
        Picasso.with(this).load(mSelectedMovie.getPosterPath()).into(mPosterImageView, new Callback() {
            @Override
            public void onSuccess() {
                scheduleStartPostponedTransition(mPosterImageView);
            }

            @Override
            public void onError() {
                Picasso.with(getBaseContext()).cancelRequest(mPosterImageView);
            }
        });
    }

    private String formatReleaseDate(String releaseDate) {
        try {
            return DateUtils.getYearFromDate(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return releaseDate;
        }
    }

    private String createTransitionName(){
        return getString(R.string.poster_transition_name) + mSelectedMovie.getMovieId();
    }

    private void toggleFAB() {
        setIsMovieFavorite();
        if (mSelectedMovie.isFavorite()) {
            mAddRemoveFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mAddRemoveFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void setIsMovieFavorite() {
        Cursor allMovies = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{String.valueOf(MovieContract.MovieEntry.COLUMN_MOVIE_ID)},
                null,
                null,
                null);

        for (int position = 0; position < allMovies.getCount(); position++) {
            allMovies.moveToPosition(position);
            int movieIdColumn = allMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

            if (allMovies.getString(movieIdColumn).equals(mSelectedMovie.getMovieId())) {
                mSelectedMovie.setFavorite(true);
                return;
            }
        }
        mSelectedMovie.setFavorite(false);
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
        supportFinishAfterTransition();
    }

    @Override
    public void onItemClickListener(int position) {
        String trailerKey = mMovieTrailerList[position].getTrailerKey();
        URL trailerUrl = NetworkUtils.buildTrailerURL(trailerKey);

        Intent playVideo = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl.toString()));
        startActivity(playVideo);
    }

    private void showTrailers(Trailer[] movieTrailerResults) {
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

    private void showReviews(Review[] movieReviewResults) {
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

    public void toggleFavorite(View view) {
        if (mSelectedMovie.isFavorite()) {
            Uri uriOfMovie = MovieContract.MovieEntry.buildMovieUriWithId(mSelectedMovie.getMovieId());
            int rowsRemoved = getContentResolver().delete(uriOfMovie, null, null);

            if(rowsRemoved > 0) {
                mSelectedMovie.setFavorite(false);
                toggleFAB();
            } else {
                Log.e(TAG,"Error deleting favorited movie from DB");
            }
        } else {
            ContentValues cv = new ContentValues();

            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mSelectedMovie.getMovieId());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, mSelectedMovie.getPosterPath());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mSelectedMovie.getRating());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mSelectedMovie.getReleaseDate());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, mSelectedMovie.getMovieSynopsis());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, mSelectedMovie.getMovieName());

            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
            mSelectedMovie.setFavorite(true);
            toggleFAB();
        }
    }

    private void setUpTrailerList(){
        CustomLinearLayoutManager trailerLayoutManager = new CustomLinearLayoutManager(this);
        mMovieTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);

        FetchTrailerDataTask fetchTrailerDataTask = new FetchTrailerDataTask(new OnFetchTrailerDataCompleted() {
            @Override
            public void OnFetchTrailerDataCompleted(Trailer[] movieTrailerResults) {
                mMovieTrailerList = movieTrailerResults;
                showTrailers(mMovieTrailerList);
            }
        });

        fetchTrailerDataTask.execute(mSelectedMovie.getMovieId());
    }

    private void setUpReviewsList(){
        CustomLinearLayoutManager reviewLayoutManager = new CustomLinearLayoutManager(this);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        FetchReviewDataTask fetchReviewDataTask = new FetchReviewDataTask(new OnFetchReviewDataCompleted() {
            @Override
            public void OnFetchReviewDataCompleted(Review[] movieReviewResults) {
                mReviewList = movieReviewResults;
                showReviews(mReviewList);
            }
        });

        fetchReviewDataTask.execute(mSelectedMovie.getMovieId());
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }
}
