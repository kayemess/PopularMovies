package com.example.android.popularmovies.models;

/**
 * Created by kristenwoodward on 1/24/17.
 */

public class Review {

    private String mAuthor;
    private String mContent;

    public Review(String author, String content){
        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() { return mAuthor; }

    public String getContent() { return mContent; }
}
