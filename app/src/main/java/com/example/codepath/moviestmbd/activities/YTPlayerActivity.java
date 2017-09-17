package com.example.codepath.moviestmbd.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.codepath.moviestmbd.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class YTPlayerActivity extends YouTubeBaseActivity {

    @Bind(R.id.player)
    YouTubePlayerView player;

    public static final String EXTRA_KEY = "key";

    private String key ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytplayer);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        key = intent.getStringExtra(EXTRA_KEY);


       /* Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MOVIE, mMovie);*/

            player.initialize(getString(R.string.yt_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(key);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
