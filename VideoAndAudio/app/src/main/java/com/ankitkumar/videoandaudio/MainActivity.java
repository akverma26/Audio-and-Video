package com.ankitkumar.videoandaudio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //for Audio
    AudioManager audioManager; //Audio System Service Stream
    SeekBar volumeControl; //SeekBar for Volume Control
    SeekBar scrubber; //SeekBar for Seeking
    MediaPlayer audio;
    //for Video
    VideoView video;
    MediaController controller; //Controller

    public void playVideo(View view) {
       video.start(); //Video Start
    }
    public void pauseVideo(View view){
        if(video.isPlaying())
            video.pause();
    }

    public void playAudio(View view){
        audio.start();
        (findViewById(R.id.button)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.button2)).setVisibility(View.VISIBLE);
    }
    public void pauseAudio(View view){
        if(audio.isPlaying())
            audio.pause();
        (findViewById(R.id.button)).setVisibility(View.VISIBLE);
        (findViewById(R.id.button2)).setVisibility(View.INVISIBLE);
    }

    public void play(View view){
        (findViewById(R.id.front)).setVisibility(View.INVISIBLE);
        if((view.getResources().getResourceEntryName(view.getId())).equals("audioPlay")){
            (findViewById(R.id.audio)).setVisibility(View.VISIBLE);
            playAudio(view);
        }
        else{
            (findViewById(R.id.video)).setVisibility(View.VISIBLE);
            playVideo(view);
        }
    }

    public void back(View view){
        pauseAudio(view);
        pauseVideo(view);
        (findViewById(R.id.audio)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.video)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.front)).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FOR VIDEO

        video = (VideoView) findViewById(R.id.videoView);
        video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video); //Set Video to videoView
        controller = new MediaController(this); //Initializing Media Controller
        controller.setAnchorView(video); //Attaching to Each Other
        video.setMediaController(controller); //Attaching to Each Other

        //FOR AUDIO

        volumeControl  = (SeekBar)findViewById(R.id.seekBar);
        audio  = MediaPlayer.create(this,R.raw.music); //Assign Audio File to Player
        audioManager  = (AudioManager) getSystemService(Context.AUDIO_SERVICE); //Interact with System Audio Service
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeControl.setMax(maxVolume); //Set SeekBar Max Value for Volume
        volumeControl.setProgress(curVolume); //Set SeekBar Current Value

        scrubber = (SeekBar)findViewById(R.id.scrubber);
        scrubber.setMax(audio.getDuration()); //Set SeekBar Max Value for Seeking

        //This Timer will run after each 100 ms to change the Progress/CurrentProgress of SeekBars
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                scrubber.setProgress(audio.getCurrentPosition());
                volumeControl.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        },0,100);

        scrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { //i is the Current Position of SeekBar
                if(b) audio.seekTo(i); //Audio Seek with Scrubber and Boolean b is for checking that SeekBar is Changed by Human (then b is True) or Automatically
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i , 0); //Volume Control According to Volume Control SeekBar Position
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
