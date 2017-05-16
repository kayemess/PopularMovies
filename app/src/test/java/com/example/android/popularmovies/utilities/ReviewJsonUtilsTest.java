package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Review;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kristenwoodward on 5/15/17.
 */
public class ReviewJsonUtilsTest {
    String jsonString = "{'id':118340,'page':1,'results':[{'id':'53f11b7c0e0a2675b8004053','author':'Binawoo','content':'This movie was so AWESOME! I loved it all and i had a bad day before watching it but it turned it around. I love action packed movies and this was great.','url':'https://www.themoviedb.org/review/53f11b7c0e0a2675b8004053'}],'total_pages':2,'total_results':6}";

    @Test
    public void getReviewsFromJson() throws Exception {
        Review[] reviews = ReviewJsonUtils.getReviewsFromJson(jsonString);
        assertNotNull(reviews);
        assertTrue(reviews.length > 0);
    }

    @Test
    public void getReviewData() throws Throwable {
        Review[] reviews = ReviewJsonUtils.getReviewsFromJson(jsonString);
        assertEquals(reviews[0].getContent(), "This movie was so AWESOME! I loved it all and i had a bad day before watching it but it turned it around. I love action packed movies and this was great.");
        assertEquals(reviews[0].getAuthor(), "Binawoo");
    }

    @Test
    public void getNumberOfReviews() throws Throwable {
        Review[] reviews = ReviewJsonUtils.getReviewsFromJson(jsonString);
        assertTrue(reviews.length == 1);
    }
}