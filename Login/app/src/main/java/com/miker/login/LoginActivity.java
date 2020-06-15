package com.miker.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.biometrics.BiometricPrompt;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.miker.login.DAO.ServicioPersona;
import com.miker.login.DAO.ServicioUsuario;
import com.miker.login.Logic.Usuario;
import com.miker.login.cancion.CancionesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private EditText txt_user;
    private EditText txt_password;
    private ImageButton btn_send;
    private ImageButton btn_finger;
    private ServicioUsuario servicioUsuario;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        txt_user = findViewById(R.id.user);
        txt_password = findViewById(R.id.password);
        //
        btn_send = findViewById(R.id.btn_send);
        btn_finger = findViewById(R.id.btn_finger);
        //
        executor = Executors.newSingleThreadExecutor();

        biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Autentificación con la huella digital")
                .setSubtitle("")
                .setDescription("")
                .setNegativeButton("Cancelar", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build();
        try {
            servicioUsuario = ServicioUsuario.getServicio(getApplicationContext());
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final LoginActivity loginActivity = this;
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Usuario usuario = servicioUsuario.login(new Usuario(txt_user.getText().toString(), txt_password.getText().toString()));
                    if (usuario != null) {
                        Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                        intent.putExtra("usuario", usuario);
                        startActivityForResult(intent, 0);
                        Toast.makeText(getApplicationContext(), "¡Bienvenido " + usuario.getPerson().getNombreCompleto() + "!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "¡Datos no reconocidos!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_finger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                            loginActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Usuario usuario = servicioUsuario.query(new Usuario(1));
                                    Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                                    intent.putExtra("usuario", usuario);
                                    startActivityForResult(intent, 0);
                                    Toast.makeText(getApplicationContext(), "¡Bienvenido " + usuario.getPerson().getNombreCompleto() + "!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

