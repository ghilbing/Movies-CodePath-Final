package com.example.codepath.moviestmbd.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.fragments.DetailFragment;
import com.example.codepath.moviestmbd.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";


    private Movie mMovie;



    @Bind(R.id.movie_detail_backdrop)
    ImageView backdrop;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout toolbarLayout;
    @Nullable
    @Bind(R.id.movie_detail_release)
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setStatusBarTransparent();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mMovie = intent.getParcelableExtra(EXTRA_MOVIE);


        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE, mMovie);


        if(savedInstanceState == null){

            Fragment fragment = DetailFragment.newInstance(mMovie);
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.recycler_container, fragment).commit();
        }

        int screenWith = getResources().getDisplayMetrics().widthPixels;
        Picasso.with(this).load(mMovie.getBackdropUrl(screenWith)).into(backdrop);
        toolbarLayout.setTitle(mMovie.getTitle());

        setupTransition();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupTransition() {
        getWindow().setEnterTransition(new Explode());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarTransparent() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // handle back arrow in toolbar:
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if(id == R.id.action_settings){
            startActivity(new Intent(this, Settings.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
