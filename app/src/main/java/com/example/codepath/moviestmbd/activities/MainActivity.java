package com.example.codepath.moviestmbd.activities;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.adapters.MoviesAdapter;
import com.example.codepath.moviestmbd.fragments.DetailFragment;
import com.example.codepath.moviestmbd.fragments.MainFragment;
import com.example.codepath.moviestmbd.model.Movie;
import com.example.codepath.moviestmbd.model.Review;
import com.example.codepath.moviestmbd.rest.ErrorApi;
import com.example.codepath.moviestmbd.rest.MovieApiDB;
import com.example.codepath.moviestmbd.rest.MovieListResponse;
import com.example.codepath.moviestmbd.rest.MovieResponse;
import com.example.codepath.moviestmbd.rest.ReviewResponse;
import com.example.codepath.moviestmbd.rest.VideoResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieListener,
        MovieApiDB.MovieListener, MovieApiDB.ReviewListener, MovieApiDB.MovieListListener, MovieApiDB.VideoListener {

    public static final String EXTRA_MOVIE = "movie";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TAG_DETAIL = "fragment_detail";
    public static String SORT = "sort";


    MovieApiDB movieApiDB;


    Movie mSelectedMovie;

    //Determines if this is a one or two pane layout
    boolean isTwoPane = false;



    @Bind(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        determinePaneLayout();


        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            mSelectedMovie = savedInstanceState.getParcelable(EXTRA_MOVIE);

        }


        movieApiDB = MovieApiDB.getInstance(getString(R.string.api_key));

        movieApiDB.requestPopularMovies(this);
        movieApiDB.requestPopularMovies(2, this);
        movieApiDB.requestRatedMovies(this);
        movieApiDB.requestRatedMovies(2, this);
        movieApiDB.requestReviews(211672, this);
        movieApiDB.requestVideos(211672, this);


    }

    //Determines wich layout we are in (tablet or phone)
    private void determinePaneLayout() {
        FrameLayout fragmentMovieDetail = (FrameLayout) findViewById(R.id.fragment_detail_container);

        if (fragmentMovieDetail != null){
            isTwoPane =true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(EXTRA_MOVIE, mSelectedMovie);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mSelectedMovie != null){
            onMovieSelected(mSelectedMovie, false, null);
        }
    }




    @Override
    public void success(MovieResponse response) {
        Log.i("MovieResponse", valueOf(response));
    }

    @Override
    public void success(MovieListResponse response) {
        Log.i("MovieListResponse", valueOf(response));
       // movies = response.getMovies();
       // mAdapter.setMovieList(response.getMovies());
    }


    @Override
    public void success(VideoResponse response) {
        Log.i("VideoResponse", valueOf(response));
    }


    @Override
    public void success(ReviewResponse response) {
        Log.i("ReviewResponse", valueOf(response));
    }


    @Override
    public void error(ErrorApi errorApi) {
        Log.i("ErrorApi", valueOf(errorApi));
    }


    @Override
    public void onMovieSelected(Movie selection, boolean onClick, View view) {


       mSelectedMovie = selection;

        if(isTwoPane) {
            DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail_container);

            if(fragment != null & selection == null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(fragment).commit();
            } else if (fragment == null || fragment.getId() != mSelectedMovie.getId()){
                Bundle bundle = new Bundle();
                bundle.putParcelable(DetailFragment.EXTRA_MOVIE, selection);
                fragment = DetailFragment.newInstance(selection);
                fragment.setArguments(bundle);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_detail_container, fragment, TAG_DETAIL).commit();

                }

            String title = selection == null ? "": selection.getTitle();
            TextView titleDetail = (TextView) findViewById(R.id.movie_detail_title);
            titleDetail.setText(title);


        }else if (onClick) {
            onMovieClicked(selection, true, view);
            /*Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, selection);
            this.startActivity(intent);
            Log.d(LOG_TAG, "Starting activity");*/
        }
    }


    public void onMovieClicked(Movie movie, boolean onClick, Object view) {
        Log.d(LOG_TAG, "Starting activity");
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);


        this.startActivity(intent);

    }

}
