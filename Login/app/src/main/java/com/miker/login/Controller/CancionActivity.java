package com.miker.login.Controller;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.miker.login.Model.Model;
import com.miker.login.R;
import com.miker.login.Model.Reproductor;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CancionActivity extends AppCompatActivity {
    private Reproductor reproductor;
    private static final int Image_Capture_Code = 1;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        model = new Model();
        model = (Model) getIntent().getSerializableExtra("model");
        //

        reproductor = Reproductor.getReproductor(
                findViewById(R.id.btnForward),
                findViewById(R.id.btnBackward),
                findViewById(R.id.btnPause),
                findViewById(R.id.btnPlay),
                findViewById(R.id.sBar),
                findViewById(R.id.txtSname),
                findViewById(R.id.txtStartTime),
                findViewById(R.id.txtSongTime),
                model.getCancionSeleccionada(),
                findViewById(R.id.background),
                getApplicationContext()
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cancion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_camera) {
            Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cInt, Image_Capture_Code);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_share) {
            Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cInt, Image_Capture_Code);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_share) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            Dialog dialog = new Dialog(this);
            loadDialog(dialog);
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadDialog(Dialog dialog) {
        //
        dialog.setContentView(R.layout.fragment_compartir);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView numero = (TextView) dialog.findViewById(R.id.number);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_send);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        //
        title.setText("Compartir Canción");
        //
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                try {
                    if (numero.getText().toString().length() != 8) {
                        numero.setError("El número debe contener 8 digitos");
                    } else {
                        SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage("+506" + numero.getText().toString(), null, reproductor.getCancion().getNombre(), null, null);
                        Toast.makeText(getApplicationContext(), "Se envio con exito el mensaje", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Falló el envio de mensaje: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CancionActivity.this, CancionesActivity.class);
        intent.putExtra("model", model);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reproductor.getmPlayer().pause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                reproductor.getImgCapture().setImageBitmap(bp);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                try {
                    FileOutputStream outputStream = getApplicationContext().openFileOutput(reproductor.getCancion().getNombre(), Context.MODE_PRIVATE);
                    outputStream.write(byteArray);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }
}