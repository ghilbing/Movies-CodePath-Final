package com.example.codepath.moviestmbd.rest;

import com.example.codepath.moviestmbd.model.Review;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by gretel on 9/8/17.
 */

public class ReviewResponse {

    @SerializedName("id")
    int id;

    @SerializedName("page")
    int page;

    @SerializedName("results")
    List<Review> reviews;

    @SerializedName("total_pages")
    int totalPages;

    @SerializedName("total_results")
    int totalResults;

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public List<Review> getReviews() {
        if (reviews == null)
            return Collections.emptyList();
        return reviews;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
