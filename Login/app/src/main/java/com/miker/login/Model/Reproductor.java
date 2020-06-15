package com.miker.login.Model;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.miker.login.Logic.Cancion;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static com.miker.login.Logic.Utils.getSong;
import static com.miker.login.Logic.Utils.loadImage;

public class Reproductor implements Serializable {
    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn, btnsave;
    private MediaPlayer mPlayer;
    private Cancion cancion;
    private TextView songName, startTime, songTime;
    private ImageView imgCapture;
    private SeekBar songPrgs;
    private int oTime = 0, sTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private Context context;
    private Handler hdlr = null;
    private static Reproductor reproductor = new Reproductor();

    public static Reproductor getReproductor(ImageButton forwardbtn, ImageButton backwardbtn, ImageButton pausebtn, ImageButton playbtn,
                                             SeekBar songPrgs, TextView songName, TextView startTime, TextView songTime, Cancion cancion,
                                             ImageView imgCapture, Context context) {
        reproductor.backwardbtn = backwardbtn;
        reproductor.forwardbtn = forwardbtn;
        reproductor.playbtn = playbtn;
        reproductor.pausebtn = pausebtn;
        reproductor.startTime = startTime;
        reproductor.songTime = songTime;
        reproductor.songPrgs = songPrgs;
        reproductor.songName = songName;
        reproductor.cancion = cancion;
        reproductor.imgCapture = imgCapture;
        reproductor.context = context;
        reproductor.load();
        return reproductor;
    }

    public Reproductor() {
        hdlr = new Handler();
    }

    public void load() {
        //
        sTime = 0;
        oTime = 0;
        eTime = 0;
        //
        songName.setText(cancion.getNombre());
        //
        loadImage(imgCapture, "/" + cancion.getNombre(), context);
        mPlayer = getSong(cancion.getNombre(), context);
        //

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSong();
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
                pausebtn.setVisibility(View.GONE);
                playbtn.setVisibility(View.VISIBLE);
            }
        });
        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = sTime + fTime;
                jump(num, num <= eTime);
            }
        });
        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = sTime - bTime;
                jump(num, num > 0);
            }
        });
    }

    public void startSong() {
        mPlayer.start();
        eTime = mPlayer.getDuration();
        sTime = mPlayer.getCurrentPosition();
        if (oTime == 0) {
            songPrgs.setMax(eTime);
            oTime = 1;
        }
        songTime.setText(String.format("%d min, %d seg", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
        startTime.setText(String.format("%d min, %d seg", TimeUnit.MILLISECONDS.toMinutes(sTime),
                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
        songPrgs.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);
        pausebtn.setVisibility(View.VISIBLE);
        playbtn.setVisibility(View.GONE);
    }

    public void jump(int num, boolean forward) {
        if (num <= eTime) {
            sTime = num;
            mPlayer.seekTo(sTime);
        } else {
            Toast.makeText(
                    context,
                    "No puede saltar hacia" + ((forward) ? " adelante" : "atrás") + " 5 segundos",
                    Toast.LENGTH_SHORT
            ).show();
        }
        if (!playbtn.isEnabled()) {
            playbtn.setEnabled(true);
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d min, %d seg", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };

    public Cancion getCancion() {
        return cancion;
    }

    public ImageView getImgCapture() {
        return imgCapture;
    }

    public MediaPlayer getmPlayer() {
        return mPlayer;
    }
}