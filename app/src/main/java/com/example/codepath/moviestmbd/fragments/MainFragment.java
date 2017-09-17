package com.example.codepath.moviestmbd.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.codepath.moviestmbd.Prefs;
import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.Sort;
import com.example.codepath.moviestmbd.adapters.MoviesAdapter;
import com.example.codepath.moviestmbd.adapters.SortSpinnerAdapter;
import com.example.codepath.moviestmbd.model.Movie;
import com.example.codepath.moviestmbd.rest.ErrorApi;
import com.example.codepath.moviestmbd.rest.MovieApiDB;
import com.example.codepath.moviestmbd.rest.MovieListResponse;
import com.example.codepath.moviestmbd.rest.MovieResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.R.attr.handle;

/**
 * Created by gretel on 9/12/17.
 */

public class MainFragment extends Fragment implements  MovieApiDB.MovieListener, MovieApiDB.MovieListListener {

    static final String LOG_TAG = MainFragment.class.getSimpleName();
    static final String EXTRA_MOVIES = "movies";

    Movie mMovie;


    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    Spinner mSortSpinner;

    int mSortMethod = Sort.POP;

    RecyclerScrollListener mScrollListener;

    int mPageMax = 20;
    int mPageSize = 20;


    MovieApiDB movieApiDB;
    MoviesAdapter mAdapter;

    OnMovieListener mListener;


    public interface OnMovieListener {
        public void onMovieSelected(Movie selection,boolean onClick, View view);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, null);

        ButterKnife.bind(this,view);

        ArrayList<Movie> movies = new ArrayList<>();

        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            mSortMethod = Prefs.getCurrentSortMethod(getActivity());
            movies = savedInstanceState.getParcelableArrayList(EXTRA_MOVIES);
        } else {
            mSortMethod = Prefs.getPreferredSortMethod(getActivity());
        }



        mAdapter = new MoviesAdapter(getContext(), mMovie, mRecyclerView, mListener);
        mScrollListener = new RecyclerScrollListener();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(movies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.addOnScrollListener(mScrollListener);



        movieApiDB = MovieApiDB.getInstance(getString(R.string.api_key));

        if(mAdapter.getItemCount() == 0) {
            if (mSortMethod == Sort.POP) {

                movieApiDB.requestPopularMovies(this);
            } else {
                movieApiDB.requestRatedMovies(this);
            }
        }

        checkNetwork();

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.spin_test);

        menuItem.setActionView(R.layout.sort_spinner);
        View view =menuItem.getActionView();

        mSortSpinner = (Spinner) view.findViewById(R.id.spinner_nav);
        mSortSpinner.setAdapter(new SortSpinnerAdapter(this, getActivity(), Sort.getOptions()));
        mSortSpinner.setSelection(mSortMethod);
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Prefs.setCurrentSortMethod(getActivity(), i);
                sortSelection(Sort.getSortMethod(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void sortSelection(int sortType){
        if(mSortMethod == sortType)
            return;

        mSortMethod = sortType;

        switch (mSortMethod) {
            case Sort.POP:
                movieApiDB.requestPopularMovies(this);
                return;
            case Sort.RAT:
                movieApiDB.requestRatedMovies(this);
                return;
            default:
                Toast.makeText(getActivity(), "Sort type does not exist", Toast.LENGTH_SHORT).show();
                return;

        }
    }

    public void removeMovie(Movie movie) {
        mAdapter.removeData(movie);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }

    private void loadPage(int page){
        movieApiDB.requestPopularMovies(page, this);
        return;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMovieListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
       bundle.putParcelableArrayList(EXTRA_MOVIES, mAdapter.mMovieList);

        super.onSaveInstanceState(bundle);
    }

    @Override
    public void success(MovieResponse response) {
        Log.i("MovieListResponse", String.valueOf(response));

        mAdapter.appendData(response.getMovie());

    }

    @Override
    public void success(MovieListResponse response) {

        if(response.getPage() == 1){
            int pageMax = (response.getTotalPages() < mPageMax) ? response.getTotalPages() : mPageMax;
            mScrollListener.totalPages = pageMax;
            mAdapter.setData(response.getMovies(), mPageSize, pageMax);
        }else {

            mAdapter.appendData(response.getMovies());
        }

    }

    @Override
    public void error(ErrorApi errorApi) {

    }

    private boolean checkNetwork() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), "Network unavailable (check your connection)", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void loadNextPage(int page) {
        //Log.d(TAG, "Load page: " + page);

        switch (mSortMethod) {
            case Sort.POP:
                movieApiDB.requestPopularMovies(page, this);
                return;
            case Sort.RAT:
                movieApiDB.requestRatedMovies(page, this);
                return;
            default:
                return;
        }

    }

    class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        int currentPage;
        int totalPages;
        int previousTotal;
        int visibleThreshold;
        boolean loading;

        public void init() {
            currentPage = 1;
            totalPages = 1;
            previousTotal = 0;
            visibleThreshold = 5;
            loading = false;
        }

        public RecyclerScrollListener() {
            super();
            init();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            //int totalItemCount = linearLayoutManager.getItemCount();
            int totalItemCount = mAdapter.mMovieList.size();
            int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

            // load finished
            if (loading && totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }

            // load more data when near end of scroll view (within threshold)
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                if (currentPage < totalPages) {
                    loadNextPage(currentPage + 1);
                    loading = true;
                }
            }
        }
    }


}
