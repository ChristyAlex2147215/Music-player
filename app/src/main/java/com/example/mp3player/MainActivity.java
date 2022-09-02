package com.example.mp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleview;
    private final ArrayList<String> songlist=new ArrayList<>();
    private  MusicAdapter adapter;


    private final static String media_path=Environment.getExternalStorageDirectory().getPath()+"/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.e("Media path",media_path);


        recycleview = findViewById(R.id.recycle_view);
        recycleview.setLayoutManager(new LinearLayoutManager(this));

//        pauseButton = findViewById(R.id.pausebuttton);
//        previousButton = findViewById(R.id.previousbutton);
//        nextButton = findViewById(R.id.nextbutton);
//        textViewfilename = findViewById(R.id.viewmusicfilename);
//        textViewProgress = findViewById(R.id.textviewprogress);
//        textViewTotaltime = findViewById(R.id.textviewtotaltime);
//        seekbarVolume = findViewById(R.id.volumeseek);
//        seekBarMusic = findViewById(R.id.musicseek);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else
        {
            getallaudiofiles();
        }
    }
    public  void getallaudiofiles() {
        if(media_path!=null) {
            File mainfile = new File(media_path);
            File[] filelist = mainfile.listFiles();
            for (File file : filelist) {
                try {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        String path = file.getAbsolutePath();
                        if (path.endsWith(".mp3")) {
                            songlist.add(path);
                            adapter.notifyDataSetChanged();
                            Log.e("media path", file + path);
                        }
                    }
                    //Log.e("media path",file.toString());
                }
                catch (NullPointerException n)
                {
                    n.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        adapter=new MusicAdapter(songlist,MainActivity.this);
        recycleview.setAdapter(adapter);
    }

    public  void scanDirectory(File directory)
    {
        if(directory!=null)
        {
            File[] filelist=directory.listFiles();
            for(File file:filelist)
            {
               // Log.e("media path",file.toString());
                if(file.isDirectory())
                {
                    scanDirectory(file);
                }
                else
                {
                    String path=file.getAbsolutePath();
                    if(path.endsWith(".mp3"))
                    {
                        songlist.add(path);
                    }
                }
               // Log.e("media path",file.toString());
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
            if(requestCode==1 && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getallaudiofiles();
            }
        }


    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(MainActivity.this, "Stopping music!", Toast.LENGTH_SHORT);
        toast.show();
       MusicActivity m=new MusicActivity();
        m.stopMusic();
    }
    }
