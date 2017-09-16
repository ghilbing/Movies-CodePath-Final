package com.example.codepath.moviestmbd.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MovieApiDB {

    static final String LOG_TAG = MovieApiDB.class.getSimpleName();

    private static MovieApiDB movieApiDB;
    private static String apiKey;
    private static MoviesApiService service;

    static final String ENDPOINT = "http://api.themoviedb.org/3";

    public MovieApiDB(String apiKey){
        this.apiKey = apiKey;
    }


    public interface MovieListener{
        public void success(MovieResponse response);
        public void error(ErrorApi errorApi);
    }

    public interface MovieListListener {
        public void success(MovieListResponse response);
        public void error(ErrorApi error);
    }

    public interface VideoListener{
        public void success(VideoResponse response);
        public void error(ErrorApi errorApi);
    }

    public static MovieApiDB getInstance(String apiKey){
        if (movieApiDB == null) {
            movieApiDB = new MovieApiDB(apiKey);
        }
        return movieApiDB;
    }

    public interface ReviewListener {
        public void success(ReviewResponse response);
        public void error(ErrorApi error);
    }

    public void requestPopularMovies(@Nullable final MovieListListener listener) {

        getMovieService().getMovies(apiKey, new Callback<MovieListResponse>() {
            @Override
            public void success(MovieListResponse response, Response httpResponse) {
                Log.d(LOG_TAG, "Number of movies found: " + response.getMovies().size());
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Api error: " + error.getMessage());
                if (listener != null) {
                    listener.error(new ErrorApi(error));
                }
            }
        });
    }

    public void requestPopularMovies(int page, @Nullable final MovieListListener listener) {

        getMovieService().getMovies(apiKey, page, new Callback<MovieListResponse>() {
            @Override
            public void success(MovieListResponse response, Response httpResponse) {
                Log.d(LOG_TAG, "Number of movies found: " + response.getMovies().size());
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Api error: " + error.getMessage());
                if (listener != null) {
                    listener.error(new ErrorApi(error));
                }
            }
        });
    }

    public void requestRatedMovies(@Nullable final MovieListListener listener) {

        getMovieService().getRatedMovies(apiKey, new Callback<MovieListResponse>() {
            @Override
            public void success(MovieListResponse response, Response httpResponse) {
                Log.d(LOG_TAG, "Number of movies found: " + response.getMovies().size());
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Api error: " + error.getMessage());
                if (listener != null) {
                    listener.error(new ErrorApi(error));
                }
            }
        });
    }

    public void requestRatedMovies(int page, @Nullable final MovieListListener listener) {

        getMovieService().getRatedMovies(apiKey, page, new Callback<MovieListResponse>() {
            @Override
            public void success(MovieListResponse response, Response httpResponse) {
                Log.d(LOG_TAG, "Number of movies found: " + response.getMovies().size());
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Api error: " + error.getMessage());
                if (listener != null) {
                    listener.error(new ErrorApi(error));
                }
            }
        });
    }

    public void requestVideos(@NonNull int movieId, @Nullable final VideoListener listener) {

        getMovieService().getVideos(movieId, apiKey, new Callback<VideoResponse>() {
            @Override
            public void success(VideoResponse response, Response httpResponse) {
                Log.d(LOG_TAG, "Total number of videos found: " + response.getVideos().size());
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Api error: " + error.getMessage());
                if (listener != null) {
                    listener.error(new ErrorApi(error));
                }
            }
        });
    }

    public void requestReviews(@NonNull int movieId, @Nullable final ReviewListener listener) {

        getMovieService().getReviews(movieId, apiKey, new Callback<ReviewResponse>() {
            @Override
            public void success(ReviewResponse response, Response httpResponse) {
                Log.d(LOG_TAG, "Number of reviews found: " + response.getReviews().size());
                if (listener != null) {
                    listener.success(response);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Api error: " + error.getMessage());
                if (listener != null) {
                    listener.error(new ErrorApi(error));
                }
            }
        });
    }



    private MoviesApiService getMovieService() {

        if (service == null ) {
            Gson gson = new GsonBuilder().create();

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setClient(new OkClient())
                    .setLogLevel(RestAdapter.LogLevel.BASIC);
            //.setLogLevel(RestAdapter.LogLevel.FULL);

            service = builder.build().create(MoviesApiService.class);
        }

        return service;
    }


}