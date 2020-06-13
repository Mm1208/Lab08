package com.miker.login.cancion;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.miker.login.Model;
import com.miker.login.R;

import java.util.concurrent.TimeUnit;


public class CancionActivity extends AppCompatActivity {
    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn;
    private MediaPlayer mPlayer;
    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion);
        model = new Model();
        model = (Model) getIntent().getSerializableExtra("model");
        backwardbtn = (ImageButton)findViewById(R.id.btnBackward);
        forwardbtn = (ImageButton)findViewById(R.id.btnForward);
        playbtn = (ImageButton)findViewById(R.id.btnPlay);
        pausebtn = (ImageButton)findViewById(R.id.btnPause);
        songName = (TextView)findViewById(R.id.txtSname);
        startTime = (TextView)findViewById(R.id.txtStartTime);
        songTime = (TextView)findViewById(R.id.txtSongTime);
        Cancion cancion = model.getCancionSeleccionada();
        songName.setText(cancion.getNombre());

        switch (cancion.getNombre()){

            case "La Bamba":
                mPlayer = MediaPlayer.create(this, R.raw.bamba);
                break;
            case "Believer":
                mPlayer = MediaPlayer.create(this, R.raw.believer);
                break;
            case "Locked Away":
                mPlayer = MediaPlayer.create(this, R.raw.lockedaway);
                break;
            case "Phorograph":
                mPlayer = MediaPlayer.create(this, R.raw.phorograph);
                break;
            case "Youre Beautiful":
                mPlayer = MediaPlayer.create(this, R.raw.yourebeautiful);
                break;
            default:
                mPlayer = MediaPlayer.create(this, R.raw.bamba);
        }

        songPrgs = (SeekBar)findViewById(R.id.sBar);
        songPrgs.setClickable(false);
        pausebtn.setEnabled(false);



        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CancionActivity.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                mPlayer.start();
                eTime = mPlayer.getDuration();
                sTime = mPlayer.getCurrentPosition();
                if(oTime == 0){
                    songPrgs.setMax(eTime);
                    oTime =1;
                }
                songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                        TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
                songPrgs.setProgress(sTime);
                hdlr.postDelayed(UpdateSongTime, 100);
                pausebtn.setEnabled(true);
                playbtn.setEnabled(false);
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                pausebtn.setEnabled(false);
                playbtn.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Pausing Audio", Toast.LENGTH_SHORT).show();
            }
        });
        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime + fTime) <= eTime)
                {
                    sTime = sTime + fTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime - bTime) > 0)
                {
                    sTime = sTime - bTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };

    public void asignaCancion(){
        Cancion cancion = model.getCancionSeleccionada();

        songName.setText(cancion.getNombre());

        if(cancion.getNombre().equals("Believer")) {
            mPlayer = MediaPlayer.create(this, R.raw.believer);
        }
        if(cancion.getNombre().equals("Locked Away")) {
            mPlayer = MediaPlayer.create(this, R.raw.lockedaway);
        }
        if(cancion.getNombre().equals("Phorograph")) {
            mPlayer = MediaPlayer.create(this, R.raw.phorograph);
        }
        if(cancion.getNombre().equals("Youre Beautiful")) {
            mPlayer = MediaPlayer.create(this, R.raw.yourebeautiful);
        }
    }
}