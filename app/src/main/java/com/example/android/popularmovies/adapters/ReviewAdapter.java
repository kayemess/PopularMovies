package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Review;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Review[] mReviewList;
    private Context mContext;

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForTrailer = R.layout.review_detail;
        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachImmediately = false;

        View view = inflater.inflate(layoutIdForTrailer, parent, shouldAttachImmediately);

        mContext = view.getContext();

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        String author = mReviewList[position].getAuthor();
        holder.mReviewAuthorTV.setText(author);

        String content = mReviewList[position].getContent();
        holder.mReviewContentTV.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null) return 0;
        return mReviewList.length;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewAuthorTV;
        public final TextView mReviewContentTV;


        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewAuthorTV = (TextView) itemView.findViewById(R.id.author_tv);
            mReviewContentTV = (TextView) itemView.findViewById(R.id.content_tv);
        }
    }

    public void setReviewData(Review[] reviewData){
        mReviewList = reviewData;
        notifyDataSetChanged();
    }
}
