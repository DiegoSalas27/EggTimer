package com.example.android.eggtimer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifTextView;

public class MainActivity extends AppCompatActivity {

    ImageView imgEgg;
    GifTextView gifEgg;
    MediaPlayer mediaPlayer, mediaPlayer2;
    TextView textView;
    SeekBar seekBar;
    Button btnHatch;
    Boolean counterIsActive = false;
    CountDownTimer countDownTimer;
    int recall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgEgg = (ImageView) findViewById(R.id.imgEgg);
        gifEgg = (GifTextView) findViewById(R.id.gifEgg);
        mediaPlayer = MediaPlayer.create(this, R.raw.fondo);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.eggcrack);
        textView = (TextView) findViewById(R.id.textView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnHatch = (Button) findViewById(R.id.btnHatch);

        seekBar.setMax(39); //this is in seconds (1 minute is 60 seconds)
        seekBar.setProgress(30); //sets the initial position

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { //i is is seconds

                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void hatch(final View view){

        if(counterIsActive == false) {

            counterIsActive = true;
            seekBar.setEnabled(false);
            btnHatch.setText("Stop");


            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) { //getProgress() is the number of seconds so * 1000 to get milliseconds

                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer((int) millisUntilFinished / 1000);
                    imgEgg.setVisibility(view.VISIBLE);
                    gifEgg.setVisibility(view.GONE);
                    recall = (int) millisUntilFinished / 1000;
                    mediaPlayer.start();
                }

                @Override
                public void onFinish() {

                    textView.setText("0:00");
                    imgEgg.setVisibility(view.GONE);
                    gifEgg.setVisibility(view.VISIBLE);
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    mediaPlayer2.start();
                    textView.setText("0:30");
                    seekBar.setProgress(30);
                    countDownTimer.cancel();
                    btnHatch.setText("HATCH");
                    seekBar.setEnabled(true);
                    counterIsActive = false;


                }
            }.start();
        } else{

            pauseTimer();

        }
    }

    public void updateTimer(int secondsLeft){

        int minutes = (int) secondsLeft / 60; //we round it down
        int seconds = secondsLeft - minutes * 60;

        String secondString = Integer.toString(seconds);

        if(seconds <= 9){

            secondString = "0" + secondString;
        }

        textView.setText(Integer.toString(minutes) + ":" + secondString);

    }
    public void pauseTimer(){

        textView.setText(Integer.toString(recall));
        seekBar.setProgress(recall);
        countDownTimer.cancel();
        btnHatch.setText("HATCH");
        mediaPlayer.pause();
        seekBar.setEnabled(true);
        counterIsActive = false;

    }
}
