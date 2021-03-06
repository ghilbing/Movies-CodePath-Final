package com.example.codepath.moviestmbd.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.activities.DetailActivity;
import com.example.codepath.moviestmbd.fragments.MainFragment;
import com.example.codepath.moviestmbd.model.Movie;
import com.example.codepath.moviestmbd.rest.MovieApiDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.R.attr.data;
import static android.content.ContentValues.TAG;

/**
 * Created by gretel on 9/13/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public ArrayList<Movie> mMovieList = new ArrayList<>();
    private Context context;
    private Movie mMovie;
    private MainFragment.OnMovieListener mListener;
    private RecyclerView mRecyclerView;

    int maxPages = -1;
    int pageSize = 20;


    public MoviesAdapter(Context context, Movie movie, RecyclerView recyclerView,  MainFragment.OnMovieListener listener) {
        this.mMovie = movie;
        this.context = context;
        this.mListener = listener;
        this.mRecyclerView = recyclerView;
    }

    public void setData(List<Movie> data, int pageSize, int maxPages){
        this.maxPages = maxPages;
        this.pageSize = pageSize;
        setData(data);
    }

    public void setData(List<Movie> data) {
        this.mMovieList.clear();
        this.mMovieList.addAll(data);
        this.notifyDataSetChanged();
        notifyMovieSelectionListener();

    }

    public void appendData(List<Movie> movies) {
        this.clearData();
        this.mMovieList.addAll(movies);
        this.notifyDataSetChanged();
    }

    public void appendData(Movie movie) {
        this.mMovieList.add(movie);
        this.notifyItemChanged(this.mMovieList.size() - 1);
        if (this.mMovieList.size() == 1) {
            notifyMovieSelectionListener();

        }
    }

    public void clearData() {
        this.maxPages = -1;
        this.mMovieList.clear();
        this.notifyDataSetChanged();
    }

    public void removeData(Movie movie){
        int index = this.mMovieList.indexOf(movie);
        if(index !=1){
            this.mMovieList.remove(index);
            this.notifyItemRemoved(index);
            notifyMovieSelectionListener();
        }
    }

    public void notifyMovieSelectionListener() {
        if (mListener != null && !mMovieList.isEmpty()) {
            View view = mRecyclerView.getChildAt(0);
            Log.d(TAG, "Found child view: " + view);
            View posterView = null;
            if (view != null) {
                posterView = view.findViewById(R.id.movie_poster_row);
                Log.d(TAG, "Found poster view: " + posterView);
            }
            mListener.onMovieSelected(mMovieList.get(0), false, posterView);
            //mRecyclerView.setSelected();
        }
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


        Movie movie = mMovieList.get(position);


        MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
        int screenWith = context.getResources().getDisplayMetrics().widthPixels;
        Picasso.with(movieViewHolder.poster.getContext())
                .load(movie.getPosterUrl(screenWith))
                .transform(new RoundedCornersTransformation(20,20))
                .placeholder(R.drawable.ic_local_movies_gray)
                .into(movieViewHolder.poster);
        movieViewHolder.title.setText(movie.getTitle());


    }



        /*holder.title.setText(movie.getTitle());
        Picasso.with(holder.poster.getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_local_movies_gray)
                .error(R.drawable.ic_local_movies_gray)
                .into(holder.poster);*/


    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.movie_poster_row)
        ImageView poster;
        @Bind(R.id.movie_title_row)
        TextView title;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Movie movie = mMovieList.get(position);
            //  Toast.makeText(context, title.getText(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);

            context.startActivity(intent);

        }
    }
}








