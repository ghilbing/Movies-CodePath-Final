package com.example.codepath.moviestmbd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.fragments.DetailFragment;
import com.example.codepath.moviestmbd.fragments.MainFragment;
import com.example.codepath.moviestmbd.model.Movie;
import com.example.codepath.moviestmbd.rest.ErrorApi;
import com.example.codepath.moviestmbd.rest.MovieApiDB;
import com.example.codepath.moviestmbd.rest.MovieListResponse;
import com.example.codepath.moviestmbd.rest.MovieResponse;
import com.example.codepath.moviestmbd.rest.ReviewResponse;
import com.example.codepath.moviestmbd.rest.VideoResponse;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieListener,
        MovieApiDB.MovieListener, MovieApiDB.ReviewListener, MovieApiDB.MovieListListener, MovieApiDB.VideoListener {

    public static final String EXTRA_MOVIE = "movie";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String TAG_DETAIL = "fragment_detail";



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:{
                startActivity(new Intent(this, Settings.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);

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

        if (isTwoPane) {
            DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail_container);


            if (fragment != null & selection == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.remove(fragment).commit();
            } else if (fragment == null || fragment.getId() != mSelectedMovie.getId()) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(DetailFragment.EXTRA_MOVIE, selection);
                fragment = DetailFragment.newInstance(selection);
                fragment.setArguments(bundle);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_detail_container, fragment, TAG_DETAIL).commit();

                /*if (view != null) {
                    ft.addSharedElement(view, getResources().getString(R.string.transition_poster));
                }*/
            }

                String title = selection == null ? "" : selection.getTitle();
                TextView titleDetail = (TextView) findViewById(R.id.movie_detail_title);
                titleDetail.setText(title);


            } else if (onClick) {
                onMovieClicked(selection, true, view);
            /*Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, selection);
            this.startActivity(intent);
            Log.d(LOG_TAG, "Starting activity");*/

        }
    }



    public void onMovieClicked(Movie movie, boolean onClick, Object view) {

        if (isTwoPane) {
            MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main_container);
            mainFragment.removeMovie(mSelectedMovie);


        } else {

            Log.d(LOG_TAG, "Starting activity");
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);

            startActivity(intent);

            //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, (View) view, getResources().getString(R.string.transition_poster));
            //ActivityCompat.startActivity(this, intent, options.toBundle());


            // this.startActivity(intent);
        }

    }

    



}
