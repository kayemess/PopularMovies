package com.example.android.popularmovies.ui;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.data.FetchReviewDataTask;
import com.example.android.popularmovies.data.FetchTrailerDataTask;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.listeners.OnFetchReviewDataCompleted;
import com.example.android.popularmovies.listeners.OnFetchTrailerDataCompleted;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.CustomLinearLayoutManager;
import com.example.android.popularmovies.utilities.DateUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
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

    private String mTitle, mSynopsis, mRating, mPoster, mReleaseDate, mMovieId;
    private boolean mIsFavorite = false;

    private Trailer[] mMovieTrailerList;
    private TrailerAdapter mMovieTrailerAdapter;

    private Review[] mReviewList;
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

    public MovieDetailsActivity() {
        //default public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        postponeEnterTransition();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setMovieDetailsFromIntent(getIntent());

        scheduleStartPostponedTransition(mPosterImageView);

        if (!mMovieId.equals("")) {
            setUpTrailerList();
            setUpReviewsList();

            // see if movie is already on favorites list
            isMovieAFavorite();

            // update FAB drawable depending on whether movie is favorited
            updateFavoriteFAB();
        } else {
            Log.e(TAG,"Error finding movie details from calling intent");
        }
    }

    private void setMovieDetailsFromIntent(Intent intent){
        if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)) {
            TextView movieTitleTV = (TextView) findViewById(R.id.details_movie_title_tv);
            movieTitleTV.setText(intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
        }

        if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RATING)) {
            TextView movieRatingTV = (TextView) findViewById(R.id.details_rating_tv);
            movieRatingTV.setText(intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RATING) + getString(R.string.rating_denominator));
        }

        if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)) {
            String releaseDate = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
            try {
                mReleaseDate = DateUtils.getYearFromDate(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
                mReleaseDate = releaseDate;
            }
            TextView releaseDateTV = (TextView) findViewById(R.id.details_release_date_tv);
            releaseDateTV.setText(mReleaseDate);
        }

        if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS)) {
            TextView movieSynopsisTV = (TextView) findViewById(R.id.details_movie_synopsis_tv);
            movieSynopsisTV.setText(intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS));
        }

        if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID)) {
            mMovieId = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        }

        if (intent.hasExtra(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER)) {
            mPosterImageView.setTransitionName(createTransitionName());
            Picasso.with(this).load(intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER)).into(mPosterImageView);
        }
    }

    private String createTransitionName(){
        return getString(R.string.poster_transition_name) + mMovieId;
    }

    private void updateFavoriteFAB() {
        if (mIsFavorite) {
            mAddRemoveFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mAddRemoveFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void isMovieAFavorite() {

        Cursor allMovies = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{String.valueOf(MovieContract.MovieEntry.COLUMN_MOVIE_ID)},
                null,
                null,
                null);

        for (int position = 0; position < allMovies.getCount(); position++) {
            allMovies.moveToPosition(position);
            int movieIdColumn = allMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

            if (allMovies.getString(movieIdColumn).equals(mMovieId)) {
                mIsFavorite = true;
                return;
            }
        }

        mIsFavorite = false;
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

    public void addRemoveFavorite(View view) {
        if (!mIsFavorite) {

            ContentValues cv = new ContentValues();

            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieId);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, mPoster);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, mRating);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mReleaseDate);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, mSynopsis);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, mTitle);

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

            mIsFavorite = true;
            updateFavoriteFAB();
        } else {
            Uri uriOfMovie = MovieContract.MovieEntry.buildMovieUriWithId(mMovieId);
            int rowsRemoved = getContentResolver().delete(uriOfMovie, null, null);

            if(rowsRemoved > 0) {
                mIsFavorite = false;
                updateFavoriteFAB();
            } else {
                Log.e(TAG,"Error deleting favorited movie from DB");
            }
        }

    }

    private void setUpTrailerList(){
        CustomLinearLayoutManager trailerLayoutManager = new CustomLinearLayoutManager(this);
        mMovieTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
        mTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);

        FetchTrailerDataTask fetchTrailerDataTask = new FetchTrailerDataTask(this, new OnFetchTrailerDataCompleted() {
            @Override
            public void OnFetchTrailerDataCompleted(Trailer[] movieTrailerResults) {
                mMovieTrailerList = movieTrailerResults;
                showTrailers(mMovieTrailerList);
            }
        });

        fetchTrailerDataTask.execute(mMovieId);
    }

    private void setUpReviewsList(){
        CustomLinearLayoutManager reviewLayoutManager = new CustomLinearLayoutManager(this);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        FetchReviewDataTask fetchReviewDataTask = new FetchReviewDataTask(this, new OnFetchReviewDataCompleted() {
            @Override
            public void OnFetchReviewDataCompleted(Review[] movieReviewResults) {
                mReviewList = movieReviewResults;
                showReviews(mReviewList);
            }
        });

        fetchReviewDataTask.execute(mMovieId);
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
