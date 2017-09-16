package com.example.codepath.moviestmbd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.adapters.DetailsAdapter;
import com.example.codepath.moviestmbd.model.Movie;
import com.example.codepath.moviestmbd.model.Review;
import com.example.codepath.moviestmbd.model.Video;
import com.example.codepath.moviestmbd.rest.ErrorApi;
import com.example.codepath.moviestmbd.rest.MovieApiDB;
import com.example.codepath.moviestmbd.rest.ReviewResponse;
import com.example.codepath.moviestmbd.rest.VideoResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.description;
import static android.content.ContentValues.TAG;

/**
 * Created by gretel on 9/12/17.
 */

public class DetailFragment extends Fragment implements MovieApiDB.ReviewListener, MovieApiDB.VideoListener {

    static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String EXTRA_MOVIE = "movie";

    Movie mMovie;
    MovieApiDB movieApiDB;

    DetailsAdapter mAdapter;

    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    public static DetailFragment newInstance(Movie movie){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }


    public DetailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mMovie = getArguments().getParcelable(EXTRA_MOVIE);
            Log.i("Movie Content: ", String.valueOf(mMovie.getTitle()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        ButterKnife.bind(this, view);

        mAdapter = new DetailsAdapter(getContext(), mMovie);

        movieApiDB = MovieApiDB.getInstance(getString(R.string.api_key));
        if(mMovie != null){
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            movieApiDB.requestReviews(mMovie.getId(), this);
            movieApiDB.requestVideos(mMovie.getId(), this);



        }

        return view;

    }

    @Override
    public void success(VideoResponse response) {
        List<Video> videos = response.getYoutubeTrailers();
        mAdapter.setVideos(videos);

    }

    @Override
    public void success(ReviewResponse response) {
        List<Review> reviews = response.getReviews();
        mAdapter.setReviews(reviews);

    }

    @Override
    public void error(ErrorApi error) {

        Log.e(TAG, "Error retrieving data from API: " + error.getReason());

    }

}
