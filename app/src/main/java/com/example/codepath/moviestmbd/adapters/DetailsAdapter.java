package com.example.codepath.moviestmbd.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codepath.moviestmbd.R;
import com.example.codepath.moviestmbd.model.Movie;
import com.example.codepath.moviestmbd.model.Review;
import com.example.codepath.moviestmbd.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.media.CamcorderProfile.get;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by gretel on 9/13/17.
 */

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static final int HEAD = 1;
    static final int TRAILER = 2;
    static final int REVIEW = 3;

    Context context;

    Movie mMovie;

    List<Review> reviews = new ArrayList<>();
    List<Video> videos = new ArrayList<>();

    public void setReviews(List<Review> reviews){
        this.reviews.clear();
        this.reviews.addAll(reviews);
        this.notifyDataSetChanged();
    }

    public void setVideos(List<Video> videos){
        this.videos.clear();
        this.videos.addAll(videos);
        this.notifyDataSetChanged();
    }

    public Uri getFirstVideoUri(){
        return !videos.isEmpty() ? videos.get(0).getYoutubeURL() : null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case HEAD:
                View headView = inflater.inflate(R.layout.item_head, parent, false);
                holder = new HeadViewHolder(headView);

            case TRAILER:
                View videoView = inflater.inflate(R.layout.item_video, parent, false);
                holder = new TrailerViewHolder(videoView);
                break;
            case REVIEW:
                View reviewView = inflater.inflate(R.layout.item_review, parent, false);
                holder = new ReviewViewHolder(reviewView);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case HEAD:
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;
                Picasso.with(headViewHolder.poster.getContext())
                        .load(mMovie.getPosterUrl())
                        .placeholder(R.drawable.ic_local_movies_gray)
                        .into(headViewHolder.poster);

                headViewHolder.rating.setText("" + mMovie.getVoteAverage());
                headViewHolder.synopsis.setText(mMovie.getOverview());
                headViewHolder.release.setText(mMovie.getReleaseDate());

                break;

            case TRAILER:
                Video video = videos.get(position-1);
                TrailerViewHolder trailerHolder = (TrailerViewHolder) holder;
                trailerHolder.videoTitle.setText(video.getName());
                break;
            case REVIEW:
                Review review = reviews.get(position - videos.size() -1);
                ReviewViewHolder reviewHolder = (ReviewViewHolder) holder;
                reviewHolder.author.setText(review.getAuthor());
                reviewHolder.content.setText("\"" + review.getContent() + "\"");
                break;
        }


    }

    @Override
    public int getItemCount() {
        return 1 +reviews.size() + videos.size();
    }

    @Override
    public int getItemViewType(int position){
        int viewType;

        if (mMovie == null)
            return -1;

        if(position ==0){
            viewType = HEAD;
        }else if(position < videos.size() + 1){
            viewType = TRAILER;
        }else {
            viewType = REVIEW;
        }
        return viewType;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.movie_detail_poster)
        ImageView poster;
        @Bind(R.id.movie_detail_rating)
        TextView rating;
        @Bind(R.id.movie_detail_release)
        TextView release;
        @Bind(R.id.movie_detail_synopsis)
        TextView synopsis;


        public HeadViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.video_name)
        TextView videoTitle;

        @Bind(R.id.view_play_button)
        ImageButton playButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.view_play_button, R.id.video_name})
        public void playTrailer(View v) {
            Uri url = videos.get(getAdapterPosition() - 1).getYoutubeURL();
            Log.d(TAG, "Play url: " + url);
            context.startActivity(new Intent(Intent.ACTION_VIEW, url));
        }
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.review_author)
        TextView author;

        @Bind(R.id.review_content)
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.review_content)
        public void openReview() {
            Review review = reviews.get(getAdapterPosition() - videos.size() - 1);
            Log.d(TAG, "Display complete review: " + mMovie.getTitle());
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
        }
    }
}
