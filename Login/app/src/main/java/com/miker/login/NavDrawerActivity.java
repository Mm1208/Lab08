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
        //     try {
        //         image.setImageURI(
        //            (usuario.getPerson().getFoto() == null) ?
        //                        getUrlImage(usuario.getPerson().getSexo(), getApplicationContext()) :
        //                     Uri.parse(usuario.getPerson().getFoto())
        //    );
        //    } catch (Exception ex) {
        //       Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        //   }

        try {
            if (usuario.getPerson().getFoto() == null) {
                image.setImageURI(getUrlImage(usuario.getPerson().getSexo(), getApplicationContext()));
            } else {
                FileInputStream fileInputStream =
                        new FileInputStream(getApplicationContext().getFilesDir().getPath() + usuario.getPerson().getFoto());
                image.setImageBitmap(BitmapFactory.decodeStream(fileInputStream));
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void scaleImage(ImageView view) throws NoSuchElementException  {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(120);

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
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
