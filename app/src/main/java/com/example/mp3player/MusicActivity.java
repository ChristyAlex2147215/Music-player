package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    private Button pauseButton,previousButton,nextButton;
    private ImageView maxVolume,minVolume;
    private TextView textViewfilename,textViewProgress,textViewTotaltime;
    private SeekBar seekbarVolume,seekBarMusic;
    String title,filePath;
    int position;
    ArrayList<String> list;
    private MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    int totalTime;
    private Animation animation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        pauseButton = findViewById(R.id.pausebutton);
        previousButton = findViewById(R.id.previousbutton);
        nextButton = findViewById(R.id.nextbutton);
        textViewfilename = findViewById(R.id.viewmusicfilename);
        textViewProgress = findViewById(R.id.textviewprogress);
        textViewTotaltime = findViewById(R.id.textviewtotaltime);
        seekbarVolume = findViewById(R.id.volumeseek);
        seekBarMusic = findViewById(R.id.musicseek);
        maxVolume=findViewById(R.id.maxvolume);
        minVolume=findViewById(R.id.minvolume);

        title=getIntent().getStringExtra("title");
        filePath=getIntent().getStringExtra("filepath");
        position=getIntent().getIntExtra("position",0);
        list=getIntent().getStringArrayListExtra("list");
        textViewfilename.setText(title);
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.reset();

                if (position == 0)
                {
                    position = list.size()-1;
                }
                else
                {
                    position--;
                }

                String newFilePath = list.get(position);

                try {
                    mediaPlayer.setDataSource(newFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    pauseButton.setBackgroundResource(R.drawable.nextbtn);

                    String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/")+1);
                    textViewfilename.setText(newTitle);

                    textViewfilename.clearAnimation();
                    //textViewfilename.startAnimation(animation);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    pauseButton.setBackgroundResource(R.drawable.playbtn);
                }
                else
                {
                    mediaPlayer.start();
                    pauseButton.setBackgroundResource(R.drawable.pausebtn);
                }


            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();

                if (position == list.size()-1)
                {
                    position = 0;
                }
                else
                {
                    position++;
                }

                String newFilePath = list.get(position);

                try {
                    mediaPlayer.setDataSource(newFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    pauseButton.setBackgroundResource(R.drawable.pausebtn);

                    String newTitle = newFilePath.substring(newFilePath.lastIndexOf("/")+1);
                    textViewfilename.setText(newTitle);

                    textViewfilename.clearAnimation();
                    //textViewfilename.startAnimation(animation);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    seekbarVolume.setProgress(progress);
                    float volumeLevel = progress / 100f;
                    mediaPlayer.setVolume(volumeLevel,volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    mediaPlayer.seekTo(progress);
                    seekBarMusic.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

         handler= new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                totalTime = mediaPlayer.getDuration();
                seekBarMusic.setMax(totalTime);

                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBarMusic.setProgress(currentPosition);
                handler.postDelayed(runnable,1000);

                String elapsedTime = createTimeLabel(currentPosition);
                String lastTime = createTimeLabel(totalTime);

                textViewProgress.setText(elapsedTime);
                textViewTotaltime.setText(lastTime);


            }
        };

        handler.post(runnable);
    }

    public String createTimeLabel(int currentPosition)
    {
        // 1 min = 60 second
        // 1 second = 1000 millisecond

        String timeLabel;
        int minute,second;

        minute = currentPosition / 1000 / 60;
        second = currentPosition / 1000 % 60;

        if (second < 10)
        {
            timeLabel = minute + ":0"+second;
        }
        else
        {
            timeLabel = minute+":"+second;
        }

        return timeLabel;
    }

    public void stopMusic()
    {
        mediaPlayer.stop();

    }
    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(MusicActivity.this, "Stopping music!", Toast.LENGTH_SHORT);
        toast.show();
        MusicActivity m=new MusicActivity();
        m.stopMusic();
    }



    }
