package com.example.codepath.moviestmbd.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gretel on 9/8/17.
 */

public class Review {

    @SerializedName("id")
    String id;

    @SerializedName("author")
    String author;

    @SerializedName("content")
    String content;

    @SerializedName("url")
    String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}