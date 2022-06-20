package com.example.telcotec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telcotec.utils.DBHelper;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.concurrent.TimeUnit;

public class Streaming extends YouTubeBaseActivity {

    private  String api_key = "AIzaSyBBlCExTbXDkpTMeSlr3AIRVI2chrGLdIs";
    private TextView time;
    private  TextView duration ;
    private  long  pageLoadStartTime ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);
        time = findViewById(R.id.yttempsdechargement);
        duration = findViewById(R.id.ytdure);
        youTubePlayerView.initialize( api_key,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo("5xVh-7ywKpE");

                        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {
                                pageLoadStartTime = System.currentTimeMillis();
                            }

                            @Override
                            public void onLoaded(String s) {

                                duration.setText("Durée :" +
                                        (youTubePlayer.getDurationMillis()  / 1000) / 60
                                        +":"
                                        + (youTubePlayer.getDurationMillis()/ 1000) % 60);

                                time.setText("Temps de la chargement : "+
                                       ( System.currentTimeMillis() - pageLoadStartTime )
                                        +" ms"
                                        );
                                DBHelper dbHelper = new DBHelper(getApplicationContext());
                                dbHelper.savestreaming(duration.getText().toString(),time.getText().toString());
                                Toast.makeText(getApplicationContext(), "data saved ", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {
                                youTubePlayer.setManageAudioFocus(false);
                            }

                            @Override
                            public void onVideoEnded() {

                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {

                            }
                        });


                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
    }
