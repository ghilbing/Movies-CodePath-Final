package com.example.codepath.moviestmbd.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gretel on 9/13/17.
 */

public class Trailer {

    @SerializedName("name")
    String name;

    @SerializedName("size")
    String size;

    @SerializedName("source")
    String source;

    @SerializedName("type")
    String type;


}
