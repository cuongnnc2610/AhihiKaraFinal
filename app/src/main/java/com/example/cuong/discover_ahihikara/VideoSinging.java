package com.example.cuong.discover_ahihikara;

import java.io.IOException;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VideoSinging extends AppCompatActivity /*implements OnClickListener*/ {

    private SurfaceView surface1;
    private Button start, stop, pre, btnPlay, btnStop, btnBack;
    private MediaPlayer mediaPlayer1;
    private String url;
    private int id;
    private int postion = 0;
    String pathSave = "";
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    private TextView song,singer;

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_singingg);
        surface1 = (SurfaceView) findViewById(R.id.surface1);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        pre = (Button) findViewById(R.id.pre);
        btnPlay = findViewById(R.id.btn_Play);
        btnStop = findViewById(R.id.btn_Stop);
        btnBack = findViewById(R.id.btn_back);

        mediaPlayer1 = new MediaPlayer();
        //final String file_path = "https://vredir.nixcdn.com/PreNCT15/AnhDangODauDayAnhKaraoke-HuongGiangIdol-5744068.mp4?st=YiyLfb5iGAPi6Dv8d_90Zw&e=1544543196&t=1544456795996";
        song = (TextView) findViewById(R.id.tv_song);
        singer = (TextView) findViewById(R.id.tv_singer);
        Intent intent = this.getIntent();
        song.setText(intent.getStringExtra("song"));
        singer.setText(intent.getStringExtra("singer"));
        final String file_path = intent.getStringExtra("urlSong");
        // Get data
        /*Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            url = bundle.getString("url");
        } catch (Exception e) {
        }

        try {
            // ID của file video.
            id = this.getRawResIdByName(url);
            //mediaPlayer1 = MediaPlayer.create(this,id);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }*/

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Yeu cau request de thuc thi
        if (checkPermissionFromDevice())
        {
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                            + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);

                    Toast.makeText(VideoSinging.this,"Recording....",Toast.LENGTH_SHORT).show();

                    //Video Playing
                    surface1.getHolder().setKeepScreenOn(true);
                    surface1.getHolder().addCallback(new SurfaceViewLis());
                    try {
                        mediaPlayer1.reset();
                        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        //AssetFileDescriptor fd = getResources().openRawResourceFd(id);
                        //mediaPlayer1.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                        mediaPlayer1.setDataSource(file_path);

                        // The video output to SurfaceView
                        mediaPlayer1.setDisplay(surface1.getHolder());
                        mediaPlayer1.prepare();
                        mediaPlayer1.start();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    start.setEnabled(false);
                    pre.setEnabled(true);
                    stop.setEnabled(true);

                }
            });

            pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    start.setEnabled(false);
                    pre.setEnabled(true);
                    stop.setEnabled(true);
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.pause();
                        mediaRecorder.pause();
                        //pre.setText("RESUME");
                        Toast.makeText(VideoSinging.this, "Paused", Toast.LENGTH_SHORT).show();
                    } else {
                        mediaPlayer1.start();
                        mediaRecorder.resume();
                        //pre.setText("PAUSE");
                        Toast.makeText(VideoSinging.this, "Resumed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer1.isPlaying())
                        mediaPlayer1.stop();
                    start.setEnabled(true);
                    pre.setEnabled(false);
                    stop.setEnabled(false);

                    mediaRecorder.stop();
                    btnPlay.setEnabled(true);
                    btnStop.setEnabled(false);

                    Toast.makeText(VideoSinging.this, "Stopped", Toast.LENGTH_SHORT).show();
                }
            });

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStop.setEnabled(true);
                    stop.setEnabled(false);
                    start.setEnabled(false);

                    mediaPlayer= new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(VideoSinging.this, "Playing", Toast.LENGTH_SHORT).show();

                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(true);
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start.setEnabled(true);
                    stop.setEnabled(false);
                    btnPlay.setEnabled(true);
                    btnStop.setEnabled(false);

                    if (mediaPlayer != null)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();
                    }
                }
            });

        }
        else
        {
            requestPermission();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        mediaPlayer1.reset();
        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor fd = getResources().openRawResourceFd(R.raw.guichoanh);
        mediaPlayer1.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        // The video output to SurfaceView
        mediaPlayer1.setDisplay(surface1.getHolder());
        mediaPlayer1.prepare();
        mediaPlayer1.start();
    }

    private class SurfaceViewLis implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (postion == 0) {
                try {
                    play();
                    mediaPlayer1.seekTo(postion);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

    }

    @Override
    protected void onPause() {
        if (mediaPlayer1.isPlaying()) {
            // The location to save the currently playing
            postion = mediaPlayer1.getCurrentPosition();
            mediaPlayer1.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer1.isPlaying())
            mediaPlayer1.stop();
        mediaPlayer1.release();
        super.onDestroy();
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    // Tìm ID ứng với tên của file nguồn (Trong thư mục raw).
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }
}
