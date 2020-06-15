package com.miker.login.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.miker.login.DAO.ServicioPersona;
import com.miker.login.Logic.Usuario;
import com.miker.login.Logic.Utils;
import com.miker.login.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.miker.login.DAO.ServicioPersona.getServicio;

public class PerfilActivity extends AppCompatActivity {
    private ImageView imgCapture;
    private Button button, save;
    private TextView userName;
    private static final int Image_Capture_Code = 1;
    Usuario usuario;
    String user;
    ServicioPersona servicioPersona;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        button = (Button) findViewById(R.id.button);
        save = (Button) findViewById(R.id.save);
        userName = (TextView) findViewById(R.id.userName);
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        try {
            servicioPersona = getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        user = usuario.getPerson().getNombre() + " " + usuario.getPerson().getApellido1() + " " + usuario.getPerson().getApellido2();
        userName.setText(user);

        imgCapture = (ImageView) findViewById(R.id.capturedImage);

        Bitmap bitmap = null;

        String ruta = null;
        if (usuario.getPerson().getFoto() != null) {
            ruta = usuario.getPerson().getFoto();
        }
        try {
            if (ruta != null) {
                FileInputStream fileInputStream =
                        new FileInputStream(getApplicationContext().getFilesDir().getPath() + ruta);
                bitmap = BitmapFactory.decodeStream(fileInputStream);
                imgCapture.setImageBitmap(bitmap);
            }else{
                imgCapture.setImageURI(Utils.getUrlImage(usuario.getPerson().getSexo(), getApplicationContext()));
            }
        } catch (Exception io) {
            Toast.makeText(this, io.getMessage(), Toast.LENGTH_LONG).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, Image_Capture_Code);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    imgCapture.buildDrawingCache();
                    Bitmap bp = imgCapture.getDrawingCache();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    FileOutputStream outputStream = getApplicationContext().openFileOutput(usuario.getPerson().getNombre(), Context.MODE_PRIVATE);
                    outputStream.write(byteArray);
                    outputStream.close();

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (IOException e2) {
                    Toast.makeText(getApplicationContext(), e2.getMessage(), Toast.LENGTH_LONG).show();
                }


                Intent intent = new Intent(PerfilActivity.this, NavDrawerActivity.class);
                intent.putExtra("usuario", usuario);
                startActivityForResult(intent, 0);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCapture.setImageBitmap(bp);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //Actualiza la ruta de la imagen de la persona
                usuario.getPerson().setFoto("/" + usuario.getPerson().getNombre());
                servicioPersona.update(usuario.getPerson());


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

}