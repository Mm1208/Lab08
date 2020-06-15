package com.miker.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.koushikdutta.ion.Ion;
import com.miker.login.Logic.Usuario;
import com.miker.login.Logic.Utils;
import com.miker.login.cancion.CancionesActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import static com.miker.login.Logic.Utils.getBitmapFromUri;
import static com.miker.login.Logic.Utils.getUriToDrawable;
import static com.miker.login.Logic.Utils.getUrlImage;
import static com.miker.login.Logic.Utils.scaleImage;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences mPrefs;
    private Usuario usuario;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrefs = this.getSharedPreferences(getString(R.string.preference_user_key), Context.MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        Menu menu = navigationView.getMenu();

        ConstraintLayout layout = (ConstraintLayout) navigationView.getHeaderView(0);

        TextView nombre = layout.findViewById(R.id.text);
        ImageView image = layout.findViewById(R.id.image);

        nombre.setText(usuario.getPerson().getNombreCompleto());
        image.setImageURI(getUrlImage(usuario.getPerson().getSexo(), getApplicationContext()));
        try {

                FileInputStream fileInputStream =
                        new FileInputStream(getApplicationContext().getFilesDir().getPath() + "/" + usuario.getPerson().getNombre());
                image.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout) {
            finish();
            Intent intent = new Intent(NavDrawerActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;

        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            intent = new Intent(NavDrawerActivity.this, PerfilActivity.class);
        } else if (id == R.id.nav_canciones) {
            intent = new Intent(NavDrawerActivity.this, CancionesActivity.class);
        }
        if (id == R.id.nav_logout) {
            finish();
            intent = new Intent(NavDrawerActivity.this, LoginActivity.class);
        } else {
            intent.putExtra("usuario", usuario);
        }
        startActivityForResult(intent, 0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
