package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity2 extends AppCompatActivity {

    private ImageView playview, pauseview, nextview, previousView, forwardview, backwardview, imageAnimate;
    private TextView textMusicNameview, TotDurationView, currenCountView;
    SeekBar musicseekBar;
    BarVisualizer barVisualizer;
    String Sname;
    String SongNAme;
    public static final String EXTRAA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mysongs;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);
        this.setTitle("Now Playing");
        playview = findViewById(R.id.buttonplay_id);
        nextview = findViewById(R.id.buttonNext_id);
        previousView = findViewById(R.id.buttonPrevious_id);
        forwardview = findViewById(R.id.buttonForward_id);
        backwardview = findViewById(R.id.buttonRewind_id);
        textMusicNameview = findViewById(R.id.Musicnametext_id);
        TotDurationView = findViewById(R.id.TextViewDuration_id);
        currenCountView = findViewById(R.id.TextViewDurationCurrent_id);
        musicseekBar = findViewById(R.id.seekBar_id);
        // barVisualizer = findViewById(R.id.Visualizer_id);
        imageAnimate = findViewById(R.id.imageViewAnimate_id);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mysongs = (ArrayList) bundle.getParcelableArrayList("songs");
        SongNAme = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        textMusicNameview.setSelected(true);
        String path = mysongs.get(position).toString();
        Uri uri = Uri.parse(path.toString());
        Sname = mysongs.get(position).getName();
        textMusicNameview.setText(Sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        thread = new Thread() {
            @Override
            public void run() {
                int totduration = mediaPlayer.getDuration();
                int currentposition = 0;
                while (currentposition < totduration) {
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        musicseekBar.setProgress(currentposition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        forwardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });
        backwardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

        musicseekBar.setMax(mediaPlayer.getDuration());
        thread.start();

        musicseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(musicseekBar.getProgress());
            }
        });

        String endTime = timecreate(mediaPlayer.getDuration());
        TotDurationView.setText(endTime);
        Handler handler = new Handler();
        final int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currenttime = timecreate(mediaPlayer.getCurrentPosition());
                currenCountView.setText(currenttime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        playview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    playview.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                    mediaPlayer.pause();
                } else {
                    playview.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    mediaPlayer.start();
                }
            }
        });

//        int  audiosessionid =mediaPlayer.getAudioSessionId();
//        if (audiosessionid !=-1){
//            barVisualizer.setAudioSessionId(audiosessionid);
//        }

        nextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position + 1) % mysongs.size());
                getmusic();
                playview.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                startAnimate(imageAnimate);
//                int  audiosessionid =mediaPlayer.getAudioSessionId();
//                if (audiosessionid!=-1){
//                    barVisualizer.setAudioSessionId(audiosessionid);
//                }

            }
        });

        previousView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position - 1) < 0) ? (mysongs.size() - 1) : (position - 1);
                getmusic();
                playview.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                startAnimateLeft(imageAnimate);
                int audiosessionid = mediaPlayer.getAudioSessionId();
//                if (audiosessionid!=-1){
//                    barVisualizer.setAudioSessionId(audiosessionid);
//                }
            }
        });

    }


    public void startAnimate(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageAnimate, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public void startAnimateLeft(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageAnimate, "rotation", 0f, -360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public void getmusic() {
        textMusicNameview.setSelected(true);
        String path = mysongs.get(position).toString();
        Uri uri = Uri.parse(path.toString());
        Sname = mysongs.get(position).getName();
        textMusicNameview.setText(Sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
    }

    public String timecreate(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        time += min + ":";
        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;


    }

}