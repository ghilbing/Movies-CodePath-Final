package com.example.codepath.moviestmbd.rest;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jose on 10/6/15.
 */


public interface MoviesApiService {
    @GET("/movie/popular")
    void getMovies(@Query("api_key") String apiKey, Callback<MovieListResponse> cb);

    @GET("/movie/popular")
    void getMovies(@Query("api_key") String apiKey, @Query("page") int page, Callback<MovieListResponse> cb);

    @GET("/movie/top_rated")
    void getRatedMovies(@Query("api_key") String apiKey, Callback<MovieListResponse> cb);

    @GET("/movie/top_rated")
    void getRatedMovies(@Query("api_key") String apiKey, @Query("page") int page, Callback<MovieListResponse> cb);

    @GET("/movie/{id}/videos")
    void getVideos(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<VideoResponse> cb);

    @GET("/movie/{id}/reviews")
    void getReviews(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<ReviewResponse> cb);

    // Get movie details by id
    @GET("/movie/{id}")
    void getMovie(@Path("id") int movieId, @Query("api_key") String apiKey, Callback<MovieResponse> cb);


}